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
    System.out.println("ellipseList = " + ellipseList);
  }

  @Override
  public Object call() throws Exception {
    while(!queue.isEmpty())
    {
      queue.drainTo(ellipseList);
      runPipeline(ellipseList);
      sleep(1000); //sleeps 1 second
    }
    return null;
  }
}
