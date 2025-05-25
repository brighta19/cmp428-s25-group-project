public class SkullWolf extends Enemy {

    static final String [] pose = new String[]{"attack", "death", "hurt", "walk"};

    static final int [] count = { 5, 7, 4, 6};

    static final int attack = 0;
    static final int death  = 1;
    static final int hurt   = 2;
    static  final int walk   = 3;

    private int idleTimer = 0;
    private static final int IDLE_DURATION = 180;
    private static final double CHASE_RANGE = 230;

    private final Player player;

    public SkullWolf(double x, double y) {
        super("swolf", x, y, 80, 80, pose, count, 5);
    }

    @Override
    public void update() {

        if (hurting) {
            if (hurtDelay <= 0) {
                hurting = false;
                hurtDelay = 20;
            } else {
                hurtDelay--;
            }
        }

        if (!dying && !hurting) {
            if (x < 100) {
                moveRight();
            } else if (x > 300) {
                moveLeft();
            }
        }

        super.update(); // position and animation
    }

    @Override
    public void updatePose() {
        int nextPose;

        if (dying) {
            nextPose = death;
            setPose(nextPose, false, false);
            return;
        }

        if (hurting) {
            nextPose = hurt;
            setPose(nextPose, false, false);
            return;
        }

        if (vx != 0) {
            nextPose = walk;
            setPose(nextPose, false, true);
            return;
        }
            nextPose = attack;
            setPose(nextPose, false, true);

    }
        public void takeDamage () {
            hurting = true;
            hurtDelay = 20;
        }
    }


