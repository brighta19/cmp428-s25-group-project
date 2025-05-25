import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.Button;

public class KnightsTaleEntrance extends Applet implements ActionListener {
    private Image background;
    private Button startButton, exitButton;
    JFrame frame;

    public KnightsTaleEntrance(JFrame frame) {
        this.frame = frame;
    }

    public void init() {
        setLayout(null); // Use absolute positioning

        background = Toolkit.getDefaultToolkit().getImage(getClass().getResource("Castle.png"));

        startButton = new Button("Start Game");
        exitButton = new Button("Exit");

        startButton.setBounds(350, 20, 100, 30);
        exitButton.setBounds(460, 20, 60, 30);

        add(startButton);
        add(exitButton);

        startButton.addActionListener(this);
        exitButton.addActionListener(this);
    }

    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            // Proceed to the next screen
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            double ratio = 9.0 / 16;
            int height = (int) (screen.width * ratio);
            PlatformerGame game = new PlatformerGame(screen.width, height);

            game.setSize(frame.getSize()); // match parent size
            frame.setContentPane(game);       // Add new content (enemy screen)
            game.init();
            game.start();

            frame.getContentPane().removeAll();      // Remove all old components (castle)
            frame.revalidate();                      // Refresh layout
            frame.repaint();                         // Force redraw

        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }
}


