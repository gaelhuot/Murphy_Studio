package Objects;

public class Accords {

    private int scale;          // Gamme
    private String name;        // Nom de l'accord
    private String shortName;   // Nom de l'accord (Solfège)
    private int[] notes;        // Notes

    public Accords() {}

    public Accords(String name, String  shortName, int[] notes)
    {
        this.name   = name;
        this.scale  = 0;
        this.notes  = notes;
        this.shortName = shortName;
    }

    public Accords(String name, String shortName, int scale, int[] notes)
    {
        this.name   = name;
        this.scale  = scale;
        this.notes  = notes;
        this.shortName = shortName;
    }

    /* Getters */
    public String getName() {
        return name;
    }

    public int getScale() {
        return scale;
    }

    public int[] getNotes() {
        return notes;
    }

    public String getShortName() {
        return shortName;
    }

    /* Setter */
    public void setName(String name) {
        this.name = name;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public void setNotes(int[] notes) {
        this.notes = notes;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Accords getWithScale(int scale)
    {
        Accords chord = this;
        for ( int i = 0; i < chord.notes.length; i++ )
            chord.notes[i]+=(12 * scale);
        return chord;
    }
}
