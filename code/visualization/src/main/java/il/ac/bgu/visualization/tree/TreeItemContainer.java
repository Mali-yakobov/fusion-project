package il.ac.bgu.visualization.tree;

import il.ac.bgu.fusion.objects.CovarianceEllipse;
import il.ac.bgu.fusion.objects.PointInTime;
import il.ac.bgu.fusion.objects.State;
import il.ac.bgu.fusion.objects.Track;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by Stas on 07/04/2017.
 */
public class TreeItemContainer implements HierarchyData {

    private ObservableList<HierarchyData> children;
    private Object containedItem;
    private Object containedGraphicItem;
    private String displayName;
    private long colorId;

    public TreeItemContainer(){
        children= FXCollections.observableList(new ArrayList<HierarchyData>());
        containedItem= null;
        containedGraphicItem= null;
        displayName= null;
        colorId= -1;
    }

    public TreeItemContainer(Object item){
        children= FXCollections.observableList(new ArrayList<HierarchyData>());
        containedItem= item;
        containedGraphicItem= null;
        displayName= determineName(item);
        colorId= -1;
    }




    @Override
    public ObservableList<HierarchyData> getChildren() {
        return this.children;
    }


    public long getColorId() {
        return colorId;
    }

    public Object getContainedItem() {
        return containedItem;
    }

    public Object getContainedGraphicItem() {
        return containedGraphicItem;
    }

    public String getDisplayName() {
        return displayName;
    }



    public void setContainedGraphicItem(Object graphicItem) {
        containedGraphicItem= graphicItem;
    }

    public void setColorId(long newColorId) {
        this.colorId = newColorId;
    }


    @Override
    public String toString() {
        return this.displayName;
    }



    private String determineName(Object item){
        String ans= null;

        if (item instanceof PointInTime)
            ans= "Timestamp: " + Long.toString(((PointInTime) item).getTimeStamp());
        else
            if (item instanceof Track)
                ans= "Track " + Long.toString(((Track) item).getId());
            else
                if (item instanceof State)
                    ans= "State";
                else
                if (item instanceof CovarianceEllipse)
                    ans= "Ellipse " + Long.toString(((CovarianceEllipse) item).getId());
                else
                    // ans= item.getClass().getSimpleName();
                    ans= "Who Am I?";
        return ans;
    }

}