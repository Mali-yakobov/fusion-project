package il.ac.bgu.fusion.fusion.algorithm;

import il.ac.bgu.fusion.objects.CovarianceEllipse;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import static java.lang.Thread.sleep;

/**
 * Created by Maayan on 27/04/2017.
 */
public class QueueToPipeline implements Runnable {

  protected BlockingQueue queue = null;
  ArrayList<CovarianceEllipse> ellipseList;

  public QueueToPipeline(BlockingQueue queue) {
    this.queue = queue;
  }

  private void runPipeline(ArrayList<CovarianceEllipse> ellipseList) {
    System.out.println("ellipseList = " + ellipseList);
  }


  @Override
  public void run() {
    while(!queue.isEmpty()){
      try {
        queue.drainTo(ellipseList);
        runPipeline(ellipseList);

        sleep(1000); //sleeps 1 second
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }


}
