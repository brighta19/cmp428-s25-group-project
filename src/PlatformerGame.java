import java.awt.*;

public class PlatformerGame extends GameBase {
    final double GRAVITY = 1.1;

    static Player player = new Player(500, 200);
    static Player enemy = new Player(1000, 200); // replace with actual enemies
    Hound dog = new Hound(700, 200);

    TileMap map;

    public void initialize() {
        player.setAcceleration(GRAVITY);
        enemy.setAcceleration(GRAVITY);
        dog.setAcceleration(GRAVITY);
        map = new TileMap("map1.txt" , 64);
        map = new TileMap("map2.txt" , 64);

    }

    public void inGameLoop() {
        player.beforeInput();
        enemy.beforeInput();


        if (player.canCrouch() && (pressing[DN] || pressing[_S])) {
            player.crouch();
        }
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
        if (player.canShootSpell() && pressing[_I]) {
            player.shootSpell();
        }
//        if (pressing[_I]) player.injureBy(new Rect(0,0,0,0), 1);
//        if (pressing[_O]) player.die();
//        if (pressing[_P]) player.revive();

        player.updatePosition();
        enemy.updatePosition();
        dog.update();

        for (int i = 0; i < Spell.spells.size(); i++) {
            Spell s = Spell.spells.get(i);
            s.beforeMove();;

            if (s.done) {
                Spell.spells.remove(s);
                continue;
            }

            s.move();
            if (s.hits(enemy)) {
                s.dispel();
                enemy.injureBy(s, 2);
            }
        }

        if (player.hits(enemy)) {
            player.registerHit(enemy);

            int dmg = player.in_air ? 3 : 1;
            enemy.injureBy(player, dmg);
        }
//        if (enemy hits player) { player.injureBy(enemy, 1); ... }

        player.in_air = true;
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
            if(enemy.overlaps(bounds[i])) {
                if(enemy.cameFromAbove()) {
                    double dy = (enemy.y + enemy.h) - bounds[i].y;
                    enemy.pushBy(0, -dy);
                    enemy.ground();
                }
            }
            if(dog.overlaps(bounds[i])) {
                if(dog.cameFromAbove()) {
                    double dy = (dog.y + dog.h) - bounds[i].y;
                    dog.pushBy(0, -dy);
                    dog.ground();
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
        enemy.draw(pen);
        dog.draw(pen);

        for (int i = 0; i < Spell.spells.size(); i++) {
            Spell.spells.get(i).draw(pen);
//            Spell.spells.get(i).drawBoxes(pen);
        }

        //TileMap.maps[TileMap.current].draw(pen);
        //Rect[] bounds = TileMap.maps[TileMap.current].getBounds();
        //for (Rect bound : bounds) {
        //pen.drawRect((int)bound.x, (int)bound.y, bound.w, bound.h);
        //}
    }
}
