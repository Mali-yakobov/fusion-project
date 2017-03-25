package il.ac.bgu.visualization;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

import com.google.gson.JsonSyntaxException;
import il.ac.bgu.fusion.objects.CovarianceEllipse;
import il.ac.bgu.fusion.util.JsonReaderWriter;
import il.ac.bgu.visualization.objects.AddEllipseBox;
import il.ac.bgu.visualization.objects.AlertWindow;
import il.ac.bgu.visualization.util.EllipseRepresentationTranslation;
import javafx.fxml.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.stage.FileChooser;


/**
 * FXML Controller class for main_container.fxml
 */
public class MainContainerController implements Initializable {

    private String filePath= null;
    private Color ellipseFillColor= Color.rgb(212, 39, 36, 0.25);
    private Color ellipseStrokeColor= Color.RED;

    @FXML
    private AnchorPane viewArea;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewArea.getStyleClass().add("viewarea-class");
    }


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
    }

    public void showAction() {
        if (filePath != null){
            try{
                ArrayList<CovarianceEllipse> CovarianceEllipseArray = JsonReaderWriter.elipseFromFile(filePath);
                Iterator<CovarianceEllipse> itr = CovarianceEllipseArray.iterator();
                while (itr.hasNext()) {
                    Ellipse tempEllipse = EllipseRepresentationTranslation.fromCovarianceToVizual(itr.next());
                    tempEllipse.setFill(ellipseFillColor);
                    tempEllipse.setStroke(ellipseStrokeColor);
                    viewArea.getChildren().addAll(tempEllipse);
                }
            }
            catch (JsonSyntaxException e){
                AlertWindow.display("Json Error", e.getMessage());
                filePath= null;
            }

        }
    }

    public void addEllipseAction() {
        Ellipse newEllipse= AddEllipseBox.display();
        newEllipse.setFill(ellipseFillColor);
        newEllipse.setStroke(ellipseStrokeColor);
        viewArea.getChildren().addAll(newEllipse);
    }

    public void clearAction() {
        viewArea.getChildren().clear(); //viewArea.getChildren().remove(newEllipse);
    }

    public void closeAction() {
        GUI.window.close();
    }


}//MainContainerController
