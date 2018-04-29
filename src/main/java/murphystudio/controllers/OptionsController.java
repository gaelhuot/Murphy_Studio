package murphystudio.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import murphystudio.models.MainModel;

import javax.sound.midi.MidiDevice;
import javax.sound.sampled.Mixer;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class OptionsController extends Controller {

    public ChoiceBox<String> audioOutput_list;
    public ChoiceBox<String> midiInput_list;
    public ChoiceBox<String> audioInput_list;
    public ChoiceBox<String> midiOutput_list;
    public Button refreshBtn;

    private HashMap<String, MidiDevice> nameToDevice;
    private HashMap<String, Mixer.Info> nameToMixer;

    private MainModel model;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        /* Ne pas mettre de code ici, on a besoin du model pour charger les données */
    }

    private void setMixer(int index)
    {
        audioOutput_list.getSelectionModel().select(index);
        this.model.mainExternInterface.setMixer(this.model.mainExternInterface.getOutputMixers().get(index));
    }

    private void setMicrophone(int index)
    {
        audioInput_list.getSelectionModel().select(index);
        this.model.mainExternInterface.setMicrophone(this.model.mainExternInterface.getInputMixers().get(index));
    }

    private void setMidiDevice(int index)
    {
        midiInput_list.getSelectionModel().select(index);
        this.model.mainExternInterface.setMidiDevice(this.model.mainExternInterface.getInputMidiDevice().get(index));
    }

    private void setMidiOutput(int index)
    {
        midiOutput_list.getSelectionModel().select(index);
        this.model.mainExternInterface.setMidiOutput(this.model.mainExternInterface.getOutputMidiDevice().get(index));
    }

    private void refresh()
    {
        // On vide les liste
        midiInput_list.getItems().clear();
        audioOutput_list.getItems().clear();
        audioInput_list.getItems().clear();

        boolean midiDeviceOk = false;
        boolean midiOutputOk = false;
        boolean outputDeviceOk = false;
        boolean inputDeviceOk = false;

        for (MidiDevice device: model.mainExternInterface.getInputMidiDevice())
        {
            midiInput_list.getItems().add(device.getDeviceInfo().getName());
            if ( ! midiDeviceOk )
                midiDeviceOk = true;
        }

        for (MidiDevice device: model.mainExternInterface.getOutputMidiDevice())
        {
            midiOutput_list.getItems().add(device.getDeviceInfo().getName());
            if ( ! midiOutputOk )
            {
                setMidiOutput(0);
                midiOutputOk = true;
            }
        }

        for ( Mixer mixer: model.mainExternInterface.getInputMixers() )
        {
            audioOutput_list.getItems().add(mixer.getMixerInfo().getName());
            if ( ! outputDeviceOk )
            {
                setMixer(0);
                outputDeviceOk = true;
            }
        }

        for ( Mixer mixer: model.mainExternInterface.getInputMixers() )
        {
            audioInput_list.getItems().add(mixer.getMixerInfo().getName());
            if ( ! inputDeviceOk )
            {
                setMicrophone(0);
                inputDeviceOk = true;
            }
        }
    }

    public void setModel(MainModel model)
    {
        this.model = model;
        refresh();
        refreshBtn.setOnMouseClicked( event -> this.refresh() );

        midiInput_list.valueProperty().addListener((observable, oldValue, newValue) -> {
            int ind = midiInput_list.getSelectionModel().getSelectedIndex();
            if ( ind != -1 )
                setMidiDevice(ind);
        });

        midiOutput_list.valueProperty().addListener((observable, oldValue, newValue) -> {
            int ind = midiOutput_list.getSelectionModel().getSelectedIndex();
            if ( ind != -1 )
                setMidiOutput(ind);
        });

        audioOutput_list.valueProperty().addListener((observable, oldValue, newValue) -> {
            int ind = audioOutput_list.getSelectionModel().getSelectedIndex();
            if ( ind != -1 )
                setMixer(ind);
        });

        audioInput_list.valueProperty().addListener((observable, oldValue, newValue) -> {
            int ind = audioInput_list.getSelectionModel().getSelectedIndex();
            if ( ind != -1 )
                setMicrophone(ind);
        });

    }
}
