package il.ac.bgu.fusion.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mali on 4/1/2017.
 */
public class State {


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
}
