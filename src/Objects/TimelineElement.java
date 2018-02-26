package Objects;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class TimelineElement extends Rectangle {

    private double start;

    public TimelineElement(double start){
        this.start = start;
        this.getStyleClass().add("timeline-element");
        this.setHeight(128.0);


        this.setFill(Color.STEELBLUE);

        AnchorPane.setLeftAnchor(this, this.start);
        this.setWidth(42.0);
    }

    public double getStart(){return this.start;}
    public double getEnd(){return this.start + this.getWidth();}


}
