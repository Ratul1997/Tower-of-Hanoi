import java.awt.event.*;
import javax.swing.*;
public class MouseMove extends JPanel implements ActionListener {
    private static JFrame f = new JFrame();
    private static Move21 t;
    private static Move y;
    public static boolean ab = false, play = false, anime = false;
    private static JMenuBar menu_bar = new JMenuBar();
    private JMenuItem new_game = new JMenuItem("New Game"),
            best_time = new JMenuItem("Animatioin"),
            exit = new JMenuItem("Exit"),
            about = new JMenuItem("About.......");

    private JMenu help = new JMenu("Help"),
            game = new JMenu("Game");
    public MouseMove(String title) {
        f.setTitle(title);
        build();
    }
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == new_game) {
            Object values[] = {3, 4, 5, 6, 7, 8, 9};
            Object val = JOptionPane.showInputDialog(f, "No. Of Disks : ", "Input",
                    JOptionPane.INFORMATION_MESSAGE, null, values, values[0]);
            if ((int) val != JOptionPane.CANCEL_OPTION) {
                if (play == false && anime == true) {
                    play = true;
                    anime = false;

                    f.remove(y);
                    t = new Move21((int) val);
                    f.add(t);
                    f.validate();
                    f.repaint();
                } else {
                    t.init((int) val);
                }
            }
        }
        if (ev.getSource()
                == best_time) {
            System.out.println("animaiton");

            Object values[] = {3, 4, 5, 6, 7, 8, 9};
            Object val = JOptionPane.showInputDialog(f, "No. Of Disks : ", "Input",
                    JOptionPane.INFORMATION_MESSAGE, null, values, values[0]);
            if ((int) val != JOptionPane.CANCEL_OPTION) {
                if (play == true && anime == false) {

                    System.out.println("1");
                    play = false;
                    anime = true;

                    f.remove(t);
                    y = new Move((int) val);
                    f.add(y);
                    f.validate();
                    f.repaint();
                } else {

                    System.out.println("2");
                    y.timer.stop();
                    f.remove(y);
                    y = new Move((int) val);
                    f.add(y);
                    f.validate();
                    f.repaint();
                }
            }
        }

        if (ev.getSource() == about) {
            System.out.println("a");
            JFrame frame2 = new JFrame("Developer");
            frame2.setLocation(300,200);
            frame2.setContentPane(new JLabel(new ImageIcon(getClass().getResource("background.jpg"))));
            frame2.setLayout(null);
            ///frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame2.add(new PIC());
            frame2.setSize(450, 250);
            frame2.setResizable(false);
            frame2.setVisible(true);

        } else if (ev.getSource()
                == exit) {
            System.out.println("2");
            System.exit(0);
        }
    }

    private void build() {

        game.add(new_game);
        game.add(best_time);
        game.add(exit);
        help.add(about);

        menu_bar.add(game);
        menu_bar.add(help);

        new_game.addActionListener(this);
        best_time.addActionListener(this);
        exit.addActionListener(this);
        about.addActionListener(this);

        f.setJMenuBar(menu_bar);
        f.setSize(660, 500);
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                () -> {
                    MouseMove obj = new MouseMove("Tower Of Hanoi v1.0");
                    t = new Move21(2);
                    play = true;
                    f.add(t);
                });
    }
}
