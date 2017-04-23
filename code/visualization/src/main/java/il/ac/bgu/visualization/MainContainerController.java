package il.ac.bgu.visualization;

import com.google.gson.JsonSyntaxException;
import il.ac.bgu.fusion.objects.CovarianceEllipse;
import il.ac.bgu.fusion.objects.PointInTime;
import il.ac.bgu.fusion.objects.State;
import il.ac.bgu.fusion.objects.Track;
import il.ac.bgu.fusion.util.JsonReaderWriter;
import il.ac.bgu.visualization.objects.AddEllipseBox;
import il.ac.bgu.visualization.objects.AlertWindow;
import il.ac.bgu.visualization.tree.HierarchyData;
import il.ac.bgu.visualization.tree.TreeItemContainer;
import il.ac.bgu.visualization.tree.TreeViewWithItems;
import il.ac.bgu.visualization.util.EllipseRepresentationTranslation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.util.*;


/**
 * FXML Controller class for main_container.fxml
 */
public class MainContainerController implements Initializable {

    private ArrayList<PointInTime> pointInTimeArray = null;
    private int pointInTimeArrayIndex= -1;

    private ArrayList<Ellipse> clickedEllipses= new ArrayList<Ellipse>();   // PROBABLY NOT NEEDED ANYMORE
    private ArrayList<CovarianceEllipse> clickedCovEllipses= new ArrayList<CovarianceEllipse>();

    private HashMap<Long, Color> colorByTrackIdTable= new HashMap<Long, Color>();

    @FXML
    private AnchorPane viewArea;
    @FXML
    private Button forwardButton;
    @FXML
    private Button backwardButton;
    @FXML
    private Button resetButton;


    /* tree related code start  */
    @FXML
    private AnchorPane treeArea;                      //get place for tree from fxml
    private TreeItem<HierarchyData> root;             //root for tree
    private TreeViewWithItems tree;                   //tree
    private ObservableList<HierarchyData> treeItems;  //data source for tree
    private ContextMenu treeMenu;                     //context menu for tree (empty space)
    private TreeItemContainer selectedItemContainer;  //currently selected tree item container
    /* tree related code end  */


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewArea.getStyleClass().add("viewarea-class");
        forwardButton.getStyleClass().add("forward-button-class");
        backwardButton.getStyleClass().add("backward-button-class");
        resetButton.getStyleClass().add("reset-button-class");

        //make context menu for viewArea (empty space):
        MenuItem addEllipseMenuItem = new MenuItem("Add Ellipse");
        ContextMenu viewAreaMenu = new ContextMenu(addEllipseMenuItem);
        viewArea.setOnMouseClicked(event -> viewAreaMenu.hide());
        viewArea.setOnContextMenuRequested(event -> {
            addEllipseMenuItem.setOnAction(e -> addEllipseOnClickAction(event.getX(), event.getY()));
            viewAreaMenu.show(viewArea, event.getScreenX(), event.getScreenY());
        });

