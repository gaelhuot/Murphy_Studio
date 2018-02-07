package Models;

import Controllers.ChordMakerController;
import Controllers.ChordSorterController;
import Controllers.PisteController;
import Controllers.PisteLayoutController;
import Objects.Accord;
import Objects.Player;
import Objects.Tile;

import javax.sound.midi.Track;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class MainModel {
    public ChordModel chordModel;
    public Player player;

    public ChordSorterController chordSorterController;
    public ChordMakerController chordMakerController;
    public PisteLayoutController pisteLayoutController;

    // Contient toutes les tracks
    public ArrayList<Track> applicationTracks;
    // Contient toute les "pistes"
    public ArrayList<PisteController> applicationPistes;

    // Permet de lier les tracks et les pistes
    public HashMap<PisteController, Track> pisteToTracks;



    public Accord selectedChord;
    public Tile selectedTile;

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
