package il.ac.bgu.fusion.fusion.algorithm;

import il.ac.bgu.fusion.objects.CovarianceEllipse;
import il.ac.bgu.fusion.util.JsonReaderWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Maayan on 27/04/2017.
 */
public class JsonToQueue implements Runnable {

  protected BlockingQueue queue = null;

  public JsonToQueue(BlockingQueue queue) {
    this.queue = queue;
  }

  @Override
  public void run() {

    String filename = "json-input";
   // String fileaddress = "D:\\sigmabit\\fusion-project\\";
    String filepath = /*fileaddress*/ filename + ".json";
    ArrayList<CovarianceEllipse> ellipseList = JsonReaderWriter.elipseFromFile(filepath);
    System.out.print(ellipseList);

    Iterator ellipseIter = ellipseList.iterator();

    while(ellipseIter.hasNext()){
      try {
        queue.put(ellipseIter.next());

      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
    }




    }
}
