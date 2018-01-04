package Objects;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import javax.sound.midi.*;
import java.util.ArrayList;

public class Player {

    private Sequencer sequencer;

    private MidiChannel midiChannel;

    private Receiver receiver;
    private Transmitter transmitter;
    private Synthesizer synthesizer;
    public ArrayList<Track> tracks;

    private MidiDevice ouputDevice;

    public SimpleIntegerProperty master_volume;

    public Player() throws MidiUnavailableException {
        master_volume = new SimpleIntegerProperty(50);

        initSequencer();
    }

    ShortMessage createMidiMessage(int command, int channel, int data1, int data2) throws InvalidMidiDataException {
        ShortMessage sm = new ShortMessage();
        sm.setMessage(command, channel, data1, data2);
        return sm;
    }

    public void playChord(Accord chord, boolean start) throws InvalidMidiDataException {
        int[] chordNotes = chord.getNotes();
        for ( int i = 0; i < chord.getNotes().length; i++ )
        {
            ShortMessage sm = createMidiMessage(
                    (start) ? ShortMessage.NOTE_ON : ShortMessage.NOTE_OFF,
                    0,
                    chord.getNotes()[i],
                    93
            );
            long timeStamp = -1;

            receiver.send(sm, timeStamp);
        }
    }

    private void initSequencer() throws MidiUnavailableException
    {
        synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();

        sequencer = MidiSystem.getSequencer();
        sequencer.open();

        this.midiChannel = synthesizer.getChannels()[0];

        receiver = new CustomReceiver(midiChannel);
    }
}
