/*----------------------------------------------------------------
 *  Author:   K. Walsh
 *  Email:    kwalsh@holycross.edu
 *  Written:  7/13/2015
 *  
 *  A simple Graphical User Interface package.
 *----------------------------------------------------------------*/

package GUI;

/**
 * <i>GUI.EventListener</i> is an interface for objects interested in hearing
 * about events related to a user interaction, such as mouse clicks and key
 * presses.
 */
public interface EventListener {

    /**
     * This function will be called whenever the user clicks (press and release
     * in quick succession).
     * @param x the x coordinate of the point where the user clicked.
     * @param y the x coordinate of the point where the user clicked.
     * @param button the name of the mouse button the user clicked, either
     * "left", "middle", or "right".
     */
    public void mouseClicked (double x, double y, String button);

    /**
     * This function will be called whenever the user presses a mouse button.
     * @param x the x coordinate of the point where the user pressed.
     * @param y the x coordinate of the point where the user pressed.
     * @param button the name of the mouse button the user pressed, either
     * "left", "middle", or "right".
     */
    public void mousePressed (double x, double y, String button);

    /**
     * This function will be called whenever the user drags the mouse while
     * holding a button. It will be called after mousePressed().
     * @param x the x coordinate of the point where the user dragged.
     * @param y the x coordinate of the point where the user dragged.
     */
    public void mouseDragged (double x, double y);

    /**
     * This function will be called whenever the user releases a mouse a
     * button. It will be called after mousePrssed() or mouseDragged().
     * @param x the x coordinate of the point where the user released.
     * @param y the x coordinate of the point where the user released.
     * @param button the name of the mouse button the user pressed, either
     * "left", "middle", or "right".
     */
    public void mouseReleased(double x, double y, String button);

    /**
     * This function will be called whenever the user moves the mouse into the
     * bounding box of this object.
     * @param x the x coordinate of the point where the mouse is.
     * @param y the x coordinate of the point where the mouse is.
     */
    public void mouseEntered(double x, double y);

    /**
     * This function will be called whenever the user moves the mouse out of
     * the bounding box of this object.
     * @param x the x coordinate of the point where the mouse is.
     * @param y the x coordinate of the point where the mouse is.
     */
    public void mouseExited(double x, double y);

    /**
     * This function will be called whenever the user types on the keyboard.
     * @param c the key pressed.
     */
    public void keyTyped(char c);

    public void specialKeyTyped(String keyText);

}
