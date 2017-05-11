package il.ac.bgu.fusion.fusion.algorithm;

import il.ac.bgu.fusion.objects.CovarianceEllipse;
import il.ac.bgu.fusion.util.JsonReaderWriter;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Maayan on 27/04/2017.
 */
public class JsonToQueue implements Runnable {

  private final BlockingQueue sharedQueue;

  public JsonToQueue(BlockingQueue sharedQueue) {
    this.sharedQueue = sharedQueue;
  }

  @Override
  public void run() {
    String filename = "forAlg";
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
    System.out.println("JsonToQueue Thread finished - no more ellipses in .json file");
  }

}

