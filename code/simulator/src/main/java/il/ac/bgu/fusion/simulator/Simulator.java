package il.ac.bgu.fusion.simulator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import il.ac.bgu.fusion.algorithms.InitialClustering;
import il.ac.bgu.fusion.objects.*;
import il.ac.bgu.fusion.util.JsonReaderWriter;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * Created by SB50307 on 19/06/2017.
 */
public class Simulator {

  public static final long MILLIS = 1_000;

  public static void main(String... args) throws FileNotFoundException {
    SimulatorConfig simulatorConfig = getSimulatorConfig("simulator-config.json");
    Simulator simulator = new Simulator();
    SimulationResult result = simulator.simulate(simulatorConfig);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    System.out.println(gson.toJson(result));

    JsonReaderWriter.objectToJson(result.getPointInTimes(), "simulatorScenarioPoints1");
    JsonReaderWriter.sensorToJson(new ArrayList<>(result.getSensors()), "simulatorScenarioSensors1");

  }

  public static SimulatorConfig getSimulatorConfig(String resourceName) throws FileNotFoundException {
    ClassLoader classLoader = Simulator.class.getClassLoader();
    File file = new File(classLoader.getResource(resourceName).getFile());
    Gson gson = new Gson();
    JsonReader reader = new JsonReader(new FileReader(file));
    return gson.fromJson(reader, SimulatorConfig.class);
  }

  public Set<Sensor> getSensors(SimulatorConfig simulatorConfig, Random random) {
    Set<Sensor> sensors = new HashSet<>();
    for (int sensorIndex = 0; sensorIndex < simulatorConfig.getNumberOfSensors(); sensorIndex++) {
      double x = random.nextDouble() * simulatorConfig.getMaxWidth();
      double y = random.nextDouble() * simulatorConfig.getMaxLength();
      sensors.add(new Sensor(sensorIndex, x, y, "Sensor_" + sensorIndex));
    }
    return sensors;
  }

  public SimulationResult simulate(SimulatorConfig simulatorConfig){
    List<PointInTime> pointInTimes = new LinkedList<>();
    Random random = new Random(simulatorConfig.getSeed());
    Set<Sensor> sensors = getSensors(simulatorConfig, random);
    Set<Track> tracks = new HashSet<>();
    createInitialTracks(simulatorConfig, tracks, sensors, random, 0);
    pointInTimes.add(dumpTracks(tracks, 0));
    for(int iteration = 1; iteration < simulatorConfig.getSimulationTimeSeconds(); iteration++){
      simulateNextStep(simulatorConfig, sensors, tracks, random, iteration * 1000);
      pointInTimes.add(dumpTracks(tracks, iteration));
    }
    return new SimulationResult(sensors, pointInTimes);

  }

  private PointInTime dumpTracks(Set<Track> tracks, long currentTime) {
    List<Track> trackList = new LinkedList<>();
    tracks.forEach(track -> trackList.add(deepCopy(track)));
    return new PointInTime(currentTime, trackList);
  }

  private Track deepCopy(Track track) {
    return new Track(track.getId(), track.getStartTime(), track.getEndTime(), deepCopy(track.getStateList()));
  }

  private List<State> deepCopy(List<State> stateList) {
    List<State> states = new LinkedList<>();
    stateList.forEach(state -> states.add(deepCopy(state)));
    return states;
  }

  private State deepCopy(State state) {
    List<CovarianceEllipse> newEllipses = new LinkedList<>();
    state.getEllipseList().forEach(ellipse -> newEllipses.add(deepCopy(ellipse)));
    return new State(newEllipses, deepCopy(state.getFusionEllipse()));
  }

  private CovarianceEllipse deepCopy(CovarianceEllipse fusionEllipse) {
    CovarianceEllipse ellipse = new CovarianceEllipse(fusionEllipse.getTimeStamp(),
                                                      fusionEllipse.getId(),
                                                      fusionEllipse.getSensor(),
                                                      fusionEllipse.getCentreX(),
                                                      fusionEllipse.getCentreY(),
                                                      fusionEllipse.getSx2(),
                                                      fusionEllipse.getSy2(),
                                                      fusionEllipse.getSxy(),
                                                      fusionEllipse.getVx(),
                                                      fusionEllipse.getVy(),
                                                      fusionEllipse.getSvx2(),
                                                      fusionEllipse.getSvy2(),
                                                      fusionEllipse.getSvxy());
    ellipse.setIsFusionEllipse(fusionEllipse.getIsFusionEllipse());
    return ellipse;
  }

