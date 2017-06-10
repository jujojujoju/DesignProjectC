package project;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.*;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import sample.CellType;
import sample.Graph;
import sample.Layout;
import sample.Model;
import sample.RandomLayout;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {

    private int numOfK;
    private String buttonFlag = "";
    public Button buttonOK;
    private Database db;
    private Graph graph;

    private Connection connection;
    private Main main;
    private Stage substage;
    private Pane root;

    private HashSet<Author> selectedAuthorSet;

    private TextArea textArea;
    private TextField textfield;

    private AnchorPane anchorPane;
    private ScrollPane scrollPane;
    private ScrollBar scrollBar;
    private List<CheckBox> checkBoxList;

    void initManager(Main main, Pane root, Stage stage) {

        this.main = main;


        textfield = new TextField();
        textArea = new TextArea();
        anchorPane = new AnchorPane();
        scrollPane = new ScrollPane();
        scrollBar = new ScrollBar();
        checkBoxList = new ArrayList<>();

        db = new Database("paperList4.txt");
        db.readFile();
        Runnable runnable = new Runnable() {
            String digest;
            @Override
            public void run() {
                System.out.println("시작");

                if(db.checkFile()) {
                    System.out.println("다르다");
                    db.readFile();
                    //구독자 추가되었는지 큐나 스택으로 확인
                    if(!db.getSubscriptStack().isEmpty()) {
                        while(!db.getSubscriptStack().isEmpty()){
                            System.out.println(db.getSubscriptStack().pop().toString() + "이 추가되었습니다.");
                        }
                    }
                }
                else {
                    System.out.println("같다");
                }
            }
        };

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(runnable,0, 10, TimeUnit.SECONDS);


        resetAuthorList();

        this.substage = stage;
        this.root = root;

        buttonOK.setLayoutX(420);
        buttonOK.setLayoutY(170);

        textArea.setLayoutX(420);
        textArea.setLayoutY(50);
        textArea.setMaxHeight(100);

        numOfK = 5;

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Stage is closing");
                service.shutdown();
            }
        });
    }

    public void Authorityscreen(ActionEvent actionEvent) {
    }

    public void sendConnection(Connection connection) {
        this.connection = connection;
    }

    public void buttonclick1(ActionEvent actionEvent) {
        resetAuthorList();
        transformToMainGraph();
    }
    public void buttonclick2(ActionEvent actionEvent) {
        resetAuthorList();
        transformToRelationGraph();
    }
    public void buttonclick3(ActionEvent actionEvent) {
        resetAuthorList();
        transformToTopKChart();
    }
    public void buttonclick4(ActionEvent actionEvent) {
        resetAuthorList();
        transformToTopKFromAuthorChart();
    }
    public void buttonclick5(ActionEvent actionEvent) {
        resetAuthorList();
        transformToReFreshDB();
    }
    public void buttonclick_subs(ActionEvent actionEvent) {

        resetAuthorList();
        transformToSubscribeList();
    }

    private String transformToSubscribeList() {

        Pane root = (Pane)substage.getScene().getRoot();
        substage.getScene().setRoot(new Pane());
        substage.setScene(null);

        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        if(!root.getChildren().contains(scrollPane)) {
            root.getChildren().addAll(getScrollPane(scene));
        }

        if(!root.getChildren().contains(textfield))
            root.getChildren().add(textfield);


        resetCheckBox();


        textArea.setText("구독기능");

        if(!root.getChildren().contains(textArea))
            root.getChildren().add(textArea);

        substage.setScene(scene);
        substage.setTitle(buttonFlag = "Subscribe");
        substage.show();


        return buttonFlag;
    }


    private void transformToReFreshDB() {

        if (buttonFlag.equals("Graph"))
        {
            transformToMainGraph();
        }
        else if(buttonFlag.equals("Relation Graph"))
        {
            transformToRelationGraph();
        }
        else if(buttonFlag.equals("Top K"))
        {
            transformToTopKChart();
        }
        else if(buttonFlag.equals("Top K For Author"))
        {
            transformToTopKFromAuthorChart();
        }

    }

    public void resetAuthorList()
    {

        int i=0;
        checkBoxList.clear();
        for(Node author : db.getAuthorSet())
        {
            CheckBox checkBox = new CheckBox();
            checkBox.setText(author.getName());
            checkBox.setLayoutX(10);
            checkBox.setLayoutY(10 + i * 20);
            i++;
            checkBoxList.add(checkBox);
        }

        textfield.setText("총 저자 : " + checkBoxList.size() + " 명");
        textfield.setLayoutX(14);
        textfield.setLayoutY(720);


        anchorPane.getChildren().addAll(checkBoxList);
    }

    private void resetCheckBox() {

        for(int i=0;i<checkBoxList.size();i++)
            checkBoxList.get(i).setSelected(false);
    }


    private ScrollPane getScrollPane(Scene scene)
    {
        double height = scene.getHeight();

        anchorPane.setMaxHeight(height);

        scrollPane.setMinHeight(height-100);
        scrollPane.setPrefHeight(height-100);
        scrollPane.setMaxHeight(height);

        scrollBar.setLayoutX(scene.getWidth()-scrollBar.getWidth());
        scrollBar.setMin(0);
        scrollBar.setOrientation(Orientation.VERTICAL);
        scrollBar.setPrefHeight(anchorPane.getHeight());

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setContent(scrollBar);
        scrollPane.setContent(anchorPane);

        scrollPane.setLayoutX(14);
        scrollPane.setLayoutY(50);

        return scrollPane;

    }


    public String transformToMainGraph()
    {
        Pane root = (Pane)substage.getScene().getRoot();
        substage.getScene().setRoot(new Pane());
        substage.setScene(null);

        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        if(!root.getChildren().contains(scrollPane)) {
            root.getChildren().addAll(getScrollPane(scene));
        }

        if(!root.getChildren().contains(textfield))
            root.getChildren().add(textfield);


        resetCheckBox();


        textArea.setText("여러명의 저자를 선택하면, 그들의 관계를 보여줍니다.");

        if(!root.getChildren().contains(textArea))
            root.getChildren().add(textArea);

        substage.setScene(scene);
        substage.setTitle(buttonFlag = "Graph");
        substage.show();


        return buttonFlag;
    }

    private String transformToRelationGraph()
    {
        Pane root = (Pane)substage.getScene().getRoot();
        substage.getScene().setRoot(new Pane());
        substage.setScene(null);

        root.getChildren().removeAll();

        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());


        textArea.setText("두명의 저자를 선택해 주세요");

        if(!root.getChildren().contains(scrollPane))
            root.getChildren().addAll(getScrollPane(scene));

        if(!root.getChildren().contains(textfield))
            root.getChildren().add(textfield);


        resetCheckBox();

        if(!root.getChildren().contains(textArea))
            root.getChildren().add(textArea);

        substage.setScene(scene);
        substage.setTitle(buttonFlag = "Relation Graph");
        substage.show();

        return buttonFlag;
    }
    private String transformToTopKChart() {
        Pane root = (Pane)substage.getScene().getRoot();
        substage.getScene().setRoot(new Pane());
        substage.setScene(null);

        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        if(root.getChildren().contains(scrollPane))
            root.getChildren().removeAll(scrollPane);

        textArea.setText("전체 저자 중 Top K를 보여줍니다.");

        if(!root.getChildren().contains(textArea))
            root.getChildren().add(textArea);

        substage.setScene(scene);
        substage.setTitle(buttonFlag = "Top K");
        substage.show();


        return buttonFlag;
    }

    private String transformToTopKFromAuthorChart() {

        Pane root = (Pane)substage.getScene().getRoot();

        substage.getScene().setRoot(new Pane());

        substage.setScene(null);

        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        if(!root.getChildren().contains(scrollPane)) {
            root.getChildren().addAll(getScrollPane(scene));
        }

        if(!root.getChildren().contains(textfield))
            root.getChildren().add(textfield);


        resetCheckBox();

        textArea.setText("선택된 저자와 관계된 저자 중 Top K ");

        if(!root.getChildren().contains(textArea))
            root.getChildren().add(textArea);

        substage.setScene(scene);
        substage.setTitle(buttonFlag = "Top K For Author");
        substage.show();

        return buttonFlag;

    }

    private void MakeNewStageForGraph(String string) {
        Layout layout;
        HashSet<Author> centerAuthorSet = new HashSet<>();

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
        {
            centerAuthorSet = addGraphComponents();
            layout = new CenterLayout(graph, centerAuthorSet);
        }
        else {
            addGraphComponentsRelation();
            layout = new RandomLayout(graph);
        }

        layout.execute();

    }
    private void MakeNewStageForTopK(String string) {

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

        addGraphComponentsTopK();

        Layout layout = new RandomLayout(graph);
        layout.execute();

    }

    private void MakeNewStageForTopKFromAuthor(String string) {

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

        addGraphComponentsTopKAroundAuthor();

        Layout layout = new RandomLayout(graph);
        layout.execute();


    }


    private HashSet<Author> addGraphComponents() {

        selectedAuthorSet = new HashSet<Author>();
        for(int i=0;i<checkBoxList.size();i++)
            if(checkBoxList.get(i).isSelected())
                selectedAuthorSet.add(new Author(checkBoxList.get(i).getText()));


        SimpleWeightedGraph<Node, DefaultWeightedEdge> weightedGraph = db.getCoauthorWeightedGraph(selectedAuthorSet);

        Model model = graph.getModel();
        graph.beginUpdate();

        for(Node author:weightedGraph.vertexSet())
            model.addCell(author.getName(), CellType.LABEL);

        Author source;
        Author target;

        for(DefaultWeightedEdge edge :weightedGraph.edgeSet()) {
            if(weightedGraph.getEdgeWeight(edge)>=1)    //웨이트가 1이상이면 표시가 되어야 합니다.
                model.addEdge((target = (Author) weightedGraph.getEdgeTarget(edge)).getName(),
                        (source = (Author) weightedGraph.getEdgeSource(edge)).getName(),
                        weightedGraph.getEdgeWeight(edge),
                        db.getCoauthorSet(target,source));
        }
        graph.endUpdate();

        return selectedAuthorSet;
    }

    private void addGraphComponentsRelation()
    {

        ArrayList<Author> authors = new ArrayList<Author>();
        for(int i=0;i<checkBoxList.size();i++)
            if(checkBoxList.get(i).isSelected())
                authors.add(new Author(checkBoxList.get(i).getText()));


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
    private void addGraphComponentsTopK() {
        Model model = graph.getModel();
            graph.beginUpdate();

        SortedMap sortedMap = (SortedMap) db.getAuthorMapByCont(numOfK);
        Iterator it = sortedMap.entrySet().iterator();

            while(it.hasNext()){
            Map.Entry<Author, Integer> entry = (Map.Entry<Author, Integer>)it.next();
            model.addTopKCell(entry.getKey().getName(),entry.getValue());
    }

        graph.endUpdate();
    }
    private void addGraphComponentsTopKAroundAuthor() {
        Model model = graph.getModel();
        graph.beginUpdate();

        Author author = new Author();
        for(int i=0;i<checkBoxList.size();i++)

            if(checkBoxList.get(i).isSelected()) {
                author = (new Author(checkBoxList.get(i).getText()));
                break;
            }

        SortedMap sortedMap = (SortedMap) db.getAuthorMapByCont(author,numOfK);
        Iterator it = sortedMap.entrySet().iterator();

        while(it.hasNext()){
            Map.Entry<Author, Integer> entry = (Map.Entry<Author, Integer>)it.next();
            model.addTopKCell(entry.getKey().getName(),entry.getValue());
        }

        graph.endUpdate();

    }

    public void buttonclick_OK(ActionEvent actionEvent) {

        int count;
        if (buttonFlag.equals("Graph"))
        {
            MakeNewStageForGraph("Graph");
        }
        else if(buttonFlag.equals("Relation Graph"))
        {
            count = 0;
            for(int i=0;i<checkBoxList.size();i++)
            {
                if(checkBoxList.get(i).isSelected())
                    count++;
                if(count>=3)
                    break;
            }
            if(count == 2)
                MakeNewStageForGraph("Relation Graph");
        }
        else if(buttonFlag.equals("Top K"))
        {
            MakeNewStageForTopK("Top K");

        }
        else if(buttonFlag.equals("Top K For Author"))
        {
            count = 0;
            for(int i=0;i<checkBoxList.size();i++)
            {
                if(checkBoxList.get(i).isSelected())
                    count++;
                if(count>=2)
                    break;
            }
            if(count == 1)
                MakeNewStageForTopKFromAuthor("Top K For Author");
        }

    }

    class CharaterSort implements Comparator<CheckBox>
    {
        @Override
        public int compare(CheckBox o1, CheckBox o2) {
            return o1.getText().compareTo(o2.getText());
        }
    }

}
