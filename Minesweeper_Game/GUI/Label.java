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
 * A <i>Label</i> object represents some text on the screen. It has a few basic
 * options for styling, such as changing the colors, font, and corner style.
 */
public class Label extends Picture {

    public static final double MARGIN = 10.0;

    // Properties.
    protected String text;
    protected Color foreground;
    protected Font font = Canvas.DEFAULT_FONT;

    /**
     * Create a new label with the given text. The position will be (0, 0).
     * The width and height will be chosen based on the font and the text.
     * @param text the text to show on the label.
     */
    public Label(String text) {
        this(0, 0, 0, 0, text);
        setSize();
        setLocation(0, 0);
    }

    /**
     * Create a new label with the given text at the given position on the
     * canvas. The width and height will be chosen based on the font and the
     * text.
     * @param x the x coordinate of the center of the label.
     * @param y the y coordinate of the center of the label.
     * @param text the text to show on the label.
     */
    public Label(double x, double y, String text) {
        this(0, 0, 0, 0, text);
        setSize();
        setCenter(x, y);
    }

    /**
     * Create a new label with the given text, with the given position and
     * size on the canvas.
     * @param x the x coordinate of the top left corner of the label.
     * @param y the y coordinate of the top left corner of the label.
     * @param width the width of the label.
     * @param height the height of the label.
     * @param text the text to show on the label.
     */
    public Label(double x, double y, double width, double height, String text) {
        super(x, y, width, height, null);
        this.text = text;
        setForegroundColor(Canvas.BLACK);
        setBackgroundColor(null);
        setBorderColor(null);
    }

    /**
     * Paint this label on a canvas. Don't call this directly, it is called by
     * the GUI system automatically.
     */
    public void repaint(Canvas canvas) {
        super.repaint(canvas);
        if (foreground != null && text != null && text.length() > 0) {
            canvas.setPenColor(foreground);
            canvas.setFont(font);
            canvas.text(x + width / 2, y + height / 2, text);
        }
    }

    /**
     * Get the text that shows on this label.
     */
    public String getText() {
        return this.text;
    }

    /**
     * Change the text that shows on this label.
     * @param text the new text for the label.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Change the foreground color of the label.
     * @param fg the foreground (text) color, or null for no text.
     */
    public void setForegroundColor(Color fg) {
        this.foreground = fg;
    }

    /**
     * Resize this label so it fits the text with a small margin. This keeps
     * the label centered at the same position.
     */
    public void setSize() {
        double w = 0, h = 0;
        if (text != null && text.length() > 0) {
            w = 3*MARGIN + Canvas.textWidth(font, text);
            h = 2*MARGIN + Canvas.textHeight(font, text);
        }
        if (image != null) {
            w = Math.max(w, image.getWidth(null));
            h = Math.max(w, image.getHeight(null));
        }
        setSize(w, h);
    }

    /**
     * Get the current font.
     */
    public Font getFont() { return font; }

    /**
     * Set the font to the default font (sans serif, 16 point). This also
     * resizes the label, while keeping it centered at its current position.
     */
    public void setFont() { setFont(Canvas.DEFAULT_FONT); }

    /**
     * Set the font to the given value. This also resizes the label, while
     * keeping it centered at its current position.
     * @param f the font to make text
     */
    public void setFont(Font f) {
        font = f;
        if (text != null && text.length() > 0) {
            setSize();
        }
    }

    /**
     * Set the font to the given face and size. This also resizes the label,
     * while keeping it centered at its current position.
     * @param face the name of the font face, e.g. "SansSerif Bold", or
     * "Serif Italic".
     * @param size the size of the font.
     */
    public void setFont(String face, int size) {
        setFont(new Font(face, Font.PLAIN, size));
    }

    /**
     * Set the font face. This also resizes the label, while keeping it centered
     * at its current position.
     * @param face the name of the font face, e.g. "SansSerif Bold", or
     * "Serif Italic".
     */
    public void setFont(String face) {
        setFont(new Font(face, Font.PLAIN, font.getSize()));
    }

    /**
     * Set the font size. The face will remain unchanged.
     * @param size the size of the font.
     */
    public void setFont(int size) {
        font = new Font(font.getName(), Font.PLAIN, size);
    }

}
