/*
 * Pain(t) final version.
 * Created by Nathaniel Bouman.
 */
package paint;

import java.awt.*;
import java.awt.geom.*;

/**<h1>Free</h1>
 *The Free class is a free draw shape that extends the drawable class.
 * @author nbouman
 */
public class Free extends Drawable {
    private Color color;
    private int width;
    private GeneralPath drawing;
    
    /**
     * The constructor for the Free object
     * @param x1 The start x of the object
     * @param y1 The start y of the object
     * @param color The color of the object
     * @param width The width of the object
     */
    public Free(int x1, int y1, Color color, int width){
        this.color = color;
        this.width = width;
        
        drawing = new GeneralPath();
        drawing.moveTo(x1,y1);
    }
    
    /**
     * The add method adds an additional segment to the Free object.
     * @param x1 The x of the end of the new segment
     * @param y1 The y of the end of the new segment
     */
    public void add(int x1, int y1){
        drawing.lineTo(x1, y1);
    }
    
    @Override
    public void draw(Graphics2D g){
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(drawing);
    }
}
