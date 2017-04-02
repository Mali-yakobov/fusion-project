package il.ac.bgu.fusion.objects;

import java.util.Hashtable;

/**
 * Created by Mali on 4/2/2017.
 */
public class DataBase {

    private Hashtable<Long,Sensor> sensors;

    public DataBase(){}//empty constructor

    public DataBase(Hashtable<Long, Sensor> sensors) {
        this.sensors = sensors;
    }

    public Hashtable<Long, Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(Hashtable<Long, Sensor> sensors) {
        this.sensors = sensors;
    }

    @Override
    public String toString() {
        return "DataBase{" +"/n"+
                "sensors=" + sensors +"/n"+
                '}';
    }
}
