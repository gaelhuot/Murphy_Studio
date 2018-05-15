package murphystudio.objects;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;


public class TimelineElement extends Rectangle {

    private double start;

    public TimelineElement(double start, double width){
        this.start = start;
        this.getStyleClass().add("timeline-element");


        this.setStrokeWidth(5.0);

        this.setHeight(111.0);

        AnchorPane.setTopAnchor(this, 0.0);
        AnchorPane.setBottomAnchor(this, 0.0);
        AnchorPane.setLeftAnchor(this, this.start);
        this.setWidth(width);
    }

    public double getStart(){return this.start;}
    public double getEnd(){return this.start + this.getWidth();}


}
