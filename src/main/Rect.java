package main;
import java.awt.Graphics;

public class Rect {
    int x;
    int y;

    int w;
    int h;

    int old_x;
    int old_y;

    public Rect(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;

        this.w = w;
        this.h = h;
    }

    public void moveBy(int dx, int dy) {
        old_x = x;
        old_y = y;

        x += dx;
        y += dy;
    }

    public void moveUp(int dy) {
        old_y = y;

        y -= dy;
    }

    public void moveDown(int dy) {
        old_y = y;

        y += dy;
    }

    public void moveLeft(int dx) {
        old_x = x;

        x -= dx;
    }

    public void moveRight(int dx) {
        old_x = x;

        x += dx;
    }

    public void pushBy(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public boolean overlaps(Rect r) {
        return (r.y <=   y +   h) &&
               (  y <= r.y + r.h) &&
               (r.x <=   x +   w) &&
               (  x <= r.x + r.w);
    }

    public boolean cameFromBelow() {
        return y > old_y;
    }

    public boolean cameFromAbove() {
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
        pen.drawRect(x,  y,  w,  h);
    }
}