package Models;

import Objects.Accord;
import Objects.Player;

import java.lang.invoke.SerializedLambda;

public class MainModel {
    public ChordModel chordModel;
    public Player player;

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
