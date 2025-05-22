public class Hound extends Enemy {

    static final String[] POSES = { "walk", "attack","hurt", "death" };
    static final int[] COUNTS = { 6, 5, 4, 8 }; // Example frame counts

    static final int POSE_IDLE = 0;
    static final int POSE_ATTACK = 1;
    static final int POSE_HURT = 2;
    static final int POSE_DEATH = 3;

    private int idleTimer = 0;
    private static final int IDLE_DURATION = 180;

    private final double leftBound;
    private final double rightBound;
    private static final double CHASE_RANGE = 230;

    private boolean movingRight = true;
    private boolean idling = false;

    private final Player player;

    public Hound(double x, double y, Player player) {
        super("Skullwolf/swolf", x, y, 128, 128, POSES, COUNTS, 5);
        this.health = 10;
        this.hitboxWidth = 108;
        this.hitboxHeight = 32;
        this.hitboxOffsetX = 20;
        this.hitboxOffsetY = 80;

        this.leftBound = x - 50;
        this.rightBound = x + 50;
        this.player = player;
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
            setPose(POSE_ATTACK, true, true); // Uses attack animations for movement
        } else {
            setPose(POSE_IDLE, false, true);  // idle loops
        }
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
                    else if (!hurting) {
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

        if (!chasing && !attacking && !idling && !hurting) {
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
            if (!hurting) vx = 0;

            if (idleTimer <= 0) {
                idling = false;
            }
        }

        super.update();
    }
}
