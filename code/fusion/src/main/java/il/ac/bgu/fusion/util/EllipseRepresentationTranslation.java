package il.ac.bgu.fusion.util;

import il.ac.bgu.fusion.objects.CovarianceEllipse;
import javafx.scene.shape.Ellipse;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;


import java.util.Arrays;

/**
 * Created by Stas on 29/11/2016.
 */
public class EllipseRepresentationTranslation {

    public static Ellipse fromCovarianceToVizual(CovarianceEllipse covElipse){
        Ellipse ans= new Ellipse();

        double[][] matrixData = { {covElipse.getSx2(), covElipse.getSxy()},
                                  {covElipse.getSxy(), covElipse.getSy2()} };
        RealMatrix covMatrix = MatrixUtils.createRealMatrix(matrixData);
        EigenDecomposition eigenStuff= new EigenDecomposition(covMatrix);

        double[] eigValues=  eigenStuff.getRealEigenvalues();
        Arrays.sort(eigValues);
        double ry= Math.sqrt(eigValues[0]);
        double rx= Math.sqrt(eigValues[1]);

        RealVector eigVector= eigenStuff.getEigenvector(0);
        double x= eigVector.getEntry(0);
        double y= eigVector.getEntry(1);
        double theta= Math.atan2(y, x);

        ans.setRadiusX(rx);
        ans.setRadiusY(ry);
        ans.setCenterX(covElipse.getCentreX());
        ans.setCenterY(covElipse.getCentreY());
        ans.setRotate(Math.toDegrees(theta));

        return ans;
    }

    public static CovarianceEllipse fromVizualToCovariance(Ellipse vizElipse) {
        CovarianceEllipse ans =  new CovarianceEllipse();

        double[][] tempMatData = { {Math.pow(vizElipse.getRadiusX(), 2), 0},
                                   {0                                  , Math.pow(vizElipse.getRadiusY(), 2)} };
        RealMatrix tempMatrix = MatrixUtils.createRealMatrix(tempMatData);

        double theta= Math.toRadians(vizElipse.getRotate());
        double[][] rData = { {Math.cos(theta), -Math.sin(theta)},
                             {Math.sin(theta), Math.cos(theta)} };

        RealMatrix R = MatrixUtils.createRealMatrix(rData);
        RealMatrix Rt = R.transpose();
        RealMatrix covMatrix= R.multiply(tempMatrix).multiply(Rt);

        ans.setCentreX(vizElipse.getCenterX());
        ans.setCentreY(vizElipse.getCenterY());
        ans.setSx2(covMatrix.getEntry(0, 0));
        ans.setSy2(covMatrix.getEntry(1, 1));
        ans.setSxy(covMatrix.getEntry(0, 1));
        return ans;
    }

}
