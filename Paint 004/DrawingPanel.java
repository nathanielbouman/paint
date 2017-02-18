/*
 * Pain(t) final version.
 * Created by Nathaniel Bouman.
 */
package paint;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**<h1>Drawing Panel</h1>
 *The drawing panel class is a panel where the user can select which shape
 * to draw and edit parameters for drawing the shape; extends JPanel.
 * @author nbouman
 */
public class DrawingPanel extends JPanel implements ActionListener{
    private Color rightColor, leftColor;//Colors for mouse buttons
    private JButton rightColorButton, leftColorButton, lineButton, rectangleButton, circleButton, freeButton, selectButton;//buttons on the panel
    private JPanel colorPanel, shapePanel, shapeMenuPanel, innerPanel;//Panels within the panel
    private JColorChooser leftColorChooser, rightColorChooser;//Color choosers
    private FreeMenuPanel freeMenuPanel;//the menu fro freedraw
    private LineMenuPanel lineMenuPanel;//The menu for lines
    private SelectionMenuPanel selectionMenuPanel;
    
    /**
     * The constructor for a drawing panel object.
     */
    public DrawingPanel(){
        setBorder(BorderFactory.createRaisedBevelBorder());
        
        innerPanel = new JPanel();//Create a panel within the panel (helps with sizing).
        innerPanel.setLayout(new BoxLayout(innerPanel,BoxLayout.Y_AXIS));
        innerPanel.setAlignmentY(0);
        innerPanel.setPreferredSize(new Dimension(175,300));
        innerPanel.setBorder(BorderFactory.createRaisedBevelBorder());

        //Set the default colors.
        rightColor=Color.black;
        leftColor=Color.white;
        
        //Create the color choosers.
        leftColorChooser = new JColorChooser();
        rightColorChooser = new JColorChooser();        
        
        //Create the color panel
        colorPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        colorPanel.setMaximumSize(new Dimension(100,50));
        c.anchor=GridBagConstraints.EAST;
        
        //Create the right color button
        c.gridx=1;
        rightColorButton = new JButton();
        rightColorButton.setBackground(rightColor);
        rightColorButton.addActionListener(this);
        rightColorButton.setPreferredSize(new Dimension(50,50));
        colorPanel.add(rightColorButton, c);
        
        //Create the left color button
        c.gridx=0;
        leftColorButton = new JButton();
        leftColorButton.setBackground(leftColor);
        leftColorButton.addActionListener(this);
        leftColorButton.setPreferredSize(new Dimension(50,50));
        colorPanel.add(leftColorButton, c);

        //Create the shape panel
        shapePanel=new JPanel();
        shapePanel.setPreferredSize(new Dimension(100,75));
        
        //Create the line button
        lineButton=new JButton("Line");
        shapePanel.add(lineButton);
        lineButton.addActionListener(this);
        
        //Create the free button
        freeButton=new JButton("Free Draw");
        shapePanel.add(freeButton);
        freeButton.addActionListener(this);
        
        //Create the select button
        selectButton=new JButton("Select");
        shapePanel.add(selectButton);
        selectButton.addActionListener(this);
        
        //Create the various shape menu panels
        lineMenuPanel = new LineMenuPanel();
        freeMenuPanel = new FreeMenuPanel();
        selectionMenuPanel = new SelectionMenuPanel();
        
        //Defaut shape menu panel is line
        shapeMenuPanel=lineMenuPanel;
        
        //Add the panels to the inner panel, and the inner panel to the drawing panel
        innerPanel.add(colorPanel);
        innerPanel.add(shapePanel);
        innerPanel.add(shapeMenuPanel);
        add(innerPanel);   
    }
    
    /**
     * The getRightColor method returns the color object for the right mouse
     * button.
     * @return Returns the color object for the right mouse button.
     */
    public Color getRightColor(){return rightColor;}
    
    /**
     * The getLeftColor method returns the color object for the right mouse
     * button.
     * @return Returns the color object for the left mouse button.
     */
    public Color getLeftColor(){return leftColor;}
    
    /**
     * The getShape method returns the number value of the type of shape
     * being drawn.
     * @return The number of the shape being drawn. 0 for line, 3 for free draw,
     * 4 for selection, -1 for none.
     */
    public int getShape(){
        if(shapeMenuPanel==lineMenuPanel)
            return 0;//Line
        else if(shapeMenuPanel==freeMenuPanel)
            return 3;//FreeDraw
        else if(shapeMenuPanel==selectionMenuPanel)
            return 4;//Selection
        else
            return -1;//None
    }
    
    /**
     * The getLineWidth method returns the width to be used for drawing a line.
     * @return The width to be used for drawing a line.
     */
    public int getLineWidth(){return lineMenuPanel.getLineWidth();}
    
    /**
     * The getFreeWidth method returns the width to be used for free draw.
     * @return The width to be used for free draw.
     */
    public int getFreeWidth(){return freeMenuPanel.getFreeWidth();}

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==rightColorButton){//Use color chooser to set right color
            Color newColor = rightColorChooser.showDialog(DrawingPanel.this,"Choose Right Mouse Color", rightColor);
            if(newColor!=rightColor && newColor!=null){
                rightColor = newColor;
                rightColorButton.setBackground(rightColor);
                validate();
                repaint();
            }
        }
        
        else if(e.getSource()==leftColorButton){//Use color chooser to set left color
            Color newColor = leftColorChooser.showDialog(DrawingPanel.this,"Choose Right Mouse COlor", leftColor);
            if(newColor!=leftColor && newColor!=null){
                leftColor = newColor;
                leftColorButton.setBackground(leftColor);
                validate();
                repaint();
            }
        }
        
        else if(e.getSource()==lineButton){//Switch to line and line menu
            innerPanel.remove(shapeMenuPanel);
            shapeMenuPanel=lineMenuPanel;
            innerPanel.add(shapeMenuPanel);
            validate();
            repaint();
        }
        
        else if(e.getSource()==freeButton){//Switch to freedraw and freedraw menu
            innerPanel.remove(shapeMenuPanel);
            shapeMenuPanel=freeMenuPanel;
            innerPanel.add(shapeMenuPanel);
            validate();
            repaint();
        }
        
        else if(e.getSource()==selectButton){//Switch to freedraw and freedraw menu
            innerPanel.remove(shapeMenuPanel);
            shapeMenuPanel=selectionMenuPanel;
            innerPanel.add(shapeMenuPanel);
            validate();
            repaint();
        }
    }
    
}
