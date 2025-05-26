import java.awt.*;
import java.util.ArrayList;

public class PlatformerGame extends GameBase {
    static int PLAYER_SPAWN_X = 500;
    static int PLAYER_SPAWN_Y = 200;
    final double GRAVITY = 1.1;
    boolean isGameWon = false;

    int level = 1;
    boolean isGameOver = false;
    Button restartButton;
    Button exitButton;

    static Player player = new Player(PLAYER_SPAWN_X, PLAYER_SPAWN_Y);
    ArrayList<Enemy> enemies = new ArrayList<>(); // to add multiple enemies

    TileMap map;

    public PlatformerGame(int width, int height) {
        super(width, height);
    }

    public void initialize() {
        player.setAcceleration(GRAVITY);

        Hound dog = new Hound(1200, 200, player);
        Rogue rog = new Rogue(1500, 200, player);
        Reaper reaper = new Reaper(2000,200 ,player);

        enemies.add(reaper);
        enemies.add(dog);
        enemies.add(rog);

        for (Enemy e : enemies) {
            e.setAcceleration(GRAVITY);
        }

        map = new TileMap("map1.txt" , 64);
        map = new TileMap("map2.txt" , 64);
        map = new TileMap("map3.txt" , 64);
        level = TileMap.current + 1;

        int centerX = WIDTH / 2 - 100;
        restartButton = new Button(centerX, HEIGHT / 2 - 50, 200, 50, "Restart");
        exitButton = new Button(centerX, HEIGHT / 2 + 20, 200, 50, "Exit");
    }

    public void inGameLoop() {
        if (isGameOver) return; // ⛔ Stop updating logic if game is over

        player.beforeInput();

        if (player.dying || player.y > 1080) {
            isGameOver = true;
            return;
        }

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

        player.updatePosition();

        for (int i = 0; i < Spell.spells.size(); i++) {
            Spell s = Spell.spells.get(i);
            s.beforeMove();

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

        player.in_air = true;

        for (Enemy e : enemies) {
            if (player.hits(e)) {
                player.registerHit(e);
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
        if (TileMap.current == 2 && player.x > map.getRightLimit() - 50) {
            isGameWon = true;
            isGameOver = true; // Optional: freeze game logic
        }

    }

    public void paint(Graphics pen) {
        if (player.x > PLAYER_SPAWN_X && player.x < TileMap.maps[TileMap.current].getRightLimit() - (WIDTH - PLAYER_SPAWN_X))
            Camera.moveBy((int)(player.x - player.old_x), 0);


        TileMap.maps[TileMap.current].draw(pen);
        player.draw(pen);

        // draw multiple enemies
        for (Enemy e : enemies) {
            e.draw(pen);
        }

        for (int i = 0; i < Spell.spells.size(); i++) {
            Spell.spells.get(i).draw(pen);
        }

        drawPlayerHealthBar(pen);
        pen.setColor(Color.YELLOW);//lol
        pen.setFont(new Font("Arial", Font.BOLD, 36));
        pen.drawString("Level " + level, WIDTH - 200, 50);

        if (isGameOver) {
            pen.setColor(new Color(0, 0, 0, 180));
            pen.fillRect(0, 0, WIDTH, HEIGHT);

            pen.setColor(Color.WHITE);
            pen.setFont(new Font("Arial", Font.BOLD, 48));

            if (isGameWon) {
                pen.drawString("YOU WON!", WIDTH / 2 - 140, 300);
            } else {
                pen.drawString("Game Over", WIDTH / 2 - 150, 300);
            }

            restartButton.draw(pen);
            exitButton.draw(pen);
        }


    }
    public void resetGame() {
        player.revive();
        teleportPlayerToStart();
        isGameOver = false;
        isGameWon = false;
        level = 1;
        TileMap.current = 0;
        Camera.reset();
        respawnEnemies();
    }



    public void drawPlayerHealthBar(Graphics pen) {
        int width = 300;
        pen.setColor(Color.WHITE);
        pen.fillRect(20, 20, (int) width, 40);
        pen.setColor(Color.RED);
        pen.fillRect(20, 20, (int) (player.getHealthPercentage() * width), 40);
    }

    public void teleportPlayerToStart() {
        player.x = player.old_x = PLAYER_SPAWN_X;
        player.y = player.old_y = PLAYER_SPAWN_Y;
    }

    public void respawnEnemies() {
        enemies.clear();
        Hound dog = new Hound(1200, 100, player);
        Rogue rog = new Rogue(1500, 100, player);
        Reaper reaper = new Reaper(1000,200 ,player);

        enemies.add(reaper);
        enemies.add(dog);
        enemies.add(rog);

        for (Enemy e : enemies) {
            e.setAcceleration(GRAVITY);
        }
    }
}
