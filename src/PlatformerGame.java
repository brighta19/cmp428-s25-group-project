import java.awt.*;
import java.util.ArrayList;

public class PlatformerGame extends GameBase {
    final double GRAVITY = 1.1;

    static Player player = new Player(500, 200);
    static Player enemy = new Player(1000, 200); // replace with actual enemies

    ArrayList<Enemy> enemies = new ArrayList<>(); // to add multiple enemies

    TileMap map;

    public void initialize() {
        player.setAcceleration(GRAVITY);
        enemy.setAcceleration(GRAVITY);

        Hound dog = new Hound(1200, 200, player);
        Rogue rog = new Rogue(1500, 200, player);
        enemies.add(dog);
        enemies.add(rog);

        for (Enemy e : enemies) {
            e.setAcceleration(GRAVITY);
        }

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

//        for (Enemy e: enemies){
//            e.update();
//        }

        for (int i = 0; i < Spell.spells.size(); i++) {
            Spell s = Spell.spells.get(i);
            s.beforeMove();;

            if (s.done) {
                Spell.spells.remove(s);
                continue;
            }

            if (s.dispelling) continue;

            s.move();

            // check if it hits an enemy
            for (Enemy e : enemies) {
                if (!e.isDying() && s.hits(e)) {
                    e.injure(s, 2); // for example, deal 3 damage
                    s.dispel();     // remove or trigger the end of the spell
                }
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

        for (Enemy e : enemies) {
            if (player.hits(e)) {
                e.injure(player, 2);
            }
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
            for (Enemy e : enemies) {
                if (e.overlaps(bounds[i])) {
                    if (e.cameFromAbove()) {
                        double dy = (e.y + e.h) - bounds[i].y;
                        e.pushBy(0, -dy);
                        e.ground();
                    }
                }
            }
        }

        // remove dead enemies
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            e.update();

            if (e.isDead()) {
                enemies.remove(i);
                i--; // avoid skipping next enemy
            }
        }

        map.checkIfNearEdge(player);
    }

    public void paint(Graphics pen) {
        Camera.moveBy((int)(player.x - player.old_x), 0);

        TileMap.maps[TileMap.current].draw(pen);
        player.draw(pen);
        player.drawBoxes(pen);
        enemy.draw(pen);

        // draw multiple enemies
        for (Enemy e : enemies) {
            e.draw(pen);
            e.drawBoxes(pen);
        }

        for (int i = 0; i < Spell.spells.size(); i++) {
            Spell.spells.get(i).draw(pen);
//            Spell.spells.get(i).drawBoxes(pen);
        }

        drawPlayerHealthBar(pen);

        //TileMap.maps[TileMap.current].draw(pen);
        //Rect[] bounds = TileMap.maps[TileMap.current].getBounds();
        //for (Rect bound : bounds) {
        //pen.drawRect((int)bound.x, (int)bound.y, bound.w, bound.h);
        //}
    }

    public void drawPlayerHealthBar(Graphics pen) {
        int width = 300;
        pen.setColor(Color.WHITE);
        pen.fillRect(20, 20, (int) width, 40);
        pen.setColor(Color.RED);
        pen.fillRect(20, 20, (int) (player.getHealthPercentage() * width), 40);
    }
}
