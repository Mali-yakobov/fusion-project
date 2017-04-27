package il.ac.bgu.visualization;

/**
 * Created by Maayan on 29/11/2016.
 * Changed by Stas on 23/03/2017 (Converted to FXML)
 */

import il.ac.bgu.fusion.objects.CovarianceEllipse;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;

import static il.ac.bgu.visualization.util.EllipseRepresentationTranslation.fromVizualToCovariance;


public class GUI extends Application {
    public static Stage window;
    public static Scene scene1;

    public static void main(String[] args) {
        launch(args);
        Ellipse firstEllTmp= new Ellipse();
        firstEllTmp.setRadiusX(20);
        firstEllTmp.setRadiusY(80);
        firstEllTmp.setRotate(45);
        CovarianceEllipse firstEll= fromVizualToCovariance(firstEllTmp);
        firstEllTmp.setRotate(135);
        CovarianceEllipse firstEll2= fromVizualToCovariance(firstEllTmp);
        firstEllTmp.setRotate(90);
        CovarianceEllipse firstEll3= fromVizualToCovariance(firstEllTmp);
        System.out.println(firstEll);
        System.out.println(firstEll2);
        System.out.println(firstEll3);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        window.setTitle("Fusion Project");
        window.setHeight(700);
        window.setWidth(1000);
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
