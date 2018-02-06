package Models;

import Controllers.ChordMakerController;
import Controllers.ChordSorterController;
import Controllers.PisteLayoutController;
import Objects.Accord;
import Objects.Player;
import Objects.Tile;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class MainModel {
    public ChordModel chordModel;
    public Player player;

    public ChordSorterController chordSorterController;
    public ChordMakerController chordMakerController;
    public PisteLayoutController pisteLayoutController;



    private Accord selectedChord;

    public MainModel()
    {
        selectedChord = null;
    }

    public void setSelectedChord(Accord accord)
    {
        this.selectedChord = accord.getClone();

    }

    public Accord getSelectedChord()
    {
        return this.selectedChord.getClone();
    }

}
