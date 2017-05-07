package il.ac.bgu.fusion.fusion.algorithm;

import il.ac.bgu.fusion.objects.CovarianceEllipse;
import il.ac.bgu.fusion.util.JsonReaderWriter;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

/**
 * Created by Maayan on 27/04/2017.
 */
public class JsonToQueue implements Callable {

  protected BlockingQueue queue = null;

  public JsonToQueue(BlockingQueue queue) {
    this.queue = queue;
  }

  @Override
  public Object call() throws Exception {

    String filename = "json-input";
    // String fileaddress = "D:\\sigmabit\\fusion-project\\";
    String filepath = /*fileaddress*/ filename + ".json";
    List<CovarianceEllipse> ellipseList = JsonReaderWriter.elipseFromFile(filepath);
    System.out.print(ellipseList);

    Iterator ellipseIter = ellipseList.iterator();

    while(ellipseIter.hasNext())
    {
      queue.put(ellipseIter.next());
    }
    return null;
  }
}
