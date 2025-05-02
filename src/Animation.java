import java.awt.*;

public class Animation {
    Image[] image;

    int current = 0;

    int duration;
    int delay;

    boolean repeats = true;
    boolean done = false;

    public Animation(String name, int count, int duration) {
        image = new Image[count];

        for(int i = 0; i < count; i++) {
            image[i] = Toolkit.getDefaultToolkit().getImage(name + "_" + i + ".png");
        }

        this.duration = duration;
        delay = duration;
    }

    public void reset() {
        current = 0;
        delay = 0;
        done = false;
    }

    public void reset(boolean repeats) {
        reset();
        this.repeats = repeats;
    }

    public Image nextImage() {
        if (done) return image[current];

        if(delay == 0) {
            current++;

            if(current >= image.length) {
                if (repeats) {
                    current = 0;
                }
                else {
                    current = image.length - 1;
                    done = true;
                }
            }

            delay = duration;
        }
        else {
            delay--;
        }

        return image[current];
    }

    public Image stillImage() {
        return image[0];
    }
}
