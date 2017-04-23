package il.ac.bgu.visualization.objects;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;

public class AlertWindow {

    
    public static void display(String title, String message) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);  //Block events to other windows
        window.setTitle(title);
        window.setMinWidth(180);
        Label label = new Label();
        label.setText(message);
        
        Button okButton = new Button("Ok");
        okButton.setOnAction(e -> {
            window.close();
        });
               
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, okButton);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.resizableProperty().set(false);
        Toolkit.getDefaultToolkit().beep();
        window.showAndWait();
    }

}



