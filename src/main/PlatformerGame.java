package main;
import java.awt.*;

public class PlatformerGame extends GameBase {
    public void initialize() {}

    public void inGameLoop() {}

    public void paint(Graphics pen) {
        pen.drawRect(50, 50, 100, 100);
        pen.drawString("Hello, world!", 50, 20);
    }
}
