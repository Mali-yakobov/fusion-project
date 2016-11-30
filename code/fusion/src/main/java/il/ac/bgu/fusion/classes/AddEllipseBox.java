package il.ac.bgu.fusion.classes;

/**
 * Created by Maayan on 30/11/2016.
 */

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddEllipseBox {

    public static void display() {
        Stage window = new Stage();
        window.setTitle("Add Ellipse");
        window.setMinWidth(300);

        //Label label = new Label();
        //label.setText("Insert the Ellipse fields");

//Creating a GridPane container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);
//Defining the Name text field
        final TextField x = new TextField();
        x.setPromptText("Enter centerX");
        x.setPrefColumnCount(10);
        x.getText();
        GridPane.setConstraints(x, 0, 0);
        grid.getChildren().add(x);
//Defining the Last Name text field
        final TextField y = new TextField();
        y.setPromptText("Enter centerY");
        GridPane.setConstraints(y, 0, 1);
        grid.getChildren().add(y);
        //Defining the Last Name text field
        final TextField lastName = new TextField();
        lastName.setPromptText("Enter ");
        GridPane.setConstraints(lastName, 0, 2);
        grid.getChildren().add(lastName);
//Defining the Comment text field
        final TextField comment = new TextField();
        comment.setPrefColumnCount(15);
        comment.setPromptText("Enter ");
        GridPane.setConstraints(comment, 0, 3);
        grid.getChildren().add(comment);
//Defining the Submit button
        Button submit = new Button("Add new Ellipse");
        GridPane.setConstraints(submit, 1, 0);
        submit.setOnAction(e ->
                // add the ellipse to the scene
                window.close());

        grid.getChildren().add(submit);
//Defining the Clear button
        Button clear = new Button("Clear");
        GridPane.setConstraints(clear, 1, 1);
        grid.getChildren().add(clear);
        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(grid);
        window.setScene(scene);
        window.showAndWait();
    }

}