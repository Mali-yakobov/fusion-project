package il.ac.bgu.fusion.classes;

/**
 * Created by Maayan on 30/11/2016.
 */

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddEllipseBox {

    public static void display() {
        Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add Ellipse");
        window.setMinWidth(300);

        Label label = new Label();
        label.setText("Insert the Ellipse fields");
        Button addButton = new Button("Add my Ellipse");
        addButton.setOnAction(e ->
                    // add the ellipse to the scene
                    window.close());


        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, addButton);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

}