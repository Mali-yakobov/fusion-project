package il.ac.bgu.visualization;

/**
 * Created by Maayan on 29/11/2016.
 */

import il.ac.bgu.fusion.objects.CovarianceEllipse;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import il.ac.bgu.fusion.util.JsonReaderWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

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
        Button buttonLoad = new Button("Load File");
        Button buttonClear= new Button("Clear");
        BorderPane layout = new BorderPane();
        layout.setLeft(sideMenu);
        TextField txtField= new TextField();


        sideMenu.getChildren().addAll(buttonLoad,buttonShow,buttonAdd,buttonClear);
        buttonLoad.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Json File");
            File file= fileChooser.showOpenDialog(window);
            if (file!=null)
                txtField.setText(file.getAbsolutePath());
        });
        AddEllipseBox addBox = new AddEllipseBox();
        buttonAdd.setOnAction(e -> {
            Ellipse newEllipse=new Ellipse();
            newEllipse=addBox.display();
            layout.getChildren().addAll(newEllipse);


        });
        buttonClear.setOnAction(event -> {
            layout.getChildren().clear();
            layout.setLeft(sideMenu);
            sideMenu.getChildren().addAll(buttonLoad,buttonShow,buttonAdd,buttonClear);
        });

        buttonShow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ArrayList<CovarianceEllipse> CovarianceEllipseArray = JsonReaderWriter.elipseFromFile(txtField.getText());
                Iterator<CovarianceEllipse> itr = CovarianceEllipseArray.iterator();
                while (itr.hasNext()) {
                    Ellipse tempEllipse =EllipseRepresentationTranslation.fromCovarianceToVizual(itr.next());
                    tempEllipse.setFill(Color.BLACK);
                    tempEllipse.setStroke(Color.BLUE);

                    layout.getChildren().addAll(tempEllipse);
                }

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
