package il.ac.bgu.fusion.fusion.algorithm;

import il.ac.bgu.fusion.objects.CovarianceEllipse;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import static java.lang.Thread.sleep;

/**
 * Created by Maayan on 27/04/2017.
 */
public class QueueToPipeline implements Callable {

  protected BlockingQueue queue = null;
  List<CovarianceEllipse> ellipseList;

  public QueueToPipeline(BlockingQueue queue) {
    this.queue = queue;
  }

  private void runPipeline(List<CovarianceEllipse> ellipseList) {
    System.out.println("pipeline :" + queue.size());
  }

  @Override
  public Object call() throws Exception {
    int ellipseCount = 0;
    while(queue.isEmpty())
    {
      System.out.printf("entering while");
      if(queue.size() > 0) {
        queue.drainTo(ellipseList);
        System.out.println("after drain"+ queue.size() );
      }

      runPipeline(ellipseList);
      sleep(5000); //sleeps 5 seconds
      System.out.println("waking up");
    }
    return null;
  }
}
