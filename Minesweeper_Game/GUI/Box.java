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
 * A <i>Box</i> object represents a rectangle on the screen. It has a few basic
 * options for styling, such as changing the colors and corner style.
 */
public class Box extends Widget {

    // Properties.
    protected Color border, background;
    protected double cornerRadius = 0.0;
    protected double borderWidth = 1.0;

    /**
     * Create a new box at (0, 0) and with a default size.
     */
    public Box() {
        this(0, 0, 10, 10);
    }

    /**
     * Create a new box with the given position. It will have a default size.
     * @param x the x coordinate of the top left corner of the box.
     * @param y the y coordinate of the top left corner of the box.
     */
    public Box(double x, double y) {
        this(x, y, 10, 10);
    }

    /**
     * Create a new box with the given position and size on the canvas.
     * @param x the x coordinate of the top left corner of the box.
     * @param y the y coordinate of the top left corner of the box.
     * @param width the width of the box.
     * @param height the height of the box.
     */
    public Box(double x, double y, double width, double height) {
        setFrame(x, y, width, height);
        setBackgroundColor(Canvas.WHITE);
        setBorderColor(Canvas.DARK_GRAY);
    }

    /**
     * Paint this box on a canvas. Don't call this directly, it is called by
     * the GUI system automatically if it has been added to a window using the
     * window's add() method.
     */
    public void repaint(Canvas canvas) {
        if (background != null) {
            canvas.setPenColor(background);
            canvas.filledRoundedRectangle(x, y, width, height, cornerRadius);
        }
        if (border != null && borderWidth >  0.0) {
            canvas.setPenRadius(borderWidth);
            canvas.setPenColor(border);
            canvas.roundedRectangle(x, y, width, height, cornerRadius);
        }
    }

    /**
     * Get the background color of the box, or null if there is no background..
     */
    public Color getBackgroundColor() {
        return background;
    }

    /**
     * Change the background color of the box.
     * @param bg the background color, or null for no background.
     */
    public void setBackgroundColor(Color bg) {
        this.background = bg;
    }

    /**
     * Get the border color of the box, or null if there is no border color.
     */
    public Color getBorderColor() {
        return border;
    }

    /**
     * Change the border color of the box.
     * @param border the border color, or null for no border.
     */
    public void setBorderColor(Color border) {
        this.border = border;
    }

    /**
     * Get the radius for the corners. Zero means there are sharp corners.
     */
    public double getCornerRadius() {
        return cornerRadius;
    }

    /**
     * Change the radius for the corners. Setting this to zero will result in
     * sharp corners.
     * @param r the radius for the box corners.
     */
    public void setCornerRadius(double r) {
        cornerRadius = r;
    }

    /**
     * Get the width for the borders. Zero means there are no borders.
     */
    public double getBorderWidth() {
        return borderWidth;
    }

    /**
     * Change the width for the borders. Setting this to zero will result in
     * no borders.
     * @param r the width of the borders.
     */
    public void setBorderWidth(double r) {
        borderWidth = r;
    }


}
