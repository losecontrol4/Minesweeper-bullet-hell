/*----------------------------------------------------------------
 *  Author:   K. Walsh
 *  Email:    kwalsh@holycross.edu
 *  Written:  7/13/2015
 *  
 *  A simple Graphical User Interface package.
 *----------------------------------------------------------------*/

package GUI;

/**
 * <i>GUI.AsyncEventAdapter</i> is meant to be used as a base class for things that
 * respond <i>asynchronously</i> to user interaction with the mouse or keyboard. For
 * each mouse or keyboard event, this spawn a thread and calls a matching
 * function at some later time on that thread. Subclasses can override these
 * matching functions.
 */
public class AsyncEventAdapter implements EventListener {

    /**
     * This function calls mouseWasClicked, asychronously.
     */
    public void mouseClicked(final double x, final double y, final String button) { 
		new Thread() { public void run() { mouseWasClicked(x, y, button); } }.start();
	}

    /**
     * This function calls mouseWasPressed, asychronously.
     */
    public void mousePressed(final double x, final double y, final String button) {
		new Thread() { public void run() { mouseWasPressed(x, y, button); } }.start();
	}

    /**
     * This function calls mouseWasDragged, asychronously.
     */
    public void mouseDragged(final double x, final double y) {
		new Thread() { public void run() { mouseWasDragged(x, y); } }.start();
	}

    /**
     * This function calls mouseWasReleased, asychronously.
     */
    public void mouseReleased(final double x, final double y, final String button) {
		new Thread() { public void run() { mouseWasReleased(x, y, button); } }.start();
	}

    /**
     * This function calls mouseDidEnter, asychronously.
     */
    public void mouseEntered(final double x, final double y) {
		new Thread() { public void run() { mouseDidEnter(x, y); } }.start();
	}

    /**
     * This function calls mouseDidExit, asychronously.
     */
    public void mouseExited(final double x, final double y) {
		new Thread() { public void run() { mouseDidExit(x, y); } }.start();
	}

    /**
     * This function calls keyWasTyped, asychronously.
     */
    public void keyTyped(final char c) {
		new Thread() { public void run() { keyWasTyped(c); } }.start();
	}

    /**
     * This function calls specialKeyWasTyped, asychronously.
     */
    public void specialKeyTyped(final String keyText) {
		new Thread() { public void run() { specialKeyWasTyped(keyText); } }.start();
	}

    /**
     * This function will be called <i>after</i> the user clicks (press and release
     * in quick succession).
     * @param x the x coordinate of the point where the user clicked.
     * @param y the x coordinate of the point where the user clicked.
     * @param button the name of the mouse button the user clicked, either
     * "left", "middle", or "right".
     */
    public void mouseWasClicked (double x, double y, String button) { }

    /**
     * This function will be called <i>after</i> the user presses a mouse button.
     * @param x the x coordinate of the point where the user pressed.
     * @param y the x coordinate of the point where the user pressed.
     * @param button the name of the mouse button the user pressed, either
     * "left", "middle", or "right".
     */
    public void mouseWasPressed (double x, double y, String button) { }

    /**
     * This function will be called <i>after</i> the user drags the mouse while
     * holding a button. It will be called after mousePressed().
     * @param x the x coordinate of the point where the user dragged.
     * @param y the x coordinate of the point where the user dragged.
     */
    public void mouseWasDragged (double x, double y) { }

    /**
     * This function will be called <i>after</i> the user releases a mouse a
     * button. It will be called after mousePrssed() or mouseDragged().
     * @param x the x coordinate of the point where the user released.
     * @param y the x coordinate of the point where the user released.
     * @param button the name of the mouse button the user pressed, either
     * "left", "middle", or "right".
     */
    public void mouseWasReleased(double x, double y, String button) { }

    /**
     * This function will be called <i>after</i> the user moves the mouse into the
     * bounding box of this object.
     * @param x the x coordinate of the point where the mouse is.
     * @param y the x coordinate of the point where the mouse is.
     */
    public void mouseDidEnter(double x, double y) { }

    /**
     * This function will be called <i>after</i> the user moves the mouse out of
     * the bounding box of this object.
     * @param x the x coordinate of the point where the mouse is.
     * @param y the x coordinate of the point where the mouse is.
     */
    public void mouseDidExit(double x, double y) { }

    /**
     * This function will be called <i>after</i> the user types on the keyboard.
     * @param c the key pressed.
     */
    public void keyWasTyped(char c) { }

    public void specialKeyWasTyped(String keyText) { }
}
