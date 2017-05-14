package il.ac.bgu.fusion.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mali on 4/1/2017.
 */
public class State {

    private long startTimeStamp;
    private long endTimeStamp;
    private List<CovarianceEllipse> ellipseList;
    private CovarianceEllipse fusionEllipse;


    public State(){}//empty constructor

    public State(List<CovarianceEllipse> ellipseList, CovarianceEllipse fusionEllipse) {
        this.ellipseList = ellipseList;
        this.fusionEllipse= fusionEllipse;
    }
    public State(List<CovarianceEllipse> ellipseList) {
        this.ellipseList = ellipseList;
    }

    public List<CovarianceEllipse> getEllipseList() {
        return ellipseList;
    }

    public void setEllipseList(List<CovarianceEllipse> ellipseList) {
        this.ellipseList = ellipseList;
    }

    @Override
    public String toString() {
        return "State{" +"\n"+
                "ellipseList=" + ellipseList +"\n"+
                '}';
    }

    public CovarianceEllipse getFusionEllipse() {
        return fusionEllipse;
    }

    public void setFusionEllipse(CovarianceEllipse fusionEllipse) {
        this.fusionEllipse = fusionEllipse;
    }

    public long getStartTimeStamp() {
        long min=this.ellipseList.get(0).getTimeStamp();
        for(CovarianceEllipse ellipse: ellipseList){
            if(ellipse.getTimeStamp()<min)
                min=ellipse.getTimeStamp();
        }
        this.startTimeStamp=min;
        return startTimeStamp;
    }

    public void setStartTimeStamp(long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public long getEndTimeStamp() {
        long max=this.ellipseList.get(0).getTimeStamp();
        for(CovarianceEllipse ellipse: ellipseList){
            if(ellipse.getTimeStamp()>max)
                max=ellipse.getTimeStamp();
        }
        this.endTimeStamp=max;
        return endTimeStamp;
    }

    public void setEndTimeStamp(long endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }
}
