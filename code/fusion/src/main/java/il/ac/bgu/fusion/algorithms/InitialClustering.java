package il.ac.bgu.fusion.algorithms;

import il.ac.bgu.fusion.objects.CovarianceEllipse;
import il.ac.bgu.fusion.objects.State;
import org.apache.commons.math3.linear.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static il.ac.bgu.fusion.util.LinearAlgebraUtils.*;

/**
 * Implementation of the Initial Clustering algorithm.
 * Created by Mali and Stas on 5/9/2017.
 */
public class InitialClustering {
  private static final double THRESHOLD = 9.21;
  private static final double INFINITY = Double.POSITIVE_INFINITY;
  private static Random rand = new Random();

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
        double distance= calcDistanceBetweenEllipses(cluster.getFusionEllipse(), rawEllipse);
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




    /*RealMatrix velocityCovariance1Inv = MatrixUtils.inverse(ellipseToVelocityCovarianceMatrix(currFussEllipse));
    RealMatrix velocityCovariance2Inv = MatrixUtils.inverse(ellipseToVelocityCovarianceMatrix(rawEllipse));

    // Calculation of merged velocity covariance ellipse and velocity position vector:
    RealMatrix mergedVelocityCovMatrix= MatrixUtils.inverse(velocityCovariance1Inv.add(velocityCovariance2Inv));
    RealMatrix mergedVelocityPosVector= calcMergedVelocityPosVector(currFussEllipse, rawEllipse, velocityCovariance1Inv, velocityCovariance2Inv, mergedVelocityCovMatrix);*/




    // Updating the state with newly merged ellipse:
    CovarianceEllipse mergedEllipse = matricesToEllipse(mergedCovMatrix, mergedPosVector
                                                        /*,mergedVelocityCovMatrix, mergedVelocityPosVector*/);
    mergedEllipse.setTimeStamp(rawEllipse.getTimeStamp());
    mergedEllipse.setId(rand.nextLong());
    mergedEllipse.setSensor(rawEllipse.getSensor());     //implement methods set/getSensors (list of all sensors from raw data)
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

    RealMatrix w1 = covMat1Inverse.multiply(mergedCovMat);  // dim=(2,2)x(2,2)=(2,2)
    RealMatrix w2 = covMat2Inverse.multiply(mergedCovMat);  // dim=(2,2)x(2,2)=(2,2)

    RealMatrix r1 = ellipseToPositionVector(ellipse1);      // dim=(1,2)
    RealMatrix r2 = ellipseToPositionVector(ellipse2);      // dim=(1,2)

    RealMatrix w1MulR1 = r1.multiply(w1);                   // dim=(1,2)x(2,2)=(1,2)
    RealMatrix w2MulR2 = r2.multiply(w2);                   // dim=(1,2)x(2,2)=(1,2)
    return  w1MulR1.add(w2MulR2);                           // dim=(1,2)+(1,2)=(2,1)
  }


  /*
 * Helper function for 'merge' above
 * Calculates the merged velocity position vector between two covariance ellipses, given some pre-calculated arguments
 */
  private static RealMatrix calcMergedVelocityPosVector(CovarianceEllipse ellipse1, CovarianceEllipse ellipse2,
                                                        RealMatrix covMat1Inverse, RealMatrix covMat2Inverse,
                                                        RealMatrix mergedCovMat){

    RealMatrix w1 = covMat1Inverse.multiply(mergedCovMat);  // dim=(2,2)x(2,2)=(2,2)
    RealMatrix w2 = covMat2Inverse.multiply(mergedCovMat);  // dim=(2,2)x(2,2)=(2,2)

    RealMatrix r1 = ellipseToVelocityPositionVector(ellipse1);      // dim=(1,2)
    RealMatrix r2 = ellipseToVelocityPositionVector(ellipse2);      // dim=(1,2)

    RealMatrix w1MulR1 = r1.multiply(w1);                   // dim=(1,2)x(2,2)=(1,2)
    RealMatrix w2MulR2 = r2.multiply(w2);                   // dim=(1,2)x(2,2)=(1,2)
    return  w1MulR1.add(w2MulR2);                           // dim=(1,2)+(1,2)=(2,1)
  }
}
