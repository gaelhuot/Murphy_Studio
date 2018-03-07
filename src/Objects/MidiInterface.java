package Objects;

import Models.MainModel;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;

public class MidiInterface {

    private MainModel model;

    private CustomReceiver receiver;
    private MidiChannel receiverMidiChannel;

    public Synthesizer synthesizer;

    public Sequencer sequencer;

    /* MIDI CONFIG */
    public int tempo = 120;
    public int MidiTick = 4;

    public MidiInterface() {
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
    public Sequencer getSequencer() {
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
    public Sequence createTrackFromChords(Accord[] chords) {
        try {

            Sequence sequence = new Sequence(Sequence.PPQ, this.MidiTick);
            Track track = sequence.createTrack();

            int totalTick = 0;

            for (Accord chord : chords) {
                switch (chord.Rythm) {
                    case 1:
                        for (Object note : chord.getNotes())
                            addNoteToTrack(track, (Integer) note, totalTick, chord.getVelocity(), chord.getTick());
                        break;
                    case 2:
                        int note = 0;
                        for (int i = 0; i < chord.getTick(); i += 2) {
                            if (note == chord.getNotes().size()) note = 0;
                            addNoteToTrack(track, chord.getNotes().get(note), totalTick + i, chord.getVelocity(), 2);

                            note++;
                        }
                        break;
                    case 3:
                        addNoteToTrack(track, chord.getNotes().get(0), totalTick, chord.getVelocity(), chord.getTick() / 2);
                        for (int i = 1; i < chord.getNotes().size(); i++)
                            addNoteToTrack(track, chord.getNotes().get(i), totalTick + chord.getTick() / 2, chord.getVelocity(), chord.getTick() / 2);
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

    public void playNote(int note) {
        try {
            ShortMessage sm = createMidiMessage(
                    ShortMessage.NOTE_ON,
                    0,
                    note,
                    50
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
    private void addNoteToTrack(Track track, int note, int start, int velocity, int duration) {
        track.add(createMidiEvent(ShortMessage.NOTE_ON, 1, note, velocity, start));
        track.add(createMidiEvent(ShortMessage.NOTE_OFF, 1, note, velocity, start + duration));
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
    private MidiEvent createMidiEvent(int command, int channel, int data1, int data2, int tick) {
        MidiEvent midiEvent = null;
        try {
            ShortMessage msg = createMidiMessage(command, channel, data1, data2);
            midiEvent = new MidiEvent(msg, tick);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        return midiEvent;
    }

    /*
     * @RETURN new Sequence cropped
     *
     * On prend la séquence, on la fait commencer par la première note et terminer par la dernière
     * Pas de blanc à la fin et au début sauf si spécifié
     */

    public Sequence cropSequence(Sequence oldSequence, int start, int end, int instrument) {
        Sequence sequence;
        try {
            sequence = new Sequence(Sequence.PPQ, oldSequence.getResolution());
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
            return null;
        }

        Track track = sequence.createTrack();
        Track oldTrack = oldSequence.getTracks()[0];

        MidiEvent endOfTrackEvent = null;

        long oldStartTick = oldTrack.get(0).getTick();

        ShortMessage shortMessage = null;
        try {
            shortMessage = new ShortMessage(ShortMessage.PROGRAM_CHANGE, 1, instrument, 0);
            track.add(new MidiEvent(shortMessage, 0));
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < oldTrack.size(); i++) {
            MidiEvent event = oldTrack.get(i);
            byte[] midiMessage = event.getMessage().getMessage();

            event.setTick(start + (event.getTick() - oldStartTick));

            // midiMessage[0] == -1 => endOfTrack
            if (midiMessage[0] != -1)
                track.add(event);
            else
                endOfTrackEvent = event;
        }

        if (endOfTrackEvent != null)
            endOfTrackEvent.setTick(end + (endOfTrackEvent.getTick() - oldStartTick));

        track.add(endOfTrackEvent);


        File f = new File("midi  file.mid");
        try {
            MidiSystem.write(sequence, 1, f);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sequence;
    }

    public long getChordGridSize(Accord[] accords) {
        long size = 0;
        for (Accord a : accords)
            size += a.getTick();
        return size;
    }

    public Sequence setInstrument(Sequence sequence, Integer instrument) {
        Sequence newSequence = null;
        Track track = sequence.getTracks()[0];
        try {



            int instr = instrument;
            System.out.println(instrument);
            newSequence = new Sequence(Sequence.PPQ, sequence.getResolution());

            Track newTrack = newSequence.createTrack();
            ShortMessage shortMessage;
            switch ( instrument )
            {
                case 1 :
                    shortMessage = new ShortMessage(ShortMessage.PROGRAM_CHANGE, 1, 1, 0);
                    break;
                case 2 :
                    shortMessage = new ShortMessage(ShortMessage.PROGRAM_CHANGE, 1, 2, 0);
                    break;
                case 3 :
                    shortMessage = new ShortMessage(ShortMessage.PROGRAM_CHANGE, 1, 3, 0);
                    break;
                case 4 :
                    shortMessage = new ShortMessage(ShortMessage.PROGRAM_CHANGE, 1, 4, 0);
                    break;
                case 5 :
                    shortMessage = new ShortMessage(ShortMessage.PROGRAM_CHANGE, 1, 5, 0);
                    break;
                default:
                    shortMessage = new ShortMessage(ShortMessage.PROGRAM_CHANGE, 1, 0, 0);
                    break;
            }
            shortMessage = new ShortMessage(ShortMessage.PROGRAM_CHANGE, 1, 15, 0);

            newTrack.add(new MidiEvent(shortMessage, 0));

            for (int i = 0; i < track.size(); i++)
                newTrack.add(track.get(i));

            return newSequence;
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        return newSequence;
    }
}
