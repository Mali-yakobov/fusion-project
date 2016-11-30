package il.ac.bgu.fusion;

import com.google.gson.Gson;
import il.ac.bgu.fusion.objects.CovarianceEllipse;
import il.ac.bgu.fusion.util.EllipseRepresentationTranslation;
import il.ac.bgu.fusion.util.JsonReaderWriter;
import javafx.scene.shape.Ellipse;
import java.io.IOException;
/**
 * Created by Guy Yafe.
 */
public class Main {



    public static void main(String ... args)throws IOException{

    /* test for reading\writing ellipses to\from file: */
    Gson gson = new Gson();
    String filename= "testEllipse";
    /*CovarianceEllipse testEl= new CovarianceEllipse(0, 0, 180, 650, 1450, 1449, 1050);
    JsonReaderWriter.elipseToFile(testEl, filename);*/

    String fileaddress= "C:\\Users\\Stas\\project\\fusion-project\\";
    String filepath= fileaddress + filename + ".json";
    CovarianceEllipse fromFileEllipse= JsonReaderWriter.elipseFromFile(filepath);

    /* test for translating between ellipse representations: */
    /*Ellipse vis= new Ellipse();
    vis.setCenterX(170);
    vis.setCenterY(400);
    vis.setRadiusX(50);
    vis.setRadiusY(20);
    vis.setRotate(45);
    CovarianceEllipse covFromVis= EllipseRepresentationTranslation.fromVizualToCovariance(vis);
    Ellipse visFromCovFromVis= EllipseRepresentationTranslation.fromCovarianceToVizual(covFromVis);
    System.out.println(vis + " " + vis.getRotate());
    System.out.println(covFromVis);
    System.out.println(visFromCovFromVis  + " " + visFromCovFromVis.getRotate());*/

        /*Ellipse visFromCovFromFile= EllipseRepresentationTranslation.fromCovarianceToVizual(fromFileEllipse);
        System.out.println(visFromCovFromFile  + " " + visFromCovFromFile.getRotate());*/


    }



  }




