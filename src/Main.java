import javax.swing.*;
import java.applet.Applet;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Knight's Tale");
        Applet applet = new KnightsTaleEntrance(frame);

        applet.init();  // Calls the init method of the Applet
        applet.start(); // Optional, good practice

        frame.setContentPane(applet);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        double ratio = 9.0 / 16;
        int height = (int) (screen.width * ratio);
        frame.setSize(screen.width, height); // Adjust based on your image

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}