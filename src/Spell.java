import java.awt.*;
import java.util.ArrayList;

public class Spell extends Sprite {
    static int SPEED = 12;
    static int MAX_TRAVEL_DISTANCE = 800;

    static ArrayList<Spell> spells = new ArrayList<>();

    static int SPRITE_WIDTH = 32;
    static int SPRITE_HEIGHT = 32;

    static int ANIMATION_DURATION = 3;
    enum Pose { TRAVEL, HIT }

    static String[] poses = { "travel", "hit" };
    static int[] count = { 4, 6 };

    int direction;
    int distanceTraveled = 0;
    boolean dispelling = false;
    int dispelDelay = 0;
    boolean done = false;

    public static void shoot(double x, double y, int direction) {
        spells.add(new Spell(x, y, direction));
    }

    public Spell(double x, double y, int direction) {
        super("spell/spell", x, y, 64, 28, poses, count, ANIMATION_DURATION);
        this.direction = direction;

        setPose(Pose.TRAVEL.ordinal(), true, true);
    }

    public void beforeMove() {
        if (dispelling) {
            if (dispelDelay == 0) {
                done = true;
                dispelling = false;
            }
            else {
                dispelDelay--;
            }
        }

        if (!dispelling && distanceTraveled > MAX_TRAVEL_DISTANCE) {
            dispel();
        }
    }

    public void move() {
        if (dispelling) return;

        moveBy(direction * SPEED, 0);
        distanceTraveled += SPEED;
    }

    public void dispel() {
        dispelling = true;

        int p = Pose.HIT.ordinal();
        dispelDelay = ANIMATION_DURATION * count[p];
        setPose(p, true, false);
    }

    public boolean hits(Rect target) {
        if (dispelling) return false;
        return overlaps(target);
    }

    public void draw(Graphics pen) {
        int offset_x = direction < 0 ? 84 : -20;
        int offset_y = -56;
        pen.drawImage(
                getImage(),
                (int)(x + offset_x) - Camera.x,
                (int)(y + offset_y) - Camera.y,
                direction * SPRITE_WIDTH * 4,
                SPRITE_HEIGHT * 4,
                null);
    }

    public void drawBoxes(Graphics pen) {
        pen.drawRect((int)x - Camera.x, (int)y - Camera.y, w, h);
    }
}
