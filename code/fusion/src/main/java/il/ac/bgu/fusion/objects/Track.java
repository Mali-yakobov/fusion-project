package il.ac.bgu.fusion.objects;

import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by Mali on 4/1/2017.
 */
public class Track {

    public Track(){}//empty constructor

    public Track(long id, long startTime, long endTime, ArrayList<State> stateList) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.stateList = stateList;
    }

    private long id;
    private long startTime;
    private long endTime;
    private ArrayList<State> stateList;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public ArrayList<State> getStateList() {
        return stateList;
    }

    public void setStateList(ArrayList<State> stateList) {
        this.stateList = stateList;
    }


    @Override
    public String toString() {
        return "Track{" +"\n"+
                "id=" + id +"\n"+
                ", startTime=" + startTime +"\n"+
                ", endTime=" + endTime +"\n"+
                ", stateList=" + stateList +"\n"+
                '}';
    }
}
