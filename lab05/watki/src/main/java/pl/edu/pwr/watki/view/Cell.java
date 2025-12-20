package pl.edu.pwr.watki.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class Cell extends StackPane {

    Rectangle underline;
    Label label;

    public Cell(Rectangle rectangle, Label label, Rectangle underline) {
        this.label = label;
        this.underline = underline;
        getChildren().addAll(rectangle, label, underline);
        StackPane.setAlignment(underline, Pos.BOTTOM_CENTER);
    }

    public void setUnderline(boolean visible) {
        this.underline.setVisible(visible);
    }

    public void setLabel(String text){
        this.label.setText(text);
    }
}
