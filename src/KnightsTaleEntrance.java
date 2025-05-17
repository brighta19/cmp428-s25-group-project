import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

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
            PlatformerGame game = new PlatformerGame();

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


