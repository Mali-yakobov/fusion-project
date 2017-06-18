package il.ac.bgu.fusion.algorithms;

import il.ac.bgu.fusion.Main;
import il.ac.bgu.fusion.objects.CovarianceEllipse;
import il.ac.bgu.fusion.objects.State;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static il.ac.bgu.fusion.Main.existingTracks;
import static il.ac.bgu.fusion.algorithms.DistanceMatrix.createNewTracks;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

/**
 * Created by Maayan on 27/04/2017.
 */
public class QueueToPipelineRunnable implements Runnable {


  private final BlockingQueue<CovarianceEllipse> sharedQueue;
  List ellipseList = new ArrayList() ;
  public QueueToPipelineRunnable(BlockingQueue<CovarianceEllipse> sharedQueue) {
    this.sharedQueue = sharedQueue;
  }

  private void runPipeline(List<CovarianceEllipse> ellipseList) {
    List<State> initialClusteringList = InitialClustering.initialClustering(ellipseList);
    System.out.println("after initial");
    if(existingTracks.size() != 0) {
      System.out.println(existingTracks);
      DistanceMatrix.distanceMatrix(existingTracks, initialClusteringList);
      System.out.println("after dm!!!!");
      DistanceMatrix.M2M(existingTracks, initialClusteringList);
      System.out.println("after M2M");
      DistanceMatrix.update(existingTracks);
      System.out.println("after Update");
    }
    else {
      createNewTracks(initialClusteringList, existingTracks);
      System.out.println("after else!!!!  ");
    }

  }


  @Override
  public void run() {

    while (true) {
      //check if queue is empty
      if (sharedQueue.size() == 0) {
        System.out.println("Queue is empty " + Thread.currentThread().getName()
                           + " is waiting , size: " + sharedQueue.size());
      }
      else {
        try {
          System.out.println("Queue is not empty " + Thread.currentThread().getName() + "is working");
          //takes all the ellipses from the queue to the ellipseList
          sharedQueue.drainTo(ellipseList);
          //sleeps 2 seconds
         Thread.sleep(2000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println("Drain succeeded");
        runPipeline(ellipseList);
        try {
          //sleeps 3 seconds
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println("OueueToPipeline Thread is trying again");
      }
    }
  }

}
