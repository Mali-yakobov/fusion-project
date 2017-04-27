package il.ac.bgu.fusion;



import il.ac.bgu.fusion.objects.*;
import il.ac.bgu.fusion.util.JsonReaderWriter;
import javafx.scene.shape.Ellipse;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Guy Yafe.
 */
public class Main {




    public static void main(String ... args)throws IOException{
        Sensor sensor1=new Sensor(123,800,620,"");
        CovarianceEllipse c1= new CovarianceEllipse(0, 0, 180, 300, 1000, 1000, 800,sensor1,50);
        CovarianceEllipse c2= new CovarianceEllipse(0, 0, 180, 300, 1400, 1449, 1000,sensor1,50);
        CovarianceEllipse c3= new CovarianceEllipse(0, 0, 250, 300, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c4=new CovarianceEllipse(0, 0, 250, 300, 1400, 1440, 1000,sensor1,50);
        CovarianceEllipse c5=new CovarianceEllipse(0, 0, 180, 500, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c6=new CovarianceEllipse(0, 0, 180, 500, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c7=new CovarianceEllipse(0, 0, 300, 300, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c8=new CovarianceEllipse(0, 0, 300, 300, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c9=new CovarianceEllipse(0, 0, 180, 400, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c10=new CovarianceEllipse(0, 0, 180, 400, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c11=new CovarianceEllipse(0, 0, 180, 500, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c12=new CovarianceEllipse(0, 0, 350, 300, 1450, 1449, 1050,sensor1,50);


///////////    P1      //////////
        ArrayList<CovarianceEllipse> cL1=new ArrayList<CovarianceEllipse>();
        cL1.add(c1);
        cL1.add(c2);
        State s1=new State(cL1);
        ArrayList<State> sL1=new ArrayList<State>();
        sL1.add(s1);
        Track t1=new Track(1,10,12,sL1);
        ArrayList<Track> tL1=new ArrayList<Track>();
        tL1.add(t1);
        PointInTime p1=new PointInTime(10,tL1);
/////////////    P2    ////////////
        ArrayList<CovarianceEllipse> cL2=new ArrayList<CovarianceEllipse>();
        cL2.add(c3);
        cL2.add(c4);
        State s2=new State(cL2);
        ArrayList<State> sL2=new ArrayList<State>();
        sL2.add(s1);
        sL2.add(s2);
        Track t2=new Track(1,10,12,sL2);
        ArrayList<Track> tL2=new ArrayList<Track>();
        tL2.add(t2);
        ArrayList<CovarianceEllipse> cL3=new ArrayList<CovarianceEllipse>();
        cL3.add(c5);
        cL3.add(c6);
        State s3=new State(cL3);
        ArrayList<State> sL22=new ArrayList<State>();
        sL22.add(s3);
        Track t22=new Track(2,10,12,sL22);
        tL2.add(t22);
        PointInTime p2=new PointInTime(10,tL2);

        ////////// P3 /////////////
        ArrayList<CovarianceEllipse> cL4=new ArrayList<CovarianceEllipse>();
        cL4.add(c7);
        cL4.add(c8);
        cL4.add(c12);
        State s4=new State(cL4);
        ArrayList<State> sL3=new ArrayList<State>();
        sL3.add(s1);
        sL3.add(s2);
        sL3.add(s4);
        Track t3=new Track(1,10,12,sL3);
        ArrayList<Track> tL3=new ArrayList<Track>();
        tL3.add(t3);
        tL3.add(t22);
        ArrayList<CovarianceEllipse> cL5=new ArrayList<CovarianceEllipse>();
        cL5.add(c9);
        cL5.add(c11);
        cL5.add(c10);
        State s5=new State(cL5);
        ArrayList<State> sL33=new ArrayList<State>();
        sL33.add(s5);


        ArrayList<CovarianceEllipse> cL66=new ArrayList<CovarianceEllipse>();
        cL66.add(c11);
        State s66=new State(cL66);
        sL3.add(s66);

        Track t33=new Track(2,10,12,sL3);
        tL3.add(t33);
        PointInTime p3=new PointInTime(10,tL2);

        //CovarianceEllipse c11=new CovarianceEllipse(0, 0, 300, 130, 1450, 1449, 1050,sensor1,50);
       // CovarianceEllipse c12=new CovarianceEllipse(0, 0, 330, 160, 1450, 1449, 1050,sensor1,50);
        //CovarianceEllipse c13=new CovarianceEllipse(0, 0, 360, 190, 1450, 1449, 1050,sensor1,50);
       // CovarianceEllipse c14=new CovarianceEllipse(0, 0, 400, 220, 1450, 1449, 1050,sensor1,50);
        //CovarianceEllipse c15=new CovarianceEllipse(0, 0, 450, 250, 1450, 1449, 1050,sensor1,50);

        ArrayList<PointInTime> listPointInTime=new ArrayList<PointInTime>();
        listPointInTime.add(p1);
        listPointInTime.add(p2);
        listPointInTime.add(p3);
        String filename= "testJsonNew";
        JsonReaderWriter.objectToJson(listPointInTime,filename);

        String fileaddress= "D:\\sigmabit\\fusion-project\\";
        String filepath= /*fileaddress +*/ filename + ".json";
        ArrayList<PointInTime> fromFileEllipse= JsonReaderWriter.jsonToObject(filepath);
        System.out.print(fromFileEllipse);





    }



  }




