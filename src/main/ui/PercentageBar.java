package ui;

import javax.swing.*;
import java.awt.*;

// Represents a rectangle that is used to indicate percent completion, with the
// completed percent coloured green and the rest of the rectangle coloured grey
public class PercentageBar extends JPanel {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 50;
    private double percent;

    // Requires: percent should be between 0 and 100
    // Effects: records percent completed
    public PercentageBar(double percent) {
        this.percent = percent;
    }

    // Modifies: this
    // Effects: draws the percent bar
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawPercentBar(g);
    }

    // Modifies: this
    // Effects: draws a grey rectangle and then overlays a green rectangle to display
    //          the percent completed
    private void drawPercentBar(Graphics g) {
        g.setColor(Color.GRAY);
        g.drawRect(0, 0, WIDTH, HEIGHT);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(new Color(15, 185, 80));
        g.drawRect(0, 0, (int) (WIDTH * percent / 100), HEIGHT);
        g.fillRect(0, 0, (int) (WIDTH * percent / 100), HEIGHT);
    }

    // Effects: sets preferred size
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }
}
