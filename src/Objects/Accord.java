package Objects;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Accord implements Cloneable{

    private HashMap<Integer, Character> characterHashMap = new HashMap<Integer, Character>(){{
        put(0, 'C');
        put(2, 'D');
        put(4, 'E');
        put(5, 'F');
        put(7, 'G');
        put(9, 'A');
        put(11, 'B');
    }};

    private int dominant;
    private boolean isMinor;
    private boolean isFifth;
    private boolean isSeventh;

    private Method methodCalled;

    private ArrayList<Integer> notes;

    private Character dominantName;
    private String name;

    private boolean isEmpty;
    private boolean isRandom;

    // Empty
    public Accord( )
    {
        this.dominant = 60;
        this.dominantName = '#';
        notes = new ArrayList<>();

        this.isEmpty = true;
        this.isRandom = false;
    }

    public Accord(int dominant)
    {
        this.dominant = dominant;
        dominantName = characterHashMap.get(dominant%12);
        notes = new ArrayList<>(Collections.singletonList(dominant));
    }

    public Accord(Character dominant)
    {
        Integer key = null;
        for (Map.Entry<Integer, Character> entry : characterHashMap.entrySet() )
            if ( entry.getValue() == dominant ) key = entry.getKey();

        if ( key == null )
        {
            System.err.println("Chord dominant " + dominant + " is null.");
            throw new NullPointerException();
        }

        this.dominant = key;
        this.dominantName = dominant;
        this.notes = new ArrayList<>(Collections.singletonList(key));
    }

    public Accord(int dominant, Boolean isMinor, Boolean isFifth, Boolean isSeventh) {
        this.dominant = dominant;
        dominantName = characterHashMap.get(dominant%12);
        this.isMinor = isMinor;
        this.isFifth = isFifth;
        this.isSeventh = isSeventh;

        if ( isMinor ) setMinor();
        else setMajor();

        if ( isSeventh && isFifth ) setSeventhWithFlattenedFifth();
        else
        {
            if ( isSeventh )
                setSeventh();
            if ( isFifth )
                setFifth();
        }
    }


    private ArrayList<Integer> getMajor() { return new ArrayList<>(Arrays.asList(dominant, dominant+4, dominant+7)); }

    private ArrayList<Integer> getMinor() { return new ArrayList<>(Arrays.asList(dominant, dominant+3, dominant+7)); }

    private ArrayList<Integer> getSeventh() {
        ArrayList<Integer> chord = ( isMinor ) ? getMinor() : getMajor();
        chord.add(dominant+10);
        return chord;
    }

    private ArrayList<Integer> getSeventhWithFlattenedFifth() {
        ArrayList<Integer> chord = getSeventh();
        return chord;
    }


    /* ---- GETTER ---- */

    public void setMajor()
    {
        isMinor = false;
        switchType();

        try { methodCalled = Accord.class.getMethod("setMajor"); }
        catch (NoSuchMethodException e) { e.printStackTrace(); }
    }

    public void setMinor()
    {
        isMinor = true;
        switchType();

        try { methodCalled = Accord.class.getMethod("setMinor"); }
        catch (NoSuchMethodException e) { e.printStackTrace(); }
    }

    private void switchType()
    {
        if ( isSeventh && isFifth )
            notes = getSeventhWithFlattenedFifth();
        if ( isSeventh )
            notes = getSeventh();
        else if ( isFifth )
            notes = getFifth();
        else if ( isMinor )
            notes = getMinor();
        else
            notes = getMajor();
        setName();
    }

    public void setSeventh()
    {
        notes = getSeventh();
        isSeventh = true;
        setName();
    }

    public void unsetSeventh()
    {
        int ind = notes.indexOf(dominant + 10);
        if ( ind != -1 )
            notes.remove(ind);
        isSeventh = false;
        setName();
    }

    public void setDominantSeven()
    {
        notes = getMajor();
        notes.add(dominant+10);
        name = dominantName + "7";

        try { methodCalled = Accord.class.getMethod("setDominantSeven"); }
        catch (NoSuchMethodException e) { e.printStackTrace(); }
    }

    public void setMinorSeventh()
    {
        notes = getMinor();
        notes.add(dominant+10);
        name = dominantName + "m7";

        try { methodCalled = Accord.class.getMethod("setMinorSeventh"); }
        catch (NoSuchMethodException e) { e.printStackTrace(); }
    }

    public void setMajorSeventh()
    {
        notes = getMajor();
        notes.add(dominant+11);
        name = dominantName + "maj7";

        try { methodCalled = Accord.class.getMethod("setMajorSeventh"); }
        catch (NoSuchMethodException e) { e.printStackTrace(); }
    }

    public void setDominantSeventhFlattenedFifth()
    {
        notes = new ArrayList<>(Arrays.asList(dominant, dominant+4, dominant+8, dominant+10));
        name = dominantName + "7b5";

        try { methodCalled = Accord.class.getMethod("setDominantSeventhFlattenedFifth"); }
        catch (NoSuchMethodException e) { e.printStackTrace(); }
    }

    public void setDominantSeventhSharpedFifth()
    {
        notes = new ArrayList<>(Arrays.asList(dominant, dominant+4, dominant+8, dominant+10));
        name = dominantName + "7#5";

        try { methodCalled = Accord.class.getMethod("setDominantSeventhSharpedFifth"); }
        catch (NoSuchMethodException e) { e.printStackTrace(); }
    }

    public void setSixth()
    {
        notes = getMajor();
        notes.add(dominant+9);
        name = dominantName + "6";

        try { methodCalled = Accord.class.getMethod("setSixth"); }
        catch (NoSuchMethodException e) { e.printStackTrace(); }
    }

    public void setMinorSixth()
    {
        notes = getMinor();
        notes.add(dominant+9);
        name = dominantName + "m6";

        try { methodCalled = Accord.class.getMethod("setMinorSixth"); }
        catch (NoSuchMethodException e) { e.printStackTrace(); }
    }

    public void setMinorNinth()
    {
        setMinorSeventh();
        notes.add(dominant+14);
        name = dominantName + "m9";

        try { methodCalled = Accord.class.getMethod("setMinorNinth"); }
        catch (NoSuchMethodException e) { e.printStackTrace(); }
    }

    public void setMajorNinth()
    {
        setMajorSeventh();
        notes.add(dominant+14);
        name = dominantName + "maj9";

        try { methodCalled = Accord.class.getMethod("setMajorNinth"); }
        catch (NoSuchMethodException e) { e.printStackTrace(); }
    }

    public void setDiminished()
    {
        notes = new ArrayList<>(Arrays.asList(dominant, dominant+3, dominant+6));
        name = dominantName + "dim";

        try { methodCalled = Accord.class.getMethod("setDiminished"); }
        catch (NoSuchMethodException e) { e.printStackTrace(); }
    }

    public void setDiminishedSeventh()
    {
        setDiminished();
        notes.add(dominant+9);
        name = dominantName + "dim7";

        try { methodCalled = Accord.class.getMethod("setDiminishedSeventh"); }
        catch (NoSuchMethodException e) { e.printStackTrace(); }
    }

    public void setAugmented()
    {
        notes = new ArrayList<>(Arrays.asList(dominant, dominant+3, dominant+8));
        name = dominantName + "aug";

        try { methodCalled = Accord.class.getMethod("setAugmented"); }
        catch (NoSuchMethodException e) { e.printStackTrace(); }
    }

    public void setSuspendedFourth()
    {
        notes = new ArrayList<>(Arrays.asList(dominant, dominant+5, dominant+7));
        name = dominantName + "sus4";

        try { methodCalled = Accord.class.getMethod("setSuspendedFourth"); }
        catch (NoSuchMethodException e) { e.printStackTrace(); }
    }

    public void setSuspendedSecond()
    {
        notes = new ArrayList<>(Arrays.asList(dominant, dominant+2, dominant+7));
        name = dominantName + "sus2";

        try { methodCalled = Accord.class.getMethod("setSuspendedSecond"); }
        catch (NoSuchMethodException e) { e.printStackTrace(); }
    }

    public void setFifth() {
        isFifth = true;
    }

    public void unsetFifth() {
        isFifth = false;
    }

    public void setSeventhWithFlattenedFifth()
    {
        isFifth = true;
        isSeventh = true;
    }

    public void unsetSeventhWithFlattenedFifth()
    {
        isFifth = false;
        isSeventh = false;
    }

    private void setName()
    {
        name = dominantName + "" + ((isMinor) ? "m" : "") + ((isSeventh) ? "7" : "" );
    }


    /* Getter */

    public boolean isMinor() {
        return isMinor;
    }

    public boolean isFifth() {
        return isFifth;
    }

    public boolean isSeventh() {
        return isSeventh;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getNotes() {
        return notes;
    }

    public ArrayList<Integer> getFifth() {
        return null;
    }

    public Accord getClone() {
        try { return (Accord) super.clone(); }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Character getDominantName() {
        return dominantName;
    }

    public Method getMethodCalled() {
        return methodCalled;
    }

    public Method[] getMethodList() {
        try {
            return new Method[]
            {
                Accord.class.getMethod("setMajor"),
                Accord.class.getMethod("setMinor"),
                Accord.class.getMethod("setDominantSeven"),
                Accord.class.getMethod("setMinorSeventh"),
                Accord.class.getMethod("setMajorSeventh"),
                Accord.class.getMethod("setDominantSeventhFlattenedFifth"),
                Accord.class.getMethod("setDominantSeventhSharpedFifth"),
                Accord.class.getMethod("setSixth"),
                Accord.class.getMethod("setMinorSixth"),
                Accord.class.getMethod("setMinorNinth"),
                Accord.class.getMethod("setMajorNinth"),
                Accord.class.getMethod("setDiminished"),
                Accord.class.getMethod("setDiminishedSeventh"),
                Accord.class.getMethod("setAugmented"),
                Accord.class.getMethod("setSuspendedFourth"),
                Accord.class.getMethod("setSuspendedSecond")
            };
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void setRandom()
    {
        this.isRandom = true;
        this.isEmpty = false;

        this.dominant = new int[]{0,2,4,5,7,9,11}[new Random().nextInt(7)] + 60;
        this.dominantName = characterHashMap.get(this.dominant%12);
        notes = new ArrayList<>(Collections.singletonList(dominant));

        Method[] methods = getMethodList();
        try {
            Method randomMethod = methods[new Random().nextInt(methods.length)];
            this.methodCalled = randomMethod;
            randomMethod.invoke(this);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void setRandom(int dominant)
    {
        this.isEmpty = false;
        this.isRandom = true;

        this.dominant = dominant%12 + 60;
        this.dominantName = characterHashMap.get(this.dominant%12);

        Method[] methods = getMethodList();
        try {
            Method randomMethod = methods[new Random().nextInt(methods.length + 1)];
            this.methodCalled = randomMethod;
            randomMethod.invoke(this);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public boolean isRandom() {
        return isRandom;
    }
}
