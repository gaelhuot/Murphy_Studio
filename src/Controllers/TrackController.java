package Controllers;

import Models.MainModel;
import Objects.Track;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TrackController extends Controller implements Initializable {

    private MainModel model;

    @FXML
    public TextField track_name_input;
    @FXML
    public Slider track_volume_slider;
    @FXML
    public ImageView track_instrument_icon;
    @FXML
    public MenuButton track_instrument_selection;
    @FXML
    public HBox track_timeline;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*
            /!\ ATTENTION /!\
            Ici le controller n'est pas encore chargé.
            La méthode initialize est appelée lorsque l'on fait FXMLLoader.load(); (CF application.Controller - @loadView() )
         */
        /* Tout ce qui agit sur le fxml, tu le code ici */
    }

    private void initAll()
    {
        /* TOut ce qui agit sur le model, tu le code ici */
//        this.model.player.tracks[le_bon_track].volume.bind(this.track_volume_slider);
    }

    public void setName(String name)
    {
        track_name_input.setText(name);
    }

    public void setModel(MainModel model) {
        this.model = model;
        initAll();
    }

}
