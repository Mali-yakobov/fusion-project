package il.ac.bgu.fusion;



import il.ac.bgu.fusion.objects.*;
import il.ac.bgu.fusion.util.JsonReaderWriter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Guy Yafe.
 */
public class Main {




  public static void main(String ... args)throws IOException{
    Sensor sensor1=new Sensor(123,800,620,"");
    CovarianceEllipse c1= new CovarianceEllipse(0, 0, 180, 300, 3920.944533000791, 2879.0554669992093, -2954.4232590366246, sensor1);
    CovarianceEllipse c2= new CovarianceEllipse(0, 0, 180, 300, 3920.944533000791, 2879.0554669992093, 2954.4232590366246,sensor1);
    CovarianceEllipse c3= new CovarianceEllipse(0, 0, 180, 300, 6354.423259036625, 445.5767409633759, 520.9445330007915,sensor1);

///////////    P1      //////////
    ArrayList<CovarianceEllipse> rawElls1=new ArrayList<CovarianceEllipse>();
    rawElls1.add(c1);
    rawElls1.add(c2);
    rawElls1.add(c3);
    State s1= new State(rawElls1);
    s1.setFusionEllipse(c3);
    ArrayList<State> states1= new ArrayList<State>();
    states1.add(s1);
    Track t1=new Track(1,10,12,states1);
    ArrayList<Track> tracks1=new ArrayList<Track>();
    tracks1.add(t1);
    PointInTime p1=new PointInTime(10,tracks1);


    ArrayList<PointInTime> listPointInTime=new ArrayList<PointInTime>();
    listPointInTime.add(p1);
    //listPointInTime.add(p2);
    //listPointInTime.add(p3);
    String filename= "testJsonNew";
    JsonReaderWriter.objectToJson(listPointInTime,filename);


    String fileaddress= "D:\\sigmabit\\fusion-project\\";
    String filepath= /*fileaddress +*/ filename + ".json";
    ArrayList<PointInTime> fromFileEllipse= JsonReaderWriter.jsonToObject(filepath);
    System.out.print(fromFileEllipse);





  }



}