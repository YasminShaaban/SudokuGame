
package testsudoku;
import javax.swing.*; 
import java.awt.*;
import static java.awt.Color.BLUE;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class SodoFrame extends JFrame {
    public static final int MAX_LEVEL = 4;
    public static final int DELAY = 120;
    private int [] values=new int [81];
    private JMenuBar mnbar;
    private JMenu settings;
    private JMenuItem newgame;
    private JMenuItem highscores;
    private JTextField [] txts=new JTextField [81];
    private JLabel lblScore;
    private JLabel lblLevel;
    private JButton submit;
    private int score=0;
    private int level=1;
    private javax.swing.Timer timer;
    private Random rand;
    private int rvalue;
    private int f;
    private int rindex;
    int count = 0 ;
    Container S;
    public static seen_ob[] final_ans;
    line l[]=new line[6];
    String highscorestring;
    public static int[] one_d_game = new int[81];
    String name ;
    String highscore="";
    HighScore scr;
    public SodoFrame() {
        this.setTitle("Sudoku");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(456, 540);
        S = this.getContentPane();
        S.setLayout(null);
        lblScore = new JLabel("Score: " + score);
        lblScore.setBounds(49, 455, 80, 40);
        lblLevel = new JLabel("Level: " + level);
        lblLevel.setBounds(330, 455, 80, 40);
        rand = new Random();
        submit = new JButton("SUBMIT");
        submit.setBounds(178, 455, 100, 30);
        l[0] = new line(150, 0, 150, 520);
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    for (int i = 0; i < 81; i++) {
                        values[i] = Integer.parseInt(txts[i].getText());
                      }
                    for (int i = 0; i < 81; i++) {
                        if (values[i] == one_d_game[i]) {
                            count++;

                        }
                    }
                    if (count - 40 == 41) {
                        JOptionPane.showMessageDialog(null, "CONGRATULATIONS :))" + "\n " + "YOUR SCORE :" + (count - 40));
                        setHighScore();
                    } else {
                        JOptionPane.showMessageDialog(null, "YOUR SCORE :" + (count - 40));
                        setHighScore();
                    }
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(null, " THERE ARE NUMBERS MISSING");
                }
            }


        });
        S.add(submit);
        mnbar = new JMenuBar();
        settings = new JMenu("Settings");
        newgame = new JMenuItem("New Game");
        highscores = new JMenuItem("Highscores");
        settings.add(newgame);
        settings.add(highscores);
        mnbar.add(settings);
        this.setJMenuBar(mnbar);
        newgame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                newGame();
                }
        });
        highscores.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                setHighScoresFrame();
            }
        });
               setTextFields();
    }
    public void setHighScore(){
        name = JOptionPane.showInputDialog(" You set a new Score.What is your name ?");
        scr = new HighScore();
        scr.addScore(name, (count - 40));
        highscorestring = scr.getHighscoreString();
        HighScoreFrame hsf = new HighScoreFrame();
        hsf.area.setText(highscorestring);
        hsf.area.setFont(new Font("Cooper Black", Font.BOLD, 30));
        hsf.area.setEditable(false);
        hsf.setVisible(true);
        increaseScore();
        newGame();
        
    }
    public void setHighScoresFrame() {
        HighScoreFrame hsf = new HighScoreFrame();
        scr = new HighScore();
        scr.loadScoreFile();
        highscorestring = scr.getHighscoreString();
        hsf.area.setText(highscorestring);
        hsf.area.setFont(new Font("Cooper Black", Font.BOLD, 20));
        // hsf.area.setForeground(Color.BLUE);
        hsf.area.setEditable(false);
        hsf.setVisible(true);

    }
    public void setTextFields() {
        for (int i = 0; i < 81; i++) {
                txts[i] = new JTextField();
            if (i == 0) {
                txts[i].setBounds(i, i, 50, 50);
                txts[i].setBorder(BorderFactory.createLineBorder(Color.blue, 2));
                txts[i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                txts[i].setFocusable(true);
                txts[i].setFont(new Font("Cooper Black", Font.BOLD, 20));
                txts[i].setForeground(Color.BLUE);
                txts[i].setBackground(Color.white);

            } else {
                txts[i].setBounds(((i) % 9) * 50, ((i) / 9) * 50, 50, 50);
                txts[i].setBorder(BorderFactory.createLineBorder(Color.blue, 2));
                txts[i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                txts[i].setFont(new Font("Cooper Black", Font.BOLD, 20));
                txts[i].setForeground(Color.BLUE);

            }

            txts[i].addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar(); // get the typed key
                    if (!(c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' ||
                            (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE) || (c == KeyEvent.VK_UP) || (c == KeyEvent.VK_DOWN) || (c == KeyEvent.VK_RIGHT)) || (c == KeyEvent.VK_LEFT)) {
                        e.consume();// Ignore this key
                    }
                    blockedSquares(e);
                }
            });

            S.add(txts[i]);
        }

        final_ans = backend();
        for (int j = 0; j < 40; j++) {
            rindex = final_ans[j].index;
            rvalue = final_ans[j].value;
            txts[rindex].setText(rvalue + "");
            txts[rindex].setEditable(false);
            txts[rindex].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        }


    }
   public void newGame (){
        this.dispose();
         SodoFrame sframe = new SodoFrame ();
         sframe.setVisible(true);
    }
   public void increaseScore(){
        lblScore.setText("Score: "+(count-40));
    }
    
   public void increaseLevel(){
        if(level<MAX_LEVEL){
            level++;
            lblLevel.setText("Level: " +level);
            timer.setDelay(DELAY-level*10);
        }
}
    public void blockedSquares(KeyEvent evt){
    String x=((JTextField)evt.getSource()).getText().toString();
    if (x.length()==0){
        return;
        }
    else {
        evt.consume();//ignore event
    }
    }

    public static seen_ob[] set_seen (int  one_d_new_game[])
    {   //seen_ob array_seen = new seen_ob();
        seen_ob[] array_seen = new seen_ob[40];
        for(int i = 0 ; i < 40; i++)
        {
            array_seen[i] = new seen_ob();
        }
        Random ran = new Random();
        int index;
        for(int i = 0 ; i <array_seen.length ; )
        {do
        {
         index =  ran.nextInt(80)+0;
         array_seen[i].index = index;
         array_seen[i].value = one_d_new_game[index];
        }
         while(!check_index(array_seen,index,i));
        i++;
        }
        for(int i =0 ; i < array_seen.length;i++)
        {   
           // System.out.print(array_seen[i].value );
          //  System.out.println(array_seen[i].index + "    ");
           
        }
        return array_seen;
       }
      public static boolean check_index (seen_ob[]  a_s,int index,int place)
       {
        for(int i = 0 ; i < place ; i++)
        {
            if(a_s[i].index == index)
                return false;
        }
        return true;
       }
       public static int[] one_d_game (int[][] game_solution)
       {
     
     int line = 9; 
     int colum = 9;
     for(int i = 0;i<line;i++)
         {
         for(int j =0 ; j< colum ; j++)
         {
             one_d_game[(i*9 )+ j] = game_solution[i][j];
             
         }
         }
         for(int i = 0 ; i < one_d_game.length ;i++)
         {
            // System.out.print(one_d_game[i]);
         }
        // System.out.println();
     return one_d_game;
    
    }
    public static int[][] initNet(int[][]net)
        
    {
    
    Random ran = new Random();
    int num_gen =0;
    
    for(int line = 0 ; line<8;line++)
    {
           
        for(int colum = 0 ; colum < 9 ;)
        {//System.out.println(" "+line + " "+colum ); 
            if(colum == 8)
                {   
                    net[line][colum]=45-(net[line][0]+net[line][1]+net[line][2]+net[line][3]+net[line][4]+net[line][5]+net[line][6]+net[line][7]);
                    if(line == 0)
                    {
                        net[line][colum]=45-(net[line][0]+net[line][1]+net[line][2]+net[line][3]+net[line][4]+net[line][5]+net[line][6]+net[line][7]);
                        colum++;
                    }
                      
                  for(int i = 0 ; i < line ;i++)
                            {
                 if((check(net[line][colum],net,line,colum) == false) || net[i][colum] == 45-(net[line][0]+net[line][1]+net[line][2]+net[line][3]+net[line][4]+net[line][5]+net[line][6]+net[line][7]))
                                     {
                                        net= correct(net,line);
                                        colum = 0;
                                        num_gen = ran.nextInt(9)+1;
                                        net[line][colum]=num_gen; 

                                        break;
                                      }
                    if(i==line-1 && (net[i][colum] != 45-(net[line][0]+net[line][1]+net[line][2]+net[line][3]+net[line][4]+net[line][5]+net[line][6]+net[line][7])) /*&& (check(num_gen,net,line,colum)*/){
                     net[line][8] =45-(net[line][0]+net[line][1]+net[line][2]+net[line][3]+net[line][4]+net[line][5]+net[line][6]+net[line][7]);     
                                colum++;}             
                }
          
                 
                 
              }
            else{
                 int flag = 21;
                 int count2=10;
                do
                {
                     if(flag >0)

                     { num_gen = ran.nextInt(9)+1;


                        net[line][colum]=num_gen;
                        flag--;
                     }
                     else
                     {
                         if (count2<10){
                             erase(net,line,colum);
                             colum=0;line=0;
                         }
                        flag =21;
                        net = correct(net,line);
                        colum = 0;
                        num_gen = ran.nextInt(9)+1;
                        net[line][colum]=num_gen;
                        count2--;
                     }
                }
             while(!check(num_gen,net,line,colum));
           //  System.out.println(net[line][colum]);   
             
                colum++; 

              
             }
                  
    }
    }    
        
    
    for(int i = 0;i<9; i++)
    {
        net[8][i]=45-(net[0][i]+net[1][i]+net[2][i]+net[3][i]+net[4][i]+net[5][i]+net[6][i]+net[7][i]);
    }
   
   return net;
    }
    public static int [][] erase  (int[][] net,int line , int colum)           
  {
    for(int i = 0 ; i <= line ; i++)
    {
        for(int j = 0 ; j <= colum ; j++)
        {
            net[i][j] = 0;
        }
    }
    return net;
    }
    public static boolean check(int inum_checked,int[][] net,int line,int colum)
        
   {
   // line = line-3;
    for(int i = 0 ;i<colum ; i++)
    {
        if(net[line][i] == net[line][colum] )
            return false;
    }
    for(int i = 0;i <line ; i++)
    {
        if(net[i][colum] == net[line][colum] )
            return false;
    }
    if(line <3 && colum < 3)
    {
        for(int i = 0 ; i <3 ;i ++)
        {
                
        
        for(int j = 0 ; j <line ;j++)
        {
            if(net[j][i] == net[line][colum])
                return false;
        }
        }
    }
    else if(2<line && line <=5 && colum < 3)
        {
        for(int i = 0 ; i < 3 ;i ++)
        {
          
        for(int j = 3 ; j <line ;j++)
        {
            if(net[j][i] == net[line][colum])
                return false;
        }}
    }
    else if(5<line && line <=8 && colum < 3)
    {
        for(int i = 0 ; i < 3 ;i ++)
        {
                
            
        for(int j = 6 ; j <line ;j++)
        {
            if(net[j][i] == net[line][colum])
                return false;
        }}
      }
    else if( 3>line && 2<colum && colum <= 5)
        {
        for(int i = 3 ; i < 6 ;i ++)
        {
                
           
        for(int j = 0 ; j <line ;j++)
        {
            if(net[j][i] == net[line][colum])
                return false;
        }}
        }
   
    else if(line > 2 && line <=5 && colum >2 && colum <=5)
        {
        for(int i = 3 ; i < 6 ;i ++)
        {
                
            
        for(int j = 3 ; j <line ;j++)
        {
            if(net[j][i] == net[line][colum])
                return false;
        }}
    }
    else if(5<line && line <=8 && colum >2 && colum <=5)
        {
            for(int i = 3 ; i < 6 ;i ++)
        {
        for(int j = 6 ; j <line ;j++)
        {
            if(net[j][i] == net[line][colum])
                return false;
        }
        }
    }
    else if(line <3 && colum > 5 && colum <= 8)
        {
        for(int i = 6 ; i < 9 ;i ++)
        {
                
       
        for(int j = 0 ; j <line ;j++)
        {
            if(net[j][i] == net[line][colum])
                return false;
        }
        }
    }
    else if(line >2 && line <=5 && colum > 5 && colum <= 8)
        {
        for(int i = 6 ; i < 9 ;i ++)
        {
                
           
        for(int j = 3 ; j <line ;j++)
        {
            if(net[j][i] == net[line][colum])
                return false;
        }}
    }
    else if(line >5 && line <= 8 && colum > 5 && colum <= 8)
        {
        for(int i = 6 ; i < 9 ;i ++)
        {
                
           
        for(int j = 6 ; j <line ;j++)
        {
            if(net[j][i] == net[line][colum])
                return false;
        }}
    }
    
    
    return true;
}
public static void printOut (int[][] net)
{
     for(int i =0; i <9 ; i++)
                {
                    for(int j = 0 ; j<9 ; j++)
                    {
                       System.out.print(net[i][j] + "  ");
                   
                    } 
                   System.out.println();
                }
}
 public static int[][]  correct(int [][] correct,int line)
 {
    
    for(int i = 0 ; i <9 ; i++)
    correct[line][i]= 0;
     return correct;
 } 
 public static seen_ob[] backend()
 {
     int[][] newgame = new int[9][9];
        
     int [][] new_game = initNet(newgame);
        
     // printOut(new_game);
        int [] one_d_new_game =  one_d_game(new_game);
         
         return set_seen(one_d_new_game);
 }
}


class seen_ob {
    int index;
    int value; 
    seen_ob()
    {
        this.index = 0;
        this.value =0;
    }
}
    
abstract class shape  { 
protected int x ;
protected int y ;
protected Color c;
    public shape (int x , int y ){
    this.x=x;
    this.y=y;
   
    }
    //public void drawable 
      public abstract void draw(Graphics g);
}

class line  {
    private int x2 ,y2,x1,y1;
   
    public line (int x1 , int y1 , int x2 , int y2  ){
            this.x1=x1;
            this.y1=y1;
            this.x2=x2;
            this.y2=y2;
            
    }
    public void draw(Graphics g){
        g.setColor(Color.BLUE);
   g.drawLine(x1, y1, x2, y2);
    }
}


public class TestSudoku {
    
    public static void main(String[] args) {
        SodoFrame s = new SodoFrame();
        s.setVisible(true);
    }
    
}