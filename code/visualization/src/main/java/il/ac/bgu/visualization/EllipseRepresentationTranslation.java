package il.ac.bgu.visualization;

import il.ac.bgu.fusion.objects.CovarianceEllipse;
import javafx.scene.shape.Ellipse;
import org.apache.commons.math3.linear.*;


import java.util.Arrays;

/**
 * Created by Stas on 29/11/2016.
 */
public class EllipseRepresentationTranslation {

    public static Ellipse fromCovarianceToVizual(CovarianceEllipse covElipse){
        Ellipse ans= new Ellipse();

        double[][] matrixData = { {covElipse.getSx2(), covElipse.getSxy()},
                                  {covElipse.getSxy(), covElipse.getSy2()} };
        Array2DRowRealMatrix covMatrix = new Array2DRowRealMatrix(matrixData);

        EigenDecomposition eigenStuff= new EigenDecomposition(covMatrix);

        double[] eigValues=  eigenStuff.getRealEigenvalues();
        Arrays.sort(eigValues);
        double ry= Math.sqrt(eigValues[0]);
        double rx= Math.sqrt(eigValues[1]);

        RealVector eigVector= eigenStuff.getEigenvector(0);
        double normal=eigVector.getNorm();
        eigVector.setEntry(0,(eigVector.getEntry(0)/normal));
        eigVector.setEntry(1,(eigVector.getEntry(1)/normal));
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
        Array2DRowRealMatrix tempMatrix = new Array2DRowRealMatrix(tempMatData);

        double theta= Math.toRadians(vizElipse.getRotate());
        double[][] rData = { {Math.cos(theta), -Math.sin(theta)},
                             {Math.sin(theta), Math.cos(theta)} };

        Array2DRowRealMatrix  R =new Array2DRowRealMatrix(rData);
        Array2DRowRealMatrix Rt = (Array2DRowRealMatrix) R.transpose();
        Array2DRowRealMatrix covMatrix= R.multiply(tempMatrix).multiply(Rt);

        ans.setCentreX(vizElipse.getCenterX());
        ans.setCentreY(vizElipse.getCenterY());
        ans.setSx2(covMatrix.getEntry(0, 0));
        ans.setSy2(covMatrix.getEntry(1, 1));
        ans.setSxy(covMatrix.getEntry(0, 1));
        return ans;
    }

}
