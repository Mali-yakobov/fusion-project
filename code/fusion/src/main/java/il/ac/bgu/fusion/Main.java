package il.ac.bgu.fusion;

import com.google.gson.Gson;
import il.ac.bgu.fusion.classes.Elipse;

/**
 * Created by Guy Yafe.
 */
public class Main {

  public static void main(String ... args){
    Gson gson = new Gson();
    Elipse el= new Elipse(124, 4, 45, 4, 1, 2, 3);
    System.out.println(gson.toJson(el));
  }

}
