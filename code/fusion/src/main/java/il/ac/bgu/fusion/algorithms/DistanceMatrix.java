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
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static il.ac.bgu.fusion.algorithms.InitialClustering.merge;
import static il.ac.bgu.fusion.util.LinearAlgebraUtils.calcDistanceBetweenEllipses;

/**
 * Implementation of the Distance Matrix algorithm.
 * Calculates statistical distance between ellipse and the last state of a track
 */

public class DistanceMatrix {

  private static final double THRESHOLD = 9.21;
  static int initialClusteringListSize;
  static int existingTracksSize;
  static double[][] staticModelDistanceMatrix;
  static double[][] linearModelDistanceMatrix;
  private static Random rand = new Random();

  private static List<State> uncorrelatedClusters;
  private static List<Correlation> CorrelationList ;
  //private static List<State> clusters;
  //private static List<Track> existedTracks;

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

  ////////////////////// distanceMatrix
  public static void distanceMatrix(List<Track> existingTracks,List<State> initialClusteringList) {
    initialClusteringListSize = initialClusteringList.size();
      existingTracksSize = existingTracks.size();
      staticModelDistanceMatrix = new double[existingTracksSize][initialClusteringListSize];
      linearModelDistanceMatrix = new double[existingTracksSize][initialClusteringListSize];
      CorrelationList = new LinkedList<>();
      uncorrelatedClusters = new LinkedList<>();

      int i = 0;
      int j = 0;
      for (Track track : existingTracks) {
        State lastState = track.getStateList().get(track.getStateList().size() - 1);//get the last state in the track
        for (State state : initialClusteringList) {
          staticModelDistanceMatrix[i][j] =
              calcDistanceBetweenEllipses(lastState.getFusionEllipse(), state.getFusionEllipse());
          linearModelDistanceMatrix[i][j] =calcDistance(lastState, state.getFusionEllipse()); //calcDistance(lastState, state.getFusionEllipse());
          j++;
        }
        i++;
      }
    System.out.println("**********************");
    System.out.println(staticModelDistanceMatrix[0].length);
  }

  public static double calcDistance(State state, CovarianceEllipse ellipse) {
    double dt = ellipse.getTimeStamp() - state.getEndTimeStamp();
    CovarianceEllipse extrapolatedEllipse = extrapolateState(state, ellipse, dt);
    double distance = calcDistanceBetweenEllipses(extrapolatedEllipse, ellipse);
    return distance;
  }

