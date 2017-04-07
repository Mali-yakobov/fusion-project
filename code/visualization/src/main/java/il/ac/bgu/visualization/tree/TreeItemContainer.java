package il.ac.bgu.visualization.tree;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * Created by Stas on 07/04/2017.
 */
public class TreeItemContainer implements HierarchyData {

    private ObservableList<HierarchyData> children;
    private Object containedItem;
    private String displayName;

    public TreeItemContainer(){
        children= FXCollections.observableList(new ArrayList<HierarchyData>());
        containedItem= null;
        displayName= null;
    }

    public TreeItemContainer(Object item){
        children= FXCollections.observableList(new ArrayList<HierarchyData>());
        containedItem= item;
        displayName= item.getClass().getSimpleName();
    }



    @Override
    public ObservableList<HierarchyData> getChildren() {
        return this.children;
    }

    public Object getContainedItem() {
        return containedItem;
    }

    public String getDisplayName() {
        return displayName;
    }


    @Override
    public String toString() {
        return displayName;
    }
}