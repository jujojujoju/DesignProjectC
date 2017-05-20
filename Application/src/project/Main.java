package project;

import com.sun.media.jfxmedia.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    private Database db = new Database();



    private FXMLLoader loader;

    @Override
    public void start(Stage primaryStage) throws Exception {

        loader = new FXMLLoader(
                getClass().getResource("main.fxml")
        );
        Pane root = loader.load();

        Scene scene = new Scene(root, 1024, 768);
        primaryStage.setScene(scene);

        Controller controller = loader.<Controller>getController();
        controller.initManager(this,root,primaryStage);

        controller.transformToMainGraph();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
