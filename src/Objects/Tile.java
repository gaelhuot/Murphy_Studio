package Objects;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class Tile extends StackPane {

    private String name;
    private int tic; //time
    private int velocity;
    public Accord accord;
    public boolean isRandom;

    public Tile() {

    }

    public Tile(String name, Pane tile) {
        this.name = name;
        this.tic = 8;
        this.velocity = 50;
    }

    public String getName(){
        return this.name;
    }

    public int getTic(){
        return this.tic;
    }

    public int getVelocity(){
        return this.velocity;
    }

    public void setTic(int tic){ this.tic = tic; }

    public void setVelocity(int velocity){ this.velocity = velocity; }


}
