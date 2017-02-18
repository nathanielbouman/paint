/*
 * Pain(t) final version.
 * Created by Nathaniel Bouman.
 */
package paint;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.util.ArrayList;
import java.util.Stack;

/**<h1>Active Image</h1>
 * The ActiveImage class contains an image that can be edited with various
 * drawing functions.
 *
 * @author nbouman
 */
public class ActiveImage extends JComponent {

    private BufferedImage img;//the BufferedImage object to be displayed.
    private String filePath;//fhe filepath of the image
    private GuiPaint gui;//The parent gui
    private Boolean drawing = false;//Is user currently drawing something?

    /**
     *currentShape is the drawable object currently being drawn by the user.
     */
    protected Drawable currentShape;//The preview before the mouse is released.
    private int startX = 0;//Values related to drawing shapes
    private int startY = 0;
    private int endX = 0;
    private int endY = 0;
    private int prevX = 0;
    private int prevY = 0;
    private int lineWidth = 1;//Width for drawing lines
    private int freeWidth = 1;//Width for freedraw
    private int button = MouseEvent.NOBUTTON;//Which button initiated the drawing
    private Graphics2D g2;//graphics on the image
    private MyMouseListener myMouseListener = new MyMouseListener();//The mouse listener

    private Color color = Color.WHITE;//The color the object is drawn with.

    
    @Override
    public void paint(Graphics g) {//the image drawn.
        g.drawImage(img, 0, 0, null);
        if (currentShape != null){//The shape being drawn (preview)
            currentShape.draw((Graphics2D) g);
        }
    }

    /**
     *The constructor for the active image.
     * @param gui The gui object where the ActiveImage will be used.
     */
    public ActiveImage(GuiPaint gui) {//Defaut constructor - no image yet.
        img = null;
        this.gui = gui;
        addMouseListener(myMouseListener);
        addMouseMotionListener(myMouseListener);
    }

    /**
     *The readImage method gets the image to use for the active image from a
     * file.
     * @param file The file object containing the path of the image to use.
     */
    public void readImage(File file) {//get the BufferedImage from file. Catch exceptions.
        try {
            img = ImageIO.read(file);
            g2 = img.createGraphics();//for drawing onto the image
            filePath = file.getPath();
        } catch (IOException e) {
            img = null;
        }
        repaint();
    }

    /**
     *The setImage method gets the image to use for the active image from a
     * buffered image.
     * @param img The buffered image object of the image to use.
     */
    public void setImage(BufferedImage img) {
        this.img = img;
        g2 = img.createGraphics();
        repaint();
    }

    /**
     *The closeImage method sets the image used by the active image to null.
     */
    public void closeImage() {//Return the BufferedImage to null, effectively closing the image.
        img = null;
    }

    /**
     * The getImage method returns the buffered image object used by the active
     * image.
     * @return Returns the buffered image object used by the active image.
     */
    public BufferedImage getImage() {//Return the BufferedImage
        return img;
    }

    /**
     *The deepGetImage method returns a deep copy of the buffered image object
     * used by the active image.
     * @return Returns a deep copy of buffered image object used by the active
     * image.
     */
    public BufferedImage deepGetImage() {//Return a copy of the BufferedImage
        return deepCopy(img);
    }

    private BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    /**
     *The getPath method returns a string of the file path used to originally
     * create the image used by the active image.
     * @return Returns a string containing the file path used to originally
     * create the image used by the active image.
     */
    public String getPath() {//Return the filepath
        return filePath;
    }

    private class MyMouseListener extends MouseInputAdapter {//Inner mouse listener class (makes things easier).

        public void mousePressed(MouseEvent e) {//Start drawing if mouse is pressed
            if ((drawing == false) && (e.getButton() != MouseEvent.BUTTON2) && (img != null)) {
                drawing = true;
                startX = e.getX();
                startY = e.getY();
                prevX = e.getX();
                prevY = e.getY();
                endX = e.getX();
                endY = e.getY();
                button = e.getButton();
                if ((currentShape instanceof Selection) && (currentShape.selected == true)) {
                    if (currentShape.getSelectionArea().contains(startX, startY)) {
                        currentShape.moveStart(startX, startY);
                    } else //if(currentShape.moved==true)
                    {
                        addShape();
                    }
                }
                updateShape();
            }

        }

        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == button && drawing == true) {//End drawing if mouse is released
                drawing = false;
                endX = e.getX();
                endY = e.getY();
                if (currentShape instanceof Selection) {
                    if (endX != startX && endY != startY) {
                        if (currentShape.selected == false) {
                            currentShape.select(img);
                        }
                    } else {
                        currentShape = null;
                    }

                } else {
                    if (currentShape != null) {
                        addShape();
                    }
                    currentShape = null;//no more preview.
                    repaint();
                }

            }
        }

        public void mouseDragged(MouseEvent e) {//Update preview while mouse is pressed
            if (drawing == true) {
                prevX = endX;
                prevY = endY;
                endX = e.getX();
                endY = e.getY();
                updateShape();
            }
        }

    }

    /**
     *The addShape method draws the current shape onto the image.
     */
    public void addShape() {//draw the shape onto the image.
        gui.undoStack.push(deepGetImage());
        gui.setUndo(true);
        gui.setRedo(false);
        gui.redoStack = new Stack();
        currentShape.draw(g2);
        currentShape = null;
        gui.saved = false;
        gui.updateTitle();

    }

    /**
     *The undoPush method pushes the current image onto the gui's undo stack.
     */
    public void undoPush() {
        gui.undoStack.push(deepGetImage());
    }

    /**
     *The redoPush method pushes the current image onto the gui's redo stack.
     */
    public void redoPush() {
        gui.redoStack.push(deepGetImage());
    }

    private void updateShape() {//Update the shape preview
        if (button == MouseEvent.BUTTON3) {
            color = gui.drawingPanel.getRightColor();
        } else if (button == MouseEvent.BUTTON1) {
            color = gui.drawingPanel.getLeftColor();
        }
        switch (gui.drawingPanel.getShape()) {
            case 0://Line
                lineWidth = gui.drawingPanel.getLineWidth();
                currentShape = new Line(startX, startY, endX, endY, color, lineWidth);
                break;
                
            case 3://free draw
                freeWidth = gui.drawingPanel.getFreeWidth();
                if (currentShape instanceof Free) {
                    currentShape.add(endX, endY);
                } else {
                    currentShape = new Free(startX, startY, color, freeWidth);
                }
                gui.saved = false;
                break;
                
            case 4://selection
                if (currentShape instanceof Selection) {
                    currentShape.updateLocation(endX, endY);
                } else {
                    currentShape = new Selection(startX, startY, endX, endY);
                }
                break;
        }
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {//Size the image would like to take up
        if (img == null) {
            return new Dimension(800, 800);
        } else {
            return new Dimension(img.getWidth(null), img.getHeight(null));
        }
    }
}
