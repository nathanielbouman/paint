/*
 * Pain(t) final version.
 * Created by Nathaniel Bouman.
 */
package paint;

import java.awt.*;

/**<h1>Line</h1>
 *The Line class is a line shape that extends the drawable class.
 * @author nbouman
 */
public class Line extends Drawable {
    private int x1, x2, y1, y2, width;
    private Color color;
    
    
    /**
     * Constructor for the line class.
     * @param x1 The start x of the line
     * @param y1 The start y of the line
     * @param x2 The end x of the line
     * @param y2 The end y of the line
     * @param color The color of the line
     * @param width The width of the line
     */
    public Line(int x1, int y1, int x2, int y2, Color color, int width){
        this.x1=x1;
        this.x2=x2;
        this.y1=y1;
        this.y2=y2;
        this.color=color;
        this.width=width;
    }
    
    @Override
    public void draw(Graphics2D g){
        g.setColor(color);
        g.setStroke(new BasicStroke(width));
        g.drawLine(x1, y1, x2, y2);
    }
}
