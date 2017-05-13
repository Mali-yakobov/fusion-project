package il.ac.bgu.fusion;

import il.ac.bgu.fusion.algorithms.JsonToQueueRunnable;
import il.ac.bgu.fusion.algorithms.QueueToPipelineRunnable;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Guy Yafe.
 */
public class Main {
  static int queueSize = 10000000;  //Integer.MAX_VALUE - exceeds VM limit
  static BlockingQueue covEllipseQueue = new ArrayBlockingQueue(queueSize);
  public static boolean done = false;


  public static void main(String ... args)throws IOException{
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