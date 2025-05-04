import java.awt.*;
import java.util.ArrayList;

public class Player extends Sprite {
    public static final double SPEED = 8;
    static final double JUMP = 22;

    static int INITIAL_HEALTH = 15;
    static int HURT_DURATION = 20;

    static int KNOCKBACK_X = 5;
    static int KNOCKBACK_Y = 10;
    static int CROUCH_SHRINK_VALUE = 40;

    static int HITBOX_WIDTH = 80;
    static int HITBOX_NORMAL_HEIGHT = 108;
    static int HITBOX_JUMP_HEIGHT = 80;

    static int SPRITE_WIDTH = 80;
    static int SPRITE_HEIGHT = 64;

    static int ANIMATION_DURATION = 3;
    enum Pose {
        IDLE, RUN, JUMP, JUMP_ATTACK_1, JUMP_ATTACK_2, CROUCH, CROUCH_ATTACK, ATTACK_1, ATTACK_2, ATTACK_3, HURT, DEATH
    }

    static String[] poses = {
            "idle", "run", "jump", "jump-attack1", "jump-attack2", "crouch", "crouch-attack", "attack1", "attack2", "attack3", "hurt", "death"
    };
    static int[] count = { 8, 8, 9, 6, 5, 3, 10, 7, 5, 8, 2, 6 };

    double vx = 0;
    double vy = 0;

    double ay = 0;
    int direction = 1;

    boolean jumping = false;
    boolean crouching = false;
    boolean attacking = false;
    boolean hurting = false;
    boolean dying = false;

    int attackDelay = 0;
    int attackType = 1;
    boolean canCombo = false;
    ArrayList<Rect> targetsAlreadyHit = new ArrayList<>();

    int health = INITIAL_HEALTH;
    int hurtDelay = 0;

    public Player(double x, double y) {
        super("warrior", x, y, 60, 108, poses, count, ANIMATION_DURATION);
    }

