package Objects;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.midi.*;

public class Bibli_midi {
    public static final int DAMPER_PEDAL = 64;
    public static final int DAMPER_ON = 127;
    public static final int DAMPER_OFF = 0;
    public static final int END_OF_TRACK = 47;
    static final int[  ] offsets = {
            // A   B  C  D  E  F  G
            -3, -1, 0, 2, 4, 5, 7 };

    ArrayList All = new ArrayList();
    public void loadMidi(String path) throws InvalidMidiDataException, IOException, MidiUnavailableException {

    }

    private void addTrack(Sequence s, int instrument, int tempo, char[  ] notes) throws InvalidMidiDataException {
        Track track = s.createTrack( );  // Begin with a new track

        ShortMessage sm = new ShortMessage( );
        sm.setMessage(ShortMessage.PROGRAM_CHANGE, 0, instrument, 0);
        track.add(new MidiEvent(sm, 0));

        int n = 0;
        int t = 0;

        int notelength = 16;
        int velocity = 64;
        int basekey = 60;
        boolean sustain = false;
        int numnotes = 0;

        while(n < notes.length) {
            char c = notes[n++];

            if (c == '+') basekey += 12;
            else if (c == '-') basekey -= 12;
            else if (c == '>') velocity += 16;
            else if (c == '<') velocity -= 16;
            else if (c == '/') {
                char d = notes[n++];
                if (d == '2') notelength = 32;
                else if (d == '4') notelength = 16;
                else if (d == '8') notelength = 8;
                else if (d == '3' && notes[n++] == '2') notelength = 2;
                else if (d == '6' && notes[n++] == '4') notelength = 1;
                else if (d == '1') {
                    if (n < notes.length && notes[n] == '6')
                        notelength = 4;
                    else notelength = 64;
                }
            }
            else if (c == 's') {
                sustain = !sustain;
                ShortMessage m = new ShortMessage( );
                m.setMessage(ShortMessage.CONTROL_CHANGE, 0,
                        DAMPER_PEDAL, sustain?DAMPER_ON:DAMPER_OFF);
                track.add(new MidiEvent(m, t));
            }
            else if (c >= 'A' && c <= 'G') {
                int key = basekey + offsets[c - 'A'];
                if (n < notes.length) {
                    if (notes[n] == 'b') {
                        key--;
                        n++;
                    }
                    else if (notes[n] == '#') {
                        key++;
                        n++;
                    }
                }

                addNote(track, t, notelength, key, velocity);
                numnotes++;
            }
            else if (c == ' ') {
                if (numnotes > 0) {
                    t += notelength;
                    numnotes = 0;
                }
            }
            else if (c == '.') {
                if (numnotes > 0) {
                    t += notelength;
                    numnotes = 0;
                }
                t += notelength;
            }
        }
    }


    public static void addNote(Track track, int startTick,
                               int tickLength, int key, int velocity)
            throws InvalidMidiDataException {

        ShortMessage on = new ShortMessage( );
        on.setMessage(ShortMessage.NOTE_ON,  0, key, velocity);
        ShortMessage off = new ShortMessage( );
        off.setMessage(ShortMessage.NOTE_OFF, 0, key, velocity);
        track.add(new MidiEvent(on, startTick));
        track.add(new MidiEvent(off, startTick + tickLength));
    } // end addNote method

    private void addChord(){

    }

    private void addSample(){
        ArrayList sample = new ArrayList();
    }

    private void isChord(){

    }
}
