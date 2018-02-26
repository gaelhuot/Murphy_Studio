package Objects;

import Models.MainModel;

import javax.sound.midi.*;

public class MidiKeyboard {

    private Synthesizer synthesizer;

    private MidiDevice device;
    private CustomReceiver receiver;

    private MidiChannel midiChannel;
    private Transmitter transmitter;

    public MidiKeyboard(MidiDevice midiDevice)
    {
        try {
            this.device = midiDevice;

            this.synthesizer = MidiSystem.getSynthesizer();
            this.synthesizer.open();
            this.midiChannel = this.synthesizer.getChannels()[0];

            this.receiver = new CustomReceiver(this.midiChannel);

            MidiSystem.getTransmitter().setReceiver(receiver);

            this.device.open();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }

    }

    public void close()
    {
        this.transmitter.close();
        this.device.close();
    }

}
