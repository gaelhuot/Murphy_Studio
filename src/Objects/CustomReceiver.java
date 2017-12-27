package Objects;

import javax.sound.midi.*;

public class CustomReceiver implements Receiver {
    Receiver rcvr;
    MidiChannel channel;

    public CustomReceiver(MidiChannel channel) throws MidiUnavailableException {
        this.channel = channel;
        try {
            this.rcvr = MidiSystem.getReceiver();
        } catch (MidiUnavailableException mue) {
            mue.printStackTrace();
        }
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        byte[] b = message.getMessage();

        int note_id = b[1];
        if ( b[2] != 0 )
            channel.noteOn(note_id, b[2]);
        else
            channel.noteOff(note_id);
    }

    @Override
    public void close() {

    }
}