        /* tree related code start  */
        treeInit();
        /* tree related code end  */
    }//initialize


    /*
     Action functions for GUI components:
     */

    public void loadFileAction() {
        String filePath= null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Json File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("json", "*.json"));
        File file= fileChooser.showOpenDialog(viewArea.getScene().getWindow());
        if (file != null)
            filePath = file.getAbsolutePath();
        if (filePath != null){
            try{
                pointInTimeArray = JsonReaderWriter.jsonToObject(filePath);
                colorByTrackIdTable.clear();
                treeItems.clear();
                fillTreeItemsFromJson(pointInTimeArray, treeItems);
                resetAction();
                // System.out.println(pointInTimeArray.get(0).getClass().getSimpleName());
            }
            catch (JsonSyntaxException e){
                AlertWindow.display("Json Error", e.getMessage());
            }
        }
    }

    public void forwardAction(){
        pointInTimeArrayIndex++;
        PointInTime pointInTime = pointInTimeArray.get(pointInTimeArrayIndex);
        clearScreen();
        showPointInTime(pointInTime);
        if (pointInTimeArrayIndex == 0) {
            backwardButton.setDisable(false);
            resetButton.setDisable(false);
        }
        else
        if (pointInTimeArrayIndex == pointInTimeArray.size()-1)
            forwardButton.setDisable(true);
    }

    public void backwardAction(){
        pointInTimeArrayIndex--;
        clearScreen();
        if (pointInTimeArrayIndex == -1) {
            backwardButton.setDisable(true);
            resetButton.setDisable(true);
        }
        else{
            PointInTime pointInTime = pointInTimeArray.get(pointInTimeArrayIndex);
            showPointInTime(pointInTime);
            if (pointInTimeArrayIndex == pointInTimeArray.size()-2)
                forwardButton.setDisable(false);
        }
    }

    public void addEllipseAction() {
        Ellipse newEllipse= AddEllipseBox.display();
        Color newColor= colorGenerator();
        ellipseSetOnClick(newEllipse, null);
        showEllipse(newEllipse, newColor);
    }

    public void addEllipseOnClickAction(double x, double y) {
        Ellipse newEllipse= AddEllipseBox.display(x, y);
        Color newColor= colorGenerator();
        ellipseSetOnClick(newEllipse, null);
        showEllipse(newEllipse, newColor);
    }

    public void clearAction() {
        clearScreen();
        pointInTimeArrayIndex= -1;
        pointInTimeArray = null;
        forwardButton.setDisable(true);
        backwardButton.setDisable(true);
        resetButton.setDisable(true);
        colorByTrackIdTable.clear();
        treeItems.clear();
    }

    public void resetAction() {
        clearScreen();
        pointInTimeArrayIndex= -1;
        forwardButton.setDisable(false);
        backwardButton.setDisable(true);
        resetButton.setDisable(true);
    }

    public void closeAction() {
        GUI.window.close();
    }


    /*
     Misc functions:
     */

    public void ellipseSetOnClick(Ellipse ellipse, CovarianceEllipse covEllipse) {
        MenuItem addEllipseMenuItem3 = new MenuItem(ellipse.toString());
        ContextMenu viewAreaMenu3 = new ContextMenu(addEllipseMenuItem3);
        ellipse.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent enterevent) {
                viewAreaMenu3.show(ellipse, enterevent.getSceneX(), enterevent.getSceneY());
            }
        });

        ellipse.setOnMouseExited(exitevent -> viewAreaMenu3.hide());

        ellipse.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)){
                Color clF= (Color) ellipse.getFill();
                if (clF.getOpacity() < 0.7)                   //was not clicked/chosen
                    ellipseSetClicked(ellipse, covEllipse);
                else                                          //was clicked/chosen
                    ellipseSetUnclicked(ellipse, covEllipse);
            }
        });


