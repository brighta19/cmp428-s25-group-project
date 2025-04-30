// TODO: change to Sprite after getting images
public class Player extends Rect {
    public static double SPEED = 14;
    final double JUMP = 22;

    double vx = 0;
    double vy = 0;

    double ay = 0;
    boolean jumping = false;

    public Player(double x, double y) {
        super(x, y, 100, 100);
    }

    public void updatePosition() {
        vy += ay;

        old_x = x;
        old_y = y;

        x += vx;
        y += vy;

        vx = 0; // stop moving when LT/RT isn't pressed
    }

    public void moveLeft() {
        vx = -SPEED;
    }

    public void moveRight() {
        vx = SPEED;
    }

    public void jump() {
        vy = -JUMP;
        jumping = true;
    }

    public void ground() {
        vy = 0;
        jumping = false;
    }

    public void setAcceleration(double ay) {
        this.ay = ay;
    }
}
