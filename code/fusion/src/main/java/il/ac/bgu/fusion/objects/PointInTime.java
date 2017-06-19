package il.ac.bgu.fusion.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mali on 4/1/2017.
 */
public class PointInTime {

    private long timeStamp;
    private List<Track> trackList;

    public PointInTime(){} //empty constructor

    public PointInTime(long timeStamp, List<Track> trackList) {
        this.timeStamp = timeStamp;
        this.trackList = trackList;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<Track> getTrackList() {
        return trackList;
    }

    public void setTrackList(List<Track> trackList) {
        this.trackList = trackList;
    }

    @Override
    public String toString() {
        return "PointInTime{" +"\n"+
                "timeStamp=" + timeStamp +"\n"+
                ", trackList=" + trackList +"\n"+
                '}';
    }
}
