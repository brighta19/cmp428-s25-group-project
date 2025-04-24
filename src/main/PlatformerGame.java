package main;
import java.awt.*;

public class PlatformerGame extends GameBase {
    Player player = new Player(50, 50);

    public void initialize() {}

    public void inGameLoop() {
        if (pressing[UP]) player.moveUp(10);
        if (pressing[DN]) player.moveDown(10);
        if (pressing[LT]) player.moveLeft(10);
        if (pressing[RT]) player.moveRight(10);
    }

    public void paint(Graphics pen) {
        player.draw(pen);
    }
}
