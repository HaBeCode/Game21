import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
class Testing
{
  JLabel[][] labels = new JLabel[4][4];
  Border b = BorderFactory.createLineBorder(Color.BLACK,5);
  Color selectedColor = Color.YELLOW;
  Color nonSelectedColor = Color.PINK;
  public void buildGUI()
  {
    MouseListener ml = new MouseAdapter(){
      public void mousePressed(MouseEvent me){
        if(me.getClickCount() == 2)//double-click
        {
          JLabel src = (JLabel)me.getSource();
          for(int x = 0, y = labels.length; x < y; x++)
          {
            for(int xx  = 0, yy = labels[x].length; xx < yy; xx++)
            {
              labels[x][xx].setBackground(src == labels[x][xx]? selectedColor:nonSelectedColor);
              labels[x][xx].setBorder(src == labels[x][xx]? b:null);
            }
          }
        }
      }
    };
    JPanel p = new JPanel(new GridLayout(4,4));
    for(int x = 0, y = labels.length; x < y; x++)
    {
      for(int xx  = 0, yy = labels[x].length; xx < yy; xx++)
      {
        labels[x][xx] = new JLabel(new ImageIcon("test.gif"));
        labels[x][xx].setOpaque(true);
        labels[x][xx].setBackground(nonSelectedColor);
        labels[x][xx].addMouseListener(ml);
        p.add(labels[x][xx]);
      }
    }
    JFrame f = new JFrame();
    f.getContentPane().add(p);
    f.pack();
    f.setLocationRelativeTo(null);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setVisible(true);
  }
  public static void main(String[] args)
  {
    SwingUtilities.invokeLater(new Runnable(){
      public void run(){
        new Testing().buildGUI();
      }
    });
  }
}
