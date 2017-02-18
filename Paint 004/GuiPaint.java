/*
 * Paint final version.
 * Created by Nathaniel Bouman.
 */
package paint;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.util.Stack;
/**<h1>GuiPaint</h1>
 * The GuiPaint class is the gui for the paint program.
 * @author nbouman
 */
public class GuiPaint implements ActionListener {

    private String version = "0.04";//Which version this is.

    /**
     *saved keeps track of whether changes have been made since the last time
     * the image was saved: True if no changes have been made, false if changes
     * have been made.
     */
    protected Boolean saved = true;//True if the no changes have been made since last save.
    private String statusText = "no file open.";//Text to be added to the title bar.
    private JFrame frame, menuFrame;//The main frame of the GUI
    private JMenuBar menuBar;//The menu bar of the GUI
    private JMenu menuFile, menuEdit;//The menu sections of the GUI

    /**
     *drawingPanel is the drawingPanel object that goes in the toolbar to 
     * allow the user to select shapes, etc.
     */
    protected DrawingPanel drawingPanel = new DrawingPanel();//What goes in the toolbar.
    private JMenuItem loadItem, saveAsItem, closeItem, undoItem, redoItem;//The menu items of the GUI
    final JFileChooser fc = new JFileChooser();//The file chooser.
    private ActiveImage activeImage = new ActiveImage(this);//The object containing the image being used.
    private JToolBar toolBar = new JToolBar(JToolBar.VERTICAL);//the toolbar.

    /**
     *undoStack is a stack containing previous versions of the image before
     * each subsequent edit.
     */
    protected Stack<BufferedImage> undoStack = new Stack();//The stack for the undone images

    /**
     *redoStack is a stack containing versions of the image before each
     * subsequent undo.
     */
    protected Stack<BufferedImage> redoStack = new Stack();//The stack for the redone images
    private boolean canUndo=false;//Can the user undo?
    private boolean canRedo=false;//Can the user redo?
    
