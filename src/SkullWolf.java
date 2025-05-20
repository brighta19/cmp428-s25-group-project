public class SkullWolf extends Enemy {

    static String [] pose = new String[]{"attack", "death", "hurt", "walk"};

    static int [] count = { 5, 7, 4, 6};

    final int attack = 0;
    final int death  = 1;
    final int hurt   = 2;
    final int walk   = 3;
    private boolean hurting = false;
    private int hurtDelay = 20;

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


