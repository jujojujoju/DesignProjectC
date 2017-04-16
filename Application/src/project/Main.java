package project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;



public class Main extends Application {

//    private Graph graph;
    Database db = new Database();
    CheckBox [] checkBoxArray;

    @Override
    public void start(Stage primaryStage) throws Exception {

        db.readFile();
        checkBoxArray = new CheckBox[db.getAuthorSet().size()];

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("main.fxml")
        );
        Pane root = loader.load();
        Controller controller = loader.<Controller>getController();
        controller.initManager(this);

//        Pane root = FXMLLoader.load(getClass().getResource("main.fxml"));

        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());


        int i=0;
        for(Node author : db.getAuthorSet())
        {
            checkBoxArray[i] = new CheckBox();
            checkBoxArray[i].setText(author.getName());
            checkBoxArray[i].setLayoutX(10);
            checkBoxArray[i].setLayoutY(10 + i * 20);

//            root.setContent(checkBoxArray[i]);
            root.getChildren().add(checkBoxArray[i]);
            i++;
        }

        primaryStage.setTitle("jujoju");
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
