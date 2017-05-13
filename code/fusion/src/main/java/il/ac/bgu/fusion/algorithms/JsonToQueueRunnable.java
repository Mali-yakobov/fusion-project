package il.ac.bgu.fusion.algorithms;

import il.ac.bgu.fusion.objects.CovarianceEllipse;
import il.ac.bgu.fusion.util.JsonReaderWriter;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Maayan on 27/04/2017.
 */
public class JsonToQueueRunnable implements Runnable {

  private final BlockingQueue sharedQueue;

  public JsonToQueueRunnable(BlockingQueue sharedQueue) {
    this.sharedQueue = sharedQueue;
  }

  @Override
  public void run() {
    String filename = "jsonForThreadTesting1";
    String filepath = filename + ".json";
    List<CovarianceEllipse> ellipseList = JsonReaderWriter.elipseFromFile(filepath);
    Iterator ellipseIter = ellipseList.iterator();

    while (ellipseIter.hasNext()) {
        try {
          //putting ellipse in queue
          sharedQueue.put(ellipseIter.next());
          System.out.println("New ellipse added to queue");
          //sleeps 2 seconds
          Thread.sleep(2000);
         // queue.notifyAll(); //do we need this?
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
    }
    System.out.println("JsonToQueueRunnable Thread finished - no more ellipses in .json file");
  }

}

