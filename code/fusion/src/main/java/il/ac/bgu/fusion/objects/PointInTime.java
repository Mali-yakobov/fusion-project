package il.ac.bgu.fusion.objects;

import java.util.ArrayList;

/**
 * Created by Mali on 4/1/2017.
 */
public class PointInTime {

    private long timeStamp;
    private ArrayList<Track> trackList;

    public PointInTime(){} //empty constructor

    public PointInTime(long timeStamp, ArrayList<Track> trackList) {
        this.timeStamp = timeStamp;
        this.trackList = trackList;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ArrayList<Track> getTrackList() {
        return trackList;
    }

    public void setTrackList(ArrayList<Track> trackList) {
        this.trackList = trackList;
    }

    @Override
    public String toString() {
        return "PointInTime{" +
                "timeStamp=" + timeStamp +
                ", trackList=" + trackList +
                '}';
    }
}
