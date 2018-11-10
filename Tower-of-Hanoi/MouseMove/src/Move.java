import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import static java.lang.Math.abs;
import javax.swing.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Timer;
public class Move extends JPanel implements ActionListener {
    private BufferedImage slate;
    private TexturePaint slatetp;
    private BufferedImage slate2;
    private TexturePaint slatetp2;
    private Image dbimage;
    private Graphics dbg;
    private boolean ab = false;
    private int count = 1;
    private static Rectangle top = null;
    private Color top_color = null;
    private double ax, ay, w, h;
    private boolean draggable = false, firstTime = false, stop = false;

    public Stack<Rectangle> stack[] = new Stack[3];
    public Stack<Color> color[] = new Stack[3];

    public Queue<Integer> q = new LinkedList<>();
    public Queue<Integer> p = new LinkedList<>();
    public Timer timer;
    public int from, to;
    public int tow;
    Move(int v) {
        tow = v;
        slate = null;
        try {
            slate = ImageIO.read(this.getClass().getResource("background.jpg"));///background picture of panel
            slate2 = ImageIO.read(this.getClass().getResource("wood.jpg"));///background picture of disk
     } catch (IOException ex) {
            Logger.getLogger(Move.class.getName()).log(Level.SEVERE, null, ex);
        }
        towerOfHanoi(v, 0, 1, 2);///tower of hanoi recurtion code to store the movement sequence
        init(v);
    }
    public void towerOfHanoi(int n, int from_rod, int aux_rod, int to_rod) {
        if (n == 1) {
            q.add(from_rod);
            p.add(to_rod);
            return;
        }
        towerOfHanoi(n - 1, from_rod, to_rod, aux_rod);
        q.add(from_rod);
        p.add(to_rod);
        towerOfHanoi(n - 1, aux_rod, from_rod, to_rod);
    }
    public void init(int v) {
        
        Color c[] = {Color.yellow, Color.red, Color.blue, Color.pink, Color.cyan, Color.magenta, Color.green, Color.orange, Color.lightGray};

        stack[0] = new Stack<Rectangle>();
        color[0] = new Stack<Color>();

        stack[1] = new Stack<Rectangle>();
        color[1] = new Stack<Color>();

        stack[2] = new Stack<Rectangle>();
        color[2] = new Stack<Color>();

        ay = 415 - (tow * 20) - 80;
        int x = 50;
        int y = 395;
        int xx = 50 / v;
        int yy = 20;
        stack[0].push(new Rectangle(x, y, 50 * 2 + 20, 20));
        color[0].push(c[0]);

        y -= 2;
        for (int i = 1; i < v; i++) {
            x += xx;
            y -= yy;

            stack[0].push(new Rectangle(x, y, (100 - x) * 2 + 20, 20));
            color[0].push(c[i]);

            y -= 2;

        }
        top = null;
        top_color = null;

        repaint();///calling paint
        

        timer = new Timer(10, this);//this is for delay
        timer.setInitialDelay(10);
        timer.start();

        if (stop) {
            timer.stop();
        }
    }

    void step() {
        if (abs(ay - top.y) <= 25) {
            --top.y;
        } else {
            top.y -= 5;
        }
        top.setFrame(top.getX(), top.y, top.width, top.height);
    }
    void step2() {
        if (abs(ax - top.x) <= 25) {
            ++top.x;
        } else {
            top.x += 5;
        }
        top.setFrame(top.x, top.y, top.width, top.height);
    }

    void step3() {
        if (abs(ax - top.x) <= 25) {
            --top.x;
        } else {
            top.x -= 5;
        }
        top.setFrame(top.x, top.y, top.width, top.height);
    }

