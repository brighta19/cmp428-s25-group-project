import java.awt.*;
import java.util.ArrayList;

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
    int hitboxOffsetX, hitboxOffsetY = 0;
    int attackDelay = 0;
    int attackCooldown = 60;
    int attackAnimationTime = 0;
    int hurtDelay = 0;
    int HURT_DURATION = 20;

    double vx = 0;
    double vy = 0;
    double ay = 0;
    int direction = -1;

    boolean inAir = false;
    boolean dying = false;
    boolean attacking = false;
    boolean hurting = false;
    boolean hasHitPlayer = false;

    private Player player;

    ArrayList<Rect> targetsAlreadyHit = new ArrayList<>();

    public Enemy(String name, double x, double y, int w, int h, String[] poses, int[] counts, int duration) {
        super(name, x, y, w, h, poses, counts, duration);
        health = (int) INITIAL_HEALTH;
    }

    public void setAcceleration(double ay) {
        this.ay = ay;
    }

    public void setPlayer(Player p) {
        this.player = p;
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
        if (vy > 0) vy = 0;
        inAir = false;
    }

    public void update() {
        if (hurting) {
            hurtDelay--;
            if (hurtDelay <= 0) {
                hurting = false;
            }
            vy += ay;
            x += vx;
            y += vy;
        }
        else if (!dying) {
            move();
        }

        if (attacking) {
            attackAnimationTime--;
            if (attackAnimationTime <= 0) {
                attacking = false;
            }
        }
        if (attackDelay > 0) {
            attackDelay--;
        }

//        updatePose();
    }

    public void attackPlayer(Player player) {
        if (attacking && !hasHitPlayer && getHitbox().overlaps(player)) {
            player.injureBy(this, 1);
            hasHitPlayer = true;
        }
    }


    public Rect getHitbox() {
        double hitboxX = x + (direction < 0 ? -hitboxWidth : hitboxWidth);
        double hitboxY = y + hitboxOffsetY;
        return new Rect(hitboxX, hitboxY, hitboxWidth, hitboxHeight);
    }

    public boolean hits (Rect target){
        return getHitbox().overlaps(target);
    }

    private void knockBack(int direction) {
        if (!dying) {
            vx = direction * 5;
            this.direction = -direction;
        }
        vy = -10;
    }

    public void injure(Rect attacker, int damage) {
        if (!hurting && !dying) {
            health -= damage;
            hurting = true;
            hurtDelay = HURT_DURATION;

            double center_x = x + w/2.0;
            double target_center_x = attacker.x + attacker.w/2.0;
            int kbDirection = (target_center_x < center_x) ? 1 : -1;
            knockBack(kbDirection);

            if (health <= 0) {
                die();
            }
        }
    }

    public void die() {
        dying = true;
        vx = 0;
    }

    public boolean isDying() {
        return dying;
    }

    public boolean isDead() {
        return dying && isAnimationDone();
    }

    public boolean isAnimationDone() {
        return animation[pose].done;
    }

    public void updatePose() {
        // Basic enemy pose logic placeholder. Should be overridden by subclasses.
        if (vx != 0) {
            moving = true;
        } else {
            moving = false;
        }
    }

    public boolean canAttack(Rect target) {
        return getHitbox().overlaps(target) && attackDelay <= 0 && !attacking;
    }

    public void tryAttack(Rect target) {
        if (canAttack(target)) {
            attacking = true;
            hasHitPlayer = false;
            attackAnimationTime = 20;
            attackDelay = attackCooldown;
        }
    }

    public Rect getAttackHitbox() {
        double hitboxX = x + (direction < 0 ? -hitboxWidth : w);
        return new Rect(hitboxX, y, hitboxWidth, hitboxHeight);
    }

    public void attack() {
        if (!attacking && attackDelay == 0) {
            attacking = true;
            attackDelay = 30; // total duration of attack (in frames)
            targetsAlreadyHit.clear();
        }
    }

//    public boolean hits(Rect target) {
//        if (!attacking || targetsAlreadyHit.contains(target)) return false;
//
//        return getAttackHitbox().overlaps(target);
//    }

    public void registerHit(Rect target) {
        targetsAlreadyHit.add(target);
    }

    @Override
    public void draw(Graphics pen) {
        updatePose();

        Image img = animation[pose].nextImage();

        int drawX = (int)x - Camera.x;
        int drawY = (int)y - Camera.y;

        if (direction == 1) {
            pen.drawImage(img, drawX + w, drawY, -w, h, null); // flipped
        } else {
            pen.drawImage(img, drawX, drawY, w, h, null);      // normal
        }
    }

    public void drawBoxes(Graphics pen) {
        // Draw body
        pen.setColor(new Color(0, 255, 0, 50));
        pen.fillRect((int)x - Camera.x, (int)y - Camera.y, w, h);

        // Draw attack hitbox if attacking
        if (attacking) {
            pen.setColor(new Color(255, 0, 0, 50));
            Rect atkBox = getHitbox();
            pen.fillRect((int)atkBox.x - Camera.x, (int)atkBox.y - Camera.y, atkBox.w, atkBox.h);
        }
    }
//    public void setState(State newState) {
//        currentState = newState;
//        updatePose();
//    }

}
