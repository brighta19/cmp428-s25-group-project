import java.awt.*;

public class PlatformerGame extends GameBase {
    final double GRAVITY = 1.1;

    Player player = new Player(200, 200);
    TileMap map = new TileMap("map1.txt" , 64);

    public void initialize() {
        player.setAcceleration(GRAVITY);
    }

    public void inGameLoop() {
        if (player.canMove()) {
            if (pressing[LT] || pressing[_A]) {
                player.moveLeft();
                Camera.moveLeft((int) Player.SPEED);
            }
            if (pressing[RT] || pressing[_D]) {
                player.moveRight();
                Camera.moveRight((int) Player.SPEED);
            }
        }
        if (player.canJump() && (pressing[UP] || pressing[_W])) {
            player.jump();
        }
        if (player.canAttack() && pressing[_U]) {
            player.attack();
        }

        player.updatePosition();

        if (player.y + player.h > 1000) {
            player.y = 1000 - player.h;
            player.ground();
        }

        Rect[] bounds = TileMap.maps[TileMap.current].getBounds();

        for(int i = 0; i < bounds.length; i++) {
            if(player.overlaps(bounds[i])) {
                if(player.cameFromAbove()) {
                    double dy = (player.y + player.h) - bounds[i].y;
                    player.pushBy(0, -dy);
                    player.ground();

                    Camera.moveDown(10);
                }
            }
        }
    }

    public void paint(Graphics pen) {
        player.draw(pen);

        Rect[] bounds = TileMap.maps[TileMap.current].getBounds();
        for (Rect bound : bounds) {
            pen.drawRect((int)bound.x, (int)bound.y, bound.w, bound.h);
        }
    }
}
