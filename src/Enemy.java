import java.awt.*;

public class Enemy extends Sprite{
	
	public enum State { //Make it static?
        IDLE, ATTACK, HURT, DEATH
    }
	
	protected State currentState = State.IDLE;
    protected boolean facingRight = true;
	
    public Enemy(String name, int x, int y, int w, int h, String[] poses, int[] count, int duration) {
        super(name, x, y, w, h, poses, count, duration);
    }
    
    /* thought of overriding the Sprite draw method to fix it not showing on the screen,
     * but it didn't work. I will continue reviewing and editing the code.
     */
    @Override
    public void draw(Graphics pen) {
//        Image frame;
//
//        if (currentState == State.IDLE || moving) {
//            frame = animation[pose].nextImage(); // animate
//        } else {
//            frame = animation[pose].stillImage(); // static frame
//        }
//
//        pen.drawImage(frame, x, y, w, h, null);
    	pen.drawImage(animation[pose].nextImage(), (int) x, (int) y, w, h, null);
        moving = false;
    }
    
    
    public void setState(State newState) {
        currentState = newState;
        updatePoseIndex();
    }

    public void setFacingRight(boolean right) {
        facingRight = right;
        updatePoseIndex();
    }
    
    public boolean isMoving() {
        return moving;
    }
    
    public void resetStateIfIdle() {
        if (!moving && currentState != State.HURT && currentState != State.DEATH) {
            setState(State.IDLE);
        }
    }

    private void updatePoseIndex() {
        // Assumes pose array uses naming like: idle_lt, idle_rt, attack_lt, attack_rt, etc.
        // poseIndex is determined by (state.ordinal() * 2 + (facingRight ? 1 : 0))
        pose = currentState.ordinal() * 2 + (facingRight ? 1 : 0);
    }

    // Optional helpers for movement
    public void moveLeft(int dx) {
        x -= dx;
        setFacingRight(false);
		setState(Enemy.State.ATTACK);
        moving = true;
    }

    public void moveRight(int dx) {
        x += dx;
        setFacingRight(true);
        setState(Enemy.State.ATTACK);
        moving = true;
    }

    public void attackRight(int dx){
        x += dx;
        setFacingRight(true);
        setState(Enemy.State.ATTACK);
        moving = true;
    }

}
