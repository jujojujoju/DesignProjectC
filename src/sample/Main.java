package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;


import javafx.scene.layout.BorderPane;


public class Main extends Application {

    int nodeNum = 60;
    Graph graph;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        graph = new Graph();
        root.setCenter(graph.getScrollPane());

        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        primaryStage.setTitle("fuckyou");
        primaryStage.setScene(scene);
        primaryStage.show();

        addGraphComponents();

        Layout layout = new RandomLayout(graph);
        layout.execute();

    }
    private void addGraphComponents() {

        Model model = graph.getModel();

        graph.beginUpdate();

        model.addCell("Cell ROOT", CellType.LABEL);

        for(int i = 0; i<nodeNum; i++)
        {
            model.addCell("Cell "+i, CellType.LABEL);
        }


        int random_node1;
        int random_node2;


        for(int i = 0; i<nodeNum/2; i++)
        {
            random_node1 = (int)(Math.random()* nodeNum);
            random_node2 = (int)(Math.random()* nodeNum);
            model.addEdge("Cell "+random_node1, "Cell "+random_node2);
        }

//
//        model.addCell("Cell A", CellType.LABEL);
//        model.addCell("Cell B", CellType.LABEL);
//        model.addCell("Cell C", CellType.LABEL);
//        model.addCell("Cell D", CellType.TRIANGLE);
//        model.addCell("Cell E", CellType.TRIANGLE);
//        model.addCell("Cell F", CellType.LABEL);
//        model.addCell("Cell G", CellType.LABEL);
//
//
//        model.addEdge("Cell A", "Cell B");
//        model.addEdge("Cell A", "Cell C");
//        model.addEdge("Cell B", "Cell C");
//        model.addEdge("Cell C", "Cell D");
//        model.addEdge("Cell B", "Cell E");
//        model.addEdge("Cell D", "Cell F");
//        model.addEdge("Cell D", "Cell G");
//        model.addEdge("Cell ROOT", "Cell G");
//        model.addEdge("Cell D", "Cell ROOT");
//


        graph.endUpdate();

    }
    public static void main(String[] args) {
        launch(args);
    }
}
