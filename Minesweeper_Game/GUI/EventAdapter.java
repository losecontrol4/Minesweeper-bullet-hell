/*----------------------------------------------------------------
 *  Author:   K. Walsh
 *  Email:    kwalsh@holycross.edu
 *  Written:  7/13/2015
 *  
 *  A simple Graphical User Interface package.
 *----------------------------------------------------------------*/

package GUI;

/**
 * <i>GUI.EventAdapter</i> is meant to be used as a base class for things that
 * respond to user interaction with the mouse or keyboard. It does nothing by
 * itself, but it defines all the necessary functions so that subclasses can
 * just override a few of them, i.e. the ones they want.
 */
public class EventAdapter {

    /**
     * This function does nothing, but its presence makes it easier for
     * subclasses to implement EventListener.
     */
    public void mouseClicked(double x, double y, String button) { }

    /**
     * This function does nothing, but its presence makes it easier for
     * subclasses to implement EventListener.
     */
    public void mousePressed(double x, double y, String button) { }

    /**
     * This function does nothing, but its presence makes it easier for
     * subclasses to implement EventListener.
     */
    public void mouseDragged(double x, double y) { }

    /**
     * This function does nothing, but its presence makes it easier for
     * subclasses to implement EventListener.
     */
    public void mouseReleased(double x, double y, String button) { }

    /**
     * This function does nothing, but its presence makes it easier for
     * subclasses to implement EventListener.
     */
    public void mouseEntered(double x, double y) { }

    /**
     * This function does nothing, but its presence makes it easier for
     * subclasses to implement EventListener.
     */
    public void mouseExited(double x, double y) { }

    /**
     * This function does nothing, but its presence makes it easier for
     * subclasses to implement EventListener.
     */
    public void keyTyped(char c) { }

    public void specialKeyTyped(String keyText) { }
}
