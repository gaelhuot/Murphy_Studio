package Objects;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.lang.reflect.Method;

public class Tile extends StackPane {

    private String name;
    private int tic; //time
    private int velocity;

    public Accord accord;
    public Method linkedMethod;

    public Label label;
    public Rectangle rectangle;

    public boolean isRandom;

    public Tile() {
        this.accord = null;
        this.name = "";
        this.tic = 0;
        this.velocity = 50;
    }

    public Tile(String name, Pane tile) {
        this.name = name;
        this.tic = 8;
        this.velocity = 50;
    }

    public Tile(boolean random) {
        this.isRandom = random;
        this.accord = null;
        this.name = "";
        this.tic = 0;
        this.velocity = 50;
    }

    public Tile(Accord selectedChord) {
        this.accord = selectedChord;
        this.name = selectedChord.getName();
        this.tic = 8;
        this.velocity = 50;

        this.label = new Label(this.name);
        this.rectangle = new Rectangle(80, 80);

        this.label.setFont(new Font(20));

        this.rectangle.setFill(Color.GRAY);
        this.rectangle.setStroke(Color.DARKGRAY);
        this.rectangle.setStrokeWidth(5);
//        this.setAlignment(Pos.BOTTOM_CENTER);

        this.getChildren().addAll(this.rectangle, this.label);
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

    public void setName(String name) {
        this.name = name;
        this.label.setText(name);
    }

    public void setTic(int tic){ this.tic = tic; }

    public void setVelocity(int velocity){ this.velocity = velocity; }

    public void toggleRandom() {
        isRandom = !isRandom;
        this.accord = null;
        this.name = "";
        this.tic = 0;
        this.velocity = 50;
    }


}
