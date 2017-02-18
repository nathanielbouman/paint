/*
 * Pain(t) final version.
 * Created by Nathaniel Bouman.
 */
package paint;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
/**<h1>Line Menu Panel</h1>
 *The LineMenuPanel class extends JPanel and allows the user to adjust
 * parameters used in creating line objects.
 * @author nbouman
 */
public class LineMenuPanel extends JPanel implements ChangeListener {
    private int width = 1;//Width of the line
    private JSlider widthSlider;//Slider for width
    private JLabel title, widthTitle;//Labels
    private JPanel widthPanel;//Panel for the width slider
    
    /**
     * The constructor for a LineMenuPanel object.
     */
    public LineMenuPanel(){
        super(new GridLayout(2,0,5,0));
        
        //Create the title of the menu
        title=new JLabel("Line",JLabel.LEFT);
        title.setFont(new Font("Serif", Font.BOLD, 12));
        add(title);
        
        //Create the panel for the width slider
        widthPanel = new JPanel(new GridLayout(2,0));
        
        //Title for the width panel
        widthTitle = new JLabel("Width: "+width,JLabel.CENTER);
        widthTitle.setFont(new Font("Serif", Font.ITALIC, 12));
        widthPanel.add(widthTitle);
        
        //Create the width slider
        widthSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
        widthPanel.add(widthSlider);
        widthSlider.addChangeListener(this);
        
        //add the width panel to the LineMenuPanel
        add(widthPanel);
    }
    
    /**
     * The getLineWidth method returns the width to be used for creating line
     * objects.
     * @return Returns the width to be used for line objects.
     */
    public int getLineWidth(){
        return width;
    }
    
    @Override
    public void stateChanged(ChangeEvent e){
         JSlider source = (JSlider)e.getSource();//Handle changes to slider
            if (!source.getValueIsAdjusting()) {
                width = (int)source.getValue();
                widthTitle.setText("Width: "+width);
            }
    }   
}
    
    
