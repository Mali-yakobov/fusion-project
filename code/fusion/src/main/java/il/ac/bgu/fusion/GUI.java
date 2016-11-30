package il.ac.bgu.fusion;/**
 * Created by Maayan on 29/11/2016.
 */

import il.ac.bgu.fusion.classes.AddEllipseBox;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;

//import com.google.gson.Gson;

public class GUI extends Application {

    Stage window;

    public static void main(String[] args) { launch(args);}

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        window.setTitle("Fusion Project");
        window.setHeight(700);
        window.setWidth(1000);

        Ellipse e2 = new Ellipse(500,300 , 150,75 );
        e2.setRotate(45);
        e2.setFill(Color.BLUEVIOLET);


        VBox sideMenu = new VBox();
        Button buttonAdd = new Button("Add Ellipse");
        Button buttonShow = new Button("Show Ellipse");
        sideMenu.getChildren().addAll(buttonAdd,buttonShow);

        AddEllipseBox addBox = new AddEllipseBox();
        buttonAdd.setOnAction(e -> addBox.display());


        BorderPane layout = new BorderPane();
        layout.setLeft(sideMenu);
        buttonShow.setOnAction(e -> {
                    layout.getChildren().addAll(e2);
                }
        );


        Scene scene1 = new Scene(layout,400,300 );
        scene1.getStylesheets().add("style.css");
        window.setScene(scene1);
        window.show();



    }



}
