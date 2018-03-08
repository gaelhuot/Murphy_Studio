package Models;

import Controllers.ChordMakerController;
import Controllers.ChordSorterController;
import Controllers.PisteController;
import Controllers.PisteLayoutController;
import Objects.*;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Track;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainModel {
    public ChordModel chordModel;

    public ChordSorterController chordSorterController;
    public ChordMakerController chordMakerController;
    public PisteLayoutController pisteLayoutController;

    // Contient toutes les tracks
    public ArrayList<Track> applicationTracks;
    // Contient toute les "pistes"
    public ArrayList<PisteController> applicationPistes;

    // Permet de lier les tracks et les pistes
    public HashMap<PisteController, Track> pisteToTracks;


    public ExternInterface mainExternInterface;
    public MidiInterface midiInterface;

    public Accord selectedChord;
    public Tile selectedTile;

    public ArrayList<Integer> sink = new ArrayList<>();
    public LinkedHashMap<String, Integer> intrumentsMIDI;

    public MainModel()
    {
        selectedChord = null;

        this.applicationTracks = new ArrayList<>();
        this.applicationPistes = new ArrayList<>();


        this.intrumentsMIDI = new LinkedHashMap<String, Integer>();
        this.intrumentsMIDI.put("Piano",0);
        this.intrumentsMIDI.put("Guitar",25);
        this.intrumentsMIDI.put("Bass",33);
        this.intrumentsMIDI.put("Choir",52);
        this.intrumentsMIDI.put("Trumpet",56);
        this.intrumentsMIDI.put("Sax",64);
        this.intrumentsMIDI.put("Flute",73);
        this.intrumentsMIDI.put("Drum",114);
    }

    public Accord getSelectedChord()
    {
        return this.selectedChord.getClone();
    }
}
