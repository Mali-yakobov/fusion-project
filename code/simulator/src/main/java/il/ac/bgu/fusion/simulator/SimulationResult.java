package il.ac.bgu.fusion.simulator;

import il.ac.bgu.fusion.objects.PointInTime;
import il.ac.bgu.fusion.objects.Sensor;

import java.util.List;
import java.util.Set;

/**
 * Created by SB50307 on 19/06/2017.
 */
public class SimulationResult {

  Set<Sensor> sensors;
  List<PointInTime> pointInTimes;

  public SimulationResult(Set<Sensor> sensors, List<PointInTime> pointInTimes) {
    this.sensors = sensors;
    this.pointInTimes = pointInTimes;
  }

  public Set<Sensor> getSensors() {
    return sensors;
  }

  public SimulationResult setSensors(Set<Sensor> sensors) {
    this.sensors = sensors;
    return this;
  }

  public List<PointInTime> getPointInTimes() {
    return pointInTimes;
  }

  public SimulationResult setPointInTimes(List<PointInTime> pointInTimes) {
    this.pointInTimes = pointInTimes;
    return this;
  }
}
