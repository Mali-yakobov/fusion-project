package il.ac.bgu.visualization;

/**
 * Created by Maayan on 29/11/2016.
 * Changed by Stas on 23/03/2017 (Converted to FXML)
 */

import il.ac.bgu.fusion.objects.CovarianceEllipse;
import il.ac.bgu.fusion.objects.State;
import il.ac.bgu.visualization.util.EllipseRepresentationTranslation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static il.ac.bgu.fusion.algorithms.InitialClustering.initialClustering;
import static il.ac.bgu.visualization.util.EllipseRepresentationTranslation.fromVizualToCovariance;


public class GUI extends Application {
    public static Stage window;
    public static Scene scene1;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        window.setTitle("Fusion Project");
        window.setMaximized(true);
        window.setOnCloseRequest(event -> {
            event.consume();
            closeProgram();
        });

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main_container.fxml"));
        scene1 = new Scene(root);
        scene1.getStylesheets().add("main_style.css");
        window.setScene(scene1);
        window.show();
    }




    private void closeProgram(){
        window.close();
    }
}
