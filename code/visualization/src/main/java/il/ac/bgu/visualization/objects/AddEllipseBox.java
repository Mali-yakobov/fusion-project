package il.ac.bgu.visualization.objects;

/**
 * Created by Maayan on 30/11/2016.
 */

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Ellipse;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddEllipseBox {
    public static Ellipse display(double...axis) {
        final double[] x = {0};
        final double[] y = { 0 };
        final double[] rX = { 0 };
        final double[] rY = { 0 };

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);  //Block events to other windows
        window.setTitle("Add Ellipse");
        window.setMinWidth(300);


//Creating a GridPane container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);
//centerX
        final TextField centerX = new TextField();
        centerX.setPromptText("Enter centerX");
        if (axis.length > 1)
            centerX.setText(Double.toString(axis[0]));
        centerX.setPrefColumnCount(10);
        centerX.getText();
        GridPane.setConstraints(centerX, 0, 0);
        grid.getChildren().add(centerX);
//centerY
        final TextField centerY = new TextField();
        centerY.setPromptText("Enter centerY");
        if (axis.length > 1)
            centerY.setText(Double.toString(axis[1]));
        centerY.getText();
        GridPane.setConstraints(centerY, 0, 1);
        grid.getChildren().add(centerY);
 //radiusX
        final TextField radiusX = new TextField();
        radiusX.setPromptText("Enter RadiusX");
        radiusX.getText();
        GridPane.setConstraints(radiusX, 0, 2);
        grid.getChildren().add(radiusX);
//radiusY
        final TextField radiusY = new TextField();
        radiusY.setPrefColumnCount(15);
        radiusY.setPromptText("Enter RadiusY");
        radiusY.getText();
        GridPane.setConstraints(radiusY, 0, 3);
        grid.getChildren().add(radiusY);

        //angle
        final TextField angle = new TextField();
        angle.setPrefColumnCount(15);
        angle.setPromptText("Angle");
        angle.getText();
        GridPane.setConstraints(angle, 0, 4);
        grid.getChildren().add(angle);

//Defining the Submit button
        Button submit = new Button("Add new Ellipse");
        GridPane.setConstraints(submit, 1, 0);

        Ellipse ellipse= new Ellipse();
        submit.setOnAction(e ->{
            x[0] =Double.valueOf(centerX.getText());
            y[0] =Double.parseDouble(centerY.getText());
            rX[0] =Double.parseDouble(radiusX.getText());
            rY[0] =Double.parseDouble(radiusY.getText());
            ellipse.setCenterX(x[0] );
            ellipse.setCenterY(y[0] );
            ellipse.setRadiusX(rX[0] );
            ellipse.setRadiusY(rY[0] );
            ellipse.setRotate(Integer.parseInt(angle.getText()));
            window.close();
        });
        grid.getChildren().add(submit);

        //Defining the Clear button
        Button clear = new Button("Clear");
        GridPane.setConstraints(clear, 1, 1);
        grid.getChildren().add(clear);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(grid);
        window.setScene(scene);
        window.resizableProperty().set(false);
        window.showAndWait();
        return ellipse;
    }

}