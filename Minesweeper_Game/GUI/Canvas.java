/*----------------------------------------------------------------
 *  Author:   K. Walsh
 *  Email:    kwalsh@holycross.edu
 *  Written:  7/13/2015
 *  
 *  A simple Graphical User Interface package.
 *----------------------------------------------------------------*/

package GUI;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

/**
 * A <i>Canvas</i> object represents a surface on which a program can draw. It
 * uses a simple graphics model so that programs can create drawings consisting
 * of points, lines, curves, images, and other elements. 
 * <p>
 * This class is much like Sedgeick and Wayne's StdDraw and Draw classes. The
 * key differences are:
 * <li>
 *   <li>This class is meant to be used in combination with the GUI.Window and
 *   GUI.Widget classes. StdDraw and Draw can be used alone.</li>
 *   <li>Screen coordinates differ. This class puts (0, 0) at the top left. This
 *   is the norm for programming. Draw and StdDraw use math-style coordinates,
 *   with (0, 0) in the bottom left.</li>
 *   <li>In this class, most rectangles and boxes are specified using top left
 *   corner and size. This is the norm for programming. Draw and StdDraw use the
 *   center point and half width, half height, which is confusing when trying to
 *   lay out grids of buttons or align various things.</li>
 *   <li>The scale in this class is fixed to be the identity, i.e. ranging from
 *   0.0 to width or height, rather than being variable or ranging from 0.0 to
 *   1.0.</li>
 * </ul>
 */
public class Canvas {

    // Pre-defined colors.
    public static final Color BLACK       = Color.BLACK;
    public static final Color BLUE        = Color.BLUE;
    public static final Color CYAN        = Color.CYAN;
    public static final Color DARK_GRAY   = Color.DARK_GRAY;
    public static final Color GRAY        = Color.GRAY;
    public static final Color GREEN       = Color.GREEN;
    public static final Color LIGHT_GRAY  = Color.LIGHT_GRAY;
    public static final Color MAGENTA     = Color.MAGENTA;
    public static final Color ORANGE      = Color.ORANGE;
    public static final Color PINK        = Color.PINK;
    public static final Color RED         = Color.RED;
    public static final Color WHITE       = Color.WHITE;
    public static final Color YELLOW      = Color.YELLOW;
    public static final Color BOOK_BLUE   = new Color(9, 90, 166);
    public static final Color BOOK_RED    = new Color(173, 32, 24);
    public static final Color DARK_BLUE   = new Color(0x0000B0);
    public static final Color DARK_GREEN  = new Color(0x00B000);
    public static final Color DARK_RED    = new Color(0xB00000);
    public static final Color DARK_PURPLE = new Color(0xB000B0);
    public static final Color LIGHT_BLACK = new Color(0x0000B0);
    public static final Color MAROON      = new Color(0xB03060);
    public static final Color TURQUOISE   = new Color(0x00CED1);

    // Default colors.
    public static final Color DEFAULT_PEN_COLOR   = BLACK;
    public static final Color DEFAULT_BACKGROUND_COLOR = WHITE;

    // Default pen radius.
    public static final double DEFAULT_PEN_RADIUS = 1.0;

    /**
     * Default font.
     */
    public static final Font DEFAULT_FONT = new Font("SansSerif", Font.PLAIN, 16);

    /**
     * Bold font.
     */
    public static final Font BOLD_FONT = new Font("SansSerif", Font.BOLD, 16);

    // Current pen color.
    private Color penColor;

    // Current background color.
    private Color backgroundColor;

    // Canvas size.
    private int width, height;

    // Current pen radius.
    private double penRadius;

    // Current font.
    private Font font;

    // The underlying graphics context.
    private Graphics2D graphics;

	// The underlying offscreen backing image, if any.
	private BufferedImage backingImage;

    /**
     * Initialize a blank canvas.
     * @param graphics the underlying graphics context.
     * @param width the width of the canvas.
     * @param height the height of the canvas.
     */
    public Canvas(Graphics2D graphics, int width, int height) {
        this.graphics = graphics;
        this.width = width;
        this.height = height;

        graphics.setColor(DEFAULT_BACKGROUND_COLOR);
        graphics.fillRect(0, 0, width, height);

        // set all options
        setBackgroundColor();
        setPenColor();
        setPenRadius();
        setFont();

        // add antialiasing
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.addRenderingHints(hints);
    }

    /**
     * Initialize a blank canvas, backed by an offscreen backing image.
     * @param image the underlying offscreen image.
     */
	public Canvas(BufferedImage image) {
		this(image.createGraphics(), image.getWidth(), image.getHeight());
		backingImage = image;
	}

