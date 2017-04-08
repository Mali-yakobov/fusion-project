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
import javafx.scene.Node;
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
    private ArrayList<Ellipse> clickedEllipses= new ArrayList<Ellipse>();
    private HashMap<Long, Color> colorByTrackIdTable= new HashMap<Long, Color>();

    @FXML
    private AnchorPane viewArea;
    @FXML
    private Button forwardButton;
    @FXML
    private Button backwardButton;
    @FXML
    private Button resetButton;


    /* tree start  */
    @FXML
    private AnchorPane treeArea;                      //get place for tree from fxml
    private TreeItem<HierarchyData> root;             //root for tree
    private TreeViewWithItems tree;                   //tree
    private ObservableList<HierarchyData> treeItems;  //data source for tree
    private ContextMenu treeMenu;                     //context menu for tree (empty space)
    private TreeItem<HierarchyData> selectedNode;     //currently selected tree item

    /* tree end  */



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

        /* tree start  */
        treeInit();
        /* tree end  */
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
                treeItems.clear();
                fillTreeItemsFromJson(pointInTimeArray, treeItems);
                resetAction();
                colorByTrackIdTable.clear();
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


        public void addEllipseAction () {
            Ellipse newEllipse = AddEllipseBox.display();
            Color cl = colorGenerator();
            newEllipse.setFill(cl);
            newEllipse.setStroke(new Color(cl.getRed(), cl.getGreen(), cl.getBlue(), 1));
            ellipseSetOnClick(newEllipse);
            viewArea.getChildren().addAll(newEllipse);

                                              for (Node node2 : viewArea.getChildren()
                                                      ) {
                                                  node2.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                                      public void handle(MouseEvent meee) {
                                                          MenuItem addEllipseMenuItem2 = new MenuItem(node2.toString());
                                                          ContextMenu viewAreaMenu2 = new ContextMenu(addEllipseMenuItem2);
                                                          viewAreaMenu2.show(node2, meee.getScreenX(), meee.getScreenY());
                                                          System.out.println("Mouse entered");
                                                          node2.setOnMouseExited(ekmeee -> viewAreaMenu2.hide());

                                                      }

                                                  });



                                              }

                                          }


    public void addEllipseOnClickAction(double x, double y) {
        Ellipse newEllipse= AddEllipseBox.display(x, y);
        Color cl= colorGenerator();
        newEllipse.setFill(cl);
        newEllipse.setStroke(new Color(cl.getRed(), cl.getGreen(), cl.getBlue(), 1));
        ellipseSetOnClick(newEllipse);
        viewArea.getChildren().addAll(newEllipse);
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

    public void ellipseSetOnClick(Ellipse ellipse) {
        ellipse.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)){
                Color clF= (Color) ellipse.getFill();
                Color clS= (Color) ellipse.getStroke();
                if (clF.getOpacity()==0.5){                   //was not clicked/chosen
                    clickedEllipses.add(ellipse);
                    ellipse.setFill(new Color(clF.getRed(), clF.getGreen(), clF.getBlue(), 0.85));
                    ellipse.setStroke(clS.invert());
                }
                else{                                          //was clicked/chosen
                    clickedEllipses.remove(ellipse);
                    ellipse.setFill(new Color(clF.getRed(), clF.getGreen(), clF.getBlue(), 0.5));
                    ellipse.setStroke(clS.invert());
                }
            }
        });
    }


    public void clearScreen() {
        viewArea.getChildren().clear(); //viewArea.getChildren().remove(newEllipse);
        clickedEllipses.clear();
    }

    public void showPointInTime(PointInTime p){
        Iterator<Track> trackIterator = p.getTrackList().iterator();
        while(trackIterator.hasNext()){
            Track currTrack= trackIterator.next(); //colorByTrackIdTable
            Iterator<State> stateIterator = currTrack.getStateList().iterator();
            if (!colorByTrackIdTable.containsKey(currTrack.getId()))
                colorByTrackIdTable.put(currTrack.getId(), colorGenerator());
            while(stateIterator.hasNext())
                showState(stateIterator.next(), colorByTrackIdTable.get(currTrack.getId()));
        }
    }

    public void showState(State state, Color color) {
        ArrayList<CovarianceEllipse> CovarianceEllipseArray = state.getEllipseList();
        Iterator<CovarianceEllipse> itr = CovarianceEllipseArray.iterator();
        while (itr.hasNext()) {
            Ellipse tempEllipse = EllipseRepresentationTranslation.fromCovarianceToVizual(itr.next());
            tempEllipse.setFill(color);
            tempEllipse.setStroke(new Color(color.getRed(), color.getGreen(), color.getBlue(), 1));
            ellipseSetOnClick(tempEllipse);
            viewArea.getChildren().addAll(tempEllipse);

        }
        for (Node node3 : viewArea.getChildren()
                ) {
            node3.setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent meee) {
                    MenuItem addEllipseMenuItem3 = new MenuItem(node3.toString());
                    ContextMenu viewAreaMenu3 = new ContextMenu(addEllipseMenuItem3);
                    viewAreaMenu3.show(node3, meee.getScreenX(), meee.getScreenY());
                //    System.out.println("Mouse entered2222");
                    node3.setOnMouseExited(ekmeee -> viewAreaMenu3.hide());
                }
            });
        }

    }


    public Color colorGenerator() {
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        return  new Color(r, g, b, 0.50);
    }



