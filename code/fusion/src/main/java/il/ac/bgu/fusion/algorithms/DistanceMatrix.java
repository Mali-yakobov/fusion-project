package il.ac.bgu.fusion.algorithms;

/**
 * Created by Maayan on 08/06/2017.
 */


import il.ac.bgu.fusion.objects.CovarianceEllipse;
import il.ac.bgu.fusion.objects.State;
import il.ac.bgu.fusion.objects.Track;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.List;

import static il.ac.bgu.fusion.util.LinearAlgebraUtils.calcDistanceBetweenEllipses;

/**
 * Implementation of the Distance Matrix algorithm.
 * Calculates statistical distance between ellipse and the last state of a track
 */

public class DistanceMatrix {


  static int states = 10;
  static int existingTracks = 10;

  static double[][] staticModelDistanceMatrix = new double[existingTracks][states];
  static double[][] linearModelDistanceMatrix = new double[existingTracks][states];


  public static distanceMatrix distanceMatrix(List<Track> trackList,List<State> stateList){
    int i=0;
    int j=0;
    for(Track track : trackList){

      State lastState=track.getStateList().get(track.getStateList().size() - 1);//get the last state in the track
      for(State state : stateList){
        staticModelDistanceMatrix[i][j]= calcDistance(lastState,state.getFusionEllipse());
        linearModelDistanceMatrix[i][j]=calcDistanceBetweenEllipses(lastState.getFusionEllipse(),state.getFusionEllipse());
        j++;
      }
    i++;
    }

    return new distanceMatrix(staticModelDistanceMatrix,linearModelDistanceMatrix);
  }
  static double calcDistance(State state, CovarianceEllipse ellipse) {
    double dt = ellipse.getTimeStamp() - state.getFusionEllipse().getTimeStamp();
    CovarianceEllipse extrapolatedEllipse = extrapolateState(state, ellipse, dt);
    double distance = calcDistanceBetweenEllipses(extrapolatedEllipse, ellipse);
    return distance;
  }

  static CovarianceEllipse extrapolateState(State state, CovarianceEllipse ellipse, double dt) {
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

/*
    double[][] matrixData = { {state.getFusionEllipse().getSx2(), state.getFusionEllipse().getSxy()},
                              {state.getFusionEllipse().getSxy(), state.getFusionEllipse().getSy2()} };
    Array2DRowRealMatrix stateCovarianceMatrix = new Array2DRowRealMatrix(matrixData);
*/
    RealMatrix newCovarianceMatrix = phiMatrix.multiply(extrapolation4d).multiply(phiMatrix.transpose());

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

  private static class distanceMatrix {
    public distanceMatrix(double[][] staticModelDistanceMatrix,
                          double[][] linearModelDistanceMatrix) {
    }
  }
}
