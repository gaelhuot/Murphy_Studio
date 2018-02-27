package Objects;

import Models.MainModel;

import javax.sound.midi.*;

public class MidiInterface {

    private MainModel model;

    private CustomReceiver receiver;
    private MidiChannel receiverMidiChannel;

    public Synthesizer synthesizer;

    public Sequencer sequencer;

    /* MIDI CONFIG */
    public int tempo = 120;
    public int MidiTick = 4;

    public MidiInterface()
    {
        try {
            this.synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();

            this.receiverMidiChannel = this.synthesizer.getChannels()[0];
            this.receiver = new CustomReceiver(this.receiverMidiChannel);

        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    /*
     * @RETURN new sequencer
     */
    public Sequencer getSequencer()
    {
        try {
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();

            return sequencer;
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }

        return null;
    }

    /*
     * @RETURN Sequence with the track
     * Include corresponding chords
     */
    public Sequence createTrackFromChords(Accord[] chords)
    {
        try {

            Sequence sequence = new Sequence(Sequence.PPQ, this.MidiTick);
            Track track = sequence.createTrack();

            int totalTick = 0;

            for ( Accord chord : chords )
            {
                switch (chord.Rythm) {
                    case 1 :
                        for ( Object note : chord.getNotes() )
                            addNoteToTrack(track, (Integer) note, totalTick, chord.getVelocity(), chord.getTick() );
                        break;
                    case 2 :
                        int note = 0;
                        for ( int i = 0; i < chord.getTick(); i+=2 )
                        {
                            if ( note == chord.getNotes().size() ) note = 0;
                            addNoteToTrack(track, chord.getNotes().get(note), totalTick + i, chord.getVelocity(), 2);

                            note++;
                        }
                        break;
                    case 3 :
                        addNoteToTrack(track, chord.getNotes().get(0), totalTick, chord.getVelocity(), chord.getTick()/2);
                        for ( int i = 1; i < chord.getNotes().size(); i++ )
                            addNoteToTrack(track, chord.getNotes().get(i), totalTick + chord.getTick()/2, chord.getVelocity(), chord.getTick()/2);
                        break;
                }
                totalTick += chord.getTick();
            }

            return sequence;
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void playNote(int note)
    {
        try {
            ShortMessage sm = createMidiMessage(
                    ShortMessage.NOTE_ON,
                    0,
                    note,
                    93
            );
            long timeStamp = -1;
            receiver.send(sm, timeStamp);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    /* PRIVATE METHOD */

    /*
     * Simple method to add a note to a track
     */
    private void addNoteToTrack(Track track, int note, int start,int velocity, int duration)
    {
        track.add(createMidiEvent(ShortMessage.NOTE_ON, 1, note, velocity, start));
        track.add(createMidiEvent(ShortMessage.NOTE_OFF, 1, note, velocity, start+duration));
    }

    /*
     * @RETURN shortMessage ("shortcut" function)
     */
    private ShortMessage createMidiMessage(int command, int channel, int data1, int data2) throws InvalidMidiDataException {
        ShortMessage sm = new ShortMessage();
        sm.setMessage(command, channel, data1, data2);
        return sm;
    }

    /*
     * @RETURN midiEvent ("shortcut" function)
     */
    private MidiEvent createMidiEvent(int command, int channel, int data1, int data2, int tick)
    {
        MidiEvent midiEvent = null;
        try{
            ShortMessage msg = createMidiMessage(command, channel, data1, data2);
            midiEvent = new MidiEvent(msg, tick);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        return midiEvent;
    }

}
