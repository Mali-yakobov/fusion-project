package il.ac.bgu.fusion;

import il.ac.bgu.fusion.algorithms.JsonToQueueRunnable;
import il.ac.bgu.fusion.algorithms.QueueToPipelineRunnable;
import il.ac.bgu.fusion.objects.Track;
import il.ac.bgu.fusion.util.JsonCreator;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Guy Yafe.
 */
public class Main {
  static BlockingQueue covEllipseQueue = new LinkedBlockingQueue();
  public static List<Track> existingTracks = new LinkedList<>();

  public static void main(String ... args)throws IOException{
    System.out.print(   JsonCreator.createSensorsFile());

    JsonCreator.createDummyScenario();
    //JsonCreator.createRawOnlyScenario1();

     // Running the threads:

    Thread jsonToQueue = new Thread( new JsonToQueueRunnable(covEllipseQueue));
    Thread queueToPipeline = new Thread( new QueueToPipelineRunnable(covEllipseQueue));
    try
    {
      System.out.println("Running jsonToQueue");
      jsonToQueue.start();
      System.out.println("Running queueToPipeline");
      queueToPipeline.start();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}