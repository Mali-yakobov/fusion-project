package il.ac.bgu.fusion.util;

<<<<<<< HEAD
import com.google.gson.Gson;
import il.ac.bgu.fusion.classes.Elipse;
import javafx.scene.shape.*;
import org.apache.commons.math.linear.*;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
=======
import il.ac.bgu.fusion.classes.Elipse;
import javafx.scene.shape.*;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

>>>>>>> e44ef3db3a8baa1aeca41085537091bcf65bab27
import java.util.Arrays;

/**
 * Created by Stas on 29/11/2016.
 */
public class EllipseRepresentationTranslation {

    public static Ellipse fromCovarianceToVizual(Elipse covElipse){
        Ellipse ans= new Ellipse();

        double[][] matrixData = { {covElipse.getSx2(), covElipse.getSxy()},
<<<<<<< HEAD
                {covElipse.getSxy(), covElipse.getSy2()} };
        RealMatrix CovMatrix = MatrixUtils.createRealMatrix(matrixData);
        EigenDecompositionImpl eigenStuff= new EigenDecompositionImpl(CovMatrix, 0);
=======
                                  {covElipse.getSxy(), covElipse.getSy2()} };
        RealMatrix CovMatrix = MatrixUtils.createRealMatrix(matrixData);
        EigenDecomposition eigenStuff= new EigenDecomposition(CovMatrix);
>>>>>>> e44ef3db3a8baa1aeca41085537091bcf65bab27

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
        ans.setRotate(Math.toDegrees(theta));

        return ans;
    }

    public static Elipse fromVizualToCovariance(Ellipse vizElipse) {
        Elipse ans =  new Elipse();;

        double[][] tempMatData = { {Math.pow(vizElipse.getRadiusX(), 2), 0},
<<<<<<< HEAD
                {0                                  , Math.pow(vizElipse.getRadiusY(), 2)} };
=======
                                   {0                                  , Math.pow(vizElipse.getRadiusY(), 2)} };
>>>>>>> e44ef3db3a8baa1aeca41085537091bcf65bab27
        RealMatrix tempMatrix = MatrixUtils.createRealMatrix(tempMatData);

        double theta= Math.toRadians(vizElipse.getRotate());
        double[][] rData = { {Math.cos(theta), -Math.sin(theta)},
<<<<<<< HEAD
                {Math.sin(theta), Math.cos(theta)} };
=======
                             {Math.sin(theta), Math.cos(theta)} };
>>>>>>> e44ef3db3a8baa1aeca41085537091bcf65bab27
        RealMatrix R = MatrixUtils.createRealMatrix(rData);
        RealMatrix Rt = R.transpose();
        RealMatrix covMatrix= R.multiply(tempMatrix).multiply(Rt);

        ans.setSx2(covMatrix.getEntry(0, 0));
        ans.setSy2(covMatrix.getEntry(1, 1));
        ans.setSxy(covMatrix.getEntry(0, 1));
        return ans;
    }

}
