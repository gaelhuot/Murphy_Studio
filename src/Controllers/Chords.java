package Controllers;

import Models.ChordModel;
import Models.MainModel;
import Objects.Accord;
import Objects.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Chords extends Controller implements Initializable {

    private ChordModel chordModel;
    private MainModel model;

    @FXML // Buttons d'accords
    private Button chordC,chordCm,chordD,chordDm,chordE,chordEm,chordF,chordFm,chordG,chordGm,chordA,chordAm,chordB,chordBm;

    private HashMap<Button, Accord> buttonToChord = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*
            /!\ ATTENTION /!\
            Ici le controller n'est pas encore chargé.
            La méthode initialize est appelée lorsque l'on fait FXMLLoader.load(); (CF application.Controller - @loadView() )
         */
    }

    private void init() {

        try {
            model.player = new Player();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }

        buttonToChord.put(chordC, chordModel.getChord("C"));
        buttonToChord.put(chordCm, chordModel.getChord("Cm"));
        buttonToChord.put(chordD, chordModel.getChord("D"));
        buttonToChord.put(chordDm, chordModel.getChord("Dm"));
        buttonToChord.put(chordE, chordModel.getChord("E"));
        buttonToChord.put(chordEm, chordModel.getChord("Em"));
        buttonToChord.put(chordF, chordModel.getChord("F"));
        buttonToChord.put(chordFm, chordModel.getChord("Fm"));
        buttonToChord.put(chordG, chordModel.getChord("G"));
        buttonToChord.put(chordGm, chordModel.getChord("Gm"));
        buttonToChord.put(chordA, chordModel.getChord("A"));
        buttonToChord.put(chordAm, chordModel.getChord("Am"));
        buttonToChord.put(chordB, chordModel.getChord("B"));
        buttonToChord.put(chordBm, chordModel.getChord("Bm"));

        for (Map.Entry<Button, Accord> entry: buttonToChord.entrySet())
        {
            entry.getKey().setOnMouseClicked(event -> {
                playChord(entry.getValue());
            });
        }
    }

    private void playChord(Accord chord)
    {
        Accord newChord = chord.getWithScale(1);
        System.out.println(newChord.getName() + " (" + newChord.getShortName() + ") : " + Arrays.toString(newChord.getNotes()));
        try {
            this.model.player.playChord(newChord.getWithScale(4), true);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    public void setModel(MainModel model) {
        this.model = model;
        this.chordModel = model.chordModel;
        init();
    }


}