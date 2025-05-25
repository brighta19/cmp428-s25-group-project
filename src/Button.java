import java.awt.*;

public class Button {
    int x, y, width, height;
    String text;
    Color bgColor = Color.DARK_GRAY;
    Color textColor = Color.WHITE;

    public Button(int x, int y, int width, int height, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
    }

    public void draw(Graphics g) {
        g.setColor(bgColor);
        g.fillRect(x, y, width, height);

        g.setColor(textColor);
        g.setFont(new Font("Arial", Font.BOLD, 24));

        // Center the text both horizontally and vertically
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        int textX = x + (width - textWidth) / 2;
        int textY = y + (height - textHeight) / 2 + fm.getAscent();

        g.drawString(text, textX, textY);
    }

    public boolean isClicked(int mx, int my) {
        return mx >= x && mx <= x + width && my >= y && my <= y + height;
    }
}



