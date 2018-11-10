
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Move21 extends JPanel implements MouseListener, MouseMotionListener {

    private BufferedImage slate;
    private TexturePaint slatetp;

    private BufferedImage slate2;
    private TexturePaint slatetp2;

    private Container c;
    private JTextArea ta;
    private JTextField tf;
    private Image dbimage;
    private Graphics dbg;
    private Rectangle rec = new Rectangle(10, 20, 30, 40);
    private boolean ab = false;
    private int count = 1;

    private static Rectangle top = null;
    private Color top_color = null;
    private int ax, ay, w, h;
    private int tower, tow;
    private boolean draggable = false, firstTime = false, stop = false;

    public Stack<Rectangle> stack[] = new Stack[3];
    public Stack<Color> color[] = new Stack[3];

    public int move = 0;
    public int penalty = 0;

    Move21(int v) {

        init(v);
        addMouseMotionListener(this);
        addMouseListener(this);

    }

    ///creating disk and tower
    public void init(int v) {
        stop=false;
        tow = v;
        move=0;
        penalty=0;
        slate = null;
        try {
            slate = ImageIO.read(this.getClass().getResource("background.jpg"));///background picture of panel
            slate2 = ImageIO.read(this.getClass().getResource("wood.jpg"));///background picture of disk

        } catch (IOException ex) {
            Logger.getLogger(Move.class.getName()).log(Level.SEVERE, null, ex);
        }

        Color c[] = {Color.yellow, Color.red, Color.blue, Color.pink, Color.cyan, Color.magenta, Color.green, Color.orange, Color.lightGray};

        stack[0] = new Stack<Rectangle>();
        color[0] = new Stack<Color>();

        stack[1] = new Stack<Rectangle>();
        color[1] = new Stack<Color>();

        stack[2] = new Stack<Rectangle>();
        color[2] = new Stack<Color>();

        ///initializing the base disk size
        int x = 50;
        int y = 397;
        int xx = 50 / v;
        int yy = 20;
        stack[0].push(new Rectangle(x, y, 50 * 2 + 20, 20));
        color[0].push(c[0]);

        y -= 2;
        for (int i = 1; i < v; i++) {
            x += xx;///diferrence between two disk size means weidth
            y -= yy;///diferrence between two disk size means height

            stack[0].push(new Rectangle(x, y, (100 - x) * 2 + 20, 20));
            color[0].push(c[i]);
            y -= 2;
        }
        top = null;
        top_color = null;

        repaint();///caling the paint 

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {///when mouse is presed on a disk
        Point p = e.getPoint();
        int i = e.getX();
        int j = e.getY();
        int n = towerPosition(p);

        tower = n;
        
        top = stack[n].peek();
        top_color = color[n].peek();

        int x = top.x + top.width;
        int y = top.y + top.height;

        if (i >= top.x && i <= x && j >= top.y && j <= y) {
            stack[n].pop();
            color[n].pop();
            ax = top.x;
            ay = top.y;

            draggable = true;
        } else {
            top = null;
            top_color = null;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {//when mouse point is realesed on a area

        Point p = e.getPoint();

        int n = towerPosition(p);

        int y = 0;

        System.out.println(n + " " + "released");

        int x = 0;
        if (draggable && top != null) {

            if (tower != n) {
                move++;
            }
            if (tower == n) {
                x = ax;
                y = ay;
            } else if ((n - tower) == 1) {
                x = ax + 200;
            } else if ((n - tower) == 2) {
                x = ax + 400;
            } else if ((n - tower) == -2) {
                x = ax - 400;
            } else if ((n - tower) == -1) {
                x = ax - 200;
            }

            if (stack[n].empty()) {
                y = 397;
            } else {
                int yy = stack[n].peek().height;
                int zz = stack[n].peek().y;
                if (stack[n].peek().width > top.width) {
                    y = zz - yy - 2;
                } else {
                    JOptionPane.showMessageDialog(this, "Wrong Move", "Tower Of Hanoi", JOptionPane.ERROR_MESSAGE);
                    x = ax;
                    y = ay;
                    n = tower;
                    penalty += 2;
                }
            }
            draggable = false;

            top.setFrame(x, y, top.width, top.height);
            stack[n].push(top);
            color[n].push(top_color);

            top = null;
            top_color = null;
            ax = 0;
            ay = 0;
        }
        if (stack[1].size() == tow || stack[2].size() == tow) {
            stop = true;
        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {///when mouse is dragged

        if (draggable) {
            top.setLocation(e.getX(), e.getY());
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    public int towerPosition(Point p) {///means where is the mouse is now or which tower has selected
        Rectangle a1 = new Rectangle(0, 0, getWidth() / 3, getHeight());
        Rectangle a2 = new Rectangle(getWidth() / 3, 0, getWidth() / 3, getHeight());
        Rectangle a3 = new Rectangle(2 * getWidth() / 3, 0, getWidth() / 3, getHeight());

        if (a1.contains(p)) {
            return 0;
        } else if (a2.contains(p)) {
            return 1;
        }
        return 2;

    }

    public void paint(Graphics g) {//for double buffer

        dbimage = createImage(getWidth(), getHeight());
        dbg = dbimage.getGraphics();

        paintComponent(dbg);
        g.drawImage(dbimage, 0, 0, this);

    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        slatetp = new TexturePaint(slate, new Rectangle(0, 0, getWidth(), getHeight()));
        slatetp2 = new TexturePaint(slate2, new Rectangle(0, 0, getWidth(), getHeight()));

        g2d.setPaint(slatetp);
        Rectangle rect = new Rectangle(0, 0, getWidth(), getHeight());
        g2d.fill(rect);

        if (stop == true) {
            
            Font font = new Font("Serif", Font.PLAIN, 40);
            g2d.setColor(Color.white);
            
            g2d.setFont(font);
            
            String s = Integer.toString(move);
            g2d.drawString("Your Move  : ", 150, 200);
            g2d.drawString(s, 450, 200);

            s = Integer.toString(penalty);
            g2d.drawString("Penalty   : ", 200, 300);

            g2d.drawString(s, 450, 300);

            g2d.drawString("Best Move   : ", 150, 400);

            int b = (int) (double) Math.pow((double) 2, (double) tow);

            s = Integer.toString(b - 1);
            g2d.drawString(s, 450, 400);

            if (move == b - 1) {

                font = new Font("Serif", Font.ITALIC, 50);

                g2d.drawString("You Win !!!!", 220, 100);
            } else {
                font = new Font("Serif", Font.BOLD, 50);

                g2d.drawString("You Loss  :(", 225, 100);

            }

        }
        if (top == null && !stop) {

            int holder_x = getWidth() / 6;
            int holder_y = 415 - (tow * 20 + 40);
            g2d.setColor(Color.white);
            g2d.setStroke(new BasicStroke(10));
            g2d.drawLine(holder_x, 415, holder_x, holder_y);
            g2d.drawLine(holder_x + 200, 415, holder_x + 200, holder_y);
            g2d.drawLine(holder_x + 400, 415, holder_x + 400, holder_y);
            g2d.drawLine(0, 422, getWidth(), 422);

            Font font = new Font("Serif", Font.PLAIN, 30);
            g2d.setFont(font);
            g2d.drawString("Move", 250, 100);
            String s = Integer.toString(move);
            g2d.drawString(s, 350, 100);

            drawtower(g2d, 0);
            drawtower(g2d, 1);
            drawtower(g2d, 2);
        } else if (draggable == true && top != null) {

            int holder_x = getWidth() / 6;
            int holder_y = 415 - (tow * 20 + 40);
            g2d.setColor(Color.white);
            g2d.setStroke(new BasicStroke(10));
            g2d.drawLine(holder_x, 415, holder_x, holder_y);
            g2d.drawLine(holder_x + 200, 415, holder_x + 200, holder_y);
            g2d.drawLine(holder_x + 400, 415, holder_x + 400, holder_y);
            g2d.drawLine(0, 422, getWidth(), 422);

            Font font = new Font("Serif", Font.PLAIN, 30);
            g2d.setFont(font);
            g2d.drawString("Move", 250, 100);
            String s = Integer.toString(move);
            g2d.drawString(s, 350, 100);

            g2d.setPaint(slatetp2);

            g2d.fill(top);
            drawtower(g2d, 0);
            drawtower(g2d, 1);
            drawtower(g2d, 2);
        }
    }

    private void drawtower(Graphics2D g2d, int n) {///drawing the tower with disks
        if (!stack[n].empty()) {
            for (int i = 0; i < stack[n].size(); i++) {

                g2d.setPaint(slatetp2);

                g2d.fill(stack[n].get(i));
            }
        }
    }
}
