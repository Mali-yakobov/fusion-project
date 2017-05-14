package il.ac.bgu.fusion.algorithms;

import il.ac.bgu.fusion.objects.CovarianceEllipse;
import il.ac.bgu.fusion.objects.State;
import org.apache.commons.math3.linear.*;

import java.util.ArrayList;
import java.util.List;

import static il.ac.bgu.fusion.util.LinearAlgebraUtils.*;

/**
 * Implementation of the Initial Clustering algorithm.
 * Created by Mali and Stas on 5/9/2017.
 */
public class InitialClustering {
  private static final double THRESHOLD= 9.21;
  private static final double INFINITY= Double.MAX_VALUE;


  /*
   * Main method of the initial clustering algorithm implementation.
   *
   * Input: List of covariance ellipses
   * Output: List of initial clusters (states)
   */
  public static List<State> initialClustering(List<CovarianceEllipse> rawEllipses){
    List<State> clusters= new ArrayList<State>();

    for(CovarianceEllipse rawEllipse: rawEllipses){
      double minDistance= INFINITY;
      State closestCluster= null;

      for(State cluster: clusters){
        double distance= calcDistance(cluster.getFusionEllipse(), rawEllipse);
        if(distance < minDistance){
          minDistance=distance;
          closestCluster= cluster;
        }
      }

      if(minDistance < THRESHOLD){
        merge(closestCluster, rawEllipse);
      }
      else {
        List<CovarianceEllipse> newRawData= new ArrayList<>();
        newRawData.add(rawEllipse);
        State newState=new State(newRawData);
        newState.setFusionEllipse(rawEllipse);
        clusters.add(newState);
      }
    }

    return clusters;
  }


  /*
   * Merge raw ellipse into existing cluster (state)
   */
  public static void merge(State cluster, CovarianceEllipse rawEllipse) {
    CovarianceEllipse currFussEllipse= cluster.getFusionEllipse();
    RealMatrix c1Inv = MatrixUtils.inverse(ellipseToCovarianceMatrix(currFussEllipse));
    RealMatrix c2Inv = MatrixUtils.inverse(ellipseToCovarianceMatrix(rawEllipse));

    // Calculation of merged covariance ellipse and position vector:
    RealMatrix mergedCovMatrix= MatrixUtils.inverse(c1Inv.add(c2Inv));
    RealMatrix mergedPosVector= calcMergedPosVector(currFussEllipse, rawEllipse, c1Inv, c2Inv, mergedCovMatrix);

    // Updating the state with newly merged ellipse:
    CovarianceEllipse mergedEllipse = matricesToEllipse(mergedCovMatrix, mergedPosVector);
    //mergedEllipse.setTimeStamp();  something logical
    //mergedEllipse.setId();         random
    //mergedEllipse.setSensor();     implement methods set/getSensors (list of all sensors)
    //mergedEllipse velocity update  same as covariance (velocity is *5* numbers)
    cluster.getEllipseList().add(rawEllipse);
    cluster.setFusionEllipse(mergedEllipse);
  }


  /*
   * Helper function for 'merge' above
   * Calculates the merged position vector between two covariance ellipses, given some pre-calculated arguments
   */
  private static RealMatrix calcMergedPosVector(CovarianceEllipse ellipse1, CovarianceEllipse ellipse2,
                                                RealMatrix covMat1Inverse, RealMatrix covMat2Inverse,
                                                RealMatrix mergedCovMat){

    RealMatrix w1= covMat1Inverse.multiply(mergedCovMat);  // dim=(2,2)x(2,2)=(2,2)
    RealMatrix w2= covMat2Inverse.multiply(mergedCovMat);  // dim=(2,2)x(2,2)=(2,2)

    RealMatrix r1= ellipseToPositionVector(ellipse1);      // dim=(1,2)
    RealMatrix r2= ellipseToPositionVector(ellipse2);      // dim=(1,2)

    RealMatrix w1MulR1= r1.multiply(w1);                   // dim=(1,2)x(2,2)=(1,2)
    RealMatrix w2MulR2= r2.multiply(w2);                   // dim=(1,2)x(2,2)=(1,2)
    return  w1MulR1.add(w2MulR2);                          // dim=(1,2)+(1,2)=(2,1)
  }

  /*
   * Calculate statistical distance between two covariance ellipses
   */
  public static double calcDistance(CovarianceEllipse ellipse1, CovarianceEllipse ellipse2){
    RealMatrix c1Inv = MatrixUtils.inverse(ellipseToCovarianceMatrix(ellipse1));
    RealMatrix c2Inv = MatrixUtils.inverse(ellipseToCovarianceMatrix(ellipse2));
    RealMatrix cInvSummed= c1Inv.add(c2Inv);                                      // dim=(2,2)

    RealMatrix r1 = ellipseToPositionVector(ellipse1);
    RealMatrix r2 = ellipseToPositionVector(ellipse2);
    RealMatrix deltaR= r1.subtract(r2);                                           // dim=(1,2)
    RealMatrix deltaRTrnsp= deltaR.transpose();                                   // dim=(2,1)

    RealMatrix DRMulCInvSummed= deltaR.multiply(cInvSummed);                      // dim=(1,2)x(2,2)=(1,2)
    RealMatrix DRMulCInvSummedMulDRTrans= DRMulCInvSummed.multiply(deltaRTrnsp);  // dim=(1,2)x(2,1)=(1,1)
    return DRMulCInvSummedMulDRTrans.getEntry(0,0);
  }


}
