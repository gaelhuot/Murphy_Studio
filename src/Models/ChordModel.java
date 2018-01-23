package Models;

import java.util.ArrayList;
import Objects.Accord;
import Objects.Note;


public class ChordModel {

    public final int Do = 0;
    public final int Do_dièse = 1;
    public final int Ré_bémol = 1;
    public final int Ré = 2;
    public final int Ré_dièse = 3;
    public final int Mi_bémol = 3;
    public final int Mi = 4;
    public final int Fa = 5;
    public final int Fa_dièse = 6;
    public final int Sol_bémol = 6;
    public final int Sol = 7;
    public final int Sol_dièse = 8;
    public final int La_bémol = 8;
    public final int La = 9;
    public final int La_dièse = 10;
    public final int Si_bémol = 10;
    public final int Si = 11;

    public ArrayList<Accord> chords = new ArrayList<>();

    public ChordModel()
    {
        /*
        chords.add(new Objects.Accord("Do Majeur", "C", new int[]{0,4,7}));
        chords.add(new Objects.Accord("Ré Majeur", "D", new int[]{2,6,9}));
        chords.add(new Objects.Accord("Mi Majeur", "E", new int[]{4,8,11}));
        chords.add(new Objects.Accord("Fa Majeur", "F", new int[]{5,9,12}));
        chords.add(new Objects.Accord("Sol Majeur", "G", new int[]{7,11,14}));
        chords.add(new Objects.Accord("La Majeur", "A", new int[]{9,13,16}));
        chords.add(new Objects.Accord("Si Majeur", "B", new int[]{11,15,18}));

        chords.add(new Objects.Accord("Do Mineur", "Cm", new int[]{0,3,7}));
        chords.add(new Objects.Accord("Ré Mineur", "Dm", new int[]{2,5,9}));
        chords.add(new Objects.Accord("Mi Mineur", "Em", new int[]{4,7,11}));
        chords.add(new Objects.Accord("Fa Mineur", "Fm", new int[]{5,8,12}));
        chords.add(new Objects.Accord("Sol Mineur", "Gm", new int[]{7,10,14}));
        chords.add(new Objects.Accord("La Mineur", "Am", new int[]{9,12,16}));
        chords.add(new Objects.Accord("Si Mineur", "Bm", new int[]{11,14,18}));
        */
    }

}
