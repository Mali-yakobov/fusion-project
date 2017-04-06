package il.ac.bgu.visualization;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

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
        Color cl= colorGenerator();
        newEllipse.setFill(cl);
        newEllipse.setStroke(new Color(cl.getRed(), cl.getGreen(), cl.getBlue(), 1));
        ellipseSetOnClick(newEllipse);
        viewArea.getChildren().addAll(newEllipse);
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
            Track currTrack= trackIterator.next();
            Iterator<State> stateIterator = currTrack.getStateList().iterator();
            if (currTrack.getColor()==null)
                currTrack.setColor(colorGenerator());
            while(stateIterator.hasNext())
                showState(stateIterator.next(), currTrack.getColor());
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
    }

    public Color colorGenerator() {
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        return  new Color(r, g, b, 0.50);
    }


}//MainContainerController
