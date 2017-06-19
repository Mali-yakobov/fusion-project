package il.ac.bgu.fusion.objects;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mali on 4/1/2017.
 */
public class Track {

  public Track() {
  }//empty constructor

  public Track(long id, long startTime, long endTime, List<State> stateList) {
    this.id = id;
    this.startTime = startTime;
    this.endTime = endTime;
    this.stateList = stateList;

    this.color = null;
  }

  private long id;
  private long startTime;
  private long endTime;
  private List<State> stateList;

  private Color color;


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

  public List<State> getStateList() {
    return stateList;
  }

  public void setStateList(ArrayList<State> stateList) {
    this.stateList = stateList;
  }


  public Color getColor() {
    return this.color;
  }

  public void setColor(Color newColor) {
    this.color = newColor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Track track = (Track) o;

    return id == track.id;
  }

  @Override
  public int hashCode() {
    return (int) (id ^ (id >>> 32));
  }

  @Override
  public String toString() {
    return "Track{" + "\n" +
           "id=" + id + "\n" +
           ", startTime=" + startTime + "\n" +
           ", endTime=" + endTime + "\n" +
           ", stateList=" + stateList + "\n" +
           '}';
  }
}
