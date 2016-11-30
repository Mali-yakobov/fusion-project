package il.ac.bgu.fusion;
/**
 * Created by Maayan on 29/11/2016.
 */

import il.ac.bgu.fusion.classes.AddEllipseBox;
import il.ac.bgu.fusion.classes.Elipse;
import il.ac.bgu.fusion.util.EllipseRepresentationTranslation;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;
import il.ac.bgu.fusion.util.JsonReaderWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.Gson;

import javax.security.auth.callback.ConfirmationCallback;

public class GUI extends Application {

    Stage window;

    public static void main(String[] args) { launch(args);}

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


        Ellipse e2 = new Ellipse(500,300 , 150,75 );
        e2.setRotate(45);
        e2.setFill(Color.TRANSPARENT);
        e2.setStroke(Color.BLUEVIOLET);


        VBox sideMenu = new VBox();
        Button buttonAdd = new Button("Add Ellipse");
        Button buttonShow = new Button("Show Ellipse");
        sideMenu.getChildren().addAll(buttonAdd,buttonShow);

        AddEllipseBox addBox = new AddEllipseBox();
        buttonAdd.setOnAction(e -> addBox.display());


        BorderPane layout = new BorderPane();


        layout.setLeft(sideMenu);
        buttonShow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Elipse e1 = JsonReaderWriter.elipseFromFile("D:\\sigmabit\\fusion-project\\testEllipse.json");
                Ellipse e3 =EllipseRepresentationTranslation.fromCovarianceToVizual(e1);
                layout.getChildren().addAll(e3);
            }}

        );


        Scene scene1 = new Scene(layout,400,300 );

        scene1.getStylesheets().add("style.css");
        window.setScene(scene1);
        window.show();



    }
    private void closeProgram(){
        window.close();
    }



}
