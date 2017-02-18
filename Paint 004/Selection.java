/*
 * Pain(t) final version.
 * Created by Nathaniel Bouman.
 */
package paint;

import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;

/**<h1>Selection</h1>
 * The selection class is a subimage of a buffered image (typically, the active
 * image) that can be moved an placed on the active image; extends drawable.
 * @author nbouman
 */
public class Selection extends Drawable {

    private int x1, x2, y1, y2, width, length, x0, y0;
    private BufferedImage img = null;
    private Rectangle blank;

    /**
     * The constructor for the Selection object.
     * @param x1 The first x bound of the selection.
     * @param y1 The first y bound of the selection.
     * @param x2 The second x bound of the selection.
     * @param y2 The second y bound of the selection.
     */
    public Selection(int x1, int y1, int x2, int y2) {
        moved = false;
        selected = false;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }
    
    /**
     * The updateLocation updates the second bound coordinates while selection
     * is in progress, and updates the location of the subimage on the
     * active image after the selection has been made.
     * @param x2 The x coordinate of the updated location.
     * @param y2 The y coordinate of the updated location.
     */
    public void updateLocation(int x2, int y2) {
        if (selected == false) {
            this.x2 = x2;
            this.y2 = y2;
        } else {
            moved = true;
            x0 += x2 - this.x2;
            y0 += y2 - this.y2;
            this.x2 = x2;
            this.y2 = y2;
        }
    }

    public void select(BufferedImage bi) {
        blank = new Rectangle(x0, y0, width, length);
        WritableRaster data = ((WritableRaster) bi.getData(new Rectangle(x0, y0, width, length))).createWritableTranslatedChild(0, 0);
        img = new BufferedImage(bi.getColorModel(), data, bi.isAlphaPremultiplied(), null);
        selected = true;

    }

    /**
     * The move start method makes the selection ready to move by collecting
     * the coordinates of the initial position of the selection.
     * @param x1 The initial x position of the selection.
     * @param y1 The initial y position of the selection.
     */
    public void moveStart(int x1, int y1) {
        this.x2 = x1;
        this.y2 = y1;
    }

    /**
     * The getSelectedArea method returns a rectangle object at the selected
     * area; if the selection has moved, this is where it was originally.
     * @return Returns a rectangle object with coordinates of the original
     * location of the selected area.
     */
    public Rectangle getSelectedArea() {
        return blank;
    }

    /**
     * The getSelectionArea method returns a rectangle object where the
     * selection is currently.
     * @return Returns a rectangle object with coordinates at the current
     * location of the selection.
     */
    public Rectangle getSelectionArea() {
        return new Rectangle(x0, y0, length, width);
    }

    /**
     * The moveEnd method stops the movement of the selection.
     */
    public void moveEnd() {
        x0 += x2 - x1;
        y0 += y2 - y1;
        System.out.println(length + ", " + width);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        if (selected == false) {
            g.setStroke(new BasicStroke());
            
            if (x2 > x1) {
                width = x2 - x1;
                x0 = x1;
            } else {
                width = x1 - x2;
                x0 = x2;
            }
            
            if (y2 > y1) {
                length = y2 - y1;
                y0 = y1;
            } else {
                length = y1 - y2;
                y0 = y2;
            }
            
            g.draw(new Rectangle2D.Double(x0, y0, width, length));
        } else {
            g.fill((Rectangle2D) blank);
            g.drawImage(img, x0, y0, null);
        }
    }
}
