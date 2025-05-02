import java.awt.*;

public class Player extends Sprite {
    static int SPRITE_WIDTH = 80;
    static int SPRITE_HEIGHT = 64;

    static int ANIMATION_DURATION = 3;
    enum Pose {
        IDLE, RUN, JUMP, JUMP_ATTACK_1, JUMP_ATTACK_2, ATTACK_1, ATTACK_2, ATTACK_3, HURT, DEATH
    }

    static String[] poses = {
            "idle", "run", "jump", "jump-attack1", "jump-attack2", "attack1", "attack2", "attack3", "hurt", "death"
    };
    static int[] count = {8, 8, 11, 6, 5, 7, 5, 8, 2, 6};

    public static final double SPEED = 8;
    static final double JUMP = 22;

    double vx = 0;
    double vy = 0;

    double ay = 0;
    int direction = 1;

    boolean jumping = false;
    boolean attacking = false;

    int attackDelay = 0;
    int attackType = 1;
    boolean canCombo = false;

    public Player(double x, double y) {
        super("warrior", x, y, 60, 108, poses, count, ANIMATION_DURATION);
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
    }

    public void moveRight() {
        vx = SPEED;
        direction = 1;
    }

    public void jump() {
        vy = -JUMP;
        jumping = true;
    }

    public void ground() {
        vy = 0;
        jumping = false;
    }

    public void attack() {
        if (canCombo) {
            if (attackType == 1) {
                attackType = 2;
            }
            else if (attackType == 2) {
                if (jumping) {
                    canCombo = false;
                    return; // can't combo after 2nd attack when jumping
                }
                else {
                    attackType = 3;
                }
            }
            else {
                canCombo = false;
                return; // can't combo after 3rd attack
            }
        }
        else {
            attackType = 1;
        }

        attacking = true;
        calculateAttackDelay();
    }

    public void calculateAttackDelay() {
        int p;
        if (jumping) {
            if (attackType == 1)
                p = Pose.JUMP_ATTACK_1.ordinal();
            else
                p = Pose.JUMP_ATTACK_1.ordinal();
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

    public boolean canMove() {
        return !attacking || jumping;
    }

    public boolean canJump() {
        return !jumping;
    }

    public boolean canAttack() {
        return !attacking;
    }

    public void setAcceleration(double ay) {
        this.ay = ay;
    }

    public void beforeInput() {
        // stop moving in case LT/RT isn't pressed
        vx = 0;

        if (attacking) {
            if (attackDelay == 0) {
                attacking = false;

                if (attackType < 3)
                    canCombo = true;
            }
            else {
                attackDelay--;
            }
        }
        else {
            canCombo = false;
        }
    }

    private void updatePose() {
        if (attacking) {
            if (jumping) {
                if (attackType == 2) {
                    setPose(Pose.JUMP_ATTACK_2.ordinal(), true);
                } else {
                    setPose(Pose.JUMP_ATTACK_1.ordinal(), true);
                }
            }
            else {
                switch (attackType) {
                    case 2:
                        setPose(Pose.ATTACK_2.ordinal(), true);
                        break;
                    case 3:
                        setPose(Pose.ATTACK_3.ordinal(), true);
                        break;
                    default:
                        setPose(Pose.ATTACK_1.ordinal(), true);
                }
            }
        }
        else if (jumping) {
            setPose(Pose.JUMP.ordinal(), true);
        }
        else if (vx != 0)
            setPose(Pose.RUN.ordinal(), true);
        else
            setPose(Pose.IDLE.ordinal(), true);
    }

    public void draw(Graphics pen) {
        updatePose();

        int offset_x = direction < 0 ? 200 : -140;
        pen.drawImage(
                getImage(),
                (int)(x + offset_x) - Camera.x,
                (int)(y - 84) - Camera.y,
                direction * SPRITE_WIDTH * 4,
                SPRITE_HEIGHT * 4,
                null);
    }

    public void drawBoxes(Graphics pen) {
        // draw the box of the body
        pen.setColor(new Color(0, 0, 255, 50));
        pen.fillRect((int)x, (int)y, w, h);
    }
}
