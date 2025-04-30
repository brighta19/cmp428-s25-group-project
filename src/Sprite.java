import java.awt.*;

public class Sprite extends Rect {
    Animation[] animation;

    boolean moving = false;

    final int UP = 0;
    final int DN = 1;
    final int LT = 2;
    final int RT = 3;

    int pose = UP;

    public Sprite(String name, double x, double y, int w, int h, String[] pose, int[] count, int duration)  {
        super(x, y, w, h);

        animation = new Animation[pose.length];

        for(int i = 0; i < pose.length; i++)
            animation[i] = new Animation(name + "_" + pose[i], count[i], duration);
    }

    public Image getImage() {
        return animation[pose].nextImage();
    }

    public void draw(Graphics pen) {
        if(moving) pen.drawImage(animation[pose].nextImage(), (int)x, (int)y, w, h, null);

        else       pen.drawImage(animation[pose].stillImage(), (int)x, (int)y, w, h, null);

        moving = false;
    }

    public void go_UP(int dy) {
        old_y = y;

        y -= dy;

        moving = true;

        pose = UP;
    }

    public void go_DN(int dy) {
        old_y = y;

        y += dy;

        moving = true;

        pose = DN;
    }

    public void go_LT(int dx) {
        old_x = x;

        x -= dx;

        moving = true;

        pose = LT;
    }

    public void go_RT(int dx) {
        old_x = x;

        x += dx;

        moving = true;

        pose = RT;
    }

}