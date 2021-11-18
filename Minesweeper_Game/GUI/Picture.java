/*----------------------------------------------------------------
 *  Author:   K. Walsh
 *  Email:    kwalsh@holycross.edu
 *  Written:  7/13/2015
 *  
 *  A simple Graphical User Interface package.
 *----------------------------------------------------------------*/

package GUI;

import java.awt.Image;

/**
 * A <i>Picture</i> object represents an image to be drawn on the screen. It has
 * a few basic options for styling, such as changing the background and border
 * color, rotating the image, and scaling the image.
 */
public class Picture extends Box {

    // Properties.
    protected String name;
    protected Image image;
    protected double angle;
    protected boolean rescaled;

    /**
     * Create a new picture using the given file. The position will be (0, 0).
     * The width and height will be chosen based on the image itself.
     * @param name the name of the image file, e.g. "flower.png". It can also be
     * a URL. Supported image formats include .gif, .jpg, and .png.
     * @throws RuntimeException if the image could not be loaded for any reason.
     */
    public Picture(String name) {
        this(0, 0, 0, 0, name);
        setSize();
        setLocation(0, 0);
    }

    /**
     * Create a new picture using the given file, to be drawn at the given
     * position on the canvas. The width and height will be chosen based on the
     * image itself.
     * @param x the x coordinate of the center of the picture.
     * @param y the y coordinate of the center of the picture.
     * @param name the name of the image file, e.g. "flower.png". It can also be
     * a URL. Supported image formats include .gif, .jpg, and .png.
     * @throws RuntimeException if the image could not be loaded for any reason.
     */
    public Picture(double x, double y, String name) {
        this(0, 0, 0, 0, name);
        setSize();
        setCenter(x, y);
    }

    /**
     * Create a new picture using the given file, to be drawn at the given
     * position and size on the canvas.
     * @param x the x coordinate of the top left corner of the picture.
     * @param y the y coordinate of the top left corner of the picture.
     * @param width the width of the picture.
     * @param height the height of the picture.
     * @param name the name of the image file, e.g. "flower.png". It can also be
     * a URL. Supported image formats include .gif, .jpg, and .png.
     * @throws RuntimeException if the image could not be loaded for any reason.
     */
    public Picture(double x, double y, double width, double height, String name) {
        super(x, y, width, height);
        if (name != null) {
            this.image = Images.load(name);
            this.name = name;
        }
        setBackgroundColor(null);
        setBorderColor(null);
    }

    /**
     * Paint this picture on a canvas. Don't call this directly, it is called by
     * the GUI system automatically.
     */
    public void repaint(Canvas canvas) {
        super.repaint(canvas);
        if (image != null) {
            if (rescaled && angle != 0) {
                canvas.picture(x, y, image, width, height, angle); 
            } else if (rescaled) {
                canvas.picture(x, y, image, width, height);
            } else if (angle != 0) {
                canvas.picture(x, y, image, angle);
            } else {
                canvas.picture(x, y, image);
            }
        }
    }

    /**
     * Get the image that will be drawn.
     */
    public Image getImage() {
        return this.image;
    }

    /**
     * Get the image name that was loaded.
     */
    public String getImageName() {
        return this.name;
    }

    /**
     * Change the image that is to be drawn. This also changes the size of the
     * picture, while keeping it centered in the same position.
     * @param image the new image.
     */
    public void setImage(Image image) {
        if (image == null) {
            this.name = null;
            this.image = null;
        } else {
            this.name = "unknown";
            this.image = image;
        }
        setSize();
    }

    /**
     * Change the image that is to be drawn. This also changes the size of the
     * picture, while keeping it centered in the same position.
     * @param name the name of the image file, e.g. "flower.png". It can also be
     * a URL. Supported image formats include .gif, .jpg, and .png.
     */
    public void setImage(String name) {
        if (name == null) {
            this.name = null;
            this.image = null;
        } else {
            this.image = Images.load(name);
            this.name = name;
        }
        setSize();
    }

    /**
     * Resize this picture so it fits the image exactly. This keeps
     * the picture centered at the same position.
     */
    public void setSize() {
        if (image != null) {
            setSize(image.getWidth(null), image.getHeight(null));
            rescaled = false;
        }
    }

    /**
     * Change the size of this object on the screen, while keeping it centered.
     * @param width new width of the the object.
     * @param height new height of the the object.
     */
    public void setSize(double width, double height) {
        super.setSize(width, height);
        if (image != null)
            rescaled = (width == image.getWidth(null))
                && (height == image.getHeight(null));
    }

}
