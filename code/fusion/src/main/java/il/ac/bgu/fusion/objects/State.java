package il.ac.bgu.fusion.objects;

import java.util.ArrayList;

/**
 * Created by Mali on 4/1/2017.
 */
public class State {

    // private long startTime;
    // private long enime;
    private ArrayList<CovarianceEllipse> ellipseList;
    private CovarianceEllipse fusionEllipse;

    public State(){}//empty constructor

    public State(ArrayList<CovarianceEllipse> ellipseList, CovarianceEllipse fusionEllipse) {
        this.ellipseList = ellipseList;
        this.fusionEllipse= fusionEllipse;
    }
    public State(ArrayList<CovarianceEllipse> ellipseList) {
        this.ellipseList = ellipseList;
    }

    public ArrayList<CovarianceEllipse> getEllipseList() {
        return ellipseList;
    }

    public void setEllipseList(ArrayList<CovarianceEllipse> ellipseList) {
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
}
