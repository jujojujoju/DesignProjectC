package project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
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

import java.util.Set;

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

        Controller controller =
                loader.<Controller>getController();
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

            root.getChildren().add(checkBoxArray[i]);
            i++;
        }

//
//    	for(int i =0;i<5;i++)
//        {
//            Author author = (Author) dbAuthorSet.iterator();
//            System.out.println(author.getName());
//
////            author.getName();
////            node.getName();
////            System.out.println(db.getAuthorSet().toString());
//        }



//        SimpleWeightedGraph<Node, DefaultWeightedEdge> weightedGraph = db.getCoauthorWeightedGraph(new Author("AAA"));


//
//    	  System.out.println(db.getAuthorMapByCont(10).toString());
//
//        root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//
//        graph = new Graph();
//
//        BorderPane root;
//        root.setCenter(graph.getScrollPane());
//
//        Scene scene = new Scene(root, 1024, 768);
//        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//
//        primaryStage.setTitle("jujoju");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//
//        addGraphComponents();
//
//        Layout layout = new RandomLayout(graph);
//        layout.execute();


        primaryStage.setTitle("jujoju");
        primaryStage.setScene(scene);
        primaryStage.show();



    }

//    private void addGraphComponents() {
//
        //SimpleWeightedGraph<Node, DefaultWeightedEdge> weightedGraph = db.getCoauthorWeightedGraph(new Author("Massimo De Gregorio"));
//        SimpleWeightedGraph<Node, DefaultWeightedEdge> weightedGraph = db.getCoauthorWeightedGraph(new Author("AAA"));
//
//        Model model = graph.getModel();
//
//        graph.beginUpdate();
//
//
//        for(Node author:weightedGraph.vertexSet())
//        {
//            model.addCell(author.getName(), CellType.LABEL);
//        }
//
//        for(DefaultWeightedEdge edge :weightedGraph.edgeSet()) {
//            if(weightedGraph.getEdgeWeight(edge)>1)
//            model.addEdge(weightedGraph.getEdgeTarget(edge).getName(), weightedGraph.getEdgeSource(edge).getName(), weightedGraph.getEdgeWeight(edge));
//        }
//
//        graph.endUpdate();
//    }


    public static void main(String[] args) {
        launch(args);
    }
}
