package il.ac.bgu.visualization.objects;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by user on 6/9/17.
 */
public class AddEllipseBox2 {

  public static VizualEllipse display(double...axis) {
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
    //radiusX
    final TextField radiusX = new TextField();
    radiusX.setPromptText("Enter RadiusX");
    radiusX.getText();
    GridPane.setConstraints(radiusX, 0, 0);
    grid.getChildren().add(radiusX);
//radiusY
    final TextField radiusY = new TextField();
    radiusY.setPrefColumnCount(15);
    radiusY.setPromptText("Enter RadiusY");
    radiusY.getText();
    GridPane.setConstraints(radiusY, 0, 1);
    grid.getChildren().add(radiusY);

    //angle
    final TextField angle = new TextField();
    angle.setPrefColumnCount(15);
    angle.setPromptText("Angle");
    angle.getText();
    GridPane.setConstraints(angle, 0, 2);
    grid.getChildren().add(angle);


//Defining the Submit button
    Button submit = new Button("Add new Ellipse");
    GridPane.setConstraints(submit, 1, 0);

    VizualEllipse ellipse= new VizualEllipse();
    submit.setOnAction(e ->{
      rX[0] =Double.parseDouble(radiusX.getText());
      rY[0] =Double.parseDouble(radiusY.getText());
      ellipse.setRadiusX(rX[0] );
      ellipse.setRadiusY(rY[0] );
      ellipse.setAngle(Integer.parseInt(angle.getText()));
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
