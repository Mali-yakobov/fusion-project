package il.ac.bgu.fusion.algorithms;

/**
 * Created by Maayan on 08/06/2017.
 */


import il.ac.bgu.fusion.objects.CovarianceEllipse;
import il.ac.bgu.fusion.objects.State;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import static il.ac.bgu.fusion.util.LinearAlgebraUtils.calcDistanceBetweenEllipses;

/**
 * Implementation of the Distance Matrix algorithm.
 * Calculates statistical distance between ellipse and the last state of a track
 */
public class DistanceMatrix {

  /*
  int states = 10;
  int existingTracks = 10;

  double[][] staticModelDistanceMatrix = new double[existingTracks][states];
  double[][] linearModelDistanceMatrix = new double[existingTracks][states];
*/

  double calcDistance(State state, CovarianceEllipse ellipse) {
    double dt = ellipse.getTimeStamp() - state.getFusionEllipse().getTimeStamp();
    CovarianceEllipse extrapolatedEllipse = extrapolateState(state, ellipse, dt);
    double distance = calcDistanceBetweenEllipses(extrapolatedEllipse, ellipse);
    return distance;
  }

  CovarianceEllipse extrapolateState(State state, CovarianceEllipse ellipse, double dt) {
    double state4d[][] ={
      {
        state.getFusionEllipse().getCentreY(), state.getFusionEllipse().getVx(),
        state.getFusionEllipse().getCentreY(), state.getFusionEllipse().getVy()

      }
    } ; //Vector representing the kinematic sata of the state
    double phi[][] ={
                      { 1, dt, 0, 0 },
                      { 0, 1, 0, 0  },
                      { 0, 0, 1, dt },
                      { 0, 0, 0, 1  }

      };

    Array2DRowRealMatrix state4dMatrix = new Array2DRowRealMatrix(state4d);
    Array2DRowRealMatrix phiMatrix = new Array2DRowRealMatrix(phi);

    RealMatrix extrapolation4d = phiMatrix.multiply(state4dMatrix); //extrapolation4d is now {x, vx, y, vy}


    double[][] matrixData = { {state.getFusionEllipse().getSx2(), state.getFusionEllipse().getSxy()},
                              {state.getFusionEllipse().getSxy(), state.getFusionEllipse().getSy2()} };
    Array2DRowRealMatrix stateCovarianceMatrix = new Array2DRowRealMatrix(matrixData);

    RealMatrix newCovarianceMatrix = phiMatrix.multiply(stateCovarianceMatrix).multiply(phiMatrix.transpose());

        /*
        ask about timeStamp , Id and Sensor of the new Ellipse
        */
    CovarianceEllipse returnedEllipse = new CovarianceEllipse(ellipse.getTimeStamp(),ellipse.getId(),ellipse.getSensor(),
                                                              extrapolation4d.getEntry(0,0),extrapolation4d.getEntry(2,0),
                                                              newCovarianceMatrix.getEntry(0,0),newCovarianceMatrix.getEntry(2,2),
                                                              newCovarianceMatrix.getEntry(0,2),extrapolation4d.getEntry(1,0),
                                                              extrapolation4d.getEntry(3,0),newCovarianceMatrix.getEntry(1,3),
                                                              newCovarianceMatrix.getEntry(1,1),newCovarianceMatrix.getEntry(3,3)
                                                              );
    //return new CovarianceEllipse(extrapolation4d, newCovarianceMatrix);
    return returnedEllipse;
  }
}