    public void dragged() {///movement of the disk logic

        if (top == null) {

            from = q.remove();
            to = p.remove();
            top = stack[from].peek();
            top_color = color[from].peek();

            draggable = true;

            stack[from].pop();
            color[from].pop();

            if (from > to) {
                firstTime = true;
            }

            if ((to == 1 && from < to && from == 0) || (to == 2 && from < to && from == 1)) {
                ax = top.x + 200;
            } else if (to == 2 && from < to && from == 0) {
                ax = top.x + 400;
            } else if (to == 0 && from > to && from == 2) {
                ax = top.x - 400;
            } else {
                ax = top.x - 200;
            }
        } else if (top.y == ay && !ab) {

            ab = true;

        } else if (top.x == ax) {
            double yy;
            double xx = 0;
            if (stack[to].empty()) {
                yy = 395;
            } else {
                yy = stack[to].peek().y - top.height - 2;
            }
            top.setFrame(top.x, yy, top.width, top.height);
            stack[to].push(top);
            color[to].push(top_color);

            draggable = false;
            top = null;
            ab = false;
            count++;

            firstTime = false;
            if (q.size() == 0) {///if all the moves are done then it will the true and line 119 will be execuited
                stop = true;
            }
        }

        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (!stop) {
            dragged();
        } else if (stop && count == 1) {
            repaint();
        }

    }

    public void paint(Graphics g) {//for removing double buffer
        dbimage = createImage(getWidth(), getHeight());
        dbg = dbimage.getGraphics();

        paintComponent(dbg);
        g.drawImage(dbimage, 0, 0, this);

    }

    public void paintComponent(Graphics g) {///painting disk 
        Graphics2D g2d = (Graphics2D) g;

        slatetp = new TexturePaint(slate, new Rectangle(0, 0, getWidth(), getHeight()));
        slatetp2 = new TexturePaint(slate2, new Rectangle(0, 0, getWidth(), getHeight()));

        g2d.setPaint(slatetp);
        Rectangle rect = new Rectangle(0, 0, getWidth(), getHeight());
        g2d.fill(rect);
        
        g2d.setColor(Color.WHITE);
        if (!stop) {
            Font font = new Font("Serif", Font.PLAIN, 30);
            g2d.setFont(font);
            g2d.drawString("Moving   :  ", 200, 100);
            String s1 = Integer.toString(from + 1);
            String s2 = Integer.toString(to + 1);
            g2d.drawString(s1, 520-150, 100);
            g2d.drawString("to ", 550-150, 100);
            g2d.drawString(s2, 600-150, 100);
            int b = (int) (double) Math.pow((double) 2, (double) tow);

            s2 = Integer.toString(b  - count );
            g2d.drawString("Moves Remainging :  ", 150, 150);
            
            g2d.drawString(s2, 450, 150);
                
        } else {
            Font font = new Font("Serif", Font.PLAIN, 30);
            g2d.setFont(font);
            g2d.drawString("Game Succesfully Done", 200, 100);
            count++;
        }
        int holder_x = (getWidth()) / 6;
        g2d.setColor(Color.white);
        int holder_y = 415 - (tow * 20 + 40);
        g2d.setStroke(new BasicStroke(10));
        g2d.drawLine(holder_x, 415, holder_x, holder_y);
        g2d.drawLine(holder_x + 200, 415, holder_x + 200, holder_y);
        g2d.drawLine(holder_x + 400, 415, holder_x + 400, holder_y);
        g2d.drawLine(0, 420, getWidth(), 420);

        if (top == null) {
            drawtower(g2d, 0);
            drawtower(g2d, 1);
            drawtower(g2d, 2);
        }
        if (draggable == true && top != null) {
            g2d.setPaint(slatetp2);
            if (!ab) {
                step();
            } else {
                if (firstTime) {
                    step3();
                } else {
                    step2();
                }
            }

            g2d.fill(top);
            drawtower(g2d, 0);
            drawtower(g2d, 1);
            drawtower(g2d, 2);
        }
    }

    private void drawtower(Graphics2D g2d, int n) {
        if (!stack[n].empty()) {
            for (int i = 0; i < stack[n].size(); i++) {
                g2d.setStroke(new java.awt.BasicStroke(3));
                g2d.setColor(Color.BLACK);
                g2d.setPaint(slatetp2);
                g2d.fill(stack[n].get(i));
            }
        }
    }
}
