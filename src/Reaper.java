public class Reaper extends Enemy {

    static final String[] pose = new String[]{"attack", "death", "hurt", "walk"};
    static final int[] count = {8, 12, 2, 8};

    static final int attack = 0;
    static final int death = 1;
    static final int hurt = 2;
    static final int walk = 3;

    private static final double CHASE_RANGE = 300;
    private static final double TELEPORT_THRESHOLD = 80;
    private static final int TELEPORT_COOLDOWN = 120;
    private boolean deathPoseSet = false;

    private final Player player;
    private int teleportTimer = 0;


    public Reaper(double x, double y, Player player) {
        super("reaper/reaper", x, y, 150, 150, pose, count, 5);
        this.health = 10;
        this.hitboxWidth = 108;
        this.hitboxHeight = 32;
        this.hitboxOffsetX = 20;
        this.hitboxOffsetY = 80;
        this.player = player;
    }

    @Override
    public void moveLeft() {
        vx = -SPEED;
        direction = 1; // face right (for right-facing sprite)
    }

    @Override
    public void moveRight() {
        vx = SPEED;
        direction = -1; // face left
    }

    public Rect getHitbox() {
        double hitboxX = x + (direction > 0 ? -hitboxWidth : hitboxWidth);
        double hitboxY = y + hitboxOffsetY;
        return new Rect(hitboxX, hitboxY, hitboxWidth, hitboxHeight);
    }

    @Override
    public void update() {
        boolean chasing = false;

        if (hurtDelay > 0) {
            hurtDelay--;
            if (hurtDelay == 0) hurting = false;
        }

        if (dying || hurting) {
            super.update();
            return;
        }

        if (player != null && !dying) {
            double distance = Math.abs(player.x - this.x);
            if (distance <= CHASE_RANGE) {
                chasing = true;
                if (canAttack(player)) {
                    tryAttack(player);
                }
                if (attacking) {
                    attackPlayer(player);
                } else {
                    if (distance < TELEPORT_THRESHOLD && teleportTimer <= 0) {
                        if (player.x < this.x) {
                            this.x = player.x - 300;

                        } else {
                            this.x = player.x + 300;

                        }
                        vx = 0;
                        teleportTimer = TELEPORT_COOLDOWN;
                    } double dx = player.x - this.x;
                    if (dx > 3) {
                        moveRight();
                    } else if (dx < -3) {
                        moveLeft();
                    } else {
                        vx = 0;
                    }
                    teleportTimer--;

                }
            }
        }

        if (!chasing && !dying && !hurting) {
            vx = 0;
        }
        super.update();
    }

    @Override
    public void updatePose() {
        if (dying) {
            if (!deathPoseSet) {
                setPose(death, true, false); // non-looping death animation
                deathPoseSet = true;
            }
            return;
        }
        if (hurting) {
            setPose(hurt, true, false);
            return;
        }
        if (attacking) {
            setPose(attack, true, true);
            return;
        }
        if (vx != 0) {
            setPose(walk, true, true);
        }
        else {
            // Stay still on the first frame of walk animation
            setPose(walk, false, false); // no loop
            animation[walk].stillImage(); // force frame 0
        }

    }

    public void takeDamage() {
        hurting = true;
        hurtDelay = 40;
    }
}
