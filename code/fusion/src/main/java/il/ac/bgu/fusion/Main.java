package il.ac.bgu.fusion;

import com.google.gson.Gson;
import il.ac.bgu.fusion.classes.Elipse;
import il.ac.bgu.fusion.util.JsonReaderWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/**
 * Created by Guy Yafe.
 */
public class Main {



  public static void main(String ... args)throws IOException{

    //test for reading\writing ellipses to\from file:
    Gson gson = new Gson();
    Elipse testEl= new Elipse(124, 4, 45, 4, 1, 2, 3);
    String filename= "testEllipse";
    String fileaddress= "C:\\Users\\Stas\\project\\fusion-project\\";
    String filepath= fileaddress + filename + ".json";

    //JsonReaderWriter.elipseToFile(testEl, filename);
    Elipse fromFileEllipse= JsonReaderWriter.elipseFromFile(filepath);
    System.out.println(fromFileEllipse);
    }

  }


