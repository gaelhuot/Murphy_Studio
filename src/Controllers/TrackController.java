package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class TrackController extends Initializable {

    @FXML
    public TextField track_name_input;
    @FXML
    public Slider track_volume_input;
    @FXML
    public ImageView track_instrument_icon;
    @FXML
    public MenuButton track_instrument_selection;
    @FXML
    public HBox track_timeline;
}
