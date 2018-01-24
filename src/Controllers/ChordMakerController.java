package Controllers;

import Models.MainModel;
import Objects.Tile;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChordMakerController extends Controller {

    private MainModel model;

    public ImageView crossAdd;
    public HBox tileContainer;
    public Button delete;

    private Rectangle selected = null;
    private int cpt = 1;

    private ArrayList<Tile> src = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tileContainer.setSpacing(5);

        delete.setVisible(false);
        crossAdd.setOnMouseClicked(event -> {
            Tile tile = new Tile();
            Rectangle accord = new Rectangle(80,80);
            Label lab = new Label(""+cpt);
            lab.setFont(new Font(20));
            cpt++;
            accord.setFill(Color.GRAY);
            accord.setStrokeWidth(5);
            accord.setStroke(Color.DARKGRAY);
            tile.getChildren().addAll(accord, lab);
            tileContainer.getChildren().add(tile);

            ContextMenu contextMenu = new ContextMenu();

            MenuItem item1 = new MenuItem("Delete");
            item1.setOnAction(MouseEvent -> {
                if(selected != null){
                    tileContainer.getChildren().remove(selected.getParent());
                    src.remove(selected);
                    System.out.println(src.size());
                    if(tileContainer.getChildren() == null) {
                        delete.setVisible(false);
                    }
                    selected = null;
                }
            });

            contextMenu.getItems().add(item1);

            tile.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {

                @Override
                public void handle(ContextMenuEvent event) {

                    contextMenu.show(tile, event.getScreenX(), event.getScreenY());
                }
            });

            delete.setVisible(true);

            event.consume();
            src.add(tile);

            tile.setOnMouseClicked(mouseEvent -> {
                mouseClik(accord);
            });


            delete.setOnMouseClicked(mouseEvent -> {
                if(selected != null){
                    tileContainer.getChildren().remove(selected.getParent());
                    src.remove(selected);
                    System.out.println(src.size());
                    if(tileContainer.getChildren() == null) {
                        delete.setVisible(false);
                    }
                    selected = null;
                }
            });

            tile.setOnDragDetected(mouseEvent -> {
                mouseClik(accord);
                accord.setStroke(Color.RED);
                final Dragboard dragBoard = tile.startDragAndDrop(TransferMode.ANY);
                dragBoard.setDragView(tile.snapshot(null, null), mouseEvent.getX(), mouseEvent.getY());

                final ClipboardContent content = new ClipboardContent();
                content.putString(lab.getText());
                dragBoard.setContent(content);
                mouseEvent.consume();
            });

            delete.setOnDragOver(dragEvent -> {
                System.out.println("onDragOver");
                if (dragEvent.getGestureSource() != delete && dragEvent.getDragboard().hasString()) {
                    dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                dragEvent.consume();
            });

            delete.setOnDragEntered((dragEvent) -> {
                System.out.println("detect enter");
                dragEvent.consume();
            });

            delete.setOnDragExited((dragEvent) -> {
                System.out.println("exited");
                dragEvent.consume();
            });

            delete.setOnDragDropped(dragEvent -> {
                tileContainer.getChildren().remove(tile);
                selected = null;
                dragEvent.consume();
            });

        });

    }

    protected void mouseClik(Rectangle accord){
        if(selected != null){
            if(selected == accord){
                accord.setStroke(Color.DARKGRAY);
                selected = null;

            }else{
                selected.setStroke(Color.DARKGRAY);
                selected = accord;
                accord.setStroke(Color.RED);

            }

        }else {
            selected = accord;
            accord.setStroke(Color.RED);
        }

    }

}
