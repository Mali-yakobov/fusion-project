package il.ac.bgu.fusion.util;

import il.ac.bgu.fusion.objects.*;
import il.ac.bgu.fusion.util.JsonReaderWriter;

import java.util.ArrayList;

/**
 * Created by Stas on 12/05/2017.
 */
public class JsonCreator {
  static Sensor sensor1=new Sensor(123, 341533.78764316335, 3846911.8923788285, "");
  static Sensor sensor2=new Sensor(124, 340851.1263149753, 3846577.3160242713, "");
  static Sensor sensor3=new Sensor(125, 340558.6486263189, 3846905.003492296, "");



  // 1 1
  static CovarianceEllipse c1= new CovarianceEllipse(0, 0, 341338.64930353255, 3847283.031396312, 3920.944533000791, 2879.0554669992093, -2954.4232590366246, sensor1);
  static CovarianceEllipse c2= new CovarianceEllipse(0, 0, 341338.64930353255, 3847283.031396312, 3920.944533000791, 2879.0554669992093, 2954.4232590366246,sensor1);
  static CovarianceEllipse c3= new CovarianceEllipse(0, 0, 341338.64930353255, 3847283.031396312, 6354.423259036625, 445.5767409633759, 520.9445330007915,sensor1);

  // 1 2
  static CovarianceEllipse c31= new CovarianceEllipse(0, 0, 341333.7358043594, 3847767.713892443, 3920.944533000791, 2879.0554669992093, -2954.4232590366246, sensor1);
  static CovarianceEllipse c32= new CovarianceEllipse(0, 0, 341333.7358043594, 3847767.713892443, 3920.944533000791, 2879.0554669992093, 2954.4232590366246,sensor1);
  static CovarianceEllipse c33= new CovarianceEllipse(0, 0, 341333.7358043594, 3847767.713892443, 6354.423259036625, 445.5767409633759, 520.9445330007915,sensor1);

  //2 1
  static CovarianceEllipse c21= new CovarianceEllipse(0, 0, 340994.8646545735, 3847354.203527985, 3920.944533000791, 2879.0554669992093, -2954.4232590366246, sensor1);
  static CovarianceEllipse c22= new CovarianceEllipse(0, 0, 340994.8646545735, 3847354.203527985, 3920.944533000791, 2879.0554669992093, 2954.4232590366246,sensor1);
  static CovarianceEllipse c23= new CovarianceEllipse(0, 0, 340994.8646545735, 3847354.203527985, 6354.423259036625, 445.5767409633759, 520.9445330007915,sensor1);

  // 1 3
  static CovarianceEllipse c41= new CovarianceEllipse(0, 0, 341350.6346041999, 3848306.60421403, 3920.944533000791, 2879.0554669992093, -2954.4232590366246, sensor1);
  static CovarianceEllipse c42= new CovarianceEllipse(0, 0, 341350.6346041999, 3848306.60421403, 3920.944533000791, 2879.0554669992093, 2954.4232590366246,sensor1);
  static CovarianceEllipse c43= new CovarianceEllipse(0, 0, 341350.6346041999, 3848306.60421403, 6354.423259036625, 445.5767409633759, 520.9445330007915,sensor1);

  // 2 2
  static CovarianceEllipse c51= new CovarianceEllipse(0, 0, 341140.218343724, 3847600.425207034, 3920.944533000791, 2879.0554669992093, -2954.4232590366246, sensor1);
  static CovarianceEllipse c52= new CovarianceEllipse(0, 0, 341140.218343724, 3847600.425207034, 3920.944533000791, 2879.0554669992093, 2954.4232590366246,sensor1);
  static CovarianceEllipse c53= new CovarianceEllipse(0, 0, 341140.218343724, 3847600.425207034, 6354.423259036625, 445.5767409633759, 520.9445330007915,sensor1);

  public static ArrayList<Sensor> createSensorsFile(){
    ArrayList<Sensor> sensors=new ArrayList<>();
    sensors.add(sensor1);
    sensors.add(sensor2);
    sensors.add(sensor3);

    String filename= "Sensors";
    JsonReaderWriter.sensorToJson(sensors, filename);
    return sensors;
  }


