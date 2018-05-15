package murphystudio.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import murphystudio.models.MainModel;
import murphystudio.objects.Accord;
import murphystudio.objects.DragResizer;
import murphystudio.objects.Tile;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequencer;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChordSorterController extends Controller {

    public Button emptyButton;
    public Button randomButton;
    public Button deleteBtn;
    public Button togglePlayTrack;
    public Button addToTimeline;
    public ChoiceBox<PisteController> timelineSelector;

    public AnchorPane chordSorterPane;
    private MainModel model;

    public ImageView crossAdd;
    public HBox tileContainer;
    public ListView<Object> tileList;

    private static Tile selected;

    private ArrayList<Tile> tiles;

    public boolean isRandomTile = false;
    public boolean isEmptyTile = false;
    private boolean pianoVisible;

    @FXML
    private Pane notePane1,notePane2,notePane3,notePane4,notePane5,notePane6,notePane7,notePane8,notePane9,notePane10,notePane11,notePane12,notePane13,notePane14,notePane15,notePane16,notePane17,notePane18,notePane19,notePane20,notePane21,notePane22,notePane23,notePane24,notePane25,notePane26,notePane27,notePane28,notePane29,notePane30,notePane31,notePane32,notePane33,notePane34,notePane35,notePane36;
    public Pane piano;
    public Pane[] notesPane;

    private Sequencer sequencer;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        selected = null;
        tiles = new ArrayList<>();

        deleteBtn.setVisible(false);
        tileContainer.setSpacing(5);

        notesPane = new Pane[]{notePane1,notePane2,notePane3,notePane4,notePane5,notePane6,notePane7,notePane8,notePane9,notePane10,notePane11,notePane12,notePane13,notePane14,notePane15,notePane16,notePane17,notePane18,notePane19,notePane20,notePane21,notePane22,notePane23,notePane24,notePane25,notePane26,notePane27,notePane28,notePane29,notePane30,notePane31,notePane32,notePane33,notePane34,notePane35,notePane36};

        initBtnsEvent();

        chordSorterPane.heightProperty().addListener((obs, oldVal, newVal) ->
                checkPianoSize());

        chordSorterPane.widthProperty().addListener((obs, oldVal, newVal) ->
                checkPianoSize());

        checkPianoSize();
    }

    private void checkPianoSize()
    {
        if (this.chordSorterPane.heightProperty().getValue() < this.chordSorterPane.getPrefHeight() - 50
                || this.chordSorterPane.widthProperty().getValue() < this.chordSorterPane.getPrefWidth())
        {
            if(pianoVisible)
            {
                piano.setManaged(false); piano.setVisible(false);
                pianoVisible = false;
            }
        }
        else
        {
            if(!pianoVisible)
            {
                piano.setManaged(true); piano.setVisible(true);
                pianoVisible = true;
            }
        }
    }

    public void resetKeys()
    {
        for (Pane aNotesPane : notesPane) aNotesPane.setStyle(null);
    }

    public void colorizeKeys()
    {
        ArrayList<Integer> notes = model.selectedChord.getNotes();
        for (Object note : notes) {
            int notePaneIndex = (int) note - 60;
            notesPane[notePaneIndex].setStyle("-fx-background-color: red");
        }
    }

    public void createTile()
    {
        // Création de la tile + composants (Rectangle, Label)
        Accord tmpAccord = new Accord();

        if ( ! isRandomTile && ! isEmptyTile ) tmpAccord = model.getSelectedChord();
        else if ( isRandomTile ) tmpAccord.setRandom();

        Tile newTile = new Tile(tmpAccord);

        isRandomTile = isEmptyTile = false;


        // Click droit
        ContextMenu rightClickContext = new ContextMenu();

        MenuItem menuItemDelete = new MenuItem("Delete");
        MenuItem menuItemSetRandom = new MenuItem("Set Random");
        SeparatorMenuItem seperator = new SeparatorMenuItem();
        MenuItem menuItemRythm1 = new MenuItem("Rythme 1");
        MenuItem menuItemRythm2 = new MenuItem("Rythme 2");
        MenuItem menuItemRythm3 = new MenuItem("Rythme 3");

        rightClickContext.getStyleClass().add("background");

        rightClickContext.getItems().addAll(menuItemDelete, menuItemSetRandom, seperator, menuItemRythm1, menuItemRythm2, menuItemRythm3);

        menuItemDelete.setOnAction(MouseEvent -> deleteSelected());
        menuItemRythm1.setOnAction(MouseEvent -> selected.accord.Rythm = 1);
        menuItemRythm2.setOnAction(MouseEvent -> selected.accord.Rythm = 2);
        menuItemRythm3.setOnAction(MouseEvent -> selected.accord.Rythm = 3);

        // On affiche le Context Menu
        newTile.setOnContextMenuRequested(contextMenuEvent -> rightClickContext.show(newTile, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY()));

        tileList.getItems().add(newTile);

        tiles.add(newTile);
        initEventHandler(newTile);
        setSelected(newTile);
        deleteBtn.setVisible(true);
    }

    private void initEventHandler(Tile newTile)
    {
        newTile.setOnMouseClicked(event -> setSelected(newTile));

        /* --- <Drag and Drop> --- */
        newTile.setOnDragDetected(event -> {
            if(DragResizer.getStatusResize()) {
                return;
            }
            setSelected(newTile);

            final Dragboard dragboard = newTile.startDragAndDrop(TransferMode.ANY);
            dragboard.setDragView(newTile.snapshot(null, null), event.getX(), event.getY());

            final ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(tileList.getItems().indexOf(newTile)));
            dragboard.setContent(content);

            event.consume();
        });

        newTile.setOnDragOver(event -> {
            if (event.getGestureSource() != newTile && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        newTile.setOnDragEntered(event -> {
            if (event.getGestureSource() != newTile && event.getDragboard().hasString()) {
                newTile.setOpacity(0.3);
            }
        });

        newTile.setOnDragExited(event -> {
            if (event.getGestureSource() != newTile && event.getDragboard().hasString()) {
                newTile.setOpacity(1);
            }
        });

        newTile.setOnDragDropped(event -> {

            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                System.out.println();
                int draggedIdx = Integer.parseInt(db.getString());
                int thisIdx = tileList.getItems().indexOf(newTile);

                tiles.remove(draggedIdx);
                tiles.add(thisIdx, selected);

                tileList.getItems().remove(draggedIdx);
                tileList.getItems().add(thisIdx, event.getGestureSource());
                success = true;
            }
            event.setDropCompleted(success);

            event.consume();
        });

        newTile.setOnDragDone(DragEvent::consume);
        /* --- </Drag and Drop> --- */


    }

    private void initBtnsEvent()
    {
        deleteBtn.setOnMouseClicked(event -> {
            if ( selected == null ) return;
            deleteSelected();
        });

        /* --- <Drag and Drop> --- */

        deleteBtn.setOnDragOver(event -> {
            if (event.getGestureSource() != deleteBtn && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        deleteBtn.setOnDragEntered(Event::consume);

        deleteBtn.setOnDragExited(Event::consume);

        deleteBtn.setOnDragDropped(event -> {
            deleteSelected();
            event.consume();
        });

        /* --- </Drag and Drop> --- */

        emptyButton.setOnMouseClicked(event -> {
            isEmptyTile = true;
            createTile();
        });

        randomButton.setOnMouseClicked(event -> {
            isRandomTile = true;
            createTile();
        });

        addToTimeline.setOnMouseClicked(event -> {
            PisteController piste = timelineSelector.getSelectionModel().getSelectedItem();
            if (piste != null){
                addChordsToTimeline(piste);
            }
        });
    }

    public void deleteSelected()
    {
        //tileContainer.getChildren().remove(selected);
        tiles.remove(selected);
        tileList.getItems().remove(selected);
        selected = null;
        if ( tiles.size() == 0 ) {
            deleteBtn.setVisible(false);
        }else {
            setSelected(tiles.get(tiles.size() - 1));
        }

    }

    private void setSelected(Tile newSelectedTile)
    {
//        newSelectedTile.rectangle.setStroke(Color.RED);
        selected = newSelectedTile;

        model.selectedTile = newSelectedTile;
        model.chordMakerController.updateFromTile(newSelectedTile);

        DragResizer.makeResizable(selected.rectangle);
    }

    private Rectangle getChildrenRectangle(Tile tile)
    {
        for ( Node n : tile.getChildren() )
            if ( n instanceof Rectangle ) return (Rectangle) n;
        return null;
    }

    private Label getChildrenLabel(Tile tile)
    {
        for ( Node n : tile.getChildren() )
            if ( n instanceof Label ) return (Label) n;
        return null;
    }

    private void initTrackBtn()
    {
        togglePlayTrack.setOnMouseClicked(event -> {
           setSequence();
        });
    }

    private void setSequence()
    {
        // Si le sequencer tourne déjà
        if ( this.sequencer.isRunning() )
        {
            togglePlayTrack.setText("Play");
            this.sequencer.stop();
        }
        // Sinon on crée les accords
        else
        {
            togglePlayTrack.setText("Pause");

            try {
                this.sequencer.setSequence(this.model.midiInterface.createTrackFromChords(getAccords()));

                this.sequencer.setTickPosition(0);

                this.sequencer.setTempoInBPM(model.midiInterface.tempo);
                this.sequencer.setLoopCount(1000);

                this.sequencer.start();
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            }

        }
    }

    public void changeSelectedTileChord(Accord accord)
    {
        if ( selected == null ) return;
        selected.accord = accord.getClone();
        selected.setName(accord.getName());
    }

    public int getSelectedIndex()
    {
        for ( int i = 0; i < tiles.size(); i++ )
            if ( tiles.get(i) == selected ) return i;
        return -1;
    }

    public static void setTileVelocity(int velocity){
        selected.setVelocity(velocity);
    }

    public static void setTileTic(int tic){
        selected.setTic(tic/10);
    }

    public void nextTile()
    {
        int index = getSelectedIndex();
        if ( index == tiles.size()-1 ) return;
        setSelected(tiles.get(index));
    }

    public void previousTile()
    {
        int index = getSelectedIndex();
        if ( index == 0 ) return;
        setSelected(tiles.get(index));
    }


    public void setModel(MainModel model) {
        this.model = model;
        crossAdd.setOnMouseClicked(event -> createTile());

        initTrackBtn();

        this.sequencer = model.midiInterface.getSequencer();

    }

    void addPisteToList(PisteController piste){
        this.timelineSelector.getItems().add(piste);
        if(this.timelineSelector.getItems().size() == 1){
            this.timelineSelector.getSelectionModel().select(0);
        }
    }

    public void removePisteFromList(PisteController piste){
        this.timelineSelector.getItems().remove(piste);
        if (this.timelineSelector.getItems().size() != 0 && this.timelineSelector.getSelectionModel().isEmpty()){
            this.timelineSelector.getSelectionModel().select(0);
        }
    }

    private Accord[] getAccords()
    {
        Accord[] accords = new Accord[tiles.size()];

        for ( int i = 0; i < tiles.size(); i++ )
            accords[i] = tiles.get(i).accord;
        return accords;
    }

    public void addChordsToTimeline(PisteController piste){
        piste.addSequence(this.model.midiInterface.createTrackFromChords(this.getAccords()));
        piste.addChords(this.model.midiInterface.getChordGridSize(this.getAccords()));
    }

}
