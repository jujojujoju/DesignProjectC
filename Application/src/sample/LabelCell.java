package sample;

import javafx.scene.control.Label;

/**
 * Created by joju on 2017. 4. 14..
 */

public class LabelCell extends Cell {

    public LabelCell(String id) {
        super(id);

        Label view = new Label(id);
        setView(view);

    }

}