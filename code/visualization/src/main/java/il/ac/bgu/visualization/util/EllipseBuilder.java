package il.ac.bgu.visualization.util;

import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MVCArray;

public class EllipseBuilder {

  private static final int DEFAULT_ELLIPSE_POINTS = 90;

  /**
   * Generates the points for an ellipse based on two radii from a center point.
   *
   * @param center
   *            The LatLong point of the center.
   * @param longRadius
   *            longitude radius in meters
   * @param latRadius
   *            latitude radius in meters
   * @return An array of LatLong points in an MVC array representing the ellipse.
   */
  public static final MVCArray buildEllipsePoints(LatLong center, double longRadius, double latRadius) {
    return buildEllipsePoints(center, longRadius, latRadius, 0.);
  }

  /**
   * Generates the points for an ellipse based on two radii from a center point and a counter clockwise rotation angle.
   *
   * @param center
   *            The LatLong point of the center.
   * @param longRadius
   *            longitude radius in meters
   * @param latRadius
   *            latitude radius in meters
   * @param rotAngle
   *            rotation angle in degree, counter clockwise
   * @return An array of LatLong points in an MVC array representing the ellipse.
   */
  public static final MVCArray buildEllipsePoints(LatLong center, double longRadius, double latRadius, double rotAngle) {
    int points = DEFAULT_ELLIPSE_POINTS;

    MVCArray res = new MVCArray();

    double longRadiusSquared = longRadius * longRadius;
    double latRadiusSquared = latRadius * latRadius;

    double radiiProduct = longRadius * latRadius;

    double theta = 0d;
    double angleIncrement = 360.0 / points;
    for (int i = 0; (i < points + 1); i++) {
      theta = i * angleIncrement;
      double r = radiiProduct / (Math.sqrt(latRadiusSquared * Math.pow(Math.sin(Math.toRadians(theta)), 2)
                                           + longRadiusSquared * Math.pow(Math.cos(Math.toRadians(theta)), 2)));
      res.push(center.getDestinationPoint(theta - rotAngle, r));
    }

    return res;
  }
  public static final MVCArray buildEllipsePoints(LatLong center, double longRadius, double latRadius, double rotAngle,int points) {


    MVCArray res = new MVCArray();

    double longRadiusSquared = longRadius * longRadius;
    double latRadiusSquared = latRadius * latRadius;

    double radiiProduct = longRadius * latRadius;

    double theta = 0d;
    double angleIncrement = 360.0 / points;
    for (int i = 0; (i < points + 1); i++) {
      theta = i * angleIncrement;
      double r = radiiProduct / (Math.sqrt(latRadiusSquared * Math.pow(Math.sin(Math.toRadians(theta)), 2)
                                           + longRadiusSquared * Math.pow(Math.cos(Math.toRadians(theta)), 2)));
      res.push(center.getDestinationPoint(theta - rotAngle, r));
    }

    return res;
  }
}
