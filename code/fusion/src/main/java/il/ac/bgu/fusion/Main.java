package il.ac.bgu.fusion;

import com.google.gson.Gson;
import il.ac.bgu.fusion.classes.Elipse;

import java.io.FileWriter;
import java.io.IOException;
/**
 * Created by Guy Yafe.
 */
public class Main {



    public static void main(String ... args)throws IOException{
    Gson gson = new Gson();
    Elipse el= new Elipse(124, 4, 45, 4, 1, 2, 3);
        String json = gson.toJson(el);
        try (FileWriter writer = new FileWriter("fuse.json")) {

            gson.toJson(el, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }


      //JsonObject obj = new JsonObject();
        //gson.toJson(el, new FileWriter("~/Documents/jsonfile"));



    }

  }


