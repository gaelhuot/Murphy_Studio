package Objects;

public class Accord {

    private int scale;          // Gamme
    private String name;        // Nom de l'accord
    private String shortName;   // Nom de l'accord (Solfège)
    private int[] notes;        // Notes

    public Accord() {}

    public Accord(String name, String  shortName, int[] notes)
    {
        this.name   = name;
        this.scale  = 0;
        this.notes  = notes;
        this.shortName = shortName;
    }

    public Accord(String name, String shortName, int[] notes, int scale)
    {
        this.name   = name;
        this.scale  = scale;
        this.shortName = shortName;

        this.notes = new int[notes.length];

        for ( int i = 0; i < notes.length; i++ )
            this.notes[i] = notes[i] + (12 * scale);
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
        for ( int i = 0; i < notes.length; i++ )
            notes[i] += 12 * scale;
    }

    public void setNotes(int[] notes) {
        this.notes = notes;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Accord getWithScale(int scale)
    {
        int[] newNotes = new int[notes.length];
        for ( int i = 0; i < notes.length; i++ )
            newNotes[i] = notes[i] + (12 * scale);
        return new Accord(name, shortName + "#" + scale, newNotes);
    }

}
