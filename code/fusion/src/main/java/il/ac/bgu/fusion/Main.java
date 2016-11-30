package il.ac.bgu.fusion;

import com.google.gson.Gson;
import il.ac.bgu.fusion.objects.CovarianceEllipse;
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
    CovarianceEllipse testEl= new CovarianceEllipse(0, 0, 500, 300, 2493, 406, 114);
    String filename= "testEllipse";


    String fileaddress= "C:\\Users\\Stas\\project\\fusion-project\\";
    String filepath= fileaddress + filename + ".json";

    JsonReaderWriter.elipseToFile(testEl, filename);
    CovarianceEllipse fromFileEllipse= JsonReaderWriter.elipseFromFile(filepath);
    System.out.println(fromFileEllipse);

    /* test for translating between ellipse representations: */
    Ellipse vis= new Ellipse();

    vis.setRadiusX(50);
    vis.setRadiusY(20);
    vis.setRotate(3.1415);
    //CovarianceEllipse covFromVis= EllipseRepresentationTranslation.fromVizualToCovariance(vis);
   // Ellipse visFromCovFromVis= EllipseRepresentationTranslation.fromCovarianceToVizual(covFromVis);
    //System.out.println(vis);
    //System.out.println(visFromCovFromVis);

    //System.out.println(visFromCovFromVis.getRotate());
    }



  }




