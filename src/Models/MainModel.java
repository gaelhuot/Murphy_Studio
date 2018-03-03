package Models;

import Controllers.ChordMakerController;
import Controllers.ChordSorterController;
import Controllers.PisteController;
import Controllers.PisteLayoutController;
import Objects.*;

import javax.sound.midi.Track;
import java.util.ArrayList;
import java.util.HashMap;

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

    public int sink = -1;

    public MainModel()
    {
        selectedChord = null;

        this.applicationTracks = new ArrayList<>();
        this.applicationPistes = new ArrayList<>();

        this.pisteToTracks = new HashMap<>();
    }

    public void setPisteToTracks(Track track, PisteController piste)
    {
        if ( this.pisteToTracks.get(piste) != null )
        {
            System.out.println("Piste déjà associé à une track");
            return;
        }

        pisteToTracks.put(piste, track);

        piste.setTrack(track);
    }

    public Accord getSelectedChord()
    {
        return this.selectedChord.getClone();
    }
}
