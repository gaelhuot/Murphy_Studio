package Objects;

import javax.sound.midi.*;

public class MidiKeyboard {

    private Synthesizer synthesizer;

    private MidiDevice device;

    private MidiChannel midiChannel;

    private CustomReceiver receiver;
    private Transmitter transmitter;
    public Sequencer sequencer;

    private Sequence sequence;

    public MidiKeyboard(MidiDevice midiDevice)
    {
        try {
            this.device = midiDevice;

            this.synthesizer = MidiSystem.getSynthesizer();
            this.synthesizer.open();
            this.midiChannel = this.synthesizer.getChannels()[0];

            this.receiver = new CustomReceiver(this.midiChannel);

            this.sequencer = MidiSystem.getSequencer();
            this.sequencer.open();;

            MidiSystem.getTransmitter().setReceiver(receiver);

            this.device.open();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void close()
    {
        if ( this.transmitter != null )
            this.transmitter.close();
        if ( this.device != null )
            this.device.close();
    }

}
