package il.ac.bgu.visualization;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

import com.google.gson.JsonSyntaxException;
import il.ac.bgu.fusion.objects.CovarianceEllipse;
import il.ac.bgu.fusion.objects.PointInTime;
import il.ac.bgu.fusion.objects.State;
import il.ac.bgu.fusion.objects.Track;
import il.ac.bgu.fusion.util.JsonReaderWriter;
import il.ac.bgu.visualization.objects.AddEllipseBox;
import il.ac.bgu.visualization.objects.AlertWindow;
import il.ac.bgu.visualization.util.EllipseRepresentationTranslation;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.stage.FileChooser;


/**
 * FXML Controller class for main_container.fxml
 */
public class MainContainerController implements Initializable {

    private String filePath= null;
    ArrayList<PointInTime> pointInTimeArray = null;
    int pointInTimeArrayIndex= -1;
    private ArrayList<Ellipse> clickedEllipses= new ArrayList<Ellipse>();


    private Color ellipseFillColor= Color.rgb(212, 4, 6, 0.50);
    private Color ellipseFillColorClicked= Color.rgb(11, 12, 255, 0.50);
    private Color ellipseStrokeColor= Color.rgb(212, 4, 6, 1.0);
    private Color ellipseStrokeColorClicked= Color.rgb(11, 12, 255, 1.0);

    @FXML
    private AnchorPane viewArea;
    @FXML
    private Button forwardButton;
    @FXML
    private Button backwardButton;
    @FXML
    private Button resetButton;

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
    }//initialize


    /*
     Action functions for GUI components:
     */

    public void loadFileAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Json File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("json", "*.json"));
        File file= fileChooser.showOpenDialog(viewArea.getScene().getWindow());
        if (file != null)
            filePath = file.getAbsolutePath();

        if (filePath != null){
            try{
                pointInTimeArray = JsonReaderWriter.jsonToObject(filePath);
                forwardButton.setDisable(false);
                //System.out.println(pointInTimeArray);
            }
            catch (JsonSyntaxException e){
                AlertWindow.display("Json Error", e.getMessage());
                filePath= null;
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
        newEllipse.setFill(ellipseFillColor);
        newEllipse.setStroke(ellipseStrokeColor);
        ellipseSetOnClick(newEllipse);
        viewArea.getChildren().addAll(newEllipse);
    }

    public void addEllipseOnClickAction(double x, double y) {
        Ellipse newEllipse= AddEllipseBox.display(x, y);
        newEllipse.setFill(ellipseFillColor);
        newEllipse.setStroke(ellipseStrokeColor);
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
    }

    public void resetAction() {
        clearScreen();
        if (pointInTimeArrayIndex != -1){
            pointInTimeArrayIndex= -1;
            forwardButton.setDisable(false);
            backwardButton.setDisable(true);
            resetButton.setDisable(true);
        }
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
                if (ellipse.getFill().equals(ellipseFillColor)){   //was not clicked/chosen
                    ellipse.setFill(ellipseFillColorClicked);
                    ellipse.setStroke(ellipseStrokeColorClicked);
                    clickedEllipses.add(ellipse);
                }
                else{                                              //was clicked/chosen
                    ellipse.setFill(ellipseFillColor);
                    ellipse.setStroke(ellipseStrokeColor);
                    clickedEllipses.remove(ellipse);
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
            Iterator<State> stateIterator = trackIterator.next().getStateList().iterator();
            while(stateIterator.hasNext())
                showState(stateIterator.next());
        }
    }

    public void showState(State state) {
        ArrayList<CovarianceEllipse> CovarianceEllipseArray = state.getEllipseList();
        Iterator<CovarianceEllipse> itr = CovarianceEllipseArray.iterator();
        while (itr.hasNext()) {
            Ellipse tempEllipse = EllipseRepresentationTranslation.fromCovarianceToVizual(itr.next());
            tempEllipse.setFill(ellipseFillColor);
            tempEllipse.setStroke(ellipseStrokeColor);
            ellipseSetOnClick(tempEllipse);
            viewArea.getChildren().addAll(tempEllipse);
        }
    }

}//MainContainerController
