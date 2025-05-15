import java.awt.*;

public class Enemy extends Sprite{

    static final double INITIAL_HEALTH = 10;
    static final double SPEED = 3;

    public enum State { //Make it static?
        IDLE, ATTACK, HURT, DEATH
    }

//	protected State currentState = State.IDLE;
//    protected boolean facingRight = true;

    int health;
    int hitboxHeight, hitboxWidth;

    double vx = 0;
    double vy = 0;
    double ay = 0;
    int direction = -1;

    boolean inAir = false;
    boolean dying = false;

    public Enemy(String name, double x, double y, int w, int h, String[] poses, int[] counts, int duration) {
        super(name, x, y, w, h, poses, counts, duration);
    }

    public void setAcceleration(double ay) {
        this.ay = ay;
    }

    public void move() {
        vy += ay;
        x += vx;
        y += vy;
    }

    public void moveLeft() {
        vx = -SPEED;
        direction = -1;
    }

    public void moveRight() {
        vx = SPEED;
        direction = 1;
    }

    public void ground() {
        vy = 0;
        inAir = false;
    }

    public void update() {
        move();
    }

    public Rect getHitbox() {
        double hitboxX = x + (direction < 0 ? -hitboxWidth : w);
        return new Rect(hitboxX, y, hitboxWidth, hitboxHeight);
    }

    public boolean hits (Rect target){
        return getHitbox().overlaps(target);
    }

    public void injure(Rect target, int damage) {
        health -= damage;
        if (health <= 0) {
            die();
        }
    }

    public void die() {
        dying = true;
    }

    public boolean isDying() {
        return dying;
    }

    public void updatePose() {
        // Basic enemy pose logic placeholder. Should be overridden by subclasses.
        if (vx != 0) {
            moving = true;
        } else {
            moving = false;
        }
    }

    @Override
    public void draw(Graphics pen) {
        updatePose();

        Image img = animation[pose].nextImage();

        int drawX = (int)x - Camera.x;
        int drawY = (int)y - Camera.y;

        if (direction == -1) {
            pen.drawImage(img, drawX + w, drawY, -w, h, null); // flipped
        } else {
            pen.drawImage(img, drawX, drawY, w, h, null);      // normal
        }
    }
    
//    public void setState(State newState) {
//        currentState = newState;
//        updatePose();
//    }

}
