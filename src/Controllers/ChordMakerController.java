package Controllers;

import Models.MainModel;
import Objects.Accord;
import Objects.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import javax.sound.midi.MidiUnavailableException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class ChordMakerController extends Controller implements Initializable {

    public Button playChordButton;
    public Label chordNameLabel;

    public RadioButton doRadio, reRadio, miRadio, faRadio, solRadio, laRadio, siRadio ;
    private ToggleGroup noteChordGroup = new ToggleGroup();

    @FXML
    private Pane notePane1,notePane2,notePane3,notePane4,notePane5,notePane6,notePane7,notePane8,notePane9,notePane10,notePane11,notePane12,notePane13,notePane14,notePane15,notePane16,notePane17,notePane18,notePane19,notePane20,notePane21,notePane22,notePane23,notePane24,notePane25,notePane26,notePane27,notePane28,notePane29,notePane30,notePane31,notePane32,notePane33,notePane34,notePane35,notePane36;
    private Pane[] notesPane;

    private Accord accord;
    private Player player;

    private Boolean isSeventh = false, isFifth = false, isMinor = false;

    private LinkedHashMap<String, Method> listToFunc;
    public ListView<String> chordListView;

    private MainModel model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listToFunc = new LinkedHashMap<>();

        notesPane = new Pane[]{notePane1,notePane2,notePane3,notePane4,notePane5,notePane6,notePane7,notePane8,notePane9,notePane10,notePane11,notePane12,notePane13,notePane14,notePane15,notePane16,notePane17,notePane18,notePane19,notePane20,notePane21,notePane22,notePane23,notePane24,notePane25,notePane26,notePane27,notePane28,notePane29,notePane30,notePane31,notePane32,notePane33,notePane34,notePane35,notePane36};

        doRadio.setToggleGroup(noteChordGroup);
        reRadio.setToggleGroup(noteChordGroup);
        miRadio.setToggleGroup(noteChordGroup);
        faRadio.setToggleGroup(noteChordGroup);
        solRadio.setToggleGroup(noteChordGroup);
        laRadio.setToggleGroup(noteChordGroup);
        siRadio.setToggleGroup(noteChordGroup);

        doRadio.fire();

        try {
            player = new Player();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }

        accord = new Accord(60, false, false, false);
        accord.setMajor();
        updtInfos();

        try {
            listToFunc.put(("Minor"), Accord.class.getMethod("setMinor"));
            listToFunc.put(("Major"), Accord.class.getMethod("setMajor"));
            listToFunc.put(("Dominant Seventh"), Accord.class.getMethod("setDominantSeven"));
            listToFunc.put(("Minor Seventh"), Accord.class.getMethod("setMinorSeventh"));
            listToFunc.put(("Major Seventh"), Accord.class.getMethod("setMajorSeventh"));
            listToFunc.put(("Dominant Seventh with Flattened fifth"), Accord.class.getMethod("setDominantSeventhFlattenedFifth"));
            listToFunc.put(("Dominant Seventh with Sharp fifth"), Accord.class.getMethod("setDominantSeventhSharpedFifth"));
            listToFunc.put(("Sixth"), Accord.class.getMethod("setSixth"));
            listToFunc.put(("Minor sixth"), Accord.class.getMethod("setMinorSixth"));
            listToFunc.put(("Minor ninth"), Accord.class.getMethod("setMinorNinth"));
            listToFunc.put(("Major ninth"), Accord.class.getMethod("setMajorNinth"));
            listToFunc.put(("Diminished"), Accord.class.getMethod("setDiminished"));
            listToFunc.put(("Diminished seventh"), Accord.class.getMethod("setDiminishedSeventh"));
            listToFunc.put(("Augmented"), Accord.class.getMethod("setAugmented"));
            listToFunc.put(("Suspended fourth"), Accord.class.getMethod("setSuspendedFourth"));
            listToFunc.put(("Suspended second"), Accord.class.getMethod("setSuspendedSecond"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        ObservableList<String> items = FXCollections.observableArrayList();

        System.out.println(listToFunc);
        for(Map.Entry <String, Method> entry : listToFunc.entrySet())
        {
            items.add(entry.getKey());
        }

        chordListView.setItems(items);

        chordListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                listToFunc.get(newValue).invoke(accord);
                updtInfos();
                model.setSelectedChord(accord);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        noteChordGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == doRadio )   this.accord = new Accord(60, isMinor, isFifth, isSeventh);
            if (newValue == reRadio )   this.accord = new Accord(62, isMinor, isFifth, isSeventh);
            if (newValue == miRadio )   this.accord = new Accord(64, isMinor, isFifth, isSeventh);
            if (newValue == faRadio )   this.accord = new Accord(65, isMinor, isFifth, isSeventh);
            if (newValue == solRadio)   this.accord = new Accord(67, isMinor, isFifth, isSeventh);
            if (newValue == laRadio )   this.accord = new Accord(69, isMinor, isFifth, isSeventh);
            if (newValue == siRadio )   this.accord = new Accord(71, isMinor, isFifth, isSeventh);

            updtInfos();
            model.setSelectedChord(accord);
        });

        playChordButton.setOnMouseClicked(event -> {
            for ( int i = 0; i < accord.getNotes().size(); i++)
                player.playNote(accord.getNotes().get(i));
        });
    }

    private void updtInfos()
    {
        chordNameLabel.setText(accord.getName());
        resetKeys();
        colorizeKeys();
    }

    private void resetKeys()
    {
        for (Pane aNotesPane : notesPane) aNotesPane.setStyle(null);
    }

    private void colorizeKeys()
    {
        ArrayList<Integer> notes = this.accord.getNotes();
        for (Object note : notes) {
            int notePaneIndex = (int) note - 60;
            notesPane[notePaneIndex].setStyle("-fx-background-color: red");
        }
    }

    public void setModel(MainModel model) {
        this.model = model;
        model.setSelectedChord(accord);
    }
}
