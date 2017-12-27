package Models;

import Objects.Accords;

import java.util.ArrayList;
import java.util.Objects;

public class ChordModel {

    public ArrayList<Accords> chords = new ArrayList<>();

    public ChordModel()
    {
        chords.add(new Accords("Do Majeur", "C", new int[]{0,4,7}));
        chords.add(new Accords("Ré Majeur", "D", new int[]{2,6,9}));
        chords.add(new Accords("Mi Majeur", "E", new int[]{4,8,11}));
        chords.add(new Accords("Fa Majeur", "F", new int[]{5,9,12}));
        chords.add(new Accords("Sol Majeur", "G", new int[]{7,11,14}));
        chords.add(new Accords("La Majeur", "A", new int[]{9,13,16}));
        chords.add(new Accords("Si Majeur", "B", new int[]{11,15,18}));

        chords.add(new Accords("Do Mineur", "Cm", new int[]{0,3,7}));
        chords.add(new Accords("Ré Mineur", "Dm", new int[]{2,5,9}));
        chords.add(new Accords("Mi Mineur", "Em", new int[]{4,7,11}));
        chords.add(new Accords("Fa Mineur", "Fm", new int[]{5,8,12}));
        chords.add(new Accords("Sol Mineur", "Gm", new int[]{7,10,14}));
        chords.add(new Accords("La Mineur", "Am", new int[]{9,12,16}));
        chords.add(new Accords("Si Mineur", "Bm", new int[]{11,14,18}));
    }

    public Accords getChord(String shortName)
    {
        for (Accords chord : chords) if (Objects.equals(chord.getShortName(), shortName)) return chord;
        return null;
    }

    Accords getChordScale(String shortName, int scale)
    {
        Accords chord = getChord(shortName);
        return ( chord == null ) ? null : chord.getWithScale(scale);
    }

}
