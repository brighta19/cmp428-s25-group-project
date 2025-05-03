import java.awt.*;

public class PlatformerGame extends GameBase {
    final double GRAVITY = 1.1;

    static Player player = new Player(500, 200);

    TileMap map;

    public void initialize() {
        player.setAcceleration(GRAVITY);
        map = new TileMap("map1.txt" , 64);
        map = new TileMap("map2.txt" , 64);
    }

    public void inGameLoop() {
        player.beforeInput();

        if (player.canMove()) {
            if (pressing[LT] || pressing[_A]) {
                player.moveLeft();
            }
            if (pressing[RT] || pressing[_D]) {
                player.moveRight();
            }
        }
        if (player.canJump() && (pressing[UP] || pressing[_W])) {
            player.jump();
        }
        if (player.canAttack() && pressing[_U]) {
            player.attack();
        }
//        if (pressing[_I]) player.injureBy(new Rect(0,0,0,0), 1);
//        if (pressing[_O]) player.die();
//        if (pressing[_P]) player.revive();

        player.updatePosition();

//        if (player.hits(enemy)) { player.registerHit(enemy); ... }
//        if (enemy hits player) { player.injureBy(enemy, 1); ... }

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
                }
            }
        }
        map.checkIfNearEdge(player);
    }

    public void paint(Graphics pen) {
        Camera.moveBy((int)(player.x - player.old_x), 0);

        TileMap.maps[TileMap.current].draw(pen);
        player.draw(pen);
//        player.drawBoxes(pen);

        //TileMap.maps[TileMap.current].draw(pen);
        //Rect[] bounds = TileMap.maps[TileMap.current].getBounds();
        //for (Rect bound : bounds) {
        //pen.drawRect((int)bound.x, (int)bound.y, bound.w, bound.h);
        //}
    }
}