  public static void createDummyScenario(){
     /*    P1:      */
    ArrayList<CovarianceEllipse> rawElls1=new ArrayList<CovarianceEllipse>();
    rawElls1.add(c1);
    rawElls1.add(c2);
    State s1= new State(rawElls1);
    s1.setFusionEllipse(c3);
    ArrayList<State> states1= new ArrayList<State>();
    states1.add(s1);
    Track t1=new Track(1, 10, 12, states1);
    ArrayList<Track> tracks1=new ArrayList<Track>();
    tracks1.add(t1);
    PointInTime p1=new PointInTime(123, tracks1);

    /*   P2      */
    ArrayList<CovarianceEllipse> rawElls12=new ArrayList<CovarianceEllipse>();
    rawElls12.add(c31);
    rawElls12.add(c32);
    State s12= new State(rawElls12);
    s12.setFusionEllipse(c33);
    ArrayList<State> states12= new ArrayList<State>();
    states12.add(s1);
    states12.add(s12);
    Track t12=new Track(1,10,12,states12);
    ArrayList<Track> tracks12=new ArrayList<Track>();
    tracks12.add(t12);

    ArrayList<CovarianceEllipse> rawElls2=new ArrayList<CovarianceEllipse>();
    rawElls2.add(c22);
    rawElls2.add(c23);
    State s2= new State(rawElls2);
    s2.setFusionEllipse(c21);
    ArrayList<State> states2= new ArrayList<State>();
    states2.add(s2);
    Track t2=new Track(2,10,12,states2);
    tracks12.add(t2);

    PointInTime p2=new PointInTime(124,tracks12);

    /*     P3      */
    ArrayList<CovarianceEllipse> rawElls13=new ArrayList<CovarianceEllipse>();
    rawElls13.add(c41);
    rawElls13.add(c42);
    State s13= new State(rawElls13);
    s13.setFusionEllipse(c43);
    ArrayList<State> states13= new ArrayList<State>();
    states13.add(s1);
    states13.add(s12);
    states13.add(s13);
    Track t13=new Track(1,10,12,states13);
    ArrayList<Track> tracks13=new ArrayList<Track>();
    tracks13.add(t13);

    ArrayList<CovarianceEllipse> rawElls21=new ArrayList<CovarianceEllipse>();
    rawElls21.add(c52);
    rawElls21.add(c53);
    State s21= new State(rawElls21);
    s21.setFusionEllipse(c51);
    ArrayList<State> states21= new ArrayList<State>();
    states21.add(s2);
    states21.add(s21);
    Track t21=new Track(2,10,12,states21);
    tracks13.add(t21);

    PointInTime p3=new PointInTime(125,tracks13);

    /* ************************************************************** */
    ArrayList<PointInTime> listPointInTime=new ArrayList<PointInTime>();
    listPointInTime.add(p1);
    listPointInTime.add(p2);
    listPointInTime.add(p3);
    String filename= "dummyScenario";
    JsonReaderWriter.objectToJson(listPointInTime, filename);
  }

  public static void createRawOnlyScenario1(){
     /*    P1:      */
    ArrayList<CovarianceEllipse> rawElls1=new ArrayList<CovarianceEllipse>();
    rawElls1.add(c1);
    rawElls1.add(c2);
    State s1= new State(rawElls1);
    ArrayList<State> states1= new ArrayList<State>();
    states1.add(s1);
    Track t1=new Track(1, 10, 12, states1);
    ArrayList<Track> tracks1=new ArrayList<Track>();
    tracks1.add(t1);
    PointInTime p1=new PointInTime(10, tracks1);

    /*   P2      */
    ArrayList<CovarianceEllipse> rawElls12=new ArrayList<CovarianceEllipse>();
    rawElls12.add(c31);
    rawElls12.add(c32);
    State s12= new State(rawElls12);
    ArrayList<State> states12= new ArrayList<State>();
    states12.add(s1);
    states12.add(s12);
    Track t12=new Track(1,10,12,states12);
    ArrayList<Track> tracks12=new ArrayList<Track>();
    tracks12.add(t12);

    ArrayList<CovarianceEllipse> rawElls2=new ArrayList<CovarianceEllipse>();
    rawElls2.add(c22);
    rawElls2.add(c23);
    State s2= new State(rawElls2);
    ArrayList<State> states2= new ArrayList<State>();
    states2.add(s2);
    Track t2=new Track(2,10,12,states2);
    tracks12.add(t2);

    PointInTime p2=new PointInTime(10,tracks12);

    /*     P3      */
    ArrayList<CovarianceEllipse> rawElls13=new ArrayList<CovarianceEllipse>();
    rawElls13.add(c41);
    rawElls13.add(c42);
    State s13= new State(rawElls13);
    ArrayList<State> states13= new ArrayList<State>();
    states13.add(s1);
    states13.add(s12);
    states13.add(s13);
    Track t13=new Track(1,10,12,states13);
    ArrayList<Track> tracks13=new ArrayList<Track>();
    tracks13.add(t13);

    ArrayList<CovarianceEllipse> rawElls21=new ArrayList<CovarianceEllipse>();
    rawElls21.add(c52);
    rawElls21.add(c53);
    State s21= new State(rawElls21);
    ArrayList<State> states21= new ArrayList<State>();
    states21.add(s2);
    states21.add(s21);
    Track t21=new Track(2,10,12,states21);
    tracks13.add(t21);

    PointInTime p3=new PointInTime(10,tracks13);

    /* ************************************************************** */
    ArrayList<PointInTime> listPointInTime=new ArrayList<PointInTime>();
    listPointInTime.add(p1);
    listPointInTime.add(p2);
    listPointInTime.add(p3);
    String filename= "rawOnlyScenario1";
    JsonReaderWriter.objectToJson(listPointInTime, filename);
  }


  public static void createJsonForThreadTesting1(){
    ArrayList<CovarianceEllipse> rawEllsForAlg =new ArrayList<CovarianceEllipse>();
    for (int i=0; i<=100000; i++)
      rawEllsForAlg.add( new CovarianceEllipse(0, 0, 150, 130, 3920.944533000791, 2879.0554669992093, -2954.4232590366246, sensor1));
    JsonReaderWriter.elipseToFile(rawEllsForAlg, "jsonForThreadTesting1");
  }



}
