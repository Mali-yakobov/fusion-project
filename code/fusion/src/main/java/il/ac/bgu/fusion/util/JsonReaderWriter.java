package il.ac.bgu.fusion.util;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import il.ac.bgu.fusion.objects.CovarianceEllipse;
import il.ac.bgu.fusion.objects.PointInTime;
import il.ac.bgu.fusion.objects.Sensor;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Stas on 29/11/2016.
 */
public class JsonReaderWriter {

 public static void elipseToFile(ArrayList<CovarianceEllipse> elipseObj, String filename){
     Gson gson = new Gson();
     try (FileWriter writer = new FileWriter(filename + ".json")) {
         gson.toJson(elipseObj, writer);
     } catch (IOException e) {
         e.printStackTrace();
     }
 }

  public static void sensorToJson(ArrayList<Sensor> sensorsObj, String filename){
    Gson gson = new Gson();
    try (FileWriter writer = new FileWriter(filename + ".json")) {
      gson.toJson(sensorsObj, writer);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public static ArrayList<Object> ObjectToFile( String filepath){
    Gson gson = new Gson();
    ArrayList<Object> ans=null;
    Type ListType = new TypeToken<ArrayList<Sensor>>(){}.getType();
    try {
      ans = gson.fromJson(new FileReader(filepath), ListType);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return ans;
  }
  public static ArrayList<Sensor> sensorToFile( String filepath){
    Gson gson = new Gson();
    ArrayList<Sensor> ans=null;
    Type ListType = new TypeToken<ArrayList<Sensor>>(){}.getType();
    try {
      ans = gson.fromJson(new FileReader(filepath), ListType);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return ans;
  }
  public static ArrayList<CovarianceEllipse> elipseFromFile(String filepath){
     Gson gson = new Gson();
     ArrayList<CovarianceEllipse> ans=null;
     Type ListType = new TypeToken<ArrayList<CovarianceEllipse>>(){}.getType();
     try {
         ans = gson.fromJson(new FileReader(filepath), ListType);
     } catch (FileNotFoundException e) {
         e.printStackTrace();
     }
     return ans;
 }


    public static void objectToJson(ArrayList<PointInTime> PointInTimeObjects, String filename){
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(filename + ".json")) {
            gson.toJson(PointInTimeObjects, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<PointInTime> jsonToObject(String filepath){
        Gson gson = new Gson();
        ArrayList<PointInTime> ans=null;
        Type ListType = new TypeToken<ArrayList<PointInTime>>(){}.getType();
        try {
            ans = gson.fromJson(new FileReader(filepath), ListType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return ans;
    }

}
