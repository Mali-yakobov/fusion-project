package il.ac.bgu.fusion.util;

import com.google.gson.Gson;
import com.sun.java.accessibility.util.GUIInitializedListener;
import il.ac.bgu.fusion.objects.CovarianceEllipse;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Mali on 12/22/2016.
 */
public class JsonReaderWriterTest {
    @Before
    public void setUp() throws Exception {

        //String filename= "testEllipse";
        //CovarianceEllipse testEl= new CovarianceEllipse(0, 0, 180, 650, 1450, 1449, 1050);
        //JsonReaderWriter.elipseToFile(testEl, filename);

        //String fileaddress= "D:\\sigmabit\\fusion-project\\";
       // String filepath= fileaddress + filename + ".json";
        //CovarianceEllipse fromFileEllipse= JsonReaderWriter.elipseFromFile(filepath);
        //System.out.print(fromFileEllipse);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void elipseToFile() throws Exception {
        String filename= "testEllipse2";
        ArrayList<CovarianceEllipse> array=new ArrayList<>();
        CovarianceEllipse El= new CovarianceEllipse(0, 0, 180, 650, 1450, 1449, 1050);
        CovarianceEllipse E2= new CovarianceEllipse(1, 1, 500, 300, 1450, 1449, 1050);
        CovarianceEllipse E3= new CovarianceEllipse(2, 2, 700, 200, 1450, 1449, 1050);

        array.add(El);
        array.add(E2);
        array.add(E3);
        JsonReaderWriter.elipseToFile(array, filename);


    }

    @Test
    public void elipseFromFile() throws Exception {

        String filename= "testEllipse";
        String fileaddress= "D:\\sigmabit\\fusion-project\\";
       String filepath= fileaddress + filename + ".json";
        ArrayList<CovarianceEllipse> fromFileEllipse= JsonReaderWriter.elipseFromFile(filepath);
        System.out.print(fromFileEllipse);
        //assertEquals("centerX",testEl.getCentreX(),fromFileEllipse.getCentreX());
    }

}