package il.ac.bgu.fusion.objects;

/**
 * Created by Mali on 4/1/2017.
 */
public class Sensor {

    private long id;
    private float xCoordinate;
    private float yCoordinate;
    private String metadata;

    public Sensor(){}//empty constructor

    public Sensor(long id, float xCoordinate, float yCoordinate, String metadata) {
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

    public float getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(float yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public float getxCoordinate() {
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
    public String toString() {
        return "Sensor{" +"\n"+
                "id=" + id +"\n"+
                ", xCoordinate=" + xCoordinate +"\n"+
                ", yCoordinate=" + yCoordinate +"\n"+
                ", metadata='" + metadata + '\'' +"\n"+
                '}';
    }
}
