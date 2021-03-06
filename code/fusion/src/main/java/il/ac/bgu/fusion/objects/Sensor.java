package il.ac.bgu.fusion.objects;

/**
 * Created by Mali on 4/1/2017.
 */
public class Sensor {

  private long id;
  private double xCoordinate;
  private double yCoordinate;
  private String metadata;

  public Sensor(long id, double xCoordinate, double yCoordinate, String metadata) {
    this.id = id;
    this.xCoordinate = xCoordinate;
    this.yCoordinate = yCoordinate;
    this.metadata = metadata;
  }


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public double getyCoordinate() {
    return yCoordinate;
  }

  public void setyCoordinate(float yCoordinate) {
    this.yCoordinate = yCoordinate;
  }

  public double getxCoordinate() {
    return xCoordinate;
  }

  public void setxCoordinate(float xCoordinate) {
    this.xCoordinate = xCoordinate;
  }

  public String getMetadata() {
    return metadata;
  }

  public void setMetadata(String metadata) {
    this.metadata = metadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Sensor sensor = (Sensor) o;

    return id == sensor.id;
  }

  @Override
  public int hashCode() {
    return (int) (id ^ (id >>> 32));
  }

  @Override
  public String toString() {
    return "Sensor{" + "\n" +
           "id=" + id + "\n" +
           ", xCoordinate=" + xCoordinate + "\n" +
           ", yCoordinate=" + yCoordinate + "\n" +
           ", metadata='" + metadata + '\'' + "\n" +
           '}';
  }
}
