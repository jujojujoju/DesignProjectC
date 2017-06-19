package sample;

import javafx.scene.control.Label;

/**
 * Created by joju on 2017. 4. 17..
 */
public class PaperLabelCell extends Cell{
    public PaperLabelCell(String id) {
        super(id);

        Label view = new Label(id);
        setView(view);

    }
}
