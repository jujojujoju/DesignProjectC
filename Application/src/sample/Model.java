package sample;

/**
 * Created by joju on 2017. 4. 14..
 */
import project.Node;

import java.util.*;

public class Model {

    private Cell graphParent;

    private List<Cell> allCells;
    private List<Cell> addedCells;
    private List<Cell> removedCells;

    private List<Edge> allEdges;
    private List<Edge> addedEdges;
    private List<Edge> removedEdges;

    private Map<String,Cell> cellMap; // <id,cell>

    private List<List<Cell>> paperListForAuthor;

    public Model() {

        graphParent = new Cell( "_ROOT_");

        // clear model, create lists
        clear();
    }

    public void clear() {
        paperListForAuthor = new ArrayList<List<Cell>>();

        allCells = new ArrayList<>();
        addedCells = new ArrayList<>();
        removedCells = new ArrayList<>();

        allEdges = new ArrayList<>();
        addedEdges = new ArrayList<>();
        removedEdges = new ArrayList<>();

        cellMap = new HashMap<>(); // <id,cell>

    }

    public void clearAddedLists() {
        addedCells.clear();
        addedEdges.clear();
    }

    public List<Cell> getAddedCells() {
        return addedCells;
    }

    public List<Cell> getRemovedCells() {
        return removedCells;
    }

    public List<Cell> getAllCells() {
        return allCells;
    }

    public List<Edge> getAddedEdges() {
        return addedEdges;
    }

    public List<Edge> getRemovedEdges() {
        return removedEdges;
    }

    public List<Edge> getAllEdges() {
        return allEdges;
    }

    public List<List<Cell>> getPaperListForAuthor(){
        return paperListForAuthor;
    }

    public void addTopKCell(String id, double NumofPaper) {
        CircleCell circleCell = new CircleCell(id,NumofPaper);
        addCell(circleCell);
    }
    public void addCell(String id, CellType type) {

        switch (type) {

            case RECTANGLE:
                RectangleCell rectangleCell = new RectangleCell(id);
                addCell(rectangleCell);
                break;

            case LABEL:
                LabelCell labelCell = new LabelCell(id);
                labelCell.setStyle("-fx-background-color: gold");
                addCell(labelCell);
                break;

            case PAPERLABEL:
                PaperLabelCell paperlabelCell = new PaperLabelCell(id);
                paperlabelCell.setStyle("-fx-background-color: aqua");
                addCell(paperlabelCell);
                break;

            case TRIANGLE:
                TriangleCell circleCell = new TriangleCell(id);
                addCell(circleCell);
                break;

            default:
                throw new UnsupportedOperationException("Unsupported type: " + type);
        }
    }

    private void addCell( Cell cell) {

        addedCells.add(cell);

        cellMap.put( cell.getCellId(), cell);

    }

    public void addEdge( String sourceId, String targetId) {

        Cell sourceCell = cellMap.get( sourceId);
        Cell targetCell = cellMap.get( targetId);

        Edge edge = new Edge( sourceCell, targetCell);

        addedEdges.add( edge);

    }

    public void addEdge( String sourceId, String targetId, double weight) {

        Cell sourceCell = cellMap.get( sourceId);
        Cell targetCell = cellMap.get( targetId);

        Edge edge = new Edge(sourceCell, targetCell, weight);

        addedEdges.add(edge);

    }
    public void addEdge(String sourceId, String targetId, double weight, Set<Node> intersection) {

        Cell sourceCell = cellMap.get( sourceId);
        Cell targetCell = cellMap.get( targetId);

        List<Cell> intersectionList = new ArrayList<>();
        for(Node node: intersection) {
            PaperLabelCell paperlabelCell = new PaperLabelCell(node.getName());
            paperlabelCell.setStyle("-fx-background-color: aqua");
            paperlabelCell.setVisible(false);
            intersectionList.add(paperlabelCell);
        }

        paperListForAuthor.add(intersectionList);
        Edge edge = new Edge(sourceCell, targetCell, weight, paperListForAuthor.indexOf(intersectionList));

        addedEdges.add(edge);
    }


    /**
     * Attach all cells which don't have a parent to graphParent
     * @param cellList
     */
    public void attachOrphansToGraphParent( List<Cell> cellList) {

        for( Cell cell: cellList) {
            if( cell.getCellParents().size() == 0) {
                graphParent.addCellChild( cell);
            }
        }

    }

    /**
     * Remove the graphParent reference if it is set
     * @param cellList
     */
    public void disconnectFromGraphParent( List<Cell> cellList) {

        for( Cell cell: cellList) {
            graphParent.removeCellChild( cell);
        }
    }

    public void merge() {

        // cells
        allCells.addAll( addedCells);
        allCells.removeAll( removedCells);

        for(int i = 0; i <paperListForAuthor.size();i++)
                allCells.addAll(paperListForAuthor.get(i));

        addedCells.clear();
        removedCells.clear();

        // edges
        allEdges.addAll( addedEdges);
        allEdges.removeAll( removedEdges);

        addedEdges.clear();
        removedEdges.clear();

    }
}