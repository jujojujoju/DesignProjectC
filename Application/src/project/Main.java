package project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;


import javafx.scene.layout.BorderPane;

import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import project.*;
import sample.CellType;
import sample.Graph;
import sample.Layout;
import sample.Model;
import sample.RandomLayout;

public class Main extends Application {

    private int nodeNum = 100;
    private Graph graph;



    Database db = new Database();

    BorderPane root;
    @Override
    public void start(Stage primaryStage) throws Exception {

//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

    	db.readFile();

        //System.out.println(db.getCoauthorGraph(new Author("AAA"), new Author("BBB")).toString());
//        SimpleWeightedGraph<Node, DefaultWeightedEdge> weightedGraph = db.getCoauthorWeightedGraph();
//        DefaultWeightedEdge e1 = weightedGraph.getEdge(new Author("AAA"), new Author("BBB"));
//        System.out.println(weightedGraph.getEdgeWeight(e1));
    	//System.out.println(db.getAuthorMapByCont(3).toString());

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

        SimpleWeightedGraph<Node, DefaultWeightedEdge> weightedGraph = db.getCoauthorWeightedGraph();

        Model model = graph.getModel();

        graph.beginUpdate();

        for(Node author:weightedGraph.vertexSet())
        {
            model.addCell(author.getName(), CellType.LABEL);
        }

        for(DefaultWeightedEdge edge :weightedGraph.edgeSet()) {
            if(weightedGraph.getEdgeWeight(edge)>1)
            model.addEdge(weightedGraph.getEdgeTarget(edge).getName(), weightedGraph.getEdgeSource(edge).getName(), weightedGraph.getEdgeWeight(edge));
        }



//
//        int random_node1;
//        int random_node2;
//
//        for(int i = 0; i<nodeNum/2; i++)
//        {
//            random_node1 = (int)(Math.random()* nodeNum);
//            random_node2 = (int)(Math.random()* nodeNum);
//            model.addEdge("Cell "+random_node1, "Cell "+random_node2);
//        }




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
