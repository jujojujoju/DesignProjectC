package project;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import sample.*;

import java.io.IOException;
import java.util.*;

/**
 * Created by joju on 2017. 6. 21..
 */

///그래프를 띄우는 새로운 stage생성 클래스


public class MakeNewStage {
    public Graph graph;
    public List<CheckBox> checkBoxList;
    public Database db;
    public int numOfK = 5;
    public HashSet<Author> selectedAuthorSet;


    public MakeNewStage(Graph graph, List<CheckBox> checkBoxList, Database db) {

        this.graph = graph;
        this.checkBoxList = checkBoxList;
        this.db = db;
    }
    
    public void MakeNewStageForGraph(String string) {
        Layout layout;
        HashSet<Author> centerAuthorSet = new HashSet<>();

        graph = new Graph();
        Stage stage = new Stage();

        BorderPane root = null;

        root = getBorderPane(string, stage, root);

        assert root != null;
        stage.show();

        if (string.equals("Graph")) {
            centerAuthorSet = addGraphComponents();
            layout = new CenterLayout(graph, centerAuthorSet);
        } else {
            addGraphComponentsRelation();
            layout = new RandomLayout(graph);
        }

        layout.execute();

    }

    public void MakeNewStageForTopK(String string) {

        graph = new Graph();
        Stage stage = new Stage();

        BorderPane root = null;
        root = getBorderPane(string, stage, root);

        assert root != null;
        stage.show();

        addGraphComponentsTopK();
        Layout layout = new RandomLayout(graph);
        layout.execute();

    }

    public void MakeNewStageForTopKFromAuthor(String string ,TextField yearBox) {

        graph = new Graph();
        Stage stage = new Stage();

        BorderPane root = null;
        root = getBorderPane(string, stage, root);

        assert root != null;
        stage.show();

        addGraphComponentsTopKAroundAuthor(yearBox);

        Layout layout = new RandomLayout(graph);
        layout.execute();
    }

    public void MakeNewStageForRecommand(String string){
        graph = new Graph();
        Stage stage = new Stage();

        BorderPane root = null;
        root = getBorderPane(string, stage, root);

        assert root != null;
        stage.show();

        addGraphComponentsRecommend();

        Layout layout = new RandomLayout(graph);
        layout.execute();
    }
    public BorderPane getBorderPane(String string, Stage stage, BorderPane root) {
        try {
            root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            root.setCenter(graph.getScrollPane());

            Scene scene = getCurrentScene(root);
            stage.setTitle(string);
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    public Scene getCurrentScene(Pane root) {
        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        return scene;
    }

    public HashSet<Author> addGraphComponents() {

        selectedAuthorSet = new HashSet<Author>();
        for (int i = 0; i < checkBoxList.size(); i++)
            if (checkBoxList.get(i).isSelected())
                selectedAuthorSet.add(new Author(checkBoxList.get(i).getText()));


        SimpleWeightedGraph<Node, DefaultWeightedEdge> weightedGraph = db.getCoauthorWeightedGraph(selectedAuthorSet);

        Model model = graph.getModel();
        graph.beginUpdate();

        for (Node author : weightedGraph.vertexSet())
            model.addCell(author.getName(), CellType.LABEL);

        Author source;
        Author target;

        for (DefaultWeightedEdge edge : weightedGraph.edgeSet()) {
            if (weightedGraph.getEdgeWeight(edge) >= 1)    //웨이트가 1이상이면 표시가 되어야 합니다.
                model.addEdge((target = (Author) weightedGraph.getEdgeTarget(edge)).getName(),
                        (source = (Author) weightedGraph.getEdgeSource(edge)).getName(),
                        weightedGraph.getEdgeWeight(edge),
                        db.getCoauthorSet(target, source));
        }
        graph.endUpdate();

        return selectedAuthorSet;
    }

    public void addGraphComponentsRelation() {

        ArrayList<Author> authors = new ArrayList<Author>();
        for (int i = 0; i < checkBoxList.size(); i++)
            if (checkBoxList.get(i).isSelected())
                authors.add(new Author(checkBoxList.get(i).getText()));


        UndirectedGraph<Node, DefaultEdge> undirectedGraph = db.getRelationGraph(authors.get(0), authors.get(1));

        Model model = graph.getModel();
        graph.beginUpdate();

        for (Node author : undirectedGraph.vertexSet()) {
            if (author.getClass().getName().equals("project.Paper"))
                model.addCell(author.getName(), CellType.PAPERLABEL);
            else
                model.addCell(author.getName(), CellType.LABEL);
        }

        for (DefaultEdge edge : undirectedGraph.edgeSet()) {
            model.addEdge(undirectedGraph.getEdgeTarget(edge).getName(), undirectedGraph.getEdgeSource(edge).getName());
        }

        graph.endUpdate();
    }

    public void addGraphComponentsTopK() {
        Model model = graph.getModel();
        graph.beginUpdate();

        SortedMap sortedMap = (SortedMap) db.getAuthorMapByCont(numOfK);
        Iterator it = sortedMap.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Author, Integer> entry = (Map.Entry<Author, Integer>) it.next();
            model.addTopKCell(entry.getKey().getName(), entry.getValue());
        }

        graph.endUpdate();
    }

    public void addGraphComponentsTopKAroundAuthor(TextField yearBox) {
        Model model = graph.getModel();
        graph.beginUpdate();

        SortedMap sortedMap;

        Author author = new Author();
        for (int i = 0; i < checkBoxList.size(); i++)
            if (checkBoxList.get(i).isSelected()) {
                author = (new Author(checkBoxList.get(i).getText()));
                break;
            }

        if(!yearBox.getText().equals("")) {
            Params params = new Params();
            params.count = numOfK;
            params.name = author.getName();
            params.year = Integer.parseInt(yearBox.getText());

            sortedMap = (SortedMap) db.getAuthorMapByCont(params);
        }
        else {
            sortedMap = (SortedMap) db.getAuthorMapByCont(author, numOfK);
        }

        System.out.println(sortedMap.toString());

        Iterator it = sortedMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Author, Integer> entry = (Map.Entry<Author, Integer>) it.next();
            model.addTopKCell(entry.getKey().getName(), entry.getValue());
        }

        graph.endUpdate();

    }

    public void addGraphComponentsRecommend() {
        Model model = graph.getModel();
        graph.beginUpdate();

        Author author = new Author();
        for (int i = 0; i < checkBoxList.size(); i++)

            if (checkBoxList.get(i).isSelected()) {
                author = (new Author(checkBoxList.get(i).getText()));
                break;
            }

        Map<Node, Double> sortedMap =  db.getRecommandPaperMap(author);
        Iterator it = sortedMap.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Node, Double> entry = (Map.Entry<Node, Double>) it.next();
            model.addTopKCell(entry.getKey().getName(), entry.getValue());
        }

        graph.endUpdate();
    }

}
