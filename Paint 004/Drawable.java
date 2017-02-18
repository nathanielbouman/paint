/*
 * Pain(t) final version.
 * Created by Nathaniel Bouman.
 */
package paint;

import java.awt.*;
import java.awt.image.*;

/**<h1>Drawable</h1>
 * The Drawable class consists of objects that can be drawn onto an active
 * image.
 *
 * @author nbouman
 */
abstract class Drawable {
    protected boolean selected,moved;
    
    /**
     * The draw method draws the object onto the graphics 2D object g.
     * @param g The graphics 2D object where the drawable will be drawn.
     */
    public void draw(Graphics2D g){}
    
    /**
     * The add method allows additional components to be added to a drawable
     * at the location specified by x1 and y1.
     * @param x1 The x location of the additional component.
     * @param y1 The y location of the additional component.
     */
    public void add(int x1, int y1){}
    
    /**
     * The updateLocation method changes the relative location of the drawable
     * on the active image it will be drawn on.
     * @param x1 The x position of the new location.
     * @param y1 The y position of the new location.
     */
    public void updateLocation(int x1, int y1){}
    
    /**
     * The moveStart method sets the previous location of the drawable as it is
     * moved.
     * @param x1 The initial x location.
     * @param y1 The initial y location.
     */
    public void moveStart(int x1, int y1){}
    
    /**
     * The moveEnd method stops the motion of the drawable, making its position
     * the current location.
     */
    public void moveEnd(){}
    
    /**
     * The select method is used to handle a subimage of a buffered image.
     * @param bi The buffered image the subimage is taken from.
     */
    public void select(BufferedImage bi){}
    
    /**
     * The getSelectionArea method returns the area of a selection as a
     * rectangle object.
     * @return Returns a rectangle object of the selection area.
     */
    public Rectangle getSelectionArea(){
        return new Rectangle(0,0,0,0);
    }   
}
