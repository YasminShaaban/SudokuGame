
package testsudoku;

import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class HighScoreFrame extends JFrame{
   JTextArea area;
   public HighScoreFrame(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500,590);
        Container c= this.getContentPane();
        this.setTitle("Highscores");
        this.setResizable(false);
        area=new JTextArea (" ");
        area.setBounds(50,20, 450,300);
        c.add(area);
    }
}
