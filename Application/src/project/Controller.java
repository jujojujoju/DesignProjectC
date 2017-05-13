package project;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.*;
import project.Author;
import project.Database;
import project.Node;

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

import java.io.IOException;
import java.util.*;

public class Controller {

    private Database db;
    private Graph graph;

    private Connection connection;
    private Main main;

    private CheckBox [] mCheckBoxArray;

    private HashSet<Author> selectedAuthorSet;

    public void initManager(Main main) {

        db = new Database();
        db.readFile();
        this.main = main;
        mCheckBoxArray = main.checkBoxArray;

    }

    public void Authorityscreen(ActionEvent actionEvent) {
    }

    public void sendConnection(Connection connection) {

        this.connection = connection;

    }

    public void buttonclick1(ActionEvent actionEvent) {
        MakeNewStageForGraph("Graph");
    }
    public void buttonclick2(ActionEvent actionEvent) {
        MakeNewStageForGraph("Relation Graph");
    }
    public void buttonclick3(ActionEvent actionEvent) {
        MakeNewStageForChart("Top K");
    }
    public void buttonclick4(ActionEvent actionEvent) {
        MakeNewStageForChart("Top K For Author");
    }

    private void MakeNewStageForGraph(String string) {

        graph = new Graph();
        Stage stage = new Stage();

        BorderPane root = null;
        try {

            root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            root.setCenter(graph.getScrollPane());

            Scene scene = new Scene(root, 1024, 768);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            stage.setTitle(string);
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
        assert root != null;
        stage.show();

        if(string.equals("Graph"))
        addGraphComponents();
        else
            addGraphComponentsRelation();

        Layout layout = new RandomLayout(graph);
        layout.execute();

    }
    private void addGraphComponents() {

        selectedAuthorSet = new HashSet<Author>();
        for(int i=0;i<mCheckBoxArray.length;i++)
        {
            if(mCheckBoxArray[i].isSelected()) {
                selectedAuthorSet.add(new Author(mCheckBoxArray[i].getText()));
            }
        }

        SimpleWeightedGraph<Node, DefaultWeightedEdge> weightedGraph = db.getCoauthorWeightedGraph(selectedAuthorSet);

        Model model = graph.getModel();
        graph.beginUpdate();

        for(Node author:weightedGraph.vertexSet())
        {
            model.addCell(author.getName(), CellType.LABEL);
        }

        for(DefaultWeightedEdge edge :weightedGraph.edgeSet()) {
            if(weightedGraph.getEdgeWeight(edge)>=1)    //웨이트가 1이상이면 표시가 되어야 합니다.
                model.addEdge(weightedGraph.getEdgeTarget(edge).getName(), weightedGraph.getEdgeSource(edge).getName(), weightedGraph.getEdgeWeight(edge));
        }

        graph.endUpdate();
    }

    private void addGraphComponentsRelation()
    {

        ArrayList<Author> authors = new ArrayList<Author>();
//
        for(int i=0;i<mCheckBoxArray.length;i++)
        {
            if(mCheckBoxArray[i].isSelected()) {
                authors.add(new Author(mCheckBoxArray[i].getText()));
            }
        }

        UndirectedGraph<Node, DefaultEdge> undirectedGraph = db.getRelationGraph(authors.get(0), authors.get(1));

        Model model = graph.getModel();
        graph.beginUpdate();

        for(Node author:undirectedGraph.vertexSet())
        {
            if(author.getClass().getName().equals("project.Paper"))
                model.addCell(author.getName(), CellType.PAPERLABEL);
            else
                model.addCell(author.getName(), CellType.LABEL);
        }

        for(DefaultEdge edge :undirectedGraph.edgeSet()) {
            model.addEdge(undirectedGraph.getEdgeTarget(edge).getName(), undirectedGraph.getEdgeSource(edge).getName());
        }

        graph.endUpdate();
    }

    private void MakeNewStageForChart(String string) {

        graph = new Graph();
        Stage stage = new Stage();


        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc =
                new BarChart<String,Number>(xAxis,yAxis);
        bc.setTitle(string);
        xAxis.setLabel("Authors");
        yAxis.setLabel("Paper Number");
        XYChart.Series series1 = new XYChart.Series();

        if(string.equals("Top K"))
            addGraphComponentsTopK(series1);
        else
        addGraphComponentsTopKAroundAuthor(series1);


        bc.getData().addAll(series1);

        BorderPane root = null;
        try {

            root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            root.setCenter(bc);

            Scene scene = new Scene(root, 1024, 768);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            stage.setTitle(string);
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
        assert root != null;
        stage.show();


        Layout layout = new RandomLayout(graph);
        layout.execute();

    }

    private void addGraphComponentsTopK(XYChart.Series series1) {

        SortedMap sortedMap = (SortedMap) db.getAuthorMapByCont(5);

        Iterator it = sortedMap.entrySet().iterator();

        while(it.hasNext()){
            Map.Entry<Author, Integer> entry = (Map.Entry<Author, Integer>)it.next();
            series1.getData().add(new XYChart.Data(entry.getKey().getName(),entry.getValue()));
        }
    }

    private void addGraphComponentsTopKAroundAuthor(XYChart.Series series1) {

        Author author = new Author();
        for(int i=0;i<mCheckBoxArray.length;i++)
        {
            if(mCheckBoxArray[i].isSelected()) {
                author = new Author(mCheckBoxArray[i].getText());
                break;
            }
        }
        SortedMap sortedMap = (SortedMap) db.getAuthorMapByCont(author,5);
        Iterator it = sortedMap.entrySet().iterator();

        while(it.hasNext()){
            Map.Entry<Author, Integer> entry = (Map.Entry<Author, Integer>)it.next();
            series1.getData().add(new XYChart.Data(entry.getKey().getName(),entry.getValue()));
        }
    }
}
