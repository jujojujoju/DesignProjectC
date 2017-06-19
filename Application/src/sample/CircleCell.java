package sample;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Created by joju on 2017. 5. 17..
 */
public class CircleCell extends Cell {

    public CircleCell( String id, double radius) {
        super(id);

        int red = (int) (radius*8);
        Circle view = new Circle(radius*10,Color.rgb(red>255?255:red,10,10));
        view.setStrokeWidth(3);
        Text text = new Text(id);
        text.setStyle("-fx-border-color: aqua");
        text.setFill(Color.YELLOWGREEN);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(view,text);

        setView( stackPane);

    }
}
