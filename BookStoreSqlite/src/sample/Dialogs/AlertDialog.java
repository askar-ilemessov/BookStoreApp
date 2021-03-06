package sample.Dialogs;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AlertDialog {

    public static void display(String title, String message){

        //Empty window
        Stage window = new Stage();
        window.setTitle(title);
        window.setMinWidth(300);
        window.setMinHeight(180);

        Label label = new Label();
        label.setText(message);
        Button buttonOK = new Button("OK");
        buttonOK.setOnAction(e -> window.close());

        VBox layout = new VBox(5);
        layout.getChildren().addAll(label,buttonOK);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();


    }
}
