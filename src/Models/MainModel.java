package Models;

import Objects.Accord;
import Objects.Player;

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
        this.selectedChord = accord;
    }

    public Accord getSelectedChord()
    {
        return this.selectedChord;
    }
}
