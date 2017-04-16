package sample;

/**
 * Created by joju on 2017. 4. 14..
 */
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Edge extends Group {

    private Cell source;
    private Cell target;


    private Line line = new Line();

    Edge(Cell source, Cell target) {

        this.source = source;
        this.target = target;

        source.addCellChild(target);
        target.addCellParent(source);

//        Line line = new Line();

        line.startXProperty().bind( source.layoutXProperty().add(source.getBoundsInParent().getWidth() / 2.0));
        line.startYProperty().bind( source.layoutYProperty().add(source.getBoundsInParent().getHeight() / 2.0));

        line.endXProperty().bind( target.layoutXProperty().add( target.getBoundsInParent().getWidth() / 2.0));
        line.endYProperty().bind( target.layoutYProperty().add( target.getBoundsInParent().getHeight() / 2.0));

        getChildren().add(line);

    }


    Edge(Cell source, Cell target, double weight) {

        this.source = source;
        this.target = target;

        source.addCellChild(target);
        target.addCellParent(source);
//
//        Line line = new Line();

        line.startXProperty().bind( source.layoutXProperty().add(source.getBoundsInParent().getWidth() / 2.0));
        line.startYProperty().bind( source.layoutYProperty().add(source.getBoundsInParent().getHeight() / 2.0));

        line.endXProperty().bind( target.layoutXProperty().add( target.getBoundsInParent().getWidth() / 2.0));
        line.endYProperty().bind( target.layoutYProperty().add( target.getBoundsInParent().getHeight() / 2.0));

        line.setStrokeWidth(weight);

        getChildren().add(line);

    }

    public void edgeClicked()
    {
//        line.setFill(Color.RED);
        line.setStrokeWidth(10);


    }

    public Cell getSource() {
        return source;
    }

    public Cell getTarget() {
        return target;
    }

}