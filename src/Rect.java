import java.awt.Graphics;

public class Rect {
    double x;
    double y;

    int w;
    int h;

    double old_x;
    double old_y;

    public Rect(double x, double y, int w, int h) {
        this.x = x;
        this.y = y;

        this.w = w;
        this.h = h;
    }

    public void moveBy(double dx, double dy) {
        old_x = x;
        old_y = y;

        x += dx;
        y += dy;
    }

    public void moveUp(double dy) {
        old_y = y;

        y -= dy;
    }

    public void moveDown(double dy) {
        old_y = y;

        y += dy;
    }

    public void moveLeft(double dx) {
        old_x = x;

        x -= dx;
    }

    public void moveRight(double dx) {
        old_x = x;

        x += dx;
    }

    public void pushBy(double dx, double dy) {
        x += dx;
        y += dy;
    }

    public boolean overlaps(Rect r) {
        return (r.y <=   y +   h) &&
               (  y <= r.y + r.h) &&
               (r.x <=   x +   w) &&
               (  x <= r.x + r.w);
    }

    public boolean cameFromAbove() {
        return y > old_y;
    }

    public boolean cameFromBelow() {
        return y < old_y;
    }

    public boolean cameFromLeft() {
        return x > old_x;
    }

    public boolean cameFromRight() {
        return x < old_x;
    }

    public boolean contains(int mx, int my) {
        return (mx > x  ) &&
               (mx < x+w) &&
               (my > y  ) &&
               (my < y+h);
    }

    public void draw(Graphics pen) {
        pen.drawRect((int)x, (int)y, w, h);
    }
}