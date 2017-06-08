package il.ac.bgu.visualization;

import il.ac.bgu.fusion.objects.CovarianceEllipse;
import il.ac.bgu.visualization.objects.VizualEllipse;
import il.ac.bgu.visualization.util.EllipseRepresentationTranslation;
import javafx.scene.shape.Ellipse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * test for translating between ellipse representations:
 * Created by Mali on 12/22/2016.
 */
public class EllipseRepresentationTranslationTest {
    double epsilon =Math.pow(10,-5);
    @Before
    public void setUp() throws Exception {

       // Ellipse ellipse= new Ellipse(170,400,50,20);
        //ellipse.setRotate(45);

       // CovarianceEllipse covarianceEllipseFromEllipse= EllipseRepresentationTranslation.fromVizualToCovariance(ellipse);
       // Ellipse ellipseFromCovarianceEllipse= EllipseRepresentationTranslation.fromCovarianceToVizual(covarianceEllipseFromEllipse);
       // System.out.println(ellipse + " " + ellipse.getRotate());
        //System.out.println(covarianceEllipseFromEllipse);
       // System.out.println(ellipseFromCovarianceEllipse  + " " + ellipseFromCovarianceEllipse.getRotate());
        //Ellipse visFromCovFromFile= EllipseRepresentationTranslation.fromCovarianceToVizual(fromFileEllipse);
        //System.out.println(visFromCovFromFile  + " " + visFromCovFromFile.getRotate());
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void fromCovarianceToVizual() throws Exception {
        CovarianceEllipse covarianceEllipse=new CovarianceEllipse(0,0,400.0,300.0,1450.0,1449.0,1050.0, null);
        VizualEllipse ellipseFromCovarianceEllipse= EllipseRepresentationTranslation.fromCovarianceToVizual(covarianceEllipse);
        CovarianceEllipse covarianceEllipseFromEllipse=EllipseRepresentationTranslation.fromVizualToCovariance(ellipseFromCovarianceEllipse);
        assertEquals("equal",covarianceEllipse.getCentreX(),covarianceEllipseFromEllipse.getCentreX());
        assertEquals("equal",covarianceEllipse.getCentreY(),covarianceEllipseFromEllipse.getCentreY());
        assertEquals("equal",covarianceEllipse.getSx2(),covarianceEllipseFromEllipse.getSx2());
        assertTrue("equal",Math.abs(covarianceEllipse.getSy2()-covarianceEllipseFromEllipse.getSy2())<=epsilon);
        assertTrue("equal",Math.abs(covarianceEllipse.getSxy()-covarianceEllipseFromEllipse.getSxy())<=epsilon);
        assertEquals("equal",covarianceEllipse.getId(),covarianceEllipseFromEllipse.getId());
        assertEquals("equal",covarianceEllipse.getTimeStamp(),covarianceEllipseFromEllipse.getTimeStamp());


    }

    @Test
    public void fromVizualToCovariance() throws Exception {
        VizualEllipse ellipse=new VizualEllipse(170, 400);
        ellipse.setAngle(45);
        CovarianceEllipse covarianceEllipseFromEllipse= EllipseRepresentationTranslation.fromVizualToCovariance(ellipse);
        VizualEllipse ellipseFromCovarianceEllipse= EllipseRepresentationTranslation.fromCovarianceToVizual(covarianceEllipseFromEllipse);
        //assertEquals("equal",ellipse.getCenterX(),ellipseFromCovarianceEllipse.getCenterX());
        //assertEquals("equal",ellipse.getCenterY(),ellipseFromCovarianceEllipse.getCenterY());
        assertEquals("equal",ellipse.getRadiusX(),ellipseFromCovarianceEllipse.getRadiusX());
        assertEquals("equal",ellipse.getRadiusY(),ellipseFromCovarianceEllipse.getRadiusY());
        assertEquals("equal",ellipse.getAngle(),ellipseFromCovarianceEllipse.getAngle());
    }

}