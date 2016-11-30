package il.ac.bgu.fusion.util;


import com.google.gson.Gson;
import il.ac.bgu.fusion.classes.Elipse;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Stas on 29/11/2016.
 */
public class JsonReaderWriter {

 public static void elipseToFile(Elipse elipseObj, String filename){
     Gson gson = new Gson();
     try (FileWriter writer = new FileWriter(filename + ".json")) {
         gson.toJson(elipseObj, writer);
     } catch (IOException e) {
         e.printStackTrace();
     }
 }

 public static Elipse elipseFromFile(String filepath){
     Gson gson = new Gson();
     Elipse ans= null;
     try {
         ans = gson.fromJson(new FileReader(filepath), Elipse.class);
     } catch (FileNotFoundException e) {
         e.printStackTrace();
     }
     return ans;
 }

}
