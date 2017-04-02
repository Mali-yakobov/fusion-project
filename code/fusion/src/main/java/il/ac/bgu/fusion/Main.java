package il.ac.bgu.fusion;



import il.ac.bgu.fusion.objects.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Guy Yafe.
 */
public class Main {



    public static void main(String ... args)throws IOException{
        Sensor sensor1=new Sensor(123,800,620,"");
        CovarianceEllipse c1= new CovarianceEllipse(0, 0, 180, 650, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c2= new CovarianceEllipse(0, 0, 180, 650, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c3= new CovarianceEllipse(0, 0, 180, 650, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c4=new CovarianceEllipse(0, 0, 180, 650, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c5=new CovarianceEllipse(0, 0, 180, 650, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c6=new CovarianceEllipse(0, 0, 180, 650, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c7=new CovarianceEllipse(0, 0, 180, 650, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c8=new CovarianceEllipse(0, 0, 180, 650, 1450, 1449, 1050,sensor1,50);
        CovarianceEllipse c9=new CovarianceEllipse(0, 0, 180, 650, 1450, 1449, 1050,sensor1,50);
        ArrayList<CovarianceEllipse> cL1=new ArrayList<CovarianceEllipse>();
        cL1.add(c1);
        cL1.add(c2);
        cL1.add(c3);

        State s1=new State(cL1);
        State s2=new State(cL1);
        State s3=new State(cL1);
        ArrayList<State> sL1=new ArrayList<State>();
        sL1.add(s1);
        sL1.add(s2);
        sL1.add(s3);
        Track t1=new Track(123,10,12,sL1);
        ArrayList<Track> tL1=new ArrayList<Track>();
        tL1.add(t1);
        PointInTime p=new PointInTime(10,tL1);




    }



  }




