package il.ac.bgu.fusion.simulator;

/**
 * Created by SB50307 on 19/06/2017.
 */
public class SimulatorConfig {

  private long seed;
  private double maxWidth;
  private double maxLength;
  private double detectionBoundingBoxSize;
  private double radialCovariance;
  private double radialCovarianceDelta;
  private double azimuthalCovariance;
  private double azimuthalCovarianceDelta;
  private int numberOfSensors;
  private double staticProbability;
  private int initialNumberOfEntities;
  private long entityHalfLifeTimeSeconds;
  private long simulationTimeSeconds;
  private int newEntityProbability;
  private double velocity;
  private double velocityDelta;
  private double directionDelta;

  public SimulatorConfig() {
  }

  public SimulatorConfig(long seed,
                         double maxWidth,
                         double maxLength,
                         double detectionBoundingBoxSize,
                         double radialCovariance,
                         double radialCovarianceDelta,
                         double azimuthalCovariance,
                         double azimuthalCovarianceDelta,
                         int numberOfSensors,
                         double staticProbability,
                         int initialNumberOfEntities,
                         long entityHalfLifeTimeSeconds,
                         long simulationTimeSeconds,
                         int newEntityProbability,
                         double velocity,
                         double velocityDelta,
                         double directionDelta) {
    this.seed = seed;
    this.maxWidth = maxWidth;
    this.maxLength = maxLength;
    this.detectionBoundingBoxSize = detectionBoundingBoxSize;
    this.radialCovariance = radialCovariance;
    this.radialCovarianceDelta = radialCovarianceDelta;
    this.azimuthalCovariance = azimuthalCovariance;
    this.azimuthalCovarianceDelta = azimuthalCovarianceDelta;
    this.numberOfSensors = numberOfSensors;
    this.staticProbability = staticProbability;
    this.initialNumberOfEntities = initialNumberOfEntities;
    this.entityHalfLifeTimeSeconds = entityHalfLifeTimeSeconds;
    this.simulationTimeSeconds = simulationTimeSeconds;
    this.newEntityProbability = newEntityProbability;
    this.velocity = velocity;
    this.velocityDelta = velocityDelta;
    this.directionDelta = directionDelta;
  }

  public long getSeed() {
    return seed;
  }

  public SimulatorConfig setSeed(long seed) {
    this.seed = seed;
    return this;
  }

  public double getMaxWidth() {
    return maxWidth;
  }

  public SimulatorConfig setMaxWidth(double maxWidth) {
    this.maxWidth = maxWidth;
    return this;
  }

  public double getMaxLength() {
    return maxLength;
  }

  public SimulatorConfig setMaxLength(double maxLength) {
    this.maxLength = maxLength;
    return this;
  }

  public double getDetectionBoundingBoxSize() {
    return detectionBoundingBoxSize;
  }

  public SimulatorConfig setDetectionBoundingBoxSize(double detectionBoundingBoxSize) {
    this.detectionBoundingBoxSize = detectionBoundingBoxSize;
    return this;
  }

  public double getRadialCovariance() {
    return radialCovariance;
  }

  public SimulatorConfig setRadialCovariance(double radialCovariance) {
    this.radialCovariance = radialCovariance;
    return this;
  }

  public double getRadialCovarianceDelta() {
    return radialCovarianceDelta;
  }

  public SimulatorConfig setRadialCovarianceDelta(double radialCovarianceDelta) {
    this.radialCovarianceDelta = radialCovarianceDelta;
    return this;
  }

  public double getAzimuthalCovariance() {
    return azimuthalCovariance;
  }

  public SimulatorConfig setAzimuthalCovariance(double azimuthalCovariance) {
    this.azimuthalCovariance = azimuthalCovariance;
    return this;
  }

  public double getAzimuthalCovarianceDelta() {
    return azimuthalCovarianceDelta;
  }

  public SimulatorConfig setAzimuthalCovarianceDelta(double azimuthalCovarianceDelta) {
    this.azimuthalCovarianceDelta = azimuthalCovarianceDelta;
    return this;
  }

  public int getNumberOfSensors() {
    return numberOfSensors;
  }

  public SimulatorConfig setNumberOfSensors(int numberOfSensors) {
    this.numberOfSensors = numberOfSensors;
    return this;
  }

  public double getStaticProbability() {
    return staticProbability;
  }

  public SimulatorConfig setStaticProbability(double staticProbability) {
    this.staticProbability = staticProbability;
    return this;
  }

  public int getInitialNumberOfEntities() {
    return initialNumberOfEntities;
  }

  public SimulatorConfig setInitialNumberOfEntities(int initialNumberOfEntities) {
    this.initialNumberOfEntities = initialNumberOfEntities;
    return this;
  }

  public long getEntityHalfLifeTimeSeconds() {
    return entityHalfLifeTimeSeconds;
  }

  public SimulatorConfig setEntityHalfLifeTimeSeconds(long entityHalfLifeTimeSeconds) {
    this.entityHalfLifeTimeSeconds = entityHalfLifeTimeSeconds;
    return this;
  }

  public long getSimulationTimeSeconds() {
    return simulationTimeSeconds;
  }

  public SimulatorConfig setSimulationTimeSeconds(long simulationTimeSeconds) {
    this.simulationTimeSeconds = simulationTimeSeconds;
    return this;
  }

  public int getNewEntityProbability() {
    return newEntityProbability;
  }

  public SimulatorConfig setNewEntityProbability(int newEntityProbability) {
    this.newEntityProbability = newEntityProbability;
    return this;
  }

  public double getVelocity() {
    return velocity;
  }

  public SimulatorConfig setVelocity(double velocity) {
    this.velocity = velocity;
    return this;
  }

  public double getVelocityDelta() {
    return velocityDelta;
  }

  public SimulatorConfig setVelocityDelta(double velocityDelta) {
    this.velocityDelta = velocityDelta;
    return this;
  }

  public double getDirectionDelta() {
    return directionDelta;
  }

  public SimulatorConfig setDirectionDelta(double directionDelta) {
    this.directionDelta = directionDelta;
    return this;
  }

  @Override
  public String toString() {
    return "SimulatorConfig{" +
           "seed=" + seed +
           ", maxWidth=" + maxWidth +
           ", maxLength=" + maxLength +
           ", detectionBoundingBoxSize=" + detectionBoundingBoxSize +
           ", radialCovariance=" + radialCovariance +
           ", radialCovarianceDelta=" + radialCovarianceDelta +
           ", azimuthalCovariance=" + azimuthalCovariance +
           ", azimuthalCovarianceDelta=" + azimuthalCovarianceDelta +
           ", numberOfSensors=" + numberOfSensors +
           ", staticProbability=" + staticProbability +
           ", initialNumberOfEntities=" + initialNumberOfEntities +
           ", entityHalfLifeTimeSeconds=" + entityHalfLifeTimeSeconds +
           ", simulationTimeSeconds=" + simulationTimeSeconds +
           ", newEntityProbability=" + newEntityProbability +
           ", velocity=" + velocity +
           ", velocityDelta=" + velocityDelta +
           ", directionDelta=" + directionDelta +
           '}';
  }

}