//viewArea.setOnMouseClicked(new EventHandler<MouseEvent>() {
//
//        public void handle(MouseEvent event) {
//            System.out.println(event.getSceneX());
//            System.out.println(event.getSceneY());
//        }
//    });

    /* tree start  */
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
                        TreeItem<HierarchyData> selectedTreeItem = (TreeItem<HierarchyData>) newValue;
                        TreeItem<HierarchyData> oldTreeItem = (TreeItem<HierarchyData>) oldValue;
                        HierarchyData newItem= selectedTreeItem.getValue();
                        HierarchyData oldItem= null;
                        if (oldTreeItem != null)
                            oldItem= oldTreeItem.getValue();

                        selectedNode= selectedTreeItem;

                        /*
                        if instanceof ....
                         */

                    }}//changed(...)
                }//new ChangeListener(){
        );//.addListener(

        treeItems= FXCollections.observableList(new ArrayList<HierarchyData>()); //init data source for tree
        tree.setItems(treeItems);                                                //bind tree with data source (items)

    }//treeInit

    private void fillTreeItemsFromJson(ArrayList<PointInTime> listOfPoints, ObservableList<HierarchyData> treeData){
        Iterator<PointInTime> pointIterator = listOfPoints.iterator();
        while(pointIterator.hasNext()){
            PointInTime currPoint= pointIterator.next();
            TreeItemContainer currPointItem= new TreeItemContainer(currPoint);  /*1*/
            Iterator<Track> trackIterator = currPoint.getTrackList().iterator();
            while(trackIterator.hasNext()){
                Track currTrack= trackIterator.next();
                TreeItemContainer currTrackItem= new TreeItemContainer(currTrack);  /*2*/
                Iterator<State> stateIterator = currTrack.getStateList().iterator();
                while(stateIterator.hasNext()){
                    State currState= stateIterator.next();
                    TreeItemContainer currStateItem= new TreeItemContainer(currState);  /*3*/
                    Iterator<CovarianceEllipse> ellipseIterator = currState.getEllipseList().iterator();
                    while (ellipseIterator.hasNext()) {
                        CovarianceEllipse currEllipse= ellipseIterator.next();
                        TreeItemContainer currEllipseItem= new TreeItemContainer(currEllipse); /*4*/
                        currStateItem.getChildren().add(currEllipseItem);                      /*4*/
                    }//no more ellipses in current state
                    currTrackItem.getChildren().add(currStateItem); /*3*/
                }//no more states in current track
                currPointItem.getChildren().add(currTrackItem);/*2*/
            }//no more tracks in current point
            treeData.add(currPointItem); /*1*/
        }//no more points in list
    }//fillTreeItemsFromJson

    /**
     * Modify TreeCell class for cell-factory of the tree
     */
    private final class NameIconAddCell extends TreeCell<HierarchyData> {
        private ContextMenu blankMenu= new ContextMenu();

        public NameIconAddCell() {
            //make context menus for items:............
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

               /* if instanceof... */

            }//else (cell is filled)
        }//updateItem

    }//NameIconAddCell
   /* tree end  */




}//MainContainerController
