package il.ac.bgu.fusion.fusion.algorithm;

import il.ac.bgu.fusion.objects.CovarianceEllipse;
import il.ac.bgu.fusion.objects.State;
import javafx.scene.shape.Ellipse;
import org.apache.commons.math3.linear.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mali on 5/9/2017.
 */
public class InitialClustring {

  private static final double THRESHOLD=9.21;
  private static final double INFINITY=-1;
  private static List<State> Clusters=new ArrayList<State>();

  public static List<State> statesList(List<CovarianceEllipse> rawDataList){

    for(CovarianceEllipse rawEllipse: rawDataList){
      double minDistance=INFINITY;
      State closerCluster=null;

      for(State cluster: Clusters){
        double distance=CalcDistance(cluster.getFusionEllipse(),rawEllipse);
        if(distance<minDistance){
          minDistance=distance;
          closerCluster=null;
        }
        if(minDistance<THRESHOLD){
          merge(cluster,rawEllipse);
        }
        else {
          List<CovarianceEllipse> cov=new ArrayList<>();
          cov.add(rawEllipse);
          State newState=new State(cov);
          Clusters.add(newState);
        }

      }

    }
    return null;
  }

  private static void merge(State cluster, CovarianceEllipse rawEllipse) {
    CovarianceEllipse c1=cluster.getFusionEllipse();
    CovarianceEllipse c2=rawEllipse;
    double[][] matrixEllipse1 = { {c1.getSx2(), c1.getSxy()},
                                  {c1.getSxy(), c1.getSy2()} };
    RealMatrix matrix1 = new Array2DRowRealMatrix(matrixEllipse1);

    double[][] matrixEllipse2 = { {c2.getSx2(), c2.getSxy()},
                                  {c2.getSxy(), c2.getSy2()} };
    RealMatrix matrix2 = new Array2DRowRealMatrix(matrixEllipse2);

    RealVector r1=new ArrayRealVector(2);
    /*
    r1.setEntry(0,ellipse2.getCentreX());
    r1.setEntry(1,ellipse2.getCentreY());

    RealVector r2=new ArrayRealVector(2);
    r2.setEntry(0,ellipse1.getCentreX());
    r2.setEntry(1,ellipse1.getCentreY());*/

    RealMatrix cInv1= MatrixUtils.inverse(matrix1);
    RealMatrix cInv2= MatrixUtils.inverse(matrix2);
    RealMatrix matrixAddedInv=MatrixUtils.inverse(cInv1.add(cInv2));

    RealMatrix w1= cInv1.multiply(matrixAddedInv);
    RealMatrix w2= cInv2.multiply(matrixAddedInv);


  }

  public static double CalcDistance(CovarianceEllipse ellipse1, CovarianceEllipse ellipse2){
    double d=0;

    double[][] matrixEllipse1 = { {ellipse2.getSx2(), ellipse2.getSxy()},
                              {ellipse2.getSxy(), ellipse2.getSy2()} };
    RealMatrix c1 = new Array2DRowRealMatrix(matrixEllipse1);

    double[][] matrixEllipse2 = { {ellipse1.getSx2(), ellipse1.getSxy()},
                              {ellipse1.getSxy(), ellipse1.getSy2()} };
    RealMatrix c2 = new Array2DRowRealMatrix(matrixEllipse2);

    RealVector r1=new ArrayRealVector(2);
    r1.setEntry(0,ellipse2.getCentreX());
    r1.setEntry(1,ellipse2.getCentreY());

    RealVector r2=new ArrayRealVector(2);
    r2.setEntry(0,ellipse1.getCentreX());
    r2.setEntry(1,ellipse1.getCentreY());

    RealVector deltaR=r1.append(r2.mapMultiply(-1));
    double[][] arrDeltaR={{deltaR.getEntry(0), deltaR.getEntry(1)}};
    RealMatrix matrixDeltaR =new Array2DRowRealMatrix(arrDeltaR);
    RealMatrix matrixDeltaRTranspose= matrixDeltaR.transpose();

    RealMatrix cInv1= MatrixUtils.inverse(c1);
    RealMatrix cInv2= MatrixUtils.inverse(c2);
    RealMatrix cInvAdded=cInv1.add(cInv2);
    RealMatrix deltaRMulcInvAdded= matrixDeltaR.multiply(cInvAdded);
    RealMatrix deltaRMulcInvAddedMulTrans= deltaRMulcInvAdded.multiply(matrixDeltaRTranspose);

    d= deltaRMulcInvAddedMulTrans.getEntry(0,0);

    System.out.println(deltaRMulcInvAddedMulTrans.getColumnDimension());
    System.out.println(deltaRMulcInvAddedMulTrans.getRowDimension());

    return d;
  }




}