    /**
     * The constructor for the GuiPaint class.
     */
    public GuiPaint() {
        //Set what the file chooser can see.
        fc.addChoosableFileFilter(new ImageFilter());
        fc.setAcceptAllFileFilterUsed(false);

        //Add the image preview feature to the file chooser.
        fc.setAccessory(new ImagePreview(fc));

        //Create the frame.
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        menuFrame = new JFrame();

        //Set the title of the frame.
        frame.setTitle("Paint version " + version + ", " + statusText + " ");

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Create the file tab of the menu bar.
        menuFile = new JMenu("File");
        menuFile.setMnemonic(KeyEvent.VK_F);
        menuFile.getAccessibleContext().setAccessibleDescription("Load, Save, Close.");
        menuBar.add(menuFile);

        //Create the edit tab of the menu bar.
        menuEdit = new JMenu("Edit");
        menuEdit.setMnemonic(KeyEvent.VK_E);
        menuEdit.getAccessibleContext().setAccessibleDescription("Edit the image.");
        menuBar.add(menuEdit);

        //Create the selection of the file tab to load an image.
        loadItem = new JMenuItem("Load Image", KeyEvent.VK_L);
        loadItem.getAccessibleContext().setAccessibleDescription("Load an image from file into Paint.");
        loadItem.addActionListener(this);
        menuFile.add(loadItem);

        //Create the selection of the file tab to save an image to a specified address.
        saveAsItem = new JMenuItem("Save Image As", KeyEvent.VK_A);
        saveAsItem.getAccessibleContext().setAccessibleDescription("Save active image to specified filepath.");
        saveAsItem.addActionListener(this);
        menuFile.add(saveAsItem);

        //Create the selection of the file tab to close the program.
        closeItem = new JMenuItem("Close Program", KeyEvent.VK_C);
        closeItem.getAccessibleContext().setAccessibleDescription("Close the program.");
        closeItem.addActionListener(this);
        menuFile.add(closeItem);
        
        undoItem = new JMenuItem("Undo", KeyEvent.VK_U);
        undoItem.getAccessibleContext().setAccessibleDescription("Undo last change.");
        undoItem.addActionListener(this);
        undoItem.setEnabled(false);
        menuEdit.add(undoItem);
        
        redoItem = new JMenuItem("Redo", KeyEvent.VK_R);
        redoItem.getAccessibleContext().setAccessibleDescription("Redo last undo.");
        redoItem.addActionListener(this);
        redoItem.setEnabled(false);
        menuEdit.add(redoItem);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//Handle closing ourself.
        frame.setLayout(new BorderLayout());//Set the frame layout.
        frame.setJMenuBar(menuBar);//Add the menu bar to the frame.
        toolBar.add(drawingPanel);//Add the drawing panel to the toolbar.
        frame.add(toolBar, BorderLayout.WEST);//Add the toolbar
        frame.add(activeImage, BorderLayout.CENTER);//Add the image holder to the frame.

        frame.pack();//Put the components in the window based on component preferred sizes.
        frame.validate();//Double check that everything is drawn as it should be.
        frame.setVisible(true);//Show the frame.

        //Create a WindowListener to handle the closing how we want.
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                onExit();//method which handles the closing.
            }
        });
    }

    
    @Override
    public void actionPerformed(ActionEvent event) {//Handle actions when events occur.
        if(activeImage.currentShape instanceof Selection&&activeImage.currentShape.selected==true)//take care of setting a selection
            activeImage.addShape();
        
        if (event.getSource() == closeItem) {
            onExit();//method which handles the closing.
        } 
        
        else if (event.getSource() == loadItem) { //load the image with a file chooser
            if (saved == false) {//If not saved, add save option.
                Object[] options = {"Yes", "Cancel", "Save First"};
                int n = JOptionPane.showOptionDialog(frame, "Are you sure you want to load a new image? Any unsaved changes will be lost.", "Close", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                if (n == 0) {
                    loadImage();
                } else if (n == 2) {
                    if (saveImage()) {
                        loadImage();
                    }
                }
            } else {
                loadImage();
            }
        } 
        
        else if (event.getSource() == saveAsItem) {//save the image with separate save method
            if (saveImage()) {
                saved = true;
            }
        } 
        
        else if (event.getSource() == undoItem){//Undo the latest change
            try{
                activeImage.redoPush();
                activeImage.setImage(undoStack.pop());
                updateTitle();
                setRedo(true);
                if(undoStack.empty())
                    setUndo(false);
            }catch(RuntimeException e){  
            }
        } 
        
        else if (event.getSource() == redoItem){//Redo the latest undo
            try{
                undoStack.push(activeImage.deepGetImage());
                activeImage.setImage(redoStack.pop());
                updateTitle();
                setUndo(true);
                if(redoStack.empty())
                setRedo(false);
            }catch(RuntimeException e){   
            }
        }
        
        updateTitle();//Something might have been done, so update the title of the frame.
    }

    /**
     * The loadImage method uses a file picker to select an image.
     */
    public void loadImage() {//load an image
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            activeImage.readImage(fc.getSelectedFile());
            frame.pack();
            saved=true;
        } else if (returnVal == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(frame, "Load cancelled.");
        } else {
            JOptionPane.showMessageDialog(frame, "Failed to load image.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
    The updateTitle method updates the title of the window.
    */
    public void updateTitle() {//Update the title of the frame.
        if (activeImage.getImage() == null) {
            statusText = "no file open.";
        } else if (saved == false) {
            statusText = "file: " + activeImage.getPath() + " [unsaved]";
        } else {
            statusText = "file: " + activeImage.getPath();
        }

        frame.setTitle("Paint version " + version + ", " + statusText + " ");
    }

    /**
     * the onExit method exits the program, giving the user appropriate prompts.
     */
    public void onExit() {//Handle exiting
        if (saved == false) {//If not saved, add save option.
            Object[] options = {"Yes", "Cancel", "Save First"};
            int n = JOptionPane.showOptionDialog(frame, "Are you sure you want to quit? Any unsaved changes will be lost.", "Close", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
            if (n == 0) {
                frame.dispose();
            } else if (n == 2) {
                if (saveImage()) {
                    frame.dispose();
                }
            }
        } else {
            Object[] options = {"Yes", "Cancel"};
            int n = JOptionPane.showOptionDialog(frame, "Are you sure you want to quit?", "Close", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
            if (n == 0) {
                frame.dispose();
            }
        }
    }
    
    /**
     * The setUndo method sets whether or not the user can use the undo 
     * feature.
     * @param b The status of the undo feature. True if it can be used, false
     * if not.
     */
    public void setUndo(boolean b){
        canUndo=b;
        undoItem.setEnabled(b);
    }
    
    /**
     * The setRedo method sets whether or not the user can use the redo 
     * feature.
     * @param b The status of the redo feature. True if it can be used, false
     * if not.
     */
    public void setRedo(boolean b){
        canRedo=b;
        redoItem.setEnabled(b);
    }

    /**
     * The saveImage method allows the user to save the image currently being
     * edited to a file using a file picker.
     * @return Returns true if saved successfully, false if not.
     */
    public Boolean saveImage() {//Save the image using file picker. Return true if successful, false otherwise.
        int returnVal = fc.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                String filePath = fc.getSelectedFile().getPath();
                int firstDot = filePath.indexOf('.');
                int lastDot = filePath.indexOf('.');
                if (!(firstDot == lastDot) || (firstDot == -1)) {//make sure there's one and only one "." in filepath
                    JOptionPane.showMessageDialog(frame, "Bad Filepath.", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                } else {
                    String format = filePath.substring(lastDot + 1);
                    if ((format.equals("png")) || (format.equals("gif")) || (format.equals("jpg"))) {//Check for approved filepath
                        ImageIO.write(activeImage.getImage(), format, new File(filePath));
                        JOptionPane.showMessageDialog(frame, "Saved image as: " + filePath);
                        return true;
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid Filetype.", "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Failed to save image.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else if (returnVal == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(frame, "Save cancelled.");
            return false;
        } else {
            JOptionPane.showMessageDialog(frame, "Failed to save image.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
