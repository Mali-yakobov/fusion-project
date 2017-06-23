package il.ac.bgu.fusion.util;

import il.ac.bgu.fusion.objects.CovarianceEllipse;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * Created by Stas on 12/05/2017.
 */
public class LinearAlgebraUtils {

  /**
   * Prints the shape of a matrix in a convenient form, for debugging purposes
   * @param matrix
   */
  public static void printShape(RealMatrix matrix){
    System.out.println("(" + matrix.getRowDimension() + ", " + matrix.getColumnDimension() + ")");
  }

  /**
   *
   * @param ell
   * @return Covariance matrix of shape (2,2)
   */
  public static RealMatrix ellipseToCovarianceMatrix(CovarianceEllipse ell){
    double[][] arr = {{ell.getSx2(), ell.getSxy()},
                      {ell.getSxy(), ell.getSy2()}};
    return new Array2DRowRealMatrix(arr);
  }

  /**
   *
   * @param ell
   * @return Velocity covariance matrix of shape (2,2)
   */
  public static RealMatrix ellipseToVelocityCovarianceMatrix(CovarianceEllipse ell){
    double[][] arr = {{ell.getSvx2(), ell.getSvxy()},
                      {ell.getSvxy(), ell.getSvy2()}};
    return new Array2DRowRealMatrix(arr);
  }


  /**
   *
   * @param ell
   * @return  Position vector (x,y), represented by matrix of shape (1,2)
   */
  public static RealMatrix ellipseToPositionVector(CovarianceEllipse ell){
    double[][] arrR1={{ell.getCentreX(), ell.getCentreY()}};
    return new Array2DRowRealMatrix(arrR1);
  }

  /**
   *
   * @param ell
   * @return  Velocity position vector (x,y), represented by matrix of shape (1,2)
   */
  public static RealMatrix ellipseToVelocityPositionVector(CovarianceEllipse ell){
    double[][] arrR1={{ell.getVx(), ell.getVy()}};
    return new Array2DRowRealMatrix(arrR1);
  }

  /**
   *
   * @param cNew Covariance matrix for new ellipse
   * @param rNew Position vector for new ellipse, represented by matrix of shape (1,2)
   * @return Covariance ellipse object, constructed from the parameter matrices
   */
  public static CovarianceEllipse matricesToEllipse(RealMatrix cNew, RealMatrix rNew
                                                    /*,RealMatrix velocityCovNew, RealMatrix velocityPosNew*/) {
    CovarianceEllipse newEllipse= new CovarianceEllipse();
    newEllipse.setCentreX(rNew.getEntry(0,0));
    newEllipse.setCentreY(rNew.getEntry(0,1));
    newEllipse.setSx2(cNew.getEntry(0,0));
    newEllipse.setSy2(cNew.getEntry(1,1));
    newEllipse.setSxy(cNew.getEntry(0,1));

    /*newEllipse.setVx(velocityPosNew.getEntry(0,0));
    newEllipse.setVy(velocityPosNew.getEntry(0,1));
    newEllipse.setSvx2(velocityCovNew.getEntry(0,0));
    newEllipse.setSvy2(velocityCovNew.getEntry(1,1));
    newEllipse.setSvxy(velocityCovNew.getEntry(0,1));*/

    newEllipse.setIsFusionEllipse(true);
    return newEllipse;
  }

  /*
 * Calculate statistical distance between two covariance ellipses
 */
  public static double calcDistanceBetweenEllipses(CovarianceEllipse ellipse1, CovarianceEllipse ellipse2){
    RealMatrix c1Inv = MatrixUtils.inverse(ellipseToCovarianceMatrix(ellipse1));
    RealMatrix c2Inv = MatrixUtils.inverse(ellipseToCovarianceMatrix(ellipse2));
    RealMatrix cInvSummed= c1Inv.add(c2Inv);                                      // dim=(2,2)

    RealMatrix r1 = ellipseToPositionVector(ellipse1);
    RealMatrix r2 = ellipseToPositionVector(ellipse2);
    RealMatrix deltaR= r1.subtract(r2);                                           // dim=(1,2)
    RealMatrix deltaRTrnsp= deltaR.transpose();                                   // dim=(2,1)

    RealMatrix DRMulCInvSummed= deltaR.multiply(cInvSummed);                      // dim=(1,2)x(2,2)=(1,2)
    RealMatrix DRMulCInvSummedMulDRTrans= DRMulCInvSummed.multiply(deltaRTrnsp);  // dim=(1,2)x(2,1)=(1,1)
    return DRMulCInvSummedMulDRTrans.getEntry(0,0);
  }

}
