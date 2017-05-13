package project;

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
    CheckBox [] checkBoxArray;

    private ScrollPane scrollPane = new ScrollPane();
    private AnchorPane anchorPane = new AnchorPane();

    ScrollBar scrollBar = new ScrollBar();

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

        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());


        double height = scene.getHeight();

        anchorPane.setMaxHeight(height);
        scrollPane.setMinHeight(height-100);
        scrollPane.setPrefHeight(height-100);
        scrollPane.setMaxHeight(height);

        int i=0;
        for(Node author : db.getAuthorSet())
        {
            checkBoxArray[i] = new CheckBox();
            checkBoxArray[i].setText(author.getName());
            checkBoxArray[i].setLayoutX(10);
            checkBoxArray[i].setLayoutY(10 + i * 20);
            anchorPane.getChildren().add(i,checkBoxArray[i]);
            i++;
        }

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setContent(scrollBar);
        scrollPane.setContent(anchorPane);
        scrollPane.setLayoutX(14);
        scrollPane.setLayoutY(14);
        root.getChildren().addAll(scrollPane);
        scrollBar.setLayoutX(scene.getWidth()-scrollBar.getWidth());
        scrollBar.setMin(0);
        scrollBar.setOrientation(Orientation.VERTICAL);
        scrollBar.setPrefHeight(anchorPane.getHeight());


        primaryStage.setTitle("Main Layout");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
