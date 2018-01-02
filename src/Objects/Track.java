package Objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Parent;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Track extends Pane
{
    public SimpleIntegerProperty volume;
    private String track_name;
//    private Instrument instrument;

    public Track(String name)
    {
        this.getStyleClass().add("track");
        double height = 100.0;
        this.setMinHeight(height);
        this.setMaxHeight(height);

        TextField text_track_name = new TextField(name);

        Slider track_volume_slider = new Slider(0,100, 100);
        this.volume = new SimpleIntegerProperty(100);
        this.volume.bind(track_volume_slider.valueProperty());

        MenuButton instrument_selection = new MenuButton();

        VBox track_control = new VBox();
        track_control.getStyleClass().add("track-control");
        track_control.getChildren().addAll(text_track_name, track_volume_slider, instrument_selection);

        Pane timeline = new Pane();
        timeline.getStyleClass().add("track-timeline");
        timeline.setMinWidth(100.0);
        timeline.setMaxWidth(Double.POSITIVE_INFINITY);
        timeline.getChildren().add(new Slider());

        BorderPane container = new BorderPane();
        container.setLeft(track_control);
        container.setCenter(timeline);

        this.getChildren().addAll(container);


    }


}
