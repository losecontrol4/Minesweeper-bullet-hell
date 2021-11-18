/*----------------------------------------------------------------
 *  Author:   K. Walsh
 *  Email:    kwalsh@holycross.edu
 *  Written:  7/13/2015
 *  
 *  A simple Graphical User Interface package.
 *----------------------------------------------------------------*/

package GUI;

import java.awt.Color;
import javax.swing.JColorChooser;

/**
 * A <i>CanvasWidget</i> object represents a surface on which a program can draw,
 * where the surface is embedded as a Widget within a Window. 
 */
public class CanvasWidget extends Widget {

	// A canvas that can be drawn to
	public final Canvas canvas;

    /**
     * Constructor: Initialize a new canvas widget with the specified
     * location, size, and resolution. The location and size can be adjusted
	 * later if needed. The resolution, specified in pixels, can't be changed.
     * @param x the x coordinate of the top left corner of the object.
     * @param y the y coordinate of the top left corner of the object.
     * @param width the width of the the object.
     * @param height the height of the the object.
     * @param pixelWidth the horizontal resolution, in pixels.
     * @param pixelHeight the vertical resolution, in pixels.
     */
	public CanvasWidget(double x, double y, double width, double height,
			int pixelWidth, int pixelHeight) { 
		super(x, y, width, height); 
		canvas = new Canvas(pixelWidth, pixelHeight);
		canvas.setAntialiasing(false);
	} 
 
    /**
     * Draw this canvas widget on another canvas. 
     * @param canvas the canvas on which the objct is to be painted.
     */
	public void repaint(Canvas surface) { 
		surface.setAntialiasing(false);
		surface.picture(x, y, canvas.getBackingImage(), width, height); 
		surface.setAntialiasing(true);
	} 

	/**
	 * Use a modal popup window to select a new pen color.
	 */
	public void showColorPicker() {
		Color oldColor = canvas.getPenColor();
		Color newColor = JColorChooser.showDialog(window.getSwingFrame(), "Pick a Color!", oldColor);
		if (newColor != null)
			canvas.setPenColor(newColor);
	}
}

