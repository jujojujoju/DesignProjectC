package project;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.control.textfield.TextFields;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.event.GraphChangeEvent;
import org.jgrapht.event.GraphEdgeChangeEvent;
import org.jgrapht.graph.*;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import sample.CellType;
import sample.Graph;
import sample.Layout;
import sample.Model;
import sample.RandomLayout;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {

    public Button buttonAddSearched;
    public TextField searchBox;
    public TextArea searchTopKArea;
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
    private Text authorSearchText;

    private AnchorPane anchorPane;
    private ScrollPane scrollPane;
    private ScrollBar scrollBar;
    private List<CheckBox> checkBoxList;
    private Subscription subscription;
    private boolean isFirst;


    void initManager(Main main, Pane root, Stage stage) {

        numOfK = 5;
        isFirst = true;

        this.main = main;

        textfield = new TextField();
        textArea = new TextArea();
        authorSearchText = new Text();
        anchorPane = new AnchorPane();
        scrollPane = new ScrollPane();
        scrollBar = new ScrollBar();
        checkBoxList = new ArrayList<>();
        subscription = new Subscription();

        db = new Database("paperList4.txt");

        MainGraphListener graphListener = new MainGraphListener();
        graphListener.setMainScene(this);
        graphListener.setSubscription(subscription);
        db.getMainGraph().addGraphListener(graphListener);

        db.readFile();
        isFirst = false;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("DB Check");
                if(db.checkFile()) {
                    System.out.println("DB업로드시작");

                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            db.readFile();
                        }
                    });
                }
                else {
                    System.out.println("No Change");
                }
            }
        };

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(runnable, 0, 10, TimeUnit.SECONDS);


        resetAuthorList();

        this.substage = stage;
        this.root = root;


        setLayoutXY(buttonOK,420,230);
        setLayoutXY(searchBox,420,180);
        setLayoutXY(buttonAddSearched,600,180);
        setLayoutXY(searchTopKArea,400,400);
        setLayoutXY(textArea,420,50);


        textArea.setMaxHeight(100);


        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Stage is closing");
                service.shutdown();
            }
        });

        TextFields.bindAutoCompletion(searchBox, t-> {

            return db.getSearchAuthorList(t.getUserText());

        });
    }

    public void setLayoutXY(Region item, double x, double y)
    {
        item.setLayoutX(x);
        item.setLayoutY(y);
    }


    public void showAlert(GraphChangeEvent e) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if(e instanceof GraphEdgeChangeEvent && !isFirst) {
            alert.setTitle("Information Dialog");
            alert.setHeaderText(((GraphEdgeChangeEvent) e).getEdgeTarget().toString());
            alert.setContentText(((GraphEdgeChangeEvent) e).getEdgeSource().toString());
            System.out.println("aaa");
            alert.showAndWait();
        }
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
    public void buttonclick_recommand(ActionEvent actionEvent) {
        resetAuthorList();
        transformToRecommandList();
    }

    private void remakeAuthorListAfterSearch(String name) {

        anchorPane.getChildren().removeAll(checkBoxList);

        int i = 0;
        checkBoxList.clear();
        for (Node author : db.getAuthorSet()) {
            if (author.getName().contains(name)) {
                CheckBox checkBox = new CheckBox();
                checkBox.setText(author.getName());
                checkBox.setLayoutX(10);
                checkBox.setLayoutY(10 + i * 20);
                i++;
                checkBoxList.add(checkBox);
            }
        }

        textfield.setText("총 저자 : " + checkBoxList.size() + " 명");
        textfield.setLayoutX(14);
        textfield.setLayoutY(720);


        anchorPane.getChildren().addAll(checkBoxList);

    }

    public void resetAuthorList() {
        anchorPane.getChildren().removeAll(checkBoxList);

        int i = 0;
        checkBoxList.clear();

        for (Node author : db.getAuthorSet()) {
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
        for (int i = 0; i < checkBoxList.size(); i++)
            checkBoxList.get(i).setSelected(false);
    }

    private String transformToSubscribeList() {

        Pane root = (Pane) substage.getScene().getRoot();
        substage.getScene().setRoot(new Pane());
        substage.setScene(null);

        Scene scene = getCurrentScene(root);

        if (!root.getChildren().contains(scrollPane)) {
            root.getChildren().addAll(getScrollPane(scene));
        }

        if (!root.getChildren().contains(textfield))
            root.getChildren().add(textfield);


        searchBox.setVisible(true);
        buttonAddSearched.setVisible(true);
        searchTopKArea.setVisible(true);


        resetCheckBox();
        CheckSomeItem();

        textArea.setText("구독기능");

        if (!root.getChildren().contains(textArea))
            root.getChildren().add(textArea);

        substage.setScene(scene);
        substage.setTitle(buttonFlag = "Subscribe");
        substage.show();


        return buttonFlag;
    }

    private void CheckSomeItem() {

        for (int i = 0; i < checkBoxList.size(); i++) {
            for (Map.Entry<Author, List<Paper>> entry : subscription.getSubscriptMap().entrySet()) {
                Author key = entry.getKey();
                List<Paper> value = entry.getValue();
                System.out.println(key.toString() + ", " + value.toString());

                if(key.getName().equals(checkBoxList.get(i).getText()))
                    checkBoxList.get(i).setSelected(true);
            }
        }

    }


    private void transformToReFreshDB() {

        if (buttonFlag.equals("Graph")) {
            transformToMainGraph();
        } else if (buttonFlag.equals("Relation Graph")) {
            transformToRelationGraph();
        } else if (buttonFlag.equals("Top K")) {
            transformToTopKChart();
        } else if (buttonFlag.equals("Top K For Author")) {
            transformToTopKFromAuthorChart();
        }

    }


    private ScrollPane getScrollPane(Scene scene) {
        double height = scene.getHeight();

        anchorPane.setMaxHeight(height);

        scrollPane.setMinHeight(height - 100);
        scrollPane.setPrefHeight(height - 100);
        scrollPane.setMaxHeight(height);

        scrollBar.setLayoutX(scene.getWidth() - scrollBar.getWidth());
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

    private Scene getCurrentScene(Pane root) {
        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        return scene;
    }

    public String transformToMainGraph() {
        Pane root = (Pane) substage.getScene().getRoot();
        substage.getScene().setRoot(new Pane());
        substage.setScene(null);

        Scene scene = getCurrentScene(root);

        if (!root.getChildren().contains(scrollPane)) {
            root.getChildren().addAll(getScrollPane(scene));
        }

        if (!root.getChildren().contains(textfield))
            root.getChildren().add(textfield);


        resetCheckBox();

        searchBox.setVisible(true);
        buttonAddSearched.setVisible(true);
        searchTopKArea.setVisible(true);

        textArea.setText("여러명의 저자를 선택하면, 그들의 관계를 보여줍니다.");

        if (!root.getChildren().contains(textArea))
            root.getChildren().add(textArea);

        substage.setScene(scene);
        substage.setTitle(buttonFlag = "Graph");
        substage.show();


        return buttonFlag;
    }

    private String transformToRelationGraph() {
        Pane root = (Pane) substage.getScene().getRoot();
        substage.getScene().setRoot(new Pane());
        substage.setScene(null);

        root.getChildren().removeAll();

        Scene scene = getCurrentScene(root);


        textArea.setText("두명의 저자를 선택해 주세요");

        searchBox.setVisible(true);
        buttonAddSearched.setVisible(true);
        searchTopKArea.setVisible(true);

        if (!root.getChildren().contains(scrollPane)) {
            root.getChildren().addAll(getScrollPane(scene));
            System.out.println("Asdfsdfsadf");
        }

        if (!root.getChildren().contains(textfield))
            root.getChildren().add(textfield);


        resetCheckBox();

        if (!root.getChildren().contains(textArea))
            root.getChildren().add(textArea);

        substage.setScene(scene);
        substage.setTitle(buttonFlag = "Relation Graph");
        substage.show();

        return buttonFlag;
    }

    private String transformToTopKChart() {
        Pane root = (Pane) substage.getScene().getRoot();
        substage.getScene().setRoot(new Pane());
        substage.setScene(null);

        Scene scene = getCurrentScene(root);

        searchBox.setVisible(false);
        buttonAddSearched.setVisible(false);
        searchTopKArea.setVisible(false);

        if (root.getChildren().contains(scrollPane))
            root.getChildren().removeAll(scrollPane);

        textArea.setText("전체 저자 중 Top K를 보여줍니다.");

        if (!root.getChildren().contains(textArea))
            root.getChildren().add(textArea);

        substage.setScene(scene);
        substage.setTitle(buttonFlag = "Top K");
        substage.show();


        return buttonFlag;
    }

    private String transformToTopKFromAuthorChart() {

        Pane root = (Pane) substage.getScene().getRoot();
        substage.getScene().setRoot(new Pane());
        substage.setScene(null);

        Scene scene = getCurrentScene(root);

        authorSearchText.setVisible(true);
        searchBox.setVisible(true);
        buttonAddSearched.setVisible(true);
        searchTopKArea.setVisible(true);


        if (!root.getChildren().contains(scrollPane)) {
            root.getChildren().addAll(getScrollPane(scene));
        }

        if (!root.getChildren().contains(textfield))
            root.getChildren().add(textfield);

        resetCheckBox();

        textArea.setText("선택된 저자와 관계된 저자 중 Top K ");

        if (!root.getChildren().contains(textArea))
            root.getChildren().add(textArea);

        substage.setScene(scene);
        substage.setTitle(buttonFlag = "Top K For Author");
        substage.show();

        return buttonFlag;

    }


    private String transformToRecommandList() {
        Pane root = (Pane) substage.getScene().getRoot();
        substage.getScene().setRoot(new Pane());
        substage.setScene(null);

        Scene scene = getCurrentScene(root);


        searchBox.setVisible(true);
        buttonAddSearched.setVisible(true);
        searchTopKArea.setVisible(true);


        if (!root.getChildren().contains(scrollPane)) {
            root.getChildren().addAll(getScrollPane(scene));
        }

        if (!root.getChildren().contains(textfield))
            root.getChildren().add(textfield);

        resetCheckBox();

        textArea.setText("선택된 저자를 기반으로 못봤을 만한 논문을 추천해준다.");

        if (!root.getChildren().contains(textArea))
            root.getChildren().add(textArea);

        substage.setScene(scene);
        substage.setTitle(buttonFlag = "Recommand");
        substage.show();

        return buttonFlag;
    }


    private BorderPane getBorderPane(String string, Stage stage, BorderPane root) {
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


    private void MakeNewStageForGraph(String string) {
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

    private void MakeNewStageForTopK(String string) {

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

    private void MakeNewStageForTopKFromAuthor(String string) {

        graph = new Graph();
        Stage stage = new Stage();

        BorderPane root = null;
        root = getBorderPane(string, stage, root);

        assert root != null;
        stage.show();

        addGraphComponentsTopKAroundAuthor();

        Layout layout = new RandomLayout(graph);
        layout.execute();
    }

    private void MakeNewStageForRecommand(String string){
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




    private HashSet<Author> addGraphComponents() {

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

    private void addGraphComponentsRelation() {

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

    private void addGraphComponentsTopK() {
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

    private void addGraphComponentsTopKAroundAuthor() {
        Model model = graph.getModel();
        graph.beginUpdate();

        Author author = new Author();
        for (int i = 0; i < checkBoxList.size(); i++)

            if (checkBoxList.get(i).isSelected()) {
                author = (new Author(checkBoxList.get(i).getText()));
                break;
            }

        SortedMap sortedMap = (SortedMap) db.getAuthorMapByCont(author, numOfK);
        Iterator it = sortedMap.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Author, Integer> entry = (Map.Entry<Author, Integer>) it.next();
            model.addTopKCell(entry.getKey().getName(), entry.getValue());
        }

        graph.endUpdate();

    }

    private void addGraphComponentsRecommend() {
        Model model = graph.getModel();
        graph.beginUpdate();

        Author author = new Author();
        for (int i = 0; i < checkBoxList.size(); i++)

            if (checkBoxList.get(i).isSelected()) {
                author = (new Author(checkBoxList.get(i).getText()));
                break;
            }

        //fixme
        Map<Node, Double> sortedMap =  db.getRecommandPaperMap(author);
        Iterator it = sortedMap.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Node, Double> entry = (Map.Entry<Node, Double>) it.next();
            model.addTopKCell(entry.getKey().getName(), entry.getValue());
        }

        graph.endUpdate();
    }

    public void buttonclick_OK(ActionEvent actionEvent) {

        int count;
        if (buttonFlag.equals("Graph")) {
            MakeNewStageForGraph("Graph");
        } else if (buttonFlag.equals("Relation Graph")) {
            count = 0;
            for (int i = 0; i < checkBoxList.size(); i++) {
                if (checkBoxList.get(i).isSelected())
                    count++;
                if (count >= 3)
                    break;
            }
            if (count == 2)
                MakeNewStageForGraph("Relation Graph");
        } else if (buttonFlag.equals("Top K")) {
            MakeNewStageForTopK("Top K");

        } else if (buttonFlag.equals("Top K For Author")) {
            count = 0;
            for (int i = 0; i < checkBoxList.size(); i++) {
                if (checkBoxList.get(i).isSelected())
                    count++;
                if (count >= 2)
                    break;
            }
            if (count == 1)
                MakeNewStageForTopKFromAuthor("Top K For Author");
        } else if (buttonFlag.equals("Recommand")) {
            count = 0;
            for (int i = 0; i < checkBoxList.size(); i++) {
                if (checkBoxList.get(i).isSelected())
                    count++;
                if (count >= 2)
                    break;
            }
            if (count == 1)
                MakeNewStageForRecommand("Recommend");
        }

    }
    public void buttonclick_Search(ActionEvent actionEvent) throws IOException {


        remakeAuthorListAfterSearch(searchBox.getText());

        if(db.getSearchAuthorObjList(searchBox.getText()).size() == 1) {
            db.getSearchAuthorObjList(searchBox.getText()).get(0).addNumOfSearch();
        }

        String text = "";
        searchTopKArea.setText("");

        int i = 1;
        for(Map.Entry<Author, Integer> entry: db.getSearchAuthorTopkList(10).entrySet())
        {
            String record =i + "위 : " + entry.getKey().getName() + " ( " + entry.getValue() + "번 )";
            System.out.println(record);
            text = text + record + "\n";
            i++;
        }

        searchTopKArea.setText(text);
    }

    class CharaterSort implements Comparator<CheckBox> {
        @Override
        public int compare(CheckBox o1, CheckBox o2) {
            return o1.getText().compareTo(o2.getText());
        }
    }

}
