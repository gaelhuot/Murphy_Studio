package Controllers;

import Models.MainModel;
import Objects.Accord;
import Objects.Tile;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import javax.sound.midi.InvalidMidiDataException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChordSorterController extends Controller {

    private MainModel model;

    public ImageView crossAdd;
    public HBox tileContainer;

    public Button deleteBtn;
    public Button togglePlayTrack;

    private Tile selected;

    private ArrayList<Tile> tiles;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        selected = null;
        tiles = new ArrayList<>();

        deleteBtn.setVisible(false);
        tileContainer.setSpacing(5);

        initDeleteBtnEvent();
    }

    public void createTile(MouseEvent event)
    {
        // Création de la tile + composants (Rectangle, Label)
        Tile newTile = new Tile(model.getSelectedChord());



        // Click droit
        ContextMenu rightClickContext = new ContextMenu();

        MenuItem menuItemDelete = new MenuItem("Delete");
        MenuItem menuItemSetRandom = new MenuItem("Set Random");
        SeparatorMenuItem seperator = new SeparatorMenuItem();
        MenuItem menuItemRythm1 = new MenuItem("Rythme 1");
        MenuItem menuItemRythm2 = new MenuItem("Rythme 2");
        MenuItem menuItemRythm3 = new MenuItem("Rythme 3");

        rightClickContext.setStyle("-fx-background: #fff");

        rightClickContext.getItems().addAll(menuItemDelete, menuItemSetRandom, seperator, menuItemRythm1, menuItemRythm2, menuItemRythm3);

        menuItemDelete.setOnAction(MouseEvent -> {
            if ( selected == null ) return;

            tileContainer.getChildren().remove(selected);
            tiles.remove(selected);

            if ( tileContainer.getChildren() == null )
                deleteBtn.setVisible(false);

            selected = null;
        });

        // On affiche le Context Menu
        newTile.setOnContextMenuRequested(contextMenuEvent -> rightClickContext.show(newTile, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY()));

        tiles.add(newTile);
        tileContainer.getChildren().add(newTile);

        initEventHandler(newTile);
        deleteBtn.setVisible(true);

        event.consume();
    }

    private void initEventHandler(Tile newTile)
    {
        newTile.setOnMouseClicked(event -> setSelected(newTile));

        /* --- <Drag and Drop> --- */
        newTile.setOnDragDetected(event -> {
            setSelected(newTile);

            final Dragboard dragboard = newTile.startDragAndDrop(TransferMode.ANY);
            dragboard.setDragView(newTile.snapshot(null, null), event.getX(), event.getY());

            final ClipboardContent content = new ClipboardContent();
            content.putString( Objects.requireNonNull(getChildrenLabel(newTile)).getText() );
            dragboard.setContent(content);

            event.consume();
        });
        /* --- </Drag and Drop> --- */


    }

    private void initDeleteBtnEvent()
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

    }

    private void deleteSelected()
    {
        tileContainer.getChildren().remove(selected);
        tiles.remove(selected);

        if ( tiles.size() == 0 )
            deleteBtn.setVisible(false);

        selected = null;
    }

    private void setSelected(Tile newSelectedTile)
    {
        if ( selected != null )
            Objects.requireNonNull(getChildrenRectangle(selected)).setStroke(Color.DARKGRAY);

        Objects.requireNonNull(getChildrenRectangle(newSelectedTile)).setStroke(Color.RED);
        selected = newSelectedTile;

        if ( ! newSelectedTile.isRandom )
        {
            model.setSelectedChord(newSelectedTile.accord);
            model.chordMakerController.setSelected();
        }
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
            // Si le sequencer tourne déjà
            if ( model.player.sequencer.isRunning() )
            {
                togglePlayTrack.setText("Play");
                model.player.sequencer.stop();
            }
            // Sinon on crée les accords
            else
            {
                togglePlayTrack.setText("Pause");
                Accord[] accords = new Accord[tiles.size()];

                for ( int i = 0; i < tiles.size(); i++ )
                {
                    accords[i] = tiles.get(i).accord;
                    System.out.println(tiles.get(i).getName());
                }
                try {
                    model.player.createTrackFromChords(accords);
                } catch (InvalidMidiDataException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void changeSelectedTileChord(Accord accord)
    {
        if ( selected == null ) return;
        selected.accord = accord.getClone();
        selected.setName(accord.getName());
    }


    public void setModel(MainModel model) {
        this.model = model;
        crossAdd.setOnMouseClicked(this::createTile);

        initTrackBtn();
    }


}
