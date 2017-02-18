/*
 * Pain(t) final version.
 * Created by Nathaniel Bouman.
 */
package paint;

import java.awt.*;
import javax.swing.*;
/**<h1>Selection Menu Panel</h1>
 *The selection menu panel class is a panel for selection objects; Extends
 * JPanel.
 * @author nbouman
 */
public class SelectionMenuPanel extends JPanel {
    private JLabel title;
    
    /**
     *Constructor for SelectionMenuPanel.
     */
    public SelectionMenuPanel(){
        super(new GridLayout(1,0,5,0));
        title=new JLabel("Select",JLabel.LEFT);
        title.setFont(new Font("Serif", Font.BOLD, 12));
        add(title);
        
    }
}
