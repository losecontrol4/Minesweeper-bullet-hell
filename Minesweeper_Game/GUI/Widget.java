/*----------------------------------------------------------------
 *  Author:   K. Walsh
 *  Email:    kwalsh@holycross.edu
 *  Written:  7/13/2015
 *  
 *  A simple Graphical User Interface package.
 *----------------------------------------------------------------*/

package GUI;

/**
 * <i>GUI.Widget</i> represents a "thing" that appears inside a GUI.Window, such
 * as a button, label, picture, or box. This class is meant to be used as a base
 * class. By itself, it doesn't draw anything at all. The code in this class
 * keeps track of the widget size and location on the screen. Each subclass
 * should implement the repaint() method to draw the widget on a GUI.Canvas.
 * A widget can be added to a GUI.Window by calling the window's add() method.
 */
public abstract class Widget extends EventAdapter {

    /**
     * The coordinates of the top left corner where this object will appear on
     * the Canvas.
     */
    protected double x, y;

    /**
     * The size of this object as it appears on the Canvas.
     */
    protected double width, height;

    /**
     * The window in which this object appears.
     */
    protected Window window;

    /**
     * Constructor: Initialize a new graphical object at position (0, 0) and
     * with width 10 and height 10. The location and size should be adjusted
     * later by calling one of the member functions.
     */
    public Widget() {
        this(0, 0, 10, 10);
    }

    /**
     * Constructor: Initialize a new graphical object with the specified
     * location and size. The location and size can be adjusted later if needed.
     * @param x the x coordinate of the top left corner of the object.
     * @param y the y coordinate of the top left corner of the object.
     * @param width the width of the the object.
     * @param height the height of the the object.
     */
    public Widget(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Change the position of this object on the screen.
     * @param x new x coordinate for the top left corner of the object.
     * @param y new y coordinate for the top left corner of the object.
     */
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Change the size of this object on the screen, while keeping it centered.
     * @param width new width of the the object.
     * @param height new height of the the object.
     */
    public void setSize(double width, double height) {
        this.x += (this.width / 2.0 - width / 2.0);
        this.y += (this.height / 2.0 - height / 2.0);
        this.width = width;
        this.height = height;
    }

    /**
     * Change the position and size of this object on the screen.
     * @param x new x coordinate for the top left corner of the object.
     * @param y new y coordinate for the top left corner of the object.
     * @param width new width of the the object.
     * @param height new height of the the object.
     */
    public void setFrame(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Change the position of this object on the screen.
     * @param x new x coordinate for the center of the object.
     * @param y new y coordinate for the center of the object.
     */
    public void setCenter(double x, double y) {
        this.x = x - width / 2;
        this.y = y - height / 2;
    }

    /**
     * Change the position and size of this object on the screen.
     * @param x new x coordinate for the center of the object.
     * @param y new y coordinate for the center of the object.
     * @param width new width of the the object.
     * @param height new height of the the object.
     */
    public void setCenter(double x, double y, double width, double height) {
        this.x = x - width / 2.0;
        this.y = y - height / 2.0;
        this.width = width;
        this.height = height;
    }

    /**
     * Check whether the given point on the Canvas is inside the bounds
     * of this object.
     * @param px the x coordinate of the point in question.
     * @param py the y coordinate of the point in question.
     */
    public boolean containsPoint(double px, double py) {
        return x <= px && px < x + width && y <= py && py < y + height;
    }

    /**
     * This function is used internally by the GUI package.
     */
    void setWindow(Window window) {
        this.window = window;
    }

    /**
     * Draw this object on a canvas. Normally, this function should draw only
     * within the rectangle defined by points (x, y) and (x+width, y+height).
     * Subclasses should implement this function. This is normally called only
     * by the GUI.Window class.
     * @param canvas the canvas on which the objct is to be painted.
     */
    public abstract void repaint(Canvas canvas);

    /**
     * Get the top left x-coordinate where this object will be drawn on the
     * canvas.
     */
    public double getX() { return x; }

    /**
     * Get the top left y-coordinate where this object will be drawn on the
     * canvas.
     */
    public double getY() { return y; }

    /**
     * Get the center x-coordinate where this object will be drawn on the
     * canvas.
     */
    public double getCenterX() { return x + width / 2; }

    /**
     * Get the center y-coordinate where this object will be drawn on the
     * canvas.
     */
    public double getCenterY() { return y + height / 2; }

    /**
     * Get the width of this object as it will be drawn on the canvas.
     */
    public double getWidth() { return width; }

    /**
     * Get the height of this object as it will be drawn on the canvas.
     */
    public double getHeight() { return height; }

}
