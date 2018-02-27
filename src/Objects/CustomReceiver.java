package Objects;

import javax.sound.midi.*;
import java.util.Arrays;

public class CustomReceiver implements Receiver {
    public Receiver rcvr;
    public MidiChannel channel;

    CustomReceiver(MidiChannel channel) {
        this.channel = channel;
        try {
            this.rcvr = MidiSystem.getReceiver();
        } catch (MidiUnavailableException mue) {
            mue.printStackTrace();
        }
    }

    public CustomReceiver(MidiChannel channel, Receiver receiver) {
        this.channel = channel;
        this.rcvr = receiver;
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {

        byte[] b = message.getMessage();

        int note_id = b[1];
        if ( b[2] != 0 )
        {
            channel.noteOn(note_id, b[2]);
        }
        else
        {
            channel.noteOff(note_id);
        }

        this.rcvr.send(message, timeStamp);
    }

    @Override
    public void close() {
        rcvr.close();
    }
}
