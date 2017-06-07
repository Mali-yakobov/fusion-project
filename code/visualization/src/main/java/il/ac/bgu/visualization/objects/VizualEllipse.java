package il.ac.bgu.visualization.objects;

import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MVCArray;
import com.lynden.gmapsfx.shapes.Polyline;
import com.lynden.gmapsfx.shapes.PolylineOptions;
import il.ac.bgu.visualization.util.EllipseBuilder;
import javafx.scene.shape.Ellipse;

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
  private boolean IsFusionEllipse;
  private ArrayList<VizualEllipse> rawList = null;
  private Polyline polyline;
  private static final char hemisphere='N';
  private static final int zone=36;


  public VizualEllipse(){

  }
  public VizualEllipse(double radiusX, double radiusY) {
    setRadiusX(radiusX);
    setRadiusY(radiusY);
  }


  public com.lynden.gmapsfx.shapes.Polyline ellipseToDraw(String color, double strokeWeight){
    org.jscience.geography.coordinates.UTM c= org.jscience.geography.coordinates.UTM.valueOf(zone, hemisphere, centreX, centreY, SI.METRE);
    org.jscience.geography.coordinates.LatLong centerPTemp= utmToLatLong(c, WGS84);

    LatLong centerP= new LatLong(centerPTemp.getCoordinates()[1], centerPTemp.getCoordinates()[0]);
    //LatLong centerP = new LatLong(31.166724, 34.793119);
    MVCArray polyluneArray = EllipseBuilder.buildEllipsePoints(centerP, radiusX, radiusY, angle);

    PolylineOptions polylineOptions=new PolylineOptions().path(polyluneArray).strokeColor(color).clickable(true).strokeWeight(strokeWeight);
    com.lynden.gmapsfx.shapes.Polyline polyline = new com.lynden.gmapsfx.shapes.Polyline(polylineOptions);
    setPolyline(polyline);
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

  public Polyline getPolyline() {
    return polyline;
  }

  public void setPolyline(Polyline polyline) {
    this.polyline = polyline;
  }
}