  private void createInitialTracks(SimulatorConfig simulatorConfig,
                                   Set<Track> tracks,
                                   Set<Sensor> sensors,
                                   Random random,
                                   long currentTime) {
    for(int trackIndex = 0; trackIndex < simulatorConfig.getInitialNumberOfEntities(); trackIndex++){
      tracks.add(createNewTrack(simulatorConfig, sensors, random, currentTime));
    }
  }

  public void simulateNextStep(SimulatorConfig simulatorConfig,
                               Set<Sensor> sensors,
                               Set<Track> tracks,
                               Random random,
                               long currentTime) {
    tryDeleteTracks(simulatorConfig, tracks, random, currentTime);
    augmentExistingTracks(simulatorConfig, sensors, tracks, random, currentTime);
    tryCreateNewTrack(simulatorConfig, sensors, tracks, random, currentTime);
  }

  private void tryCreateNewTrack(SimulatorConfig simulatorConfig,
                                 Set<Sensor> sensors,
                                 Set<Track> tracks,
                                 Random random,
                                 long currentTime) {
    double value = random.nextDouble();
    if (value < simulatorConfig.getNewEntityProbability()) {
      tracks.add(createNewTrack(simulatorConfig, sensors, random, currentTime));
    }
  }

  private Track createNewTrack(SimulatorConfig simulatorConfig, Set<Sensor> sensors, Random random, long currentTime) {
    double x = random.nextDouble() * simulatorConfig.getMaxWidth();
    double y = random.nextDouble() * simulatorConfig.getMaxLength();
    double azimuth = random.nextDouble() * 2 * Math.PI;
    double velocity =
        simulatorConfig.getVelocity() + simulatorConfig.getVelocityDelta() * (2 * random.nextDouble() - 1);
    Collection<CovarianceEllipse> detections =
        createDetectionsForPosition(simulatorConfig, random, sensors, x, y, velocity, azimuth, currentTime);
    State state = mergeDetections(detections);
    state.getFusionEllipse().setIsFusionEllipse(true);
    List<State> states = new LinkedList<>();
    states.add(state);
    return new Track(random.nextLong(), currentTime, currentTime, states);
  }

  private void tryDeleteTracks(SimulatorConfig simulatorConfig, Set<Track> tracks, Random random, long currentTime) {
    Set<Track> tracksToRemove = new HashSet<>();
    tracks.forEach(track -> {
      long startTime = track.getStartTime();
      double deltaTime = currentTime - startTime;
      double decay = Math.pow(2, deltaTime / simulatorConfig.getEntityHalfLifeTimeSeconds());
      double shouldDecay = random.nextDouble();
      if (shouldDecay > decay) {
        tracksToRemove.add(track);
      }
    });
    tracks.removeAll(tracksToRemove);
  }

  private void augmentExistingTracks(SimulatorConfig simulatorConfig,
                                     Set<Sensor> sensors,
                                     Set<Track> tracks,
                                     Random random,
                                     long currentTime) {
    tracks.forEach(track -> augmentTrack(simulatorConfig, sensors, track, random, currentTime));
  }

  private void augmentTrack(SimulatorConfig simulatorConfig,
                            Set<Sensor> sensors,
                            Track track,
                            Random random,
                            long currentTime) {
    List<State> states = track.getStateList();
    int numberOfStates = states.size();
    State lastState = states.get(numberOfStates - 1);
    double azimuth = calcNextAzimuth(simulatorConfig, random, states, numberOfStates);
    double vx = lastState.getFusionEllipse().getVx();
    double vy = lastState.getFusionEllipse().getVy();
    double velocity = Math.sqrt(vx * vx + vy * vy);
    double velocityDelta = simulatorConfig.getVelocityDelta() * (random.nextDouble() * 2 - 1);
    velocity += velocityDelta;
    double distance = velocity * (currentTime - lastState.getEndTimeStamp()) / MILLIS;
    double x = lastState.getFusionEllipse().getCentreX() + distance * Math.cos(azimuth);
    double y = lastState.getFusionEllipse().getCentreY() + distance * Math.sin(azimuth);
    Collection<CovarianceEllipse> detections =
        createDetectionsForPosition(simulatorConfig, random, sensors, x, y, velocity, azimuth, currentTime);
    State nextState = mergeDetections(detections);
    nextState.getFusionEllipse().setIsFusionEllipse(true);
    states.add(nextState);
    track.setEndTime(currentTime);
  }