    public void updatePosition() {
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

    public void jump() {
        vy = -JUMP;
        jumping = true;

        if (crouching) {
            stopCrouching();
        }

        if (attacking) {
            resetAttack();
        }
    }

    public void crouch() {
        if (crouching) return;

        crouching = true;
        y += CROUCH_SHRINK_VALUE;
        h -= CROUCH_SHRINK_VALUE;

        if (attacking) {
            resetAttack();
        }
    }

    private void stopCrouching() {
        if (!crouching) return;

        crouching = false;
        y -= CROUCH_SHRINK_VALUE;
        h += CROUCH_SHRINK_VALUE;

        if (attacking) {
            attacking = false;
            canCombo = false;
        }

        // fixes a bug with performing multiple crouch attacks
        updatePose();
    }

    public void ground() {
        if (vy > 0) vy = 0;
        jumping = false;
    }

    public void attack() {
        if (canCombo) {
            attackType++;
        }
        else {
            attackType = 1;
        }

        attacking = true;
        targetsAlreadyHit.clear();
        calculateAttackDelay();

//        if (vx != 0 && !jumping) {
//            // moves forward according to the length of the attack animation (nice for combos)
//            x += direction * (SPEED * attackDelay/ANIMATION_DURATION);
//        }
    }

    public void calculateAttackDelay() {
        int p;
        if (crouching) {
            p = Pose.CROUCH_ATTACK.ordinal();
        }
        else if (jumping) {
            if (attackType == 1)
                p = Pose.JUMP_ATTACK_1.ordinal();
            else
                p = Pose.JUMP_ATTACK_2.ordinal();
        }
        else {
            if (attackType == 1)
                p = Pose.ATTACK_1.ordinal();
            else if (attackType == 2)
                p = Pose.ATTACK_2.ordinal();
            else
                p = Pose.ATTACK_3.ordinal();
        }

        attackDelay = ANIMATION_DURATION * count[p];
    }

    private void resetAttack() {
        canCombo = false;
        attack();
    }

    public boolean canMove() {
        return !dying && !hurting && !crouching && (!attacking || jumping);
    }

    public boolean canJump() {
        return !dying && !hurting && !jumping;
    }

    public boolean canCrouch() {
        return !dying && !hurting && !jumping;
    }

    public boolean canAttack() {
        return !dying && !hurting && !attacking;
    }

    public void setAcceleration(double ay) {
        this.ay = ay;
    }

    private Rect getHitbox() {
        double hitbox_x = x + (direction < 0 ? -HITBOX_WIDTH : w);
        int hitbox_h = jumping ? HITBOX_JUMP_HEIGHT : HITBOX_NORMAL_HEIGHT;
        return new Rect(
                hitbox_x,
                y,
                HITBOX_WIDTH,
                hitbox_h);
    }

    public boolean hits(Rect target) {
        if (!attacking) return false;
        if (targetsAlreadyHit.contains(target)) return false;

        Rect hitbox = getHitbox();
        return hitbox.overlaps(target);
    }

    public void registerHit(Rect target) {
        targetsAlreadyHit.add(target);
    }

    public void injureBy(Rect attacker, int damage) {
        hurt(damage);

        double player_center_x = x + w/2.0;
        double attacker_center_x = attacker.x + attacker.w/2.0;
        int direction = player_center_x < attacker_center_x ? -1 : 1;
        knockBack(direction);

        if (health == 0) {
            die();
        }
    }

    private void hurt(int damage) {
        health -= damage;
        if (health < 0) health = 0;

        if (!dying) {
            hurting = true;
            hurtDelay = HURT_DURATION;
        }
    }

    private void knockBack(int direction) {
        if (!dying) {
            vx = direction * KNOCKBACK_X;
            this.direction = -direction;
        }

        vy = -KNOCKBACK_Y;
    }

    public void die() {
        dying = true;
    }

    public void revive() {
        dying = false;
        health = INITIAL_HEALTH;
    }

    public void beforeInput() {
        // stop moving in case LT/RT isn't pressed
        if (!hurting) {
            vx = 0;
        }

        if (hurting) {
            if (hurtDelay == 0) {
                hurting = false;
            }
            else {
                hurtDelay--;
            }
        }

        if (attacking) {
            if (attackDelay == 0) {
                attacking = false;

                if (crouching) {
                    canCombo = false;
                }
                else if (jumping && attackType < 2) {
                    canCombo = true;
                }
                else if (attackType < 3) {
                    canCombo = true;
                }
                else {
                    canCombo = false;
                }
            }
            else {
                attackDelay--;
            }
        }
        else {
            canCombo = false;
        }

        if (crouching && !attacking) {
            stopCrouching();
        }

        old_x = x;
        old_y = y;
    }

    private void updatePose() {
        Pose p;
        boolean repeats = false;

        if (dying) {
            p = Pose.DEATH;
        }
        else if (hurting) {
            p = Pose.HURT;
            repeats = true;
        }
        else if (attacking) {
            if (crouching) {
                p = Pose.CROUCH_ATTACK;
            }
            else if (jumping) {
                if (attackType == 2) {
                    p = Pose.JUMP_ATTACK_2;
                } else {
                    p = Pose.JUMP_ATTACK_1;
                }
            }
            else {
                switch (attackType) {
                    case 2:
                        p = Pose.ATTACK_2;
                        break;
                    case 3:
                        p = Pose.ATTACK_3;
                        break;
                    default:
                        p = Pose.ATTACK_1;
                }
            }
        }
        else if (jumping) {
            p = Pose.JUMP;
        }
        else if (crouching) {
            p = Pose.CROUCH;
        }
        else if (vx != 0) {
            p = Pose.RUN;
            repeats = true;
        }
        else {
            p = Pose.IDLE;
            repeats = true;
        }

        setPose(p.ordinal(), true, repeats);
    }

    public void draw(Graphics pen) {
        updatePose();

        int offset_x = direction < 0 ? 200 : -140;
        int offset_y = crouching ? -124 : -84;
        pen.drawImage(
                getImage(),
                (int)(x + offset_x) - Camera.x,
                (int)(y + offset_y) - Camera.y,
                direction * SPRITE_WIDTH * 4,
                SPRITE_HEIGHT * 4,
                null);
    }

    public void drawBoxes(Graphics pen) {
        // draw the box of the body
        pen.setColor(new Color(0, 0, 255, 50));
        pen.fillRect((int)x - Camera.x, (int)y - Camera.y, w, h);

        // draw the hitbox
        if (attacking) {
            pen.setColor(new Color(255, 0, 0, 50));
            Rect hb = getHitbox();
            pen.fillRect((int)hb.x - Camera.x, (int)hb.y - Camera.y, hb.w, hb.h);
        }
    }
}
