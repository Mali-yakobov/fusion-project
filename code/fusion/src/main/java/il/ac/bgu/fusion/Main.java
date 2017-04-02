package il.ac.bgu.fusion;



import com.google.gson.Gson;
import il.ac.bgu.fusion.objects.*;
import il.ac.bgu.fusion.util.JsonReaderWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Guy Yafe.
 */
public class Main {




    public static void main(String ... args)throws IOException{
        Sensor sensor1=new Sensor(123,800,620,"");
        CovarianceEllipse c1= new CovarianceEllipse(0, 0, 180, 300, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c2= new CovarianceEllipse(0, 0, 200, 300, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c3= new CovarianceEllipse(0, 0, 220, 300, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c4=new CovarianceEllipse(0, 0, 240, 400, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c5=new CovarianceEllipse(0, 0, 300, 400, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c6=new CovarianceEllipse(0, 0, 330, 400, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c7=new CovarianceEllipse(0, 0, 360, 500, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c8=new CovarianceEllipse(0, 0, 400, 500, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c9=new CovarianceEllipse(0, 0, 450, 500, 1450, 1449, 1050,sensor1,50);

        ArrayList<CovarianceEllipse> cL1=new ArrayList<CovarianceEllipse>();
        cL1.add(c1);
        cL1.add(c2);
        cL1.add(c3);
        ArrayList<CovarianceEllipse> cL2=new ArrayList<CovarianceEllipse>();
        cL2.add(c4);
        cL2.add(c5);
        cL2.add(c6);
        ArrayList<CovarianceEllipse> cL3=new ArrayList<CovarianceEllipse>();
        cL3.add(c7);
        cL3.add(c8);
        cL3.add(c9);
        State s1=new State(cL1);
        State s2=new State(cL2);
        State s3=new State(cL3);
        ArrayList<State> sL1=new ArrayList<State>();
        sL1.add(s1);
        sL1.add(s2);
        sL1.add(s3);
        Track t1=new Track(123,10,12,sL1);
        ArrayList<Track> tL1=new ArrayList<Track>();
        tL1.add(t1);
        PointInTime p1=new PointInTime(10,tL1);



        CovarianceEllipse c10=new CovarianceEllipse(0, 0, 240, 100, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c11=new CovarianceEllipse(0, 0, 300, 130, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c12=new CovarianceEllipse(0, 0, 330, 160, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c13=new CovarianceEllipse(0, 0, 360, 190, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c14=new CovarianceEllipse(0, 0, 400, 220, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c15=new CovarianceEllipse(0, 0, 450, 250, 1450, 1449, 1050,sensor1,50);
        ArrayList<CovarianceEllipse> cL12=new ArrayList<CovarianceEllipse>();
        cL12.add(c10);
        cL12.add(c11);
        cL12.add(c12);
        ArrayList<CovarianceEllipse> cL22=new ArrayList<CovarianceEllipse>();
        cL22.add(c13);
        cL22.add(c14);
        cL22.add(c15);
        State s12=new State(cL12);
        State s22=new State(cL22);
        ArrayList<State> sL12=new ArrayList<State>();
        sL12.add(s12);
        sL12.add(s22);
        Track t12=new Track(123,10,12,sL12);
        ArrayList<Track> tL12=new ArrayList<Track>();
        tL12.add(t12);
        PointInTime p2=new PointInTime(10,tL12);




        ArrayList<PointInTime> listPointInTime=new ArrayList<PointInTime>();
        listPointInTime.add(p1);
        listPointInTime.add(p2);
        String filename= "testJson";
        JsonReaderWriter.objectToJsonFile(listPointInTime,filename);

        String fileaddress= "D:\\sigmabit\\fusion-project\\";
        String filepath= fileaddress + filename + ".json";
        ArrayList<PointInTime> fromFileEllipse= JsonReaderWriter.jsonToObject(filepath);
        System.out.print(fromFileEllipse);





    }



  }




