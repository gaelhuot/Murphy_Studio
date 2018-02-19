package Controllers;

import Models.MainModel;
import Objects.Accord;
import Objects.Tile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class ChordMakerController extends Controller implements Initializable {

    public Button playChordButton;
    public Button saveChordButton;

    public Label chordNameLabel;

    public RadioButton doRadio, reRadio, miRadio, faRadio, solRadio, laRadio, siRadio ;
    public BorderPane chordMakerPane;
    private ToggleGroup noteChordGroup = new ToggleGroup();

    private Boolean isSeventh = false, isFifth = false, isMinor = false;

    private LinkedHashMap<String, Method> listToFunc;
    public ListView<String> chordListView;
    private ObservableList<String> items;

    private MainModel model;

    // 0 : Not / 1 : In function / 2 : Function finished
    private int printFromTile = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listToFunc = new LinkedHashMap<>();

        doRadio.setToggleGroup(noteChordGroup);
        reRadio.setToggleGroup(noteChordGroup);
        miRadio.setToggleGroup(noteChordGroup);
        faRadio.setToggleGroup(noteChordGroup);
        solRadio.setToggleGroup(noteChordGroup);
        laRadio.setToggleGroup(noteChordGroup);
        siRadio.setToggleGroup(noteChordGroup);

        doRadio.fire();

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
        this.items = FXCollections.observableArrayList();

        for(Map.Entry <String, Method> entry : listToFunc.entrySet())
            items.add(entry.getKey());

        chordListView.setItems(items);
        chordListView.getSelectionModel().select(1);

        chordListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ( newValue == null || printFromTile == 1 ) return;

            try {
                listToFunc.get(newValue).invoke(model.selectedChord);
                updtInfos();
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        noteChordGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if ( newValue == null ) return;

            Method lastFunction = model.selectedChord.getMethodCalled();

            if (newValue == doRadio )   model.selectedChord = new Accord(60, isMinor, isFifth, isSeventh);
            if (newValue == reRadio )   model.selectedChord = new Accord(62, isMinor, isFifth, isSeventh);
            if (newValue == miRadio )   model.selectedChord = new Accord(64, isMinor, isFifth, isSeventh);
            if (newValue == faRadio )   model.selectedChord = new Accord(65, isMinor, isFifth, isSeventh);
            if (newValue == solRadio)   model.selectedChord = new Accord(67, isMinor, isFifth, isSeventh);
            if (newValue == laRadio )   model.selectedChord = new Accord(69, isMinor, isFifth, isSeventh);
            if (newValue == siRadio )   model.selectedChord = new Accord(71, isMinor, isFifth, isSeventh);

            try {
                Accord.class.getMethod(lastFunction.getName()).invoke(model.selectedChord);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }

            updtInfos();
        });

        playChordButton.setOnMouseClicked(event -> {
            Accord ch = ( printFromTile == 2 ) ? model.selectedTile.accord : model.selectedChord;
            for (int i = 0; i < ch.getNotes().size(); i++)
                this.model.player.playNote(ch.getNotes().get(i));
        });

        saveChordButton.setOnMouseClicked(event -> saveChord());

    }

    private void updtInfos()
    {
        if ( printFromTile == 2 ) printFromTile = 0;
        chordNameLabel.setText(model.selectedChord.getName());
        this.model.chordSorterController.resetKeys();
        this.model.chordSorterController.colorizeKeys();
    }

    private void saveChord()
    {
        if ( model != null && model.selectedChord != null && printFromTile != 1 )
            model.chordSorterController.changeSelectedTileChord(model.selectedChord);

    }


    private String getChordNameByMethod(Accord accord)
    {
        if ( accord.getMethodCalled() == null ) return null;

        String name = null;
        for ( Map.Entry<String, Method> entry : listToFunc.entrySet() )
            if (entry.getValue().getName().equals(accord.getMethodCalled().getName())) name = entry.getKey();

        return name;
    }

    private void fireRadio(Accord ch)
    {
        switch (ch.getDominantName()) {
            case 'A' : laRadio.fire(); break;
            case 'B' : siRadio.fire(); break;
            case 'C' : doRadio.fire(); break;
            case 'D' : reRadio.fire(); break;
            case 'E' : miRadio.fire(); break;
            case 'F' : faRadio.fire(); break;
            case 'G' : solRadio.fire(); break;
        }
    }

    public void updateFromTile(Tile tile)
    {
        printFromTile = 1;

        Accord ch = tile.accord;
        chordListView.getSelectionModel().select(getChordNameByMethod(model.selectedTile.accord));
        fireRadio(ch);

        chordNameLabel.setText(ch.getName());
        for (Pane aNotesPane : this.model.chordSorterController.notesPane) aNotesPane.setStyle(null);
        for (int note : ch.getNotes()) {
            int notePaneIndex = note - 60;
            this.model.chordSorterController.notesPane[notePaneIndex].setStyle("-fx-background-color: red");
        }

        printFromTile = 2;
    }

    public void setModel(MainModel model) {
        this.model = model;

        model.selectedChord = new Accord(60, false, false, false);
        model.selectedChord.setMajor();
        updtInfos();

    }
}
