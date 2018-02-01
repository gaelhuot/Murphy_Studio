package Controllers;

import Models.MainModel;
import Objects.Tile;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChordMakerControllerRefactored extends Controller {

    private MainModel model;

    public ImageView crossAdd;
    public HBox tileContainer;
    public Button deleteBtn;

    private Tile selected;

    private ArrayList<Tile> tiles;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        selected = null;
        tiles = new ArrayList<>();

        deleteBtn.setVisible(false);
        tileContainer.setSpacing(5);

        crossAdd.setOnMouseClicked(this::createTile);
        initDeleteBtnEvent();
    }

    private void createTile(MouseEvent event)
    {
        // Création de la tile + composants (Rectangle, Label)
        Tile newTile = new Tile();

        Rectangle tileSquare = new Rectangle(80, 80);
        Label tileLabel = new Label("" + tiles.size());

        tileLabel.setFont(new Font(20));

        tileSquare.setFill(Color.GRAY);
        tileSquare.setStroke(Color.DARKGRAY);
        tileSquare.setStrokeWidth(5);

        newTile.getChildren().addAll(tileSquare, tileLabel);

        // Click droit
        ContextMenu rightClickContext = new ContextMenu();

        MenuItem menuItemDelete = new MenuItem("Delete");
        rightClickContext.getItems().add(menuItemDelete);

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
        System.out.println(newSelectedTile);
        if ( selected != null )
            Objects.requireNonNull(getChildrenRectangle(selected)).setStroke(Color.DARKGRAY);

        Objects.requireNonNull(getChildrenRectangle(newSelectedTile)).setStroke(Color.RED);
        selected = newSelectedTile;
    }

    private Rectangle getChildrenRectangle(Tile tile)
    {
        System.out.println(tile.getChildren());
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
}