//
    }//ellipseSetOnClick

    public void ellipseSetClicked(Ellipse ellipse, CovarianceEllipse covEllipse) {
        Color clF= (Color) ellipse.getFill();
        if (clF.getOpacity() < 0.7){
            clickedEllipses.add(ellipse);
            if (covEllipse != null)
                clickedCovEllipses.add(covEllipse);
            Color clS= (Color) ellipse.getStroke();
            ellipse.setFill(new Color(clF.getRed(), clF.getGreen(), clF.getBlue(), 0.9));
            ellipse.setStroke(clS.invert());
        }
    }

    public void ellipseSetUnclicked(Ellipse ellipse, CovarianceEllipse covEllipse) {
        Color clF= (Color) ellipse.getFill();
        if (clF.getOpacity() >= 0.7){
            clickedEllipses.remove(ellipse);
            if (covEllipse != null)
                clickedCovEllipses.remove(covEllipse);
            Color clS= (Color) ellipse.getStroke();
            ellipse.setFill(new Color(clF.getRed(), clF.getGreen(), clF.getBlue(), 0.6));
            ellipse.setStroke(clS.invert());
        }
    }


    public void clearScreen() {
        viewArea.getChildren().clear(); //viewArea.getChildren().remove(newEllipse);
        clickedEllipses.clear();
        clickedCovEllipses.clear();
    }

    public void showPointInTime(PointInTime p){
        Iterator<Track> trackIterator = p.getTrackList().iterator();
        while(trackIterator.hasNext()){
            Track currTrack= trackIterator.next(); //colorByTrackIdTable
            Iterator<State> stateIterator = currTrack.getStateList().iterator();
           /* if (!colorByTrackIdTable.containsKey(currTrack.getId()))
                colorByTrackIdTable.put(currTrack.getId(), colorGenerator());*/
            while(stateIterator.hasNext())
                showState(stateIterator.next(), colorByTrackIdTable.get(currTrack.getId()));
        }
    }

    public void showState(State state, Color color) {
        ArrayList<CovarianceEllipse> CovarianceEllipseArray = state.getEllipseList();
        Iterator<CovarianceEllipse> itr = CovarianceEllipseArray.iterator();
        while (itr.hasNext()) {
            CovarianceEllipse tempCovEllipse= itr.next();
            Ellipse tempEllipse = EllipseRepresentationTranslation.fromCovarianceToVizual(tempCovEllipse);
            ellipseSetOnClick(tempEllipse, tempCovEllipse);
            showEllipse(tempEllipse, color);
        }
    }

    public void showEllipse(Ellipse elipse, Color color){
        elipse.setFill(color);
        elipse.setStroke(new Color(color.getRed(), color.getGreen(), color.getBlue(), 1));
        viewArea.getChildren().addAll(elipse);
    }

    public Color colorGenerator() {
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        return  new Color(r, g, b, 0.6);
    }







    /* tree related code start  */

    /**
     * On-click settings of the tree cells are here
     * */
    private void treeInit() {
        //init root (for tree):
        root= new TreeItem<>(new TreeItemContainer());
        root.setExpanded(true);

        //init tree:
        tree= new TreeViewWithItems(root);
        tree.setShowRoot(false);

        //set tree and add tree to GUI
        tree.prefWidthProperty().bind(treeArea.widthProperty());    //bound tree width to parent width
        tree.prefHeightProperty().bind(treeArea.heightProperty());  //bound tree height to parent height
        tree.setEditable(true);
        treeArea.getChildren().add(tree);

        //set cell factory for tree
        this.tree.setCellFactory(new Callback<TreeView<HierarchyData>,TreeCell<HierarchyData>>(){
            @Override
            public TreeCell<HierarchyData> call(TreeView<HierarchyData> p) {
                return new NameIconAddCell();
            }
        });

        //set actions for selecting a node on tree
        this.tree.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ObservableValue observable, Object oldValue, Object newValue) {  if(newValue != null){

                        TreeItem<HierarchyData> selectedTreeNode = (TreeItem<HierarchyData>) newValue;
                        TreeItem<HierarchyData> oldTreeNode = (TreeItem<HierarchyData>) oldValue;

                        TreeItemContainer newItemContainer= (TreeItemContainer)selectedTreeNode.getValue();
                        selectedItemContainer= newItemContainer;
                        TreeItemContainer oldItemContainer= null;
                        if (oldTreeNode != null)
                            oldItemContainer= (TreeItemContainer)oldTreeNode.getValue();

                        unclickAll();
                        recursiveTreeClick(newItemContainer);

                        /* if old item exist: */
                        /*if (oldItemContainer != null) {
                            Object oldItem = oldItemContainer.getContainedItem();
                            Object oldGraphicItem = oldItemContainer.getContainedGraphicItem();

                            if (oldItem instanceof CovarianceEllipse) {
                                CovarianceEllipse covEllipse = (CovarianceEllipse) oldItem;
                                if (oldGraphicItem != null) {
                                    Ellipse ellipse = (Ellipse) oldGraphicItem;
                                    ellipseSetUnclicked(ellipse, covEllipse);
                                }
                            }
                        }*/ /* if old item exist: */


                        /* dealing with the new item: */
                        /*Object newItem= newItemContainer.getContainedItem();
                        Object newGraphicItem= newItemContainer.getContainedGraphicItem();

                        if (newItem instanceof CovarianceEllipse){
                            CovarianceEllipse covEllipse= (CovarianceEllipse)newItem;
                            if (newGraphicItem != null){
                                Ellipse ellipse= (Ellipse)newGraphicItem;
                                ellipseSetClicked(ellipse, covEllipse);
                            }
                        }*/

                    }}//changed(...)
                }//new ChangeListener(){
        );//.addListener(

        treeItems= FXCollections.observableList(new ArrayList<HierarchyData>()); //init data source for tree
        tree.setItems(treeItems);                                                //bind tree with data source (items)

    }//treeInit

    /**cc
     * Modify TreeCell class for cell-factory of the tree
     *
     * On-create settings of the tree cells are here
     */
    private final class NameIconAddCell extends TreeCell<HierarchyData> {
        private ContextMenu defaultTreeContextMenu= new ContextMenu();

        public NameIconAddCell() {
            //make default context menu for tree nodes:
            MenuItem defaultTreeContextMenuShowItem = new MenuItem("Show");
            MenuItem defaultTreeContextMenuHideItem = new MenuItem("Hide");
            defaultTreeContextMenuHideItem.setDisable(true);
            defaultTreeContextMenu.getItems().addAll(defaultTreeContextMenuShowItem, defaultTreeContextMenuHideItem);
            //set actions for default context menu:
            defaultTreeContextMenuShowItem.setOnAction( e -> {
                defaultTreeContextMenuShowItem.setDisable(true);
                defaultTreeContextMenuHideItem.setDisable(false);
                recursiveShowItemContainer(selectedItemContainer);
            });
            defaultTreeContextMenuHideItem.setOnAction( e -> {
                defaultTreeContextMenuShowItem.setDisable(false);
                defaultTreeContextMenuHideItem.setDisable(true);
                recursiveHideItemContainer(selectedItemContainer);
            });
        }//NameIconAddCell

        @Override
        public void updateItem(HierarchyData item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) { //cell is empty and not being rendered (for example collapsed parrent):
                setText(null);
                setContextMenu(null);
                //setGraphic(null);
            }
            else { //cell is filled, specific settings depends on the class of the item represented by the cell:
                //setGraphic(getItem().getNode());   //if we use CSS, not needed
                setText(getItem().toString());
                setContextMenu(defaultTreeContextMenu);

               /* if instanceof... */

            }//else (cell is filled)
        }//updateItem

    }//NameIconAddCell

    private void fillTreeItemsFromJson(ArrayList<PointInTime> listOfPoints, ObservableList<HierarchyData> treeData){
        Iterator<PointInTime> pointIterator = listOfPoints.iterator();
        while(pointIterator.hasNext()){
            PointInTime currPoint= pointIterator.next();
            TreeItemContainer currPointItem= new TreeItemContainer(currPoint);  /*1*/
            Iterator<Track> trackIterator = currPoint.getTrackList().iterator();
            while(trackIterator.hasNext()){
                Track currTrack= trackIterator.next();
                if (!colorByTrackIdTable.containsKey(currTrack.getId()))
                    colorByTrackIdTable.put(currTrack.getId(), colorGenerator());
                TreeItemContainer currTrackItem= new TreeItemContainer(currTrack);  /*2*/
                currTrackItem.setColorId(currTrack.getId());
                Iterator<State> stateIterator = currTrack.getStateList().iterator();
                while(stateIterator.hasNext()){
                    State currState= stateIterator.next();
                    TreeItemContainer currStateItem= new TreeItemContainer(currState);  /*3*/
                    currStateItem.setColorId(currTrack.getId());
                    Iterator<CovarianceEllipse> ellipseIterator = currState.getEllipseList().iterator();
                    while (ellipseIterator.hasNext()) {
                        CovarianceEllipse currEllipse= ellipseIterator.next();
                        TreeItemContainer currEllipseItem= new TreeItemContainer(currEllipse); /*4*/
                        currEllipseItem.setColorId(currTrack.getId());
                        currStateItem.getChildren().add(currEllipseItem);                      /*4*/
                    }//no more ellipses in current state
                    currTrackItem.getChildren().add(currStateItem); /*3*/
                }//no more states in current track
                currPointItem.getChildren().add(currTrackItem);/*2*/
            }//no more tracks in current point
            treeData.add(currPointItem); /*1*/
        }//no more points in list
    }//fillTreeItemsFromJson


    private void recursiveShowItemContainer(TreeItemContainer itemContainer){
        Object item= itemContainer.getContainedItem();

        if (item instanceof CovarianceEllipse)
            showEllipseContainer(itemContainer);
        else{
            ObservableList<HierarchyData> childrenList= itemContainer.getChildren();
            if (childrenList.size()==0)
                return;
            Iterator<HierarchyData> childrenListIterator = childrenList.iterator();
            while (childrenListIterator.hasNext())
                recursiveShowItemContainer((TreeItemContainer)childrenListIterator.next());
        }
    }

    private void showEllipseContainer(TreeItemContainer ellipseContainer){
        CovarianceEllipse covEllipse= (CovarianceEllipse)ellipseContainer.getContainedItem();
        if (ellipseContainer.getContainedGraphicItem() == null){
            Ellipse ellipse = EllipseRepresentationTranslation.fromCovarianceToVizual(covEllipse);
            ellipseSetOnClick(ellipse, covEllipse);
            showEllipse(ellipse, colorByTrackIdTable.get(ellipseContainer.getColorId()));
            ellipseSetClicked(ellipse, covEllipse);
            ellipseContainer.setContainedGraphicItem(ellipse);
        }
    }


    private void recursiveHideItemContainer(TreeItemContainer itemContainer){
        Object item= itemContainer.getContainedItem();

        if (item instanceof CovarianceEllipse)
            hideEllipseContainer(itemContainer);
        else{
            ObservableList<HierarchyData> childrenList= itemContainer.getChildren();
            if (childrenList.size()==0)
                return;
            Iterator<HierarchyData> childrenListIterator = childrenList.iterator();
            while (childrenListIterator.hasNext())
                recursiveHideItemContainer((TreeItemContainer)childrenListIterator.next());
        }
    }

    private void hideEllipseContainer(TreeItemContainer ellipseContainer){
        CovarianceEllipse covEllipse= (CovarianceEllipse)ellipseContainer.getContainedItem();
        Ellipse ellipse= (Ellipse)ellipseContainer.getContainedGraphicItem();
        if (ellipse != null){
            viewArea.getChildren().remove(ellipse);
            clickedEllipses.remove(ellipse);
            clickedCovEllipses.remove(covEllipse);
            ellipseContainer.setContainedGraphicItem(null);
        }
    }


    private void recursiveTreeClick(TreeItemContainer itemContainer){
        Object item= itemContainer.getContainedItem();
        Object graphicItem= itemContainer.getContainedGraphicItem();

        if (item instanceof CovarianceEllipse) {
            if (graphicItem != null) {
                CovarianceEllipse covEllipse = (CovarianceEllipse) item;
                Ellipse ellipse = (Ellipse) graphicItem;
                ellipseSetClicked(ellipse, covEllipse);
            }
        }
        else{
            ObservableList<HierarchyData> childrenList= itemContainer.getChildren();
            if (childrenList.size()==0)
                return;
            Iterator<HierarchyData> childrenListIterator = childrenList.iterator();
            while (childrenListIterator.hasNext())
                recursiveTreeClick((TreeItemContainer)childrenListIterator.next());
        }
    }

    private void unclickAll(){
        ArrayList<Ellipse> toUnclickList= new ArrayList<Ellipse>();

        Iterator<Ellipse> clickedEllipseIterator = clickedEllipses.iterator();
        while (clickedEllipseIterator.hasNext()) {
            Ellipse toUnclick= clickedEllipseIterator.next();
            if (clickedEllipses.contains(toUnclick))
                toUnclickList.add(toUnclick);
        }

        Iterator<Ellipse> toUnclickIterator = toUnclickList.iterator();
        while (toUnclickIterator.hasNext())
            ellipseSetUnclicked(toUnclickIterator.next(), null);

        clickedEllipses.clear();
        clickedCovEllipses.clear();
    }

    /* tree related code end  */





}//MainContainerController
