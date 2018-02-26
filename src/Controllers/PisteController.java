package Controllers;

import Models.MainModel;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javax.sound.midi.Track;
import java.net.URL;
import java.util.ResourceBundle;

public class PisteController extends Controller
{

    @FXML
    public TextField piste_name_input;
    @FXML
    public Slider piste_volume_slider;
    @FXML
    public ImageView piste_instrument_icon;
    @FXML
    public MenuButton piste_instrument_selection;
    @FXML
    public ScrollPane scrollpane;
    @FXML
    public Button deletePistePtn;
    public Button recordPisteBtn;

    public Track track;
    public AnchorPane timeline;

    private boolean isRecording;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        /*
            /!\ ATTENTION /!\
            Ici le controller n'est pas encore chargé.
            La méthode initialize est appelée lorsque l'on fait FXMLLoader.load(); (CF application.Controller - @loadView() )
         */
        /* Tout ce qui agit sur le fxml, tu le code ici */
    }

    private void initAll()
    {
        this.deletePistePtn.setOnMouseClicked(event -> {
            this.model.pisteLayoutController.removePiste(this);
        });

        recordPisteBtn.setOnMouseClicked(event -> {
            /*System.out.println("StartRecording");
            System.out.println(model.player.sequencer.getSequence() );

            if ( model.player.sequencer.isRecording() )
            {
                model.player.stopRecording();
                recordPisteBtn.setText("○");
            }
            else
            {
                model.player.startRecording();
                recordPisteBtn.setText("■");
            }*/
        });

//        Exemple de placement de tile
        Rectangle tile = new Rectangle();
        tile.setHeight(128);
        tile.setWidth(256);
        tile.setFill(Color.AQUA);

        this.timeline.getChildren().add(tile);
        AnchorPane.setLeftAnchor(tile, 128.0);
    }

    public void setName(String name)
    {
        piste_name_input.setText(name);
    }

    public void setModel(MainModel model) {
        this.model = model;
        initAll();
    }

    public void setTrack(Track track) {

    }
}
