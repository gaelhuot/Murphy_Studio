package Objects;

import javax.sound.midi.*;

public class CustomReceiver implements Receiver {
    private Receiver rcvr;
    public MidiChannel channel;

    CustomReceiver(MidiChannel channel) {
        System.out.println("New receiver !");
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

    public MidiChannel getChannel() {
        return channel;
    }

    public void changeVolume(int value)
    {
        channel.controlChange(7, value);
    }

    @Override
    public void close() {
        rcvr.close();
    }
}
