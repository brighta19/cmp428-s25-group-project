public class Hound extends Enemy {

    static final String[] POSES = { "idle", "attack", "death" };
    static final int[] COUNTS = { 6, 5, 8 }; // Example frame counts

    static final int POSE_IDLE = 0;
    static final int POSE_WALK = 1;
    static final int POSE_DEATH = 2;

    public Hound(double x, double y) {
        super("Skullwolf/swolf", x, y, 128, 128, POSES, COUNTS, 5);
    }

    @Override
    public void updatePose() {
        if (dying) {
            setPose(POSE_DEATH, false, false); // death does not loop
        } else if (vx != 0) {
            setPose(POSE_WALK, false, true);  // walk loops
        } else {
            setPose(POSE_IDLE, false, true);  // idle loops
        }
    }

    // Add movement logic or AI
    @Override
    public void update() {
        // Example: basic patrol logic
        if (!dying) {
            if (x < 100) {
                moveRight();
            } else if (x > 300) {
                moveLeft();
            }
        }

        super.update(); // move and update pose
    }
}
