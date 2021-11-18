/*----------------------------------------------------------------
 *  Author:   K. Walsh
 *  Email:    kwalsh@holycross.edu
 *  Written:  7/13/2015
 *  
 *  A simple Graphical User Interface package.
 *----------------------------------------------------------------*/

package GUI;

import java.awt.Color;
import java.awt.Font;

/**
 * A <i>Button</i> object represents a clickable button on the screen. The
 * visual design is somewhat inspired by the Flat-UI Bootstrap theme. Really, it
 * is just a Label with some added hilighting effects.
 */
public class Button extends Label implements EventListener {

    // Properties.
    private boolean active = false, pressed = false;

    /**
     * Create a new button with the given text. The position will be (0, 0).
     * The width and height will be chosen based on the font and the text.
     * @param text the text to show on the button.
     */
    public Button(String text) {
        super(text);
        setFont(Canvas.BOLD_FONT);
        setLocation(0, 0);
        setSize();
        setForegroundColor(Canvas.WHITE);
        setBackgroundColor(Canvas.GREEN.darker());
        setBorderColor(Canvas.DARK_GRAY);
        setCornerRadius(4.0);
    }

    /**
     * Create a new button with the given text at the given position on the
     * canvas. The width and height will be chosen based on the font and the
     * text.
     * @param x the x coordinate of the center of the button.
     * @param y the y coordinate of the center of the button.
     * @param text the text to show on the button.
     */
    public Button(double x, double y, String text) {
        this(text);
        setCenter(x, y);
    }

    /**
     * Create a new button with the given text, with the given position and
     * size on the canvas.
     * @param x the x coordinate of the top left corner of the button.
     * @param y the y coordinate of the top left corner of the button.
     * @param width the width of the button.
     * @param height the height of the button.
     * @param text the text to show on the button.
     */
    public Button(double x, double y, double width, double height, String text) {
        this(text);
        setFrame(x, y, width, height);
    }

    /**
     * Paint this button on a canvas. Don't call this directly, it is called by
     * the GUI system automatically.
     */
    public void repaint(Canvas canvas) {
        Color bg = background;
        if (bg != null && pressed) {
            background = bg.darker();
            super.repaint(canvas);
            background = bg;
        } else if (bg != null && active) {
            background = bg.brighter();
            super.repaint(canvas);
            background = bg;
        } else {
            super.repaint(canvas);
        }
    }

    /**
     * Don't call this directly, it is called automatically
     */
    public void mousePressed(double x, double y, String button) {
        if (button.equals("left")) {
            pressed = true;
            window.refresh();
        }
    }

    /**
     * Don't call this directly, it is called automatically
     */
    public void mouseReleased(double x, double y, String button) {
        if (button.equals("left")) {
            pressed = false;
            window.refresh();
        }
    }

    /**
     * Don't call this directly, it is called automatically
     */
    public void mouseEntered(double x, double y) {
        active = true;
        window.refresh();
    }

    /**
     * Don't call this directly, it is called automatically
     */
    public void mouseExited(double x, double y) {
        active = false;
        window.refresh();
    }
}