    /**
     * Initialize a blank canvas, backed by an offscreen backing image.
     * @param width the width of the canvas and backing image.
     * @param height the height of the canvas and backing image.
     */
	public Canvas(int width, int height) {
		this(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
	}

    /*----------------------------------------------------------------
     *  Visual properties
     *----------------------------------------------------------------*/

	/**
	 * Enable or disable anti-aliasing. Disabling anti-aliasing makes lines look
	 * blocky/pixelated, especially when scaled up. Enabling anti-aliasing makes
	 * lines look smoother, but can sometimes look slightly blury, especially
	 * when scaled up.
	 */
	public void setAntialiasing(boolean enable) {
        RenderingHints hints;
		if (enable) {
			hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		} else {
			hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		}
        graphics.addRenderingHints(hints);
	}

    /**
     * Reset the pen radius, pen color, and font to the thier defaults. The
     * background color is left unchanged.
     */
    public void reset() {
        setPenColor();
        setPenRadius();
        setFont();
    }

    /**
     * Get the current pen radius.
     */
    public double getPenRadius() { return penRadius; }

    /**
     * Set the pen size to the default (1.0).
     */
    public void setPenRadius() { setPenRadius(DEFAULT_PEN_RADIUS); }

    /**
     * Set the radius of the pen to the given size.
     * @param r the radius of the pen.
     * @throws RuntimeException if r is negative.
     */
    public void setPenRadius(double r) {
        if (r < 0) throw new RuntimeException("pen radius must be positive");
        penRadius = r;
        graphics.setStroke(new BasicStroke((float)penRadius,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    }

    /**
     * Get the current pen color.
     */
    public Color getPenColor() { return penColor; }

    /**
     * Set the pen color to the default color (black).
     */
    public void setPenColor() { setPenColor(DEFAULT_PEN_COLOR); }

    /**
     * Set the pen color to the given color.
     * @param color the Color to make the pen.
     */
    public void setPenColor(Color color) {
        penColor = color;
        graphics.setColor(color);
    }

    /**
     * Set the pen color to the given RGB color.
     * @param red the amount of red (between 0 and 255)
     * @param green the amount of green (between 0 and 255)
     * @param blue the amount of blue (between 0 and 255)
     * @throws IllegalArgumentException if the amount of red, green, or blue are outside prescribed range
     */
    public void setPenColor(int red, int green, int blue) {
        if (red   < 0 || red   >= 256) throw new IllegalArgumentException("amount of red must be between 0 and 255");
        if (green < 0 || green >= 256) throw new IllegalArgumentException("amount of red must be between 0 and 255");
        if (blue  < 0 || blue  >= 256) throw new IllegalArgumentException("amount of red must be between 0 and 255");
        setPenColor(new Color(red, green, blue));
    }

    /**
     * Get the current background color.
     */
    public Color getBackgroundColor() { return backgroundColor; }

    /**
     * Set the background color to the default color (WHITE).
     */
    public void setBackgroundColor() { setBackgroundColor(DEFAULT_BACKGROUND_COLOR); }

    /**
     * Set the background color to the given color.
     * @param color the Color to make the background
     */
    public void setBackgroundColor(Color color) {
        backgroundColor = color;
    }

    /**
     * Set the background color to the given RGB color.
     * @param red the amount of red (between 0 and 255)
     * @param green the amount of green (between 0 and 255)
     * @param blue the amount of blue (between 0 and 255)
     * @throws IllegalArgumentException if the amount of red, green, or blue are outside prescribed range
     */
    public void setBackgroundColor(int red, int green, int blue) {
        if (red   < 0 || red   >= 256) throw new IllegalArgumentException("amount of red must be between 0 and 255");
        if (green < 0 || green >= 256) throw new IllegalArgumentException("amount of red must be between 0 and 255");
        if (blue  < 0 || blue  >= 256) throw new IllegalArgumentException("amount of red must be between 0 and 255");
        setBackgroundColor(new Color(red, green, blue));
    }

    /**
     * Get the current font.
     */
    public Font getFont() { return font; }

    /**
     * Set the font to the default font (sans serif, 16 point).
     */
    public void setFont() { setFont(DEFAULT_FONT); }

    /**
     * Set the font to the given value.
     * @param f the font to make text
     */
    public void setFont(Font f) { font = f; }

    /**
     * Set the font to the given face and size.
     * @param face the name of the font face, e.g. "SansSerif Bold", or
     * "Serif Italic".
     * @param size the size of the font.
     */
    public void setFont(String face, int size) {
        font = new Font(face, Font.PLAIN, size);
    }

    /**
     * Set the font face. The size will remain unchanged.
     * @param face the name of the font face, e.g. "SansSerif Bold", or
     * "Serif Italic".
     */
    public void setFont(String face) {
        font = new Font(face, Font.PLAIN, font.getSize());
    }

    /**
     * Set the font size. The face will remain unchanged.
     * @param size the size of the font.
     */
    public void setFont(int size) {
        font = new Font(font.getName(), Font.PLAIN, size);
    }

    /*----------------------------------------------------------------
     *  Drawing shapes
     *----------------------------------------------------------------*/

    /**
     * Clear the screen to the default color (white).
     */
    public void clear() {
        clear(backgroundColor);
    }

    /**
     * Clear the screen to the given color.
     * @param color the Color to make the background
     */
    public void clear(Color color) {
        graphics.setColor(color);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(penColor);
    }

    /**
     * Draw a line from (x0, y0) to (x1, y1).
     * @param x0 the x-coordinate of the starting point
     * @param y0 the y-coordinate of the starting point
     * @param x1 the x-coordinate of the destination point
     * @param y1 the y-coordinate of the destination point
     */
    public void line(double x0, double y0, double x1, double y1) {
        graphics.draw(new Line2D.Double(x0, y0, x1, y1));
    }

    /**
     * Draw one pixel at (x, y).
     * @param x the x-coordinate of the pixel
     * @param y the y-coordinate of the pixel
     */
    private void pixel(double x, double y) {
        graphics.fillRect((int)Math.round(x), (int)Math.round(y), 1, 1);
    }

	/**
	 * Get the currently drawn color at a point (x, y). This only works
	 * if there is an offscreen backing image, otherwise it returns null.
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
	 */
	public Color colorAt(double x, double y) {
		if (backingImage == null)
			return null;
		else
			return new Color(backingImage.getRGB((int)Math.round(x), (int)Math.round(y)));
	}

    /**
     * Draw a point at (x, y) using the size of the pen radius.
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     */
    public void point(double x, double y) {
        if (penRadius <= 1.0) {
            pixel(x, y);
        } else {
            double r = penRadius;
            graphics.fill(new Ellipse2D.Double(x - r/2.0, y - r/2.0, r, r));
        }
    }

    /**
     * Draw a circle of radius r, centered on (x, y).
     * @param x the x-coordinate of the center of the circle
     * @param y the y-coordinate of the center of the circle
     * @param r the radius of the circle
     * @throws RuntimeException if the radius of the circle is negative
     */
    public void circle(double x, double y, double r) {
        if (r < 0) throw new RuntimeException("circle radius can't be negative");
        if (r <= 1.0) pixel(x, y);
        else graphics.draw(new Ellipse2D.Double(x - r, y - r, 2*r, 2*r));
    }

    /**
     * Draw filled circle of radius r, centered on (x, y).
     * @param x the x-coordinate of the center of the circle
     * @param y the y-coordinate of the center of the circle
     * @param r the radius of the circle
     * @throws RuntimeException if the radius of the circle is negative
     */
    public void filledCircle(double x, double y, double r) {
        if (r < 0) throw new RuntimeException("circle radius can't be negative");
        if (r <= 1.0) pixel(x, y);
        else graphics.fill(new Ellipse2D.Double(x - r, y - r, 2*r, 2*r));
    }


    /**
     * Draw an ellipse inside of an imaginary box of the given position and size.
     * @param x the x-coordinate of the top left corner of an imaginary box around the ellipse
     * @param y the y-coordinate of the top left corner of an imaginary box around the ellipse
     * @param width the width of an imaginary box around the ellipse.
     * @param height the height of an imaginary box around the ellipse.
     * @throws RuntimeException if either the width or height are negative.
     */
    public void ellipse(double x, double y, double width, double height) {
        if (width < 0 || height < 0)
            throw new RuntimeException("width and height can't be negative");
        if (width <= 1.0 && height <= 1.0) pixel(x, y);
        else graphics.draw(new Ellipse2D.Double(x, y, width, height));
    }

    /**
     * Draw a filled ellipse inside of an imaginary box of the given position and size.
     * @param x the x-coordinate of the top left corner of an imaginary box around the ellipse
     * @param y the y-coordinate of the top left corner of an imaginary box around the ellipse
     * @param width the width of an imaginary box around the ellipse.
     * @param height the height of an imaginary box around the ellipse.
     * @throws RuntimeException if either the width or height are negative.
     */
    public void filledEllipse(double x, double y, double width, double height) {
        if (width < 0 || height < 0)
            throw new RuntimeException("width and height can't be negative");
        if (width <= 1.0 && height <= 1.0) pixel(x, y);
        else graphics.fill(new Ellipse2D.Double(x, y, width, height));
    }

    /**
     * Draw an arc of radius r, centered on (x, y), from angle1 to angle2 (in degrees).
     * @param x the x-coordinate of the center of the circle
     * @param y the y-coordinate of the center of the circle
     * @param r the radius of the circle
     * @param angle1 the starting angle. 0 would mean an arc beginning at 3 o'clock.
     * @param angle2 the angle at the end of the arc. For example, if
     *        you want a 90 degree arc, then angle2 should be angle1 + 90.
     * @throws RuntimeException if the radius of the circle is negative
     */
    public void arc(double x, double y, double r, double angle1, double angle2) {
        if (r < 0) throw new RuntimeException("arc radius can't be negative");
        while (angle2 < angle1) angle2 += 360;
        if (r <= 1.0) pixel(x, y);
        else graphics.draw(new Arc2D.Double(x - r, y - r, 2*r, 2*r, angle1, angle2 - angle1, Arc2D.OPEN));
    }

    /**
     * Draw a square of given size at the given position.
     * @param x the x-coordinate of the top left corner of the square
     * @param y the y-coordinate of the top left corner of the square
     * @param size the length of a side of the square
     * @throws RuntimeException if size is negative
     */
    public void square(double x, double y, double size) {
        if (size < 0) throw new RuntimeException("square side length can't be negative");
        if (size <= 1.0) pixel(x, y);
        else graphics.draw(new Rectangle2D.Double(x, y, size, size));
    }

    /**
     * Draw a filled square of given size at the given position.
     * @param x the x-coordinate of the top left corner of the square
     * @param y the y-coordinate of the top left corner of the square
     * @param size the length of a side of the square
     * @throws RuntimeException if size is negative
     */
    public void filledSquare(double x, double y, double size) {
        if (size < 0) throw new RuntimeException("square side length can't be negative");
        if (size <= 1.0) pixel(x, y);
        else graphics.fill(new Rectangle2D.Double(x, y, size, size));
    }

    /**
     * Draw a rectangle of given size at the given position.
     * @param x the x-coordinate of the top left corner of the rectangle
     * @param y the y-coordinate of the top left corner of the rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @throws RuntimeException if either the width or height are negative.
     */
    public void rectangle(double x, double y, double width, double height) {
        if (width < 0 || height < 0)
            throw new RuntimeException("rectangle size can't be negative");
        if (width <= 1.0 && height <= 1.0) pixel(x, y);
        else graphics.draw(new Rectangle2D.Double(x, y, width, height));
    }

    /**
     * Draw a filled rectangle of given size at the given position.
     * @param x the x-coordinate of the top left corner of the rectangle
     * @param y the y-coordinate of the top left corner of the rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @throws RuntimeException if either the width or height are negative.
     */
    public void filledRectangle(double x, double y, double width, double height) {
        if (width < 0 || height < 0)
            throw new RuntimeException("rectangle size can't be negative");
        if (width <= 1.0 && height <= 1.0) pixel(x, y);
        else graphics.fill(new Rectangle2D.Double(x, y, width, height));
    }

    /**
     * Draw a rectangle of given size at the given position, with rounded
     * corners. The radius of the corners is given by the pen radius.
     * @param x the x-coordinate of the top left corner of the rectangle
     * @param y the y-coordinate of the top left corner of the rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @throws RuntimeException if either the width or height are negative.
     */
    public void roundedRectangle(double x, double y, double width, double height) {
        roundedRectangle(x, y, width, height, penRadius);
    }

    /**
     * Draw a rectangle of given size at the given position, with rounded
     * corners.
     * @param x the x-coordinate of the top left corner of the rectangle
     * @param y the y-coordinate of the top left corner of the rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param radius the corner radius, or zero for sharp corners
     * @throws RuntimeException if either the width or height are negative.
     */
    public void roundedRectangle(double x, double y, double width, double height, double radius) {
        if (radius == 0.0) rectangle(x, y, width, height);
        else  {
            if (width < 0 || height < 0 || radius < 0)
                throw new RuntimeException("rectangle size can't be negative");
            if (width <= 1.0 && height <= 1.0) pixel(x, y);
            else graphics.draw(new RoundRectangle2D.Double(x, y, width, height, radius, radius));
        }
    }

    /**
     * Draw a filled rectangle of given size at the given position, with rounded
     * corners. The radius of the corners is given by the pen radius.
     * @param x the x-coordinate of the top left corner of the rectangle
     * @param y the y-coordinate of the top left corner of the rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @throws RuntimeException if either the width or height are negative.
     */
    public void filledRoundedRectangle(double x, double y, double width, double height) {
        filledRoundedRectangle(x, y, width, height, penRadius);
    }

    /**
     * Draw a filled rectangle of given size at the given position, with rounded
     * corners. 
     * @param x the x-coordinate of the top left corner of the rectangle
     * @param y the y-coordinate of the top left corner of the rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param radius the corner radius, or zero for sharp corners
     * @throws RuntimeException if the width, height, or radius are negative.
     */
    public void filledRoundedRectangle(double x, double y, double width, double height, double radius) {
        if (radius == 0.0) filledRectangle(x, y, width, height);
        else  {
            if (width < 0 || height < 0 || radius < 0)
                throw new RuntimeException("rectangle size can't be negative");
            if (width <= 1.0 && height <= 1.0) pixel(x, y);
            else graphics.fill(new RoundRectangle2D.Double(x, y, width, height, radius, radius));
        }
    }

    /**
     * Draw a filled rectangle of given size at the given position, with a
     * bevel to give it a classic "raised 3D" look. The pen radius determines
     * the bevel thickness.
     * @param x the x-coordinate of the top left corner of the rectangle
     * @param y the y-coordinate of the top left corner of the rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @throws RuntimeException if the width, height, or thickness are negative.
     */
    public void raisedBevelRectangle(double x, double y, double width, double height) {
        bevelRectangle(x, y, width, height, penRadius, true);
    }

    /**
     * Draw a filled rectangle of given size at the given position, with a
     * bevel to give it a classic "raised 3D" look. 
     * @param x the x-coordinate of the top left corner of the rectangle
     * @param y the y-coordinate of the top left corner of the rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param t the thickness of the bevel edge
     * @throws RuntimeException if the width, height, or thickness are negative.
     */
    public void raisedBevelRectangle(double x, double y,
            double width, double height,
            double t) {
        bevelRectangle(x, y, width, height, t, true);
    }

    /**
     * Draw a filled rectangle of given size at the given position, with a
     * bevel to give it a classic "sunken 3D" look. The pen radius determines
     * the bevel thickness.
     * @param x the x-coordinate of the top left corner of the rectangle
     * @param y the y-coordinate of the top left corner of the rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @throws RuntimeException if the width, height, or thickness are negative.
     */
    public void sunkenBevelRectangle(double x, double y, double width, double height) {
        bevelRectangle(x, y, width, height, penRadius, false);
    }

    /**
     * Draw a filled rectangle of given size at the given position, with a
     * bevel to give it a classic "sunken 3D" look. The pen radius determines
     * the bevel thickness.
     * @param x the x-coordinate of the top left corner of the rectangle
     * @param y the y-coordinate of the top left corner of the rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param t the thickness of the bevel edge
     * @throws RuntimeException if the width, height, or thickness are negative.
     */
    public void sunkenBevelRectangle(double x, double y,
            double width, double height,
            double t) {
        bevelRectangle(x, y, width, height, t, false);
    }

    /**
     * Draw a filled rectangle of given size at the given position, with a bevel
     * to give it a classic "raised 3D" or "sunken 3D" look. 
     * @param x the x-coordinate of the top left corner of the rectangle
     * @param y the y-coordinate of the top left corner of the rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param t the thickness of the bevel edge
     * @param raised whether it should look raised or sunkedn.
     * @throws RuntimeException if the width, height, or thickness are negative.
     */
    public void bevelRectangle(double x, double y,
            double width, double height,
            double t, boolean raised) {
        if (width < 0 || height < 0 || t < 0)
            throw new RuntimeException("rectangle size can't be negative");
        if (width <= 1.0 && height <= 1.0) {
            pixel(x, y);
        } else {
            if (2*t >= width || 2*t >= height)
                t = Math.min(width, height) / 2.0;

            // make entire area dark (or bright)
            graphics.setColor(raised ? penColor.darker() : penColor.brighter());
            graphics.fill(new Rectangle2D.Double(x, y, width, height));

            // make top half bright (or dark)
            GeneralPath path = new GeneralPath();
            path.moveTo((float)x, (float)(y + height));
            path.lineTo((float)x, (float)y);
            path.lineTo((float)(x + width), (float)y);
            path.lineTo((float)(x + width - t), (float)(y + t));
            path.lineTo((float)(x + t), (float)(y + height - t));
            path.closePath();
            graphics.setColor(raised ? penColor.brighter() : penColor.darker());
            graphics.fill(path);

            // make middle portion original color
            graphics.setColor(penColor);
            graphics.fill(new Rectangle2D.Double(x + t, y + t, width - 2 * t, height - 2 * t));
        }
    }

    /**
     * Draw a closed polygon with the given (x[i], y[i]) coordinates.
     * @param x an array of all the x-coordindates of the polygon
     * @param y an array of all the y-coordindates of the polygon
     */
    public void polygon(double[] x, double[] y) {
        polygon(x, y, true);
    }

    /**
     * Draw a polygon with the given (x[i], y[i]) coordinates.
     * @param x an array of all the x-coordindates of the polygon.
     * @param y an array of all the y-coordindates of the polygon.
     * @param closed whether the polygon should be closed or not.
     */
    public void polygon(double[] x, double[] y, boolean closed) {
        int N = x.length;
        GeneralPath path = new GeneralPath();
        path.moveTo((float)x[0], (float)y[0]);
        for (int i = 0; i < N; i++)
            path.lineTo((float)x[i], (float)y[i]);
        if (closed)
            path.closePath();
        graphics.draw(path);
    }

    /**
     * Draw a filled polygon with the given (x[i], y[i]) coordinates.
     * @param x an array of all the x-coordindates of the polygon
     * @param y an array of all the y-coordindates of the polygon
     */
    public void filledPolygon(double[] x, double[] y) {
        int N = x.length;
        GeneralPath path = new GeneralPath();
        path.moveTo((float)x[0], (float)y[0]);
        for (int i = 0; i < N; i++)
            path.lineTo((float)x[i], (float)y[i]);
        path.closePath();
        graphics.fill(path);
    }


    /*----------------------------------------------------------------
     *  Drawing images
     *----------------------------------------------------------------*/

    /**
     * Draw picture (gif, jpg, or png) with top left corner at (x, y).
     * @param x the top left x-coordinate of the image
     * @param y the top left y-coordinate of the image
     * @param s the name of the image/picture, e.g., "ball.gif"
     * @throws RuntimeException if the image is corrupt
     */
    public void picture(double x, double y, String s) {
        picture(x, y, Images.load(s));
    }

    /**
     * Draw picture (gif, jpg, or png) with center at (x, y).
     * @param x the center x-coordinate of the image
     * @param y the center y-coordinate of the image
     * @param s the name of the image/picture, e.g., "ball.gif"
     * @throws RuntimeException if the image is corrupt
     */
    public void pictureCentered(double x, double y, String s) {
        pictureCentered(x, y, Images.load(s));
    }

    /**
     * Draw picture (gif, jpg, or png) with top left corner at (x, y), rotated
     * counterclockwise about its center the given number of degrees.
     * @param x the top left x-coordinate of the image
     * @param y the top left y-coordinate of the image
     * @param s the name of the image/picture, e.g., "ball.gif"
     * @param degrees is the number of degrees to rotate counterclockwise
     * @throws RuntimeException if the image is corrupt
     */
    public void picture(double x, double y, String s, double degrees) {
        picture(x, y, Images.load(s), degrees);
    }

    /**
     * Draw picture (gif, jpg, or png) centered on (x, y), rotated
     * counterclockwise about its center given number of degrees.
     * @param x the center x-coordinate of the image
     * @param y the center y-coordinate of the image
     * @param s the name of the image/picture, e.g., "ball.gif"
     * @param degrees is the number of degrees to rotate counterclockwise
     * @throws RuntimeException if the image is corrupt
     */
    public void pictureCentered(double x, double y, String s, double degrees) {
        pictureCentered(x, y, Images.load(s), degrees);
    }

    /**
     * Draw picture (gif, jpg, or png) with top left corner at (x, y), rescaled to w-by-h.
     * @param x the top left x coordinate of the image
     * @param y the top left y coordinate of the image
     * @param s the name of the image/picture, e.g., "ball.gif"
     * @param w the width of the image, after rescaling
     * @param h the height of the image, after rescaling
     * @throws RuntimeException if the image is corrupt
     */
    public void picture(double x, double y, String s, double w, double h) {
        picture(x, y, Images.load(s), w, h);
    }


    /**
     * Draw picture (gif, jpg, or png) centered on (x, y), rescaled to w-by-h.
     * @param x the center x coordinate of the image
     * @param y the center y coordinate of the image
     * @param s the name of the image/picture, e.g., "ball.gif"
     * @param w the width of the image, after rescaling
     * @param h the height of the image, after rescaling
     * @throws RuntimeException if the image is corrupt
     */
    public void pictureCentered(double x, double y, String s, double w, double h) {
        pictureCentered(x, y, Images.load(s), w, h);
    }

    /**
     * Draw picture (gif, jpg, or png) with top left at (x, y), rotated
     * given number of degrees about its center, rescaled to w-by-h.
     * @param x the top left x-coordinate of the image
     * @param y the top left y-coordinate of the image
     * @param s the name of the image/picture, e.g., "ball.gif"
     * @param w the width of the image, after rescaling
     * @param h the height of the image, after rescaling
     * @param degrees is the number of degrees to rotate counterclockwise
     * @throws RuntimeException if the image is corrupt
     */
    public void picture(double x, double y, String s, double w, double h, double degrees) {
        picture(x, y, Images.load(s), w, h, degrees);
    }

    /**
     * Draw picture (gif, jpg, or png) centered on (x, y), rotated
     * given number of degrees, rescaled to w-by-h.
     * @param x the center x-coordinate of the image
     * @param y the center y-coordinate of the image
     * @param s the name of the image/picture, e.g., "ball.gif"
     * @param w the width of the image, after rescaling
     * @param h the height of the image, after rescaling
     * @param degrees is the number of degrees to rotate counterclockwise
     * @throws RuntimeException if the image is corrupt
     */
    public void pictureCentered(double x, double y, String s, double w, double h, double degrees) {
        pictureCentered(x, y, Images.load(s), w, h, degrees);
    }

    /**
     * Draw picture with top left corner at (x, y).
     * @param x the top left x-coordinate of the image
     * @param y the top left y-coordinate of the image
     * @param image the image to draw
     * @throws RuntimeException if the image is corrupt
     */
    public void picture(double x, double y, Image image) {
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        if (w < 0 || h < 0) throw new RuntimeException("image is corrupt");
        graphics.drawImage(image, (int)Math.round(x), (int)Math.round(y), null);
    }

    /**
     * Draw picture with center at (x, y).
     * @param x the center x-coordinate of the image
     * @param y the center y-coordinate of the image
     * @param image the image to draw
     * @throws RuntimeException if the image is corrupt
     */
    public void pictureCentered(double x, double y, Image image) {
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        if (w < 0 || h < 0) throw new RuntimeException("image is corrupt");
        graphics.drawImage(image, (int)Math.round(x - w/2.0), (int)Math.round(y - h/2.0), null);
    }

    /**
     * Draw picture with top left corner at (x, y), rotated counterclockwise
     * about its center the given number of degrees.
     * @param x the top left x-coordinate of the image
     * @param y the top left y-coordinate of the image
     * @param image the image to draw
     * @param degrees is the number of degrees to rotate counterclockwise
     * @throws RuntimeException if the image is corrupt
     */
    public void picture(double x, double y, Image image, double degrees) {
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        if (w < 0 || h < 0) throw new RuntimeException("image is corrupt");
        graphics.rotate(Math.toRadians(-degrees), x + w/2.0, y + h/2.0);
        graphics.drawImage(image, (int)Math.round(x), (int)Math.round(y), null);
        graphics.rotate(Math.toRadians(+degrees), x + w/2.0, y + h/2.0);
    }

    /**
     * Draw picture centered on (x, y), rotated counterclockwise about its
     * center given number of degrees.
     * @param x the center x-coordinate of the image
     * @param y the center y-coordinate of the image
     * @param image the image to draw
     * @param degrees is the number of degrees to rotate counterclockwise
     * @throws RuntimeException if the image is corrupt
     */
    public void pictureCentered(double x, double y, Image image, double degrees) {
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        if (w < 0 || h < 0) throw new RuntimeException("image is corrupt");
        graphics.rotate(Math.toRadians(-degrees), x, y);
        graphics.drawImage(image, (int)Math.round(x - w/2.0), (int)Math.round(y - h/2.0), null);
        graphics.rotate(Math.toRadians(+degrees), x, y);
    }

    /**
     * Draw picture with top left corner at (x, y), rescaled to w-by-h.
     * @param x the top left x coordinate of the image
     * @param y the top left y coordinate of the image
     * @param image the image to draw
     * @param w the width of the image, after rescaling
     * @param h the height of the image, after rescaling
     * @throws RuntimeException if the image is corrupt
     */
    public void picture(double x, double y, Image image, double w, double h) {
        if (w < 0 || h < 0) throw new RuntimeException("rescale size can't be negative");
        if (w <= 1.0 && h <= 1.0) pixel(x, y);
        else graphics.drawImage(image, (int)Math.round(x), (int)Math.round(y),
                (int)Math.round(w), (int)Math.round(h), null);
    }


    /**
     * Draw picture centered on (x, y), rescaled to w-by-h.
     * @param x the center x coordinate of the image
     * @param y the center y coordinate of the image
     * @param image the image to draw
     * @param w the width of the image, after rescaling
     * @param h the height of the image, after rescaling
     * @throws RuntimeException if the image is corrupt
     */
    public void pictureCentered(double x, double y, Image image, double w, double h) {
        if (w < 0 || h < 0) throw new RuntimeException("rescale size can't be negative");
        if (w <= 1.0 && h <= 1.0) pixel(x, y);
        else graphics.drawImage(image, (int)Math.round(x - w/2.0), (int)Math.round(y - h/2.0),
                (int)Math.round(w), (int)Math.round(h), null);
    }

    /**
     * Draw picture with top left at (x, y), rotated given number of degrees
     * about its center, rescaled to w-by-h.
     * @param x the top left x-coordinate of the image
     * @param y the top left y-coordinate of the image
     * @param image the image to draw
     * @param w the width of the image, after rescaling
     * @param h the height of the image, after rescaling
     * @param degrees is the number of degrees to rotate counterclockwise
     * @throws RuntimeException if the image is corrupt
     */
    public void picture(double x, double y, Image image, double w, double h, double degrees) {
        if (w < 0 || h < 0) throw new RuntimeException("rescale size can't be negative");
        if (w <= 1.0 && h <= 1.0) {
            pixel(x, y);
        } else {
            graphics.rotate(Math.toRadians(-degrees), x - w/2.0, y - h/2.0);
            graphics.drawImage(image, (int)Math.round(x), (int)Math.round(y),
                    (int)Math.round(w), (int)Math.round(h), null);
            graphics.rotate(Math.toRadians(+degrees), x - w/2.0, y - h/2.0);
        }
    }

    /**
     * Draw picture centered on (x, y), rotated given number of degrees,
     * rescaled to w-by-h.
     * @param x the center x-coordinate of the image
     * @param y the center y-coordinate of the image
     * @param image the image to draw
     * @param w the width of the image, after rescaling
     * @param h the height of the image, after rescaling
     * @param degrees is the number of degrees to rotate counterclockwise
     * @throws RuntimeException if the image is corrupt
     */
    public void pictureCentered(double x, double y, Image image, double w, double h, double degrees) {
        if (w < 0 || h < 0) throw new RuntimeException("rescale size can't be negative");
        if (w <= 1.0 && h <= 1.0) {
            pixel(x, y);
        } else {
            graphics.rotate(Math.toRadians(-degrees), x, y);
            graphics.drawImage(image, (int)Math.round(x - w/2.0), (int)Math.round(y - h/2.0),
                    (int)Math.round(w), (int)Math.round(h), null);
            graphics.rotate(Math.toRadians(+degrees), x, y);
        }
    }


    /*----------------------------------------------------------------
     *  Drawing text
     *----------------------------------------------------------------*/

    // A component for getting font metrics.
    private static final java.awt.Canvas fontCanvas = new java.awt.Canvas();

    /**
     * Calculate out how wide a string will be when shown on screen using the
     * default font.
     * @param s the string in question.
     */
    public static double textWidth(String s) {
        return textWidth(DEFAULT_FONT, s);
    }

    /**
     * Calculate out how wide a string will be when shown on screen using the
     * given font.
     * @param font the font.
     * @param s the string in question.
     * @return the width
     */
    public static double textWidth(Font font, String s) {
        FontMetrics metrics = fontCanvas.getFontMetrics(font);
        return metrics.stringWidth(s);
    }

    /**
     * Calculate out how wide a string will be when shown on screen using the
     * default font.
     * @param s the string in question.
     */
    public static double textHeight(String s) {
        return textHeight(DEFAULT_FONT, s);
    }

    /**
     * Calculate out how wide a string will be when shown on screen using the
     * given font.
     * @param font the font.
     * @param s the string in question.
     * @return the height
     */
    public static double textHeight(Font font, String s) {
        FontMetrics metrics = fontCanvas.getFontMetrics(font);
        return metrics.getHeight();
    }

    /**
     * Write the given text string in the current font, centered on (x, y).
     * @param x the center x-coordinate of the text
     * @param y the center y-coordinate of the text
     * @param s the text
     */
    public void text(double x, double y, String s) {
        graphics.setFont(font);
        FontMetrics metrics = graphics.getFontMetrics();
        int w = metrics.stringWidth(s);
        int h = metrics.getHeight();
        int d = metrics.getDescent();
        graphics.drawString(s, (float)(x - w/2.0), (float)(y - d + h/2.0));
    }

    /**
     * Write the given text string in the current font, centered on (x, y) and
     * rotated by the specified number of degrees
     * @param x the center x-coordinate of the text
     * @param y the center y-coordinate of the text
     * @param s the text
     * @param degrees is the number of degrees to rotate counterclockwise
     */
    public void text(double x, double y, String s, double degrees) {
        graphics.rotate(Math.toRadians(-degrees), x, y);
        text(x, y, s);
        graphics.rotate(Math.toRadians(+degrees), x, y);
    }

    /**
     * Write the given text string in the current font, left-aligned at (x, y).
     * @param x the left middle x-coordinate of the text
     * @param y the left middle y-coordinate of the text
     * @param s the text
     */
    public void textLeft(double x, double y, String s) {
        graphics.setFont(font);
        FontMetrics metrics = graphics.getFontMetrics();
        int h = metrics.getHeight();
        int d = metrics.getDescent();
        graphics.drawString(s, (float)x, (float)(y - d + h/2.0));
    }

	/**
	 * Get the offscreen backing image for this canvas. If there is no
	 * offscreen backing image, this function returns null.
	 */
	public BufferedImage getBackingImage() {
		return backingImage;
	}

	/**
	 * Pause the program for a short amount of time. Useful for creating
	 * animations.
	 * @param ms the number of milliseconds to pause (1000 milliseconds = 1 second)
	 */
	public static void pause(int ms) {
		try { if (!Window.headless) Thread.sleep(ms); }
		catch (InterruptedException e) { System.out.println("Error sleeping"); }
	}

}
