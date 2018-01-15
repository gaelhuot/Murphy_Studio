package Controllers;

import Models.MainModel;
import Objects.Accord;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javax.sound.midi.InvalidMidiDataException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PisteLayoutController extends Controller
{

    private MainModel model;

    private ArrayList<PisteController> pistes = new ArrayList<PisteController>();

    public ToolBar piste_layout_button_bar;
    public Button addPisteBtn;
    public Button removePisteBtn;
    public Button playPisteBtn;
    public VBox PisteLayoutVBox;

    public void initialize(URL location, ResourceBundle resources) {

    }

    private void initPistesLayout() {
        addPisteBtn.setOnMouseClicked(event -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/piste.fxml"));
                Pane piste = fxmlLoader.load();

                PisteController pisteController = fxmlLoader.getController();
                pisteController.setModel(model);
                pistes.add(pisteController);

                pisteController.setName("Piste #" + pistes.size());
                PisteLayoutVBox.getChildren().add(piste);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        removePisteBtn.setOnMouseClicked(event -> {
            if ( pistes.size() == 0 ) return;
            PisteLayoutVBox.getChildren().remove(pistes.size() - 1);
            pistes.remove(pistes.size() - 1);
        });

        playPisteBtn.setOnMouseClicked(event-> {
            Accord[] test = {
                    model.chordModel.getChord("C").getWithScale(4),
                    model.chordModel.getChord("C").getWithScale(4),
                    model.chordModel.getChord("C").getWithScale(5),
                    model.chordModel.getChord("C").getWithScale(5),
                    model.chordModel.getChord("Am").getWithScale(3),
                    model.chordModel.getChord("Am").getWithScale(3),
                    model.chordModel.getChord("Am").getWithScale(4),
                    model.chordModel.getChord("Am").getWithScale(4),
                    model.chordModel.getChord("F").getWithScale(3),
                    model.chordModel.getChord("F").getWithScale(3),
                    model.chordModel.getChord("F").getWithScale(4),
                    model.chordModel.getChord("F").getWithScale(4),
                    model.chordModel.getChord("G").getWithScale(3),
                    model.chordModel.getChord("G").getWithScale(3),
                    model.chordModel.getChord("G").getWithScale(4),
                    model.chordModel.getChord("G").getWithScale(4),
            };
            try {
                model.player.createPisteFromChords(test);
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            }
        });
    }

    public void setModel(MainModel model)
    {
        this.model = model;
        initPistesLayout();
    }


}
