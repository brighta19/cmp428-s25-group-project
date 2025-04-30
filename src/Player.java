import java.awt.*;

public class Player extends Sprite {
    static int SPRITE_WIDTH = 80;
    static int SPRITE_HEIGHT = 64;

    static String[] poses = {"idle", "run", "jump", "attack1", "attack2", "hurt", "death"};
    static int[] count = {7, 7, 10, 5, 4, 2, 5};

    enum Pose {
        IDLE, RUN, JUMP, ATTACK_1, ATTACK_2, HURT, DEATH
    }

    public static final double SPEED = 8;
    static final double JUMP = 22;

    double vx = 0;
    double vy = 0;

    double ay = 0;
    int direction = 1;

    boolean jumping = false;
    boolean attacking = false;

    public Player(double x, double y) {
        super("warrior", x, y, 60, 108, poses, count, 10);
    }

    public void updatePosition() {
        vy += ay;

        old_x = x;
        old_y = y;

        x += vx;
        y += vy;
    }

    public void moveLeft() {
        vx = -SPEED;
        direction = -1;

        if (!jumping)
            pose = Pose.RUN.ordinal();
    }

    public void moveRight() {
        vx = SPEED;
        direction = 1;

        if (!jumping)
            pose = Pose.RUN.ordinal();
    }

    public void jump() {
        vy = -JUMP;
        jumping = true;
        pose = Pose.JUMP.ordinal();
    }

    public void ground() {
        vy = 0;
        jumping = false;
    }

    public void setAcceleration(double ay) {
        this.ay = ay;
    }

    public void draw(Graphics pen) {
        Image image = getImage();

        int offset_x = direction < 0 ? 200 : -140;

        pen.drawImage(
                image,
                (int)x + offset_x,
                (int)y - 84,
                direction * SPRITE_WIDTH * 4,
                SPRITE_HEIGHT * 4,
                null);

        // draw the body
        pen.drawRect((int)x, (int)y, w, h);

        // stop moving when LT/RT isn't pressed
        vx = 0;
        if (!jumping)
            pose = Pose.IDLE.ordinal();
    }
}
