package il.ac.bgu.visualization;

import com.sun.java.accessibility.util.GUIInitializedListener;
import il.ac.bgu.fusion.objects.CovarianceEllipse;
import javafx.scene.shape.Ellipse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;


/**
 * test for translating between ellipse representations:
 * Created by Mali on 12/22/2016.
 */
public class EllipseRepresentationTranslationTest {
    @Before
    public void setUp() throws Exception {

        Ellipse ellipse= new Ellipse(170,400,50,20);
        ellipse.setRotate(45);

        CovarianceEllipse covarianceEllipseFromEllipse= EllipseRepresentationTranslation.fromVizualToCovariance(ellipse);
        Ellipse ellipseFromCovarianceEllipse= EllipseRepresentationTranslation.fromCovarianceToVizual(covarianceEllipseFromEllipse);
        System.out.println(ellipse + " " + ellipse.getRotate());
        System.out.println(covarianceEllipseFromEllipse);
        System.out.println(ellipseFromCovarianceEllipse  + " " + ellipseFromCovarianceEllipse.getRotate());
        //Ellipse visFromCovFromFile= EllipseRepresentationTranslation.fromCovarianceToVizual(fromFileEllipse);
        //System.out.println(visFromCovFromFile  + " " + visFromCovFromFile.getRotate());
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void fromCovarianceToVizual() throws Exception {
        Ellipse ellipse= new Ellipse(170,400,50,20);
        ellipse.setRotate(45);
        CovarianceEllipse covarianceEllipseFromEllipse= EllipseRepresentationTranslation.fromVizualToCovariance(ellipse);
        Ellipse ellipseFromCovarianceEllipse= EllipseRepresentationTranslation.fromCovarianceToVizual(covarianceEllipseFromEllipse);
        assertEquals("equal",ellipse.getCenterX(),ellipseFromCovarianceEllipse.getCenterX());
        assertEquals("equal",ellipse.getRotate(),ellipseFromCovarianceEllipse.getRotate());
    }

    @Test
    public void fromVizualToCovariance() throws Exception {


    }

}