package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;


import javafx.scene.layout.BorderPane;

import java.io.IOException;


public class Main extends Application {

    private int nodeNum = 100;
    private Graph graph;

    BorderPane root;
    @Override
    public void start(Stage primaryStage) throws Exception {

//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        BorderPane root = new BorderPane();

        graph = new Graph();
        root.setCenter(graph.getScrollPane());

        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        primaryStage.setTitle("jujoju");
        primaryStage.setScene(scene);
        primaryStage.show();

        addGraphComponents();

        Layout layout = new RandomLayout(graph);
        layout.execute();

//        subscreen();

    }
    private void addGraphComponents() {

        Model model = graph.getModel();

        graph.beginUpdate();

        model.addCell("Cell ROOT", CellType.LABEL);

        for(int i = 0; i<nodeNum; i++)
        {
            model.addCell("Cell "+i, CellType.LABEL);
        }
        model.addCell("sssssss",CellType.TRIANGLE);
        model.addCell("sssssss",CellType.RECTANGLE);

        int random_node1;
        int random_node2;

        for(int i = 0; i<nodeNum/2; i++)
        {
            random_node1 = (int)(Math.random()* nodeNum);
            random_node2 = (int)(Math.random()* nodeNum);
            model.addEdge("Cell "+random_node1, "Cell "+random_node2);
        }

        graph.endUpdate();
    }
    public void subscreen()
    {
//        BorderPane /**/sub = new BorderPane();


        Label label = new Label("Sdf");

        root.setCenter(label);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
