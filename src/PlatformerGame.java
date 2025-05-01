import java.awt.*;

public class PlatformerGame extends GameBase {
    final double GRAVITY = 1.1;

    Player player = new Player(200, 200);
    TileMap map;

    public void initialize() {
        player.setAcceleration(GRAVITY);
        map = new TileMap("map1.txt" , 64);
        map = new TileMap("map2.txt" , 64);
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
        //to enter map to the left
        if( player.x < (TileMap.maps[TileMap.current].getLeftLimit()) && (TileMap.current != 0)) {
        	map.changeMap(TileMap.current - 1);
        	player.x = TileMap.maps[TileMap.current].getRightLimit();
        	Camera.x = TileMap.maps[TileMap.current].getRightLimit() - 1920;
        }
        //to enter the room to the right
        if( player.x > (TileMap.maps[TileMap.current].getRightLimit()) && (TileMap.current != 1)) {
        	map.changeMap(TileMap.current + 1);
        	player.x = TileMap.maps[TileMap.current].getLeftLimit();
        	Camera.x = TileMap.maps[TileMap.current].getLeftLimit();
        }
    }

    public void paint(Graphics pen) {
        player.draw(pen);
        TileMap.maps[TileMap.current].draw(pen);
        Rect[] bounds = TileMap.maps[TileMap.current].getBounds();
        for (Rect bound : bounds) {
            pen.drawRect((int)bound.x, (int)bound.y, bound.w, bound.h);
        }
    }
}
