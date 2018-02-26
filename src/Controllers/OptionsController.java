package Controllers;

import Models.MainModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class OptionsController extends Controller {

    private MainModel model;
    public Button refreshBtn;
    public ChoiceBox<String> midiInput_list;

    private HashMap<String, MidiDevice> nameToDevice;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        /* Ne pas mettre de code ici, on a besoin du model pour charger les données */
    }

    private void init()
    {
        refreshBtn.setOnMouseClicked( event -> this.init() );

        this.model.player.MidiInput = null;

        // On vide la liste
        midiInput_list.getItems().clear();

        // On récupère les midi devices
        MidiDevice midiDevice;
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();

        this.model.player.midiDevices = new ArrayList<>();
        this.nameToDevice = new HashMap<>();

        for (int i = 0; i < infos.length; i++) {
            try {
                midiDevice = MidiSystem.getMidiDevice(infos[i]);
                this.model.player.midiDevices.add(midiDevice);

                String name = midiDevice.getDeviceInfo().getName();

                nameToDevice.put(name, midiDevice);

                midiInput_list.getItems().add(name);
            } catch (MidiUnavailableException ignored) {}
        }

        midiInput_list.valueProperty().addListener((ChangeListener) (observable, oldValue, newValue)
                -> this.model.player.MidiInput = nameToDevice.get(newValue));

        
    }

    public void setModel(MainModel model)
    {
        this.model = model;
        init();
    }
}
