package main;
import java.awt.*;

public class Animation {
    Image[] image;

    int current = 0;

    int duration;
    int delay;

    public Animation(String name, int count, int duration) {
        image = new Image[count];

        for(int i = 0; i < count; i++) {
            image[i] = Toolkit.getDefaultToolkit().getImage(name + "_" + i + ".GIF");
        }

        this.duration = duration;

        delay = duration;
    }

    public Image nextImage() {
        if(delay == 0) {
            current++;

            if(current >= image.length)  current = 1;

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