  public static CovarianceEllipse extrapolateState(State state, CovarianceEllipse ellipse, double dt) {
    double state4d[][] ={
        {state.getFusionEllipse().getCentreY()}, {state.getFusionEllipse().getVx()},
        {state.getFusionEllipse().getCentreY()}, {state.getFusionEllipse().getVy()}

    }; //Vector representing the kinematic data of the state

    double phi[][] ={
                      { 1, dt, 0, 0 },
                      { 0, 1, 0, 0  },
                      { 0, 0, 1, dt },
                      { 0, 0, 0, 1  }

      };

    Array2DRowRealMatrix state4dMatrix = new Array2DRowRealMatrix(state4d);
    System.out.println("RowDimension" + state4dMatrix.getRowDimension() + "ColumnDimension" + state4dMatrix.getColumnDimension());
    Array2DRowRealMatrix phiMatrix = new Array2DRowRealMatrix(phi);

    RealMatrix extrapolation4d = phiMatrix.multiply(state4dMatrix); //extrapolation4d is now {x, vx, y, vy}
    CovarianceEllipse fusEllipse=state.getFusionEllipse();
    double stateCovarianceArray [][] ={
        { fusEllipse.getSx2(), 0, fusEllipse.getSxy(), 0 },
        { 0, fusEllipse.getSvx2(), 0, fusEllipse.getSvxy() },
        { fusEllipse.getSxy(), 0, fusEllipse.getSy2(), 0 },
        { 0, fusEllipse.getSvxy() , 0, fusEllipse.getSvy2() }
    };

    Array2DRowRealMatrix stateCovariance = new Array2DRowRealMatrix(stateCovarianceArray);
    RealMatrix newCovarianceMatrix = phiMatrix.multiply(stateCovariance).multiply(phiMatrix.transpose());

    CovarianceEllipse returnedEllipse = new CovarianceEllipse(ellipse.getTimeStamp(), ellipse.getId() ,ellipse.getSensor(),
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
  public static void M2M(List<Track> existingTracks,List<State> initialClusteringList){// add parameters
    System.out.println("after inside M2M");
    boolean haveMin = false;
    double minValue = 0;
    String model = "";
   Pair<Integer,Integer> index = null; ///<Track,State>
    double staticMin = findMinValue(staticModelDistanceMatrix).getFirst();
    double linearMin = findMinValue(linearModelDistanceMatrix).getFirst();
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
    if (minValue < THRESHOLD)//??
      haveMin=true;

    while (haveMin){
      //create new Correlation object

      Track t = existingTracks.get(index.getFirst());
      State s = initialClusteringList.get(index.getSecond());

      Correlation newCorrelation = new Correlation(t,s,model);
      CorrelationList.add(newCorrelation);

      removeFromArray(index,staticModelDistanceMatrix);
      removeFromArray(index,linearModelDistanceMatrix);
      haveMin=false;

      staticMin=findMinValue(staticModelDistanceMatrix).getFirst();
      linearMin=findMinValue(linearModelDistanceMatrix).getFirst();

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
      if (minValue < THRESHOLD && minValue!= -1)//??
        haveMin=true;
    }

    addToUncorrelated(staticModelDistanceMatrix, initialClusteringList);

  }

  private static void addToUncorrelated(double[][] distanceMatrix , List<State> initialClusteringList){
    for(int i=0; i<distanceMatrix.length; i++){
        if(distanceMatrix[0][i] != -1){
          State s = initialClusteringList.get(i);
          if(!uncorrelatedClusters.contains(s)) //???
            uncorrelatedClusters.add(s);
        }
      }
  }

  private static void removeFromArray(Pair<Integer,Integer> index, double[][] array){
    int t=index.getFirst();
    int s=index.getSecond();
    for(int i=0; i<array.length; i++)//remove row (track)
      array[t][i] = -1;
    for(int i=0; i<array[0].length; i++) //remove col (state)
      array[i][s] = -1;
  }

  private static Pair<Double, Pair<Integer, Integer>> findMinValue(double[][] array){
    Pair<Integer,Integer> index=new Pair<Integer, Integer>(0,0);
    double min = array[0][0];
    for (int i=0; i<array.length; i++){
      for (int j=0; j< array[i].length; j++){
        if(array[i][j]<min) {
          min = array[i][j];
          index=new Pair<Integer, Integer>(i,j);
        }
      }
    }
   Pair<Double,Pair<Integer,Integer>> res = new Pair<Double, Pair<Integer, Integer>>(min,index);
    return res;
  }
///////////// end of M2M

  /////// Update:  //////////////
  public static void update(List<Track> existingTracks){
    for (Correlation correlationObject : CorrelationList){
      State newState = correlationObject.state;
      List<State> stateArrayList = correlationObject.track.getStateList();
      State lastState = stateArrayList.get(stateArrayList.size()-1);
      if (correlationObject.model=="static"){
        merge(lastState, newState.getFusionEllipse());
        lastState.getEllipseList().addAll(newState.getEllipseList()); // update raw data
      }
      else {//correlationObject.model=="linear"
        // merge with the extrapolatedEllipse and then concatenation
        double dt = newState.getFusionEllipse().getTimeStamp() - lastState.getEndTimeStamp();
        CovarianceEllipse extrapolatedEllipse = extrapolateState(lastState, newState.getFusionEllipse(), dt);
        merge(newState, extrapolatedEllipse);
        stateArrayList.add(newState);
      }
    }
    createNewTracks(uncorrelatedClusters, existingTracks);
    uncorrelatedClusters.clear();
    CorrelationList.clear();

  }

  public static void createNewTracks(List<State> uncorrelatedClusters, List<Track> existingTracks){
    for(State state : uncorrelatedClusters){
      ArrayList<State> stateArrayList = new ArrayList<>();
      stateArrayList.add(state);
      Track track = new Track(0, state.getStartTimeStamp(), state.getEndTimeStamp(), stateArrayList);
      track.setId(rand.nextLong());
      existingTracks.add(track);
    }

  }
////////////////// end of Update

}
