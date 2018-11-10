
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Locale;
import javax.imageio.ImageIO;

public class PIC extends JPanel {
    
    public PIC()
    {
        setBackground(Color.WHITE);

        setLayout(new GridLayout(6, 1));
        setLocation(125, 0);
        setSize(310, 200);
        
        JLabel b[]=new JLabel[6];
        for(int i=0;i<6;i++)b[i]=new JLabel();
        
        
        b[0].setText("Chittagong University of Egineering & Technology");
        b[0].setFont(new Font("Serif",Font.ITALIC,14));
       
        b[2].setText("Name: Ratul Bhowmick ");
        b[2].setFont(new Font("Serif",Font.ITALIC,20));
        
        b[1].setText("Dept: Computer Science & Engineering");
        b[1].setFont(new Font("Serif",Font.ITALIC,17));
        
        b[3].setText("ID No: 1604038    Scetion: A");
        b[3].setFont(new Font("Serif",Font.ITALIC,20));
        
        b[4].setText("Email: ratulbhowmick65@gmail.com");
        b[4].setFont(new Font("Serif",Font.ITALIC,18));
        
        
        
        b[5].setText("Language : Java , Swing");
        b[5].setFont(new Font("Serif",Font.ITALIC,20));
        
        //b[6].setText("asdasd");
        
        for(int i=0;i<6;i++)add(b[i]);
        
        
        
        setOpaque(false);
        
       
    }
    
    
}
