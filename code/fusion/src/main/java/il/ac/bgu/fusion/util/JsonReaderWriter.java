package il.ac.bgu.fusion.util;


import com.google.gson.Gson;
import il.ac.bgu.fusion.objects.CovarianceEllipse;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Stas on 29/11/2016.
 */
public class JsonReaderWriter {

 public static void elipseToFile(CovarianceEllipse elipseObj, String filename){
     Gson gson = new Gson();
     try (FileWriter writer = new FileWriter(filename + ".json")) {
         gson.toJson(elipseObj, writer);
     } catch (IOException e) {
         e.printStackTrace();
     }
 }

 public static CovarianceEllipse elipseFromFile(String filepath){
     Gson gson = new Gson();
     CovarianceEllipse ans= null;
     try {
         ans = gson.fromJson(new FileReader(filepath), CovarianceEllipse.class);
     } catch (FileNotFoundException e) {
         e.printStackTrace();
     }
     return ans;
 }

}
