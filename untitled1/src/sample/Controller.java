package sample;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class Controller {


    public void Authorityscreen(ActionEvent actionEvent) {
//        Main main = new Main();
//        main.subscreen();

        BorderPane root = new BorderPane();

        Scene scene = new Scene(root, 1024, 768);



        Label label = new Label("Sdf");

        root.setCenter(label);



    }


}