  private Collection<CovarianceEllipse> createDetectionsForPosition(SimulatorConfig simulatorConfig,
                                                                    Random random,
                                                                    Set<Sensor> sensors,
                                                                    double x,
                                                                    double y,
                                                                    double velocity, double azimuth, long currentTime) {
    Set<CovarianceEllipse> detections = new HashSet<>();
    sensors.forEach(sensor -> detections.add(createDetectionForSensor(simulatorConfig,
                                                                      random,
                                                                      sensor,
                                                                      x,
                                                                      y,
                                                                      velocity,
                                                                      azimuth,
                                                                      currentTime)));
    return detections;
  }

  private CovarianceEllipse createDetectionForSensor(SimulatorConfig simulatorConfig,
                                                     Random random,
                                                     Sensor sensor,
                                                     double x,
                                                     double y,
                                                     double velocity,
                                                     double velocityDirection,
                                                     long currentTime) {
    double deltaXFromSensor = x - sensor.getxCoordinate();
    double deltaYFromSensor = y - sensor.getyCoordinate();
    double distanceFromSensor = Math.sqrt(deltaXFromSensor * deltaXFromSensor + deltaYFromSensor * deltaYFromSensor);
    double angleFromSensor = Math.atan2(deltaYFromSensor, deltaXFromSensor);

    double radialSTD = Math.sqrt(simulatorConfig.getRadialCovariance());
    double azimuthalSTD = Math.sqrt(simulatorConfig.getAzimuthalCovariance());

    double detectionRadialDistance = distanceFromSensor + random.nextGaussian() * radialSTD;
    double azimuthalError = random.nextGaussian() * azimuthalSTD;
    double detectionAzimuth = angleFromSensor + azimuthalError;

    double detectionX = sensor.getxCoordinate() + detectionRadialDistance * Math.cos(detectionAzimuth);
    double detectionY = sensor.getyCoordinate() + detectionRadialDistance * Math.sin(detectionAzimuth);

    RealMatrix radialCovarianceMatrix
        = new Array2DRowRealMatrix(new double[][]{{radialSTD * radialSTD, 0},
                                                  {0, azimuthalError * azimuthalError * detectionRadialDistance * detectionRadialDistance}});
    RealMatrix rotationMatrix
        = new Array2DRowRealMatrix(new double[][]{{Math.cos(angleFromSensor), -Math.sin(angleFromSensor)},
                                                  {Math.sin(angleFromSensor), Math.cos(angleFromSensor)}});
    RealMatrix rotationMatrixInverse
        = new Array2DRowRealMatrix(new double[][]{{Math.cos(angleFromSensor), Math.sin(angleFromSensor)},
                                                  {-Math.sin(angleFromSensor), Math.cos(angleFromSensor)}});
    RealMatrix cartesianCovarianceMatrix =
        rotationMatrixInverse.multiply(radialCovarianceMatrix.multiply(rotationMatrix));
    return new CovarianceEllipse(currentTime,
                                 random.nextLong(),
                                 sensor,
                                 detectionX,
                                 detectionY,
                                 cartesianCovarianceMatrix.getEntry(0, 0),
                                 cartesianCovarianceMatrix.getEntry(1, 1),
                                 cartesianCovarianceMatrix.getEntry(0, 1),
                                 velocity * Math.cos(velocityDirection),
                                 velocity * Math.sin(velocityDirection),
                                 simulatorConfig.getVelocityDelta() * simulatorConfig.getVelocityDelta(),
                                 simulatorConfig.getVelocityDelta() * simulatorConfig.getVelocityDelta(),
                                 0);
  }

  private State mergeDetections(Collection<CovarianceEllipse> detections) {
    final boolean[] firstEllipse = {true};
    final State[] state = new State[1];
    detections.forEach(detection -> {
      if (firstEllipse[0]) {
        List<CovarianceEllipse> rawData = new LinkedList<>();
        rawData.add(detection);
        state[0] = new State(rawData, detection);
        firstEllipse[0] = false;
      } else {
        InitialClustering.merge(state[0], detection);
      }
    });
    return state[0];
  }

  private double calcNextAzimuth(SimulatorConfig simulatorConfig,
                                 Random random,
                                 List<State> states,
                                 int numberOfStates) {
    State lastState = states.get(numberOfStates - 1);
    double deltaY = lastState.getFusionEllipse().getVy();
    double deltaX = lastState.getFusionEllipse().getVx();
    double azimuth = Math.atan2(deltaY, deltaX);
    double directionDelta = simulatorConfig.getDirectionDelta();
    double deltaAzimuth = directionDelta * (random.nextDouble() * 2 - 1);
    azimuth += deltaAzimuth;
    return azimuth;
  }
}
