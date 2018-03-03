package Objects;

import Controllers.ChordSorterController;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;

import java.awt.*;

/**
 * {@link DragResizer} can be used to add mouse listeners to a {@link Region}
 * and make it resizable by the user by clicking and dragging the border in the
 * same way as a window.
 * <p>
 * Only height resizing is currently implemented. Usage: <pre>DragResizer.makeResizable(myAnchorPane);</pre>
 *
 * @author atill
 *
 * Modify by us
 *
 */
public class DragResizer {

    /**
     * The margin around the control that a user can click in to start resizing
     * the region.
     */
    private static final int RESIZE_MARGIN = 12;
    private static final int MIN_H = 20;
    private static final int MAX_H = 147;
    private static final int MIN_W = 40;

    private static Rectangle region;

    private double x;
    private double y;

    private boolean initHeight;
    private static int state;

    private boolean dragging;

    private DragResizer(Rectangle aRegion) {
        region = aRegion;
    }

    public static void makeResizable(Rectangle region) {
        final DragResizer resizer = new DragResizer(region);

        region.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resizer.mousePressed(event);
            }});
        region.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resizer.mouseDragged(event);
            }});
        region.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resizer.mouseOver(event);
            }});
        region.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resizer.mouseReleased(event);
            }});
    }

    protected void mouseReleased(MouseEvent event) {
        dragging = false;
        region.setCursor(Cursor.DEFAULT);
        ChordSorterController.setTileVelocity((int)region.getHeight() - 20);
        ChordSorterController.setTileTic((int)region.getWidth());
    }

    protected void mouseOver(MouseEvent event) {
        if(isInNorthDraggableZone(event)) {
            region.setCursor(Cursor.S_RESIZE);
            state = 1;
        }
        else if(isInWestDraggableZone(event)) {
            region.setCursor(Cursor.W_RESIZE);
            state = 2;
        }
        else {
            region.setCursor(Cursor.DEFAULT);
            state = 0;
        }
    }

    protected boolean isInWestDraggableZone(MouseEvent event) {
        return event.getX() > (region.getWidth() - RESIZE_MARGIN);
    }

    protected boolean isInNorthDraggableZone(MouseEvent event) {
        return event.getY() < RESIZE_MARGIN;
    }

    protected void mouseDragged(MouseEvent event) {
        if(!dragging) {
            return;
        }

        if(state == 1){
            double mousey = event.getY();

            System.out.println(mousey);
            double newHeight = region.getHeight() + (y - mousey*2);
            if(newHeight < MIN_H){
                region.setHeight(MIN_H);
            }else if (newHeight > MAX_H){
                region.setHeight(MAX_H);
            }else {
                region.setHeight(newHeight);
            }

            y = mousey;
        }else{
            if(state == 2) {
                int mousex = (int) event.getX();
                int newWhidth = mousex;
                if(newWhidth < MIN_W){
                    region.setWidth(MIN_W);
                }else {
                    newWhidth = mousex / 40;
                    if((mousex % 40) > 20){
                        newWhidth+=1;
                    }
                    newWhidth*=40;
                    region.setWidth(newWhidth);
                }

                x = mousex;
            }
        }
    }

    protected void mousePressed(MouseEvent event) {
        // ignore clicks outside of the draggable margin
        if(!isInNorthDraggableZone(event) && !isInWestDraggableZone(event)) {
            return;
        }

        dragging = true;

        // make sure that the minimum height is set to the current height once,
        // setting a min height that is smaller than the current height will
        // have no effect

        x = event.getX();
        y = event.getY();
    }

    public static boolean getStatusResize(){
        if(state == 1 || state == 2){
            return true;
        }
        return false;
    }


}