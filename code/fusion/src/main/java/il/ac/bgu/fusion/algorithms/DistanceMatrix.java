package il.ac.bgu.fusion.algorithms;

/**
 * Created by Maayan on 08/06/2017.
 */


import il.ac.bgu.fusion.objects.CovarianceEllipse;
import il.ac.bgu.fusion.objects.State;
import il.ac.bgu.fusion.objects.Track;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static il.ac.bgu.fusion.util.LinearAlgebraUtils.calcDistanceBetweenEllipses;

/**
 * Implementation of the Distance Matrix algorithm.
 * Calculates statistical distance between ellipse and the last state of a track
 */

public class DistanceMatrix {
  private static final double THRESHOLD= 9.21;
  static int states = 10;
  static int existingTracks = 10;

  static double[][] staticModelDistanceMatrix = new double[existingTracks][states];
  static double[][] linearModelDistanceMatrix = new double[existingTracks][states];

  private static List<State> uncorrelatedClusters;
  private static List<Correlation> CorrelationList;
  private static List<State> clusters;
  private static List<Track> existedTracks;

  private static class distanceMatrix {
    public distanceMatrix(double[][] staticModelDistanceMatrix,
                          double[][] linearModelDistanceMatrix) {
    }
  }

  private static class Correlation{
    private Track track;
    private State state;
    private String model;

    public Correlation(Track track, State state, String model) {
      this.track = track;
      this.state = state;
      this.model = model;
    }
  }

  /////// Update:  //////////////
  public static void update(List<Correlation> correlations){
    for(Correlation correlationObject : correlations){
      if(correlationObject.model=="static"){
        ArrayList<State> stateArrayList=correlationObject.track.getStateList();
        stateArrayList.add(correlationObject.state);
        correlationObject.track.setStateList(stateArrayList);
      }
      else{//merge with the extrapolatedEllipse and then shirshur??


      }

    }
  }
  public static void createNewTracks(List<State> uncorrelatedClusters){
    for(State state : uncorrelatedClusters){
      Track track=new Track();
      ArrayList<State> stateArrayList=new ArrayList<>();
      stateArrayList.add(state);
      track.setStateList(stateArrayList);
      //add the new Track to the existing track list
    }
  }
////////////////// end of Update

  ////////////////////// distanceMatrix
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
////////////////// end of distanceMatrix

////////////////    M2M
  private static void M2M(){
    boolean haveMin=false;
    double minValue=0;
    String model="";
   Pair<Integer,Integer> index=null;
    double staticMin=findMinValue(staticModelDistanceMatrix).getFirst();
    double linearMin=findMinValue(linearModelDistanceMatrix).getFirst();
    if(staticMin-linearMin<=0) {
      minValue = staticMin;
      index=findMinValue(staticModelDistanceMatrix).getSecond();
      model="static";
    }
    else {
      minValue = linearMin;
      index=findMinValue(linearModelDistanceMatrix).getSecond();
      model="linear";
    }
    if (minValue < THRESHOLD)
      haveMin=true;

    while (haveMin){
      //create new Correlation object
      State s=clusters.get(index.getFirst());
      Track t=existedTracks.get(index.getSecond());
      Correlation newCorrelation=new Correlation(t,s,model);
      CorrelationList.add(newCorrelation);
      removeFromArray(index,staticModelDistanceMatrix);
      removeFromArray(index,linearModelDistanceMatrix);
      haveMin=false;
      if(staticMin-linearMin<=0) {
        minValue = staticMin;
        index=findMinValue(staticModelDistanceMatrix).getSecond();
      }
      else {
        minValue = linearMin;
        index=findMinValue(linearModelDistanceMatrix).getSecond();
      }
      if (minValue < THRESHOLD)
        haveMin=true;
    }
    addToUncorrelated(staticModelDistanceMatrix);

  }

  private static void addToUncorrelated(double[][] array){
    for(int i=0; i<array.length; i++){
      for (int j=0; j< array[i].length; j++){
        if(array[i][j]!=0){
          State s=clusters.get(i);
          if(!uncorrelatedClusters.contains(s))
            uncorrelatedClusters.add(s);
        }

      }
    }
  }

  private static void removeFromArray(Pair<Integer,Integer> index, double[][] array){
    int s=index.getFirst();
    int t=index.getSecond();
    for(int i=0; i<array.length; i++)
      array[i][t] = 0;
    for(int i=0; i<array[0].length; i++)
      array[s][i] = 0;
  }
  private static Pair<Double, Pair<Integer, Integer>> findMinValue(double[][] array){
    Pair<Integer,Integer> index=new Pair<Integer, Integer>(0,0);
    double min=array[0][0];
    for(int i=0; i<array.length; i++){
      for (int j=0; j< array[i].length; j++){
        if(array[i][j]<min) {
          min = array[i][j];
          index=new Pair<Integer, Integer>(i,j);
        }
      }
    }
   Pair<Double,Pair<Integer,Integer>> res=new Pair<Double, Pair<Integer, Integer>>(min,index);
    return res;
  }
///////////// end of M2M
}
