public class Rogue extends Enemy {

    static final String[] POSES = { "idle", "attack", "walk", "pre_attack", "death" };
    static final int[] COUNTS = { 6, 4, 6, 6, 6 }; // Example frame counts

    static final int POSE_IDLE = 0;
    static final int POSE_ATTACK = 1;
    static final int POSE_WALK = 2;
    static final int POSE_HURT = 3; // uses pre-attack animation
    static final int POSE_DEATH = POSE_HURT; // same as POSE_HURT

    private int idleTimer = 0;
    private static final int IDLE_DURATION = 180;

    private final double leftBound;
    private final double rightBound;
    private static final double CHASE_RANGE = 230;

    private boolean movingRight = true;
    private boolean idling = false;

    private final Player player;

    public Rogue(double x, double y, Player player) {
        super("rogue/rogue", x, y, 128, 128, POSES, COUNTS, 5);
        this.health = 10;
        this.hitboxWidth = 40;
        this.hitboxHeight = 96;
        this.hitboxOffsetX = 40;
        this.hitboxOffsetY = 16;

        this.leftBound = x - 50;
        this.rightBound = x + 50;
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

    @Override
    public void updatePose() {
        if (dying) {
            setPose(POSE_DEATH, true, false); // death does not loop
        } else if (attacking) {
            setPose(POSE_ATTACK, true, false);
        } else if (hurting) {
            setPose(POSE_HURT, true, false);  // walk loops
        } else if (Math.abs(vx) > 0.1){
            setPose(POSE_WALK, true, true); // Uses attack animations for movement
        } else {
            setPose(POSE_IDLE, false, true);  // idle loops
        }
    }

    // Having trouble getting the hitboxes right
    @Override
    public Rect getHitbox() {
        double hitboxX = x + (direction < 0 ? w : -w + hitboxWidth + hitboxOffsetX);
        double hitboxY = y + hitboxOffsetY;
        return new Rect(hitboxX, hitboxY, hitboxWidth, hitboxHeight);
    }

    // Add movement logic or AI
    @Override
    public void update() {
        boolean chasing = false;

        if (!dying && !hurting) {

            if (player != null) {
                double distance = Math.abs(player.x - this.x);

                if (distance <= CHASE_RANGE) {
                    chasing = true;
                    idling = false;

                    if (canAttack(player)) {
                        tryAttack(player);
                    }

                    if (attacking){
                        attackPlayer(player);
                        System.out.println("Hound attacked player!");
                    }
                    else {
                        double dx = player.x - this.x;
                        if (dx > 5) {
                            moveRight();
                        } else if (dx < -5) {
                            moveLeft();
                        } else {
                            vx = 0;
//                            setPose(POSE_IDLE, false, true);// stop if reached player
                        }
                    }
                }
            }


        }

        if (!chasing && !attacking && !idling) {
            if (movingRight) {
                moveRight();
                if (x >= rightBound) {
                    movingRight = false;
                    idling = true;
                    idleTimer = IDLE_DURATION;
                    vx = 0;
                }
            } else {
                moveLeft();
                if (x <= leftBound) {
                    movingRight = true;
                    idling = true;
                    idleTimer = IDLE_DURATION;
                    vx = 0;
                }
            }
        }

        if (idling) {
            idleTimer--;
            vx = 0;

            if (idleTimer <= 0) {
                idling = false;
            }
        }

        super.update();
    }
}
