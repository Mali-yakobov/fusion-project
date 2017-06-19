package il.ac.bgu.visualization.objects;

import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MVCArray;
import com.lynden.gmapsfx.shapes.Polyline;
import com.lynden.gmapsfx.shapes.PolylineOptions;
import il.ac.bgu.visualization.util.EllipseBuilder;
import javafx.scene.shape.Ellipse;
import org.jscience.geography.coordinates.UTM;

import javax.measure.unit.SI;
import java.util.ArrayList;

import static org.jscience.geography.coordinates.UTM.utmToLatLong;
import static org.jscience.geography.coordinates.crs.ReferenceEllipsoid.WGS84;

/**
 * Created by Mali on 6/6/2017.
 */
public class VizualEllipse {
  private double centreX;
  private double centreY;
  private double radiusX;
  private double radiusY;
  private double angle;
  private long timeStamp;
  private long id;

  private boolean IsFusionEllipse;
  private ArrayList<VizualEllipse> rawList = null;
  private com.lynden.gmapsfx.shapes.Polyline polylineObject;

  private static final int zone=36;
  private static final char hemisphere='N';
  private boolean visible;
  private boolean visibleRaw;
  private LatLong latLong;
  private boolean clicked;

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  private String color;


  public VizualEllipse(){
    visible=true;
    visibleRaw=false;
    clicked=false;
  }
  public VizualEllipse(double radiusX, double radiusY) {
    visible=true;
    visibleRaw=false;
    clicked=false;
    setRadiusX(radiusX);
    setRadiusY(radiusY);
  }


  public com.lynden.gmapsfx.shapes.Polyline ellipseToDraw(String color, double strokeWeight){
    this.color= color;
    org.jscience.geography.coordinates.UTM c= org.jscience.geography.coordinates.UTM.valueOf(zone, hemisphere, centreX, centreY, SI.METRE);
    org.jscience.geography.coordinates.LatLong centerPTemp= utmToLatLong(c, WGS84);

    LatLong centerP= new LatLong(centerPTemp.getCoordinates()[1], centerPTemp.getCoordinates()[0]);
    MVCArray polylineArray = EllipseBuilder.buildEllipsePoints(centerP, radiusX, radiusY, angle);

    double opacity= 0.6;
    if (this.isFusionEllipse())
      opacity= 1;

    PolylineOptions polylineOptions= new PolylineOptions().path(polylineArray).strokeColor(color).clickable(true).strokeWeight(strokeWeight).strokeOpacity(opacity);
    com.lynden.gmapsfx.shapes.Polyline polyline = new com.lynden.gmapsfx.shapes.Polyline(polylineOptions);
    setPolylineObject(polyline);
    this.latLong=centerP;
    return polyline;
  }

  public void addToRaw(VizualEllipse ellipse) {
    if (this.rawList == null) {
      this.rawList = new ArrayList<VizualEllipse>();
    }
    this.rawList.add(ellipse);
  }


  public double getCentreX() {
    return centreX;
  }

  public void setCentreX(double centreX) {
    this.centreX = centreX;
  }

  public double getCentreY() {
    return centreY;
  }

  public void setCentreY(double centreY) {
    this.centreY = centreY;
  }

  public double getRadiusX() {
    return radiusX;
  }

  public void setRadiusX(double radiusX) {
    this.radiusX = radiusX;
  }

  public double getRadiusY() {
    return radiusY;
  }

  public void setRadiusY(double radiusY) {
    this.radiusY = radiusY;
  }

  public double getAngle() {
    return angle;
  }

  public void setAngle(double angle) {
    this.angle = angle;
  }

  public boolean isFusionEllipse() {
    return IsFusionEllipse;
  }

  public void setFusionEllipse(boolean fusionEllipse) {
    IsFusionEllipse = fusionEllipse;
  }

  public ArrayList<VizualEllipse> getRawList() {
    return rawList;
  }

  public void setRawList(ArrayList<VizualEllipse> rawList) {
    this.rawList = rawList;
  }



  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public Polyline getPolylineObject() {
    return polylineObject;
  }

  public void setPolylineObject(Polyline polylineObject) {
    this.polylineObject = polylineObject;
  }




  public LatLong getLatLong() {
    return latLong;
  }

  public void setLatLong(LatLong latLong) {
    this.latLong = latLong;
  }

  public boolean isVisibleRaw() {
    return visibleRaw;
  }

  public void setVisibleRaw(boolean visibleRaw) {
    this.visibleRaw = visibleRaw;
  }

  public boolean isClicked() {
    return clicked;
  }

  public void setClicked(boolean clicked) {
    this.clicked = clicked;
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(long timeStamp) {
    this.timeStamp = timeStamp;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }
}
