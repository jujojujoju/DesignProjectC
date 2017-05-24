package sample;

/**
 * Created by joju on 2017. 4. 14..
 */
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import project.Database;
import project.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Edge extends Group {
    private int index;
    private Cell source;
    private Cell target;
    private double weight;
    private Line line = new Line();

    Edge(Cell source, Cell target) {

        this.source = source;
        this.target = target;

        source.addCellChild(target);
        target.addCellParent(source);

        line.startXProperty().bind( source.layoutXProperty().add(source.getBoundsInParent().getWidth() / 2.0));
        line.startYProperty().bind( source.layoutYProperty().add(source.getBoundsInParent().getHeight() / 2.0));

        line.endXProperty().bind( target.layoutXProperty().add( target.getBoundsInParent().getWidth() / 2.0));
        line.endYProperty().bind( target.layoutYProperty().add( target.getBoundsInParent().getHeight() / 2.0));

        getChildren().add(line);

    }


    Edge(Cell source, Cell target, double weight) {

        this.source = source;
        this.target = target;
        this.weight = weight*3;

        source.addCellChild(target);
        target.addCellParent(source);

        line.startXProperty().bind( source.layoutXProperty().add(source.getBoundsInParent().getWidth() / 2.0));
        line.startYProperty().bind( source.layoutYProperty().add(source.getBoundsInParent().getHeight() / 2.0));

        line.endXProperty().bind( target.layoutXProperty().add( target.getBoundsInParent().getWidth() / 2.0));
        line.endYProperty().bind( target.layoutYProperty().add( target.getBoundsInParent().getHeight() / 2.0));

        line.setStrokeWidth(this.weight);

        getChildren().add(line);

    }

    Edge(Cell source, Cell target, double weight, int index) {

        this.source = source;
        this.target = target;
        this.weight = weight*3;
        this.index = index;

        source.addCellChild(target);
        target.addCellParent(source);

        line.startXProperty().bind( source.layoutXProperty().add(source.getBoundsInParent().getWidth() / 2.0));
        line.startYProperty().bind( source.layoutYProperty().add(source.getBoundsInParent().getHeight() / 2.0));

        line.endXProperty().bind( target.layoutXProperty().add( target.getBoundsInParent().getWidth() / 2.0));
        line.endYProperty().bind( target.layoutYProperty().add( target.getBoundsInParent().getHeight() / 2.0));

        line.setStrokeWidth(this.weight);

        getChildren().add(line);

    }

    public void edgeClicked(Graph graph)
    {

        double paperPointX = (line.getStartX() + line.getEndX())/2;
        double paperPointY = (line.getStartY() + line.getEndY())/2;

        List<Cell> cellList = graph.getModel().getPaperListForAuthor().get(this.index);
        for(int i=0;i<cellList.size();i++ ) {
            if(!cellList.get(i).isVisible()) {
                cellList.get(i).setVisible(true);
                cellList.get(i).relocate(paperPointX, paperPointY + (18 * i));
            }else
                cellList.get(i).setVisible(false);
        }

    }

    public Cell getSource() {
        return source;
    }

    public Cell getTarget() {
        return target;
    }

    public double getWeight() {
        return weight;
    }


}