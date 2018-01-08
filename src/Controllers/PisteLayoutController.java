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

public class PisteLayoutController extends Controller implements Initializable {

    private MainModel model;

    private ArrayList<PisteController> tracks = new ArrayList<PisteController>();

    public ToolBar track_layout_button_bar;
    public Button addTrackBtn;
    public Button removeTrackBtn;
    public Button playTrackBtn;
    public VBox trackLayoutVBox;

    public void initialize(URL location, ResourceBundle resources) {

    }

    private void initTracksLayout() {
        addTrackBtn.setOnMouseClicked(event -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/piste.fxml"));
                Pane track = fxmlLoader.load();

                PisteController trackController = fxmlLoader.getController();
                trackController.setModel(model);
                tracks.add(trackController);

                trackController.setName("Track #" + tracks.size());
                trackLayoutVBox.getChildren().add(track);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        removeTrackBtn.setOnMouseClicked(event -> {
            if ( tracks.size() == 0 ) return;
            trackLayoutVBox.getChildren().remove(tracks.size() - 1);
            tracks.remove(tracks.size() - 1);
        });

        playTrackBtn.setOnMouseClicked(event-> {
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
                model.player.createTrackFromChords(test);
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            }
        });
    }

    public void setModel(MainModel model)
    {
        this.model = model;
        initTracksLayout();
    }


}
