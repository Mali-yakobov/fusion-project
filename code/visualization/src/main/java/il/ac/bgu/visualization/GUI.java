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
        firstEllTmp.setRotate(50);
        CovarianceEllipse firstEll1= fromVizualToCovariance(firstEllTmp);
        System.out.println(firstEll1);

        Ellipse firstEllTmp2= new Ellipse();
        firstEllTmp2.setRadiusX(20);
        firstEllTmp2.setRadiusY(80);
        firstEllTmp2.setRotate(130);
        CovarianceEllipse firstEll2= fromVizualToCovariance(firstEllTmp2);
        System.out.println(firstEll2);

        Ellipse firstEllTmp3= new Ellipse();
        firstEllTmp3.setRadiusX(20);
        firstEllTmp3.setRadiusY(80);
        firstEllTmp3.setRotate(95);
        CovarianceEllipse firstEll3= fromVizualToCovariance(firstEllTmp3);
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
