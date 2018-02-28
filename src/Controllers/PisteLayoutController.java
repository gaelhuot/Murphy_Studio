package Controllers;

import Models.MainModel;
import Objects.Accord;
import application.Main;
import javafx.beans.property.DoublePropertyBase;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequencer;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PisteLayoutController extends Controller
{
    private MainModel model;

    public ScrollBar timeline_scrollbar;
    private ArrayList<PisteController> pistes = new ArrayList<PisteController>();

    public ToolBar piste_layout_button_bar;
    public Button addPisteBtn;
    public Button playPisteBtn;
    public VBox PisteLayoutVBox;

    private boolean isPlaying = false;

    public void initialize(URL location, ResourceBundle resources) {

    }

    private void initPistesLayout() {
        addPiste();
        addPisteBtn.setOnMouseClicked(event -> {
            addPiste();
        });

        playPisteBtn.setOnMouseClicked(event -> {
            if ( ! this.isPlaying )
            {
                for ( int i = 1; i < pistes.size(); i++ )
                {
                    if ( pistes.get(i).sequence != null  )
                        pistes.get(i).play();
                }
                this.isPlaying = true;
            }
            else
            {
                for ( int i = 1; i < pistes.size(); i++ )
                {
                    if ( pistes.get(i).sequence != null  )
                        pistes.get(i).stop();
                }
                this.isPlaying = false;
            }
        });
    }

    public void setModel(MainModel model)
    {
        this.model = model;
        this.model.pisteLayoutController = this;
        initPistesLayout();
    }

    private void addPiste()
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/piste.fxml"));
            Pane piste = fxmlLoader.load();

            PisteController pisteController = fxmlLoader.getController();
            pisteController.setModel(model);
            pistes.add(pisteController);

            pisteController.setName("Piste #" + pistes.size());
            PisteLayoutVBox.getChildren().add(piste);
            pisteController.scrollpane.hvalueProperty().bindBidirectional(this.timeline_scrollbar.valueProperty());
            this.timeline_scrollbar.maxProperty().bind(pisteController.scrollpane.vmaxProperty());
            this.model.chordSorterController.addPisteToList(pisteController);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void removePiste(PisteController piste)
    {
        for (int i = this.pistes.size() - 1; i >= 0; i--)
        {
            if (this.pistes.get(i) == piste)
            {
                PisteLayoutVBox.getChildren().remove(i);
                pistes.remove(i);
            }
        }
        this.model.chordSorterController.removePisteFromList(piste);
    }

}
