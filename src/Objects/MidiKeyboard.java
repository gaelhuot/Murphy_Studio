package Objects;

import Models.MainModel;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.Receiver;

public class MidiKeyboard {

    private MainModel model;

    private MidiDevice device;
    private Receiver receiver;

    private MidiChannel midiChannel;

    public MidiKeyboard(MidiDevice midiDevice)
    {
        this.device = midiDevice;
    }

}
