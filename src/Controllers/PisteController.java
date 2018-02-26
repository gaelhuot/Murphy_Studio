package Controllers;

import Models.MainModel;
import Objects.TimelineElement;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Track;
import java.net.URL;
import java.util.ArrayList;
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

    private Sequencer sequencer;
    private Sequence sequence;

    private double end;

    private ArrayList<TimelineElement> chords;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.end = 0.0;
        this.chords = new ArrayList<TimelineElement>();
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
            System.out.println("start recording");

            if ( this.sequencer.isRecording() )
            {
                this.sequencer.stopRecording();
                recordPisteBtn.setText("○");
            }
            else
            {
                this.sequencer.startRecording();
                recordPisteBtn.setText("■");
            }
        });

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

    public void addChords(){
        TimelineElement new_e = new TimelineElement(this.end);
        this.chords.add(new_e);
        this.timeline.getChildren().add(new_e);
        updateEnd();
    }

    public void updateEnd(){
        this.end = 0.0;
        for (TimelineElement e: this.chords) {
            if (e.getEnd() > this.end){
                this.end = e.getEnd();
            }
        }
        this.end++;
    }

    public String toString(){
        return this.piste_name_input.getCharacters().toString();
    }
}
