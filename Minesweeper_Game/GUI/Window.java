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
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * A <i>Window</i> object represents a window on the screen. "Widgets" (see
 * class GUI.Widget), such as buttons and labels, can be added to the window,
 * and the window will automatically call each widget's repaint() function
 * periodically as needed. The window can also have a custom background (see the
 * repaintWindowBackground() function). This class is very much like the Draw
 * and StdDraw classes by Sedgewick and Wayne, with some differences:
 * <ul>
 *   <li>This class provides only window-related functions. Draw and StdDraw
 *   also have code for the drawing.</li>
 *   <li>This class provides somewhat richer support for interactive GUIs.</li>
 *  </ul>
 *  For documentation Draw and StdDraw, see
 *  <a href="http://introcs.cs.princeton.edu/31datatype" target="_top">Section 3.1</a> of
 *  <i>Introduction to Programming in Java: An Interdisciplinary Approach</i> by
 *  Robert Sedgewick and Kevin Wayne.
 */
public class Window extends EventAdapter implements EventListener {

    /**
     * Default canvas width.
     */
    public static final int DEFAULT_WIDTH = 640;

    /**
     * Default canvas height.
     */
    public static final int DEFAULT_HEIGHT = 480;

    /**
     * The size of this window as it appears on the screen.
     */
    protected double width, height;

    // Lock objects for synchronization.
    private Object closeLock = new Object();
    private Object refreshLock = new Object();
    private Object mouseLock = new Object();
    private Object keyLock = new Object();

    /**
     * The underlying frame for drawing to the screen.
     */
    private JFrame frame;

    /**
     * Images for double buffering.
     */
    private BufferedImage offscreenImage, onscreenImage;

    /**
     * Setting headless to true will disable all graphics and run in
     * console-only mode.
     */
    public static boolean headless = false;

    /**
     * Graphics contexts for drawing into double buffering images.
     */
    private Graphics2D offscreen, onscreen;

    /**
     * Canvas for subclasses and GUI.Widget classes to use for painting in the
     * window.
     */
    private Canvas canvas;

    /**
     * The title of this window.
     */
    private String title;

    /**
     * Last known location (center of window) on screen.
     */
    private Point center;

    // Whether a refresh is pending or not.
    private boolean refreshPending = false;

    // Last known mouse state.
    private boolean leftMousePressed = false;
    private boolean middleMousePressed = false;
    private boolean rightMousePressed = false;
    private double mouseX, mouseY;
    private double dragX, dragY;

    // Last known keyboard state.
    private LinkedList<Character> keysTyped = new LinkedList<Character>();
    private TreeSet<Integer> keysDown = new TreeSet<Integer>();

    // Listeners to be notified of events.
    private ArrayList<EventListener> listeners = new ArrayList<EventListener>();

    // Children to be painted.
    private ArrayList<Widget> children = new ArrayList<Widget>();

    // Children to be potentially notified of events.
    private ArrayList<Widget> childListeners = new ArrayList<Widget>();

    // Children currently receiving events.
    private ArrayList<Widget> childActivated = new ArrayList<Widget>();

    // A Timer to cause periodic refreshing during animation.
    private javax.swing.Timer animation;

    /**
     * Initialize a new window with no title and default size (640x480).
     */
    public Window() {
	this("", DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Initialize a new window with given title and default size (640x480).
     * @param title the window title.
     */
    public Window(String title) {
	this(title, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Initialize a new window with given title and size.
     * @param width the window width..
     * @param height the window width..
     * @param title the window title.
     */
    public Window(String title, int width, int height) {
	this.width = width;
	this.height = height;
	this.title = title;

	if (!headless)
	    frame = new JFrame();
	offscreenImage = new BufferedImage((int)width, (int)height, BufferedImage.TYPE_INT_ARGB);
	onscreenImage  = new BufferedImage((int)width, (int)height, BufferedImage.TYPE_INT_ARGB);
	offscreen = offscreenImage.createGraphics();
	onscreen  = onscreenImage.createGraphics();
	canvas = new Canvas(offscreen, (int)width, (int)height);

	// Use a JLabel to get mouse events.
	JLabel content = new JLabel(new ImageIcon(onscreenImage));
	InputListener listener = new InputListener();
	content.addMouseListener(listener);
	content.addMouseMotionListener(listener);
	if (!headless) {
	    frame.setContentPane(content);

	    // Use frame to get keyboard, since JLabel cannot get keyboard focus.
	    frame.addKeyListener(listener);
	    frame.addWindowListener(new CloseWindowListener());
	    // frame.addComponentListener(new HideWindowListener());
	    frame.setResizable(false);
	    // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      // closes all windows
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);      // closes only current window
	    frame.setTitle(title);
	    frame.setJMenuBar(createMenuBar());
	    frame.pack();
	    frame.setFocusable(true); 
	    frame.requestFocusInWindow();

	    // Center in the window
	    if (center == null) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		center = new Point(dim.width / 2, dim.height / 2);
	    }
	    Dimension dim = frame.getSize();
	    frame.setLocation(
		    center.x - dim.width / 2,
		    center.y - dim.height / 2);
	}

	// Start listening for events
	if (this instanceof EventListener)
	    listeners.add((EventListener)this);

	// Prepare animation. The 1 ms delay is just a placeholder, it will be
	// changed in animate().
	animation = new javax.swing.Timer(1, new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		refresh();
	    }
	});
    }

    // Create the menu bar.
    private JMenuBar createMenuBar() {
	JMenuBar menuBar = new JMenuBar();
	JMenu menu = new JMenu("File");
	menuBar.add(menu);
	JMenuItem menuItem1 = new JMenuItem(" Save...   ");
	menuItem1.addActionListener(new SaveAsListener());
	menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	menu.add(menuItem1);
	return menuBar;
    }


    /*----------------------------------------------------------------
     *  Controlling the window and its appearance
     *----------------------------------------------------------------*/

    /**
     * Get the width of this window as it will be drawn on the canvas.
     */
    public double getWidth() { return width; }

    /**
     * Get the height of this window as it will be drawn on the canvas.
     */
    public double getHeight() { return height; }


    /**
     * Set the background color to the given color.
     * @param color the Color to make the background
     */
    public void setBackgroundColor(Color color) {
	canvas.setBackgroundColor(color);
    }

    /**
     * Make this window appear on the screen. Also triggers a repaint. If window
     * is already opened, then it just triggers a repaint.
     */
    public void show() {
	if (!headless && !frame.isVisible()) {
	    frame.setVisible(true);
	}
	refresh();
    }

    /**
     * Make this window appear on the screen, then wait for the user to close
     * it. Also triggers a repaint. If window is already opened, then it just
     * triggers a repaint and waits for the user to close it.
     */
    public void showAndWait() {
	show();
	waitForClose();
    }

    /**
     * Make this window appear on the screen, then continually trigger repaints
     * so it looks animated. If window is already opened, then it just starts
     * triggering the repainting. This function does not return until the user
     * closes the window.
     * @param fps the desired number of frames per second for the animation
     */
    public void showAndAnimate(double fps) {
	show();
	animate(fps);
	waitForClose();
    }

    /**
     * Wait for this window to be closed. 
     */
    public void waitForClose() {
	if (headless)
	    return;
	synchronized(closeLock) {
	    while (frame.isVisible()) {
		try {closeLock.wait();} catch (InterruptedException e) { }
	    }
	}
    }

    /**
     * Hide (aka close) this window so it does not appear on the screen. If the
     * window is already closed, this has no effect.
     */
    public void hide() {
	if (!headless && frame.isVisible()) {
	    animation.stop();
	    Dimension dim = frame.getSize();
	    center = frame.getLocationOnScreen();
	    center.translate(dim.width / 2, dim.height / 2);
	    frame.setVisible(false);
	    frame.dispose();
	}
    }

    /**
     * Trigger a repaint so as to refresh the contents of this window. This will
     * cause repaint() to be called for this window, eventually. If the window
     * is not visible or if refresh() was already called recently, this function
     * has no effect.
     */
    public void refresh() {
	synchronized (refreshLock) {
	    if (refreshPending || (!headless && !frame.isVisible())) return;
	    refreshPending = true;
	}
	java.awt.EventQueue.invokeLater(new Runnable() {
	    public void run() {
		hardRefresh();    
	    }});
    }

    /**
     * Animate this window by triggering repainting periodically to refresh
     * contents of this window. If the window is already anaimating, this
     * function changes the speed of the animation. 
     * @param fps the desired number of frames per second for the animation
     */
    public void animate(double fps) {
	int delayMillis = (int)Math.round(1000.0 / fps);
	animation.setDelay(delayMillis);
	animation.restart();
    }

    /**
     * Stop any animation in this window, cancelling the periodic refreshing.
     * If the window is not already anaimating, this function does nothing.
     */
    public void stopAnimation() {
	animation.stop();
    }

    // Perform the actual refresh.
    private void hardRefresh() {
	synchronized (refreshLock) {
	    if (!refreshPending) return;
	    refreshPending = false;
	    refreshLock.notifyAll();
	    if (!headless && !frame.isVisible()) return;
	}
	canvas.reset();
	canvas.clear();
	// First paint the background.
	repaintWindowBackground(canvas); // this draws to offscreenImage
	// Next paint any child widgets.
	for (Widget widget : children) {
	    canvas.setPenColor();
	    canvas.setPenRadius();
	    widget.repaint(canvas); // this draws to offscreenImage
	}
	// Copy the image to the screen.
	if (!headless) {
	    onscreen.drawImage(offscreenImage, 0, 0, null);
	    frame.repaint();
	}
    }

    /**
     * Repaint the background of this window onto the given canvas. Subclasses
     * can implement this method to draw things on the screen. This method will
     * be called periodically, whenever the system decides the screen is out of
     * date. This will get called after the canvas has been cleared, and before
     * painting any of the GUI.Widget (that is, buttons, labels, and other
     * things that were passed as arguments to add()). Do not call this method
     * directly, since it will be called automatically by the system as needed.
     * Instead, you can call refresh() manually at any time, which will
     * (eventually) cause the system to invoke this method for you.
     */
    public void repaintWindowBackground(Canvas canvas) { }

    /**
     * Get the underlying Java Swing JFrame for this window. If running in
     * headless mode, this function returns null, since there is no underlying
     * JFrame in that case.
     */
    public JFrame getSwingFrame() {
	return frame;
    }

    // Listener for window close events.
    private class CloseWindowListener extends WindowAdapter {
	public void	windowClosed(WindowEvent e) {
	    stopAnimation();
	    synchronized(closeLock) {
		closeLock.notifyAll();
	    }
	}
    }

    // Listen for window hiding events.
    /* private class HideWindowListener extends ComponentAdapter {
       public void	componentHidden(ComponentEvent e) {
       synchronized(closeLock) {
       closeLock.notifyAll();
       }
       }
       } */

    /**
     * Add a new GUI.Widget to this window. Whenever a repaint() of this window
     * happens, the widget's repaint() function will be called. In adddition, if
     * the widget implements the GUI.EventListener interface, then it will be
     * notified of relevant mouse and keyboard events.
     */
    public void add(Widget widget) {
	double x = widget.getX();
	double y = widget.getY();
	double w = widget.getWidth();
	double h = widget.getHeight();
	if (x < 0.0 || y < 0.0 || x + w > width || y + h > height) {
	    System.out.println("Warning: A graphical widget lies partly outside the bounds of the window.");
	    System.out.println("Window size is " + width + " x " + height +".");
	    System.out.println("Widget is at (" + x + ", " + y + ") with size " + w + " x " + h + ".");
	}
	widget.setWindow(this);
	children.add(widget);
	if (widget instanceof EventListener)
	    childListeners.add(widget);
    }


    /*----------------------------------------------------------------
     *  Mouse and keyboard status and interaction
     *----------------------------------------------------------------*/

    /**
     * Is the left mouse button being pressed?
     * @return true or false
     */
    public boolean mousePressed() {
	synchronized (mouseLock) {
	    return leftMousePressed;
	}
    }

    /**
     * Is the left mouse button being pressed?
     * @return true or false
     */
    public boolean leftMousePressed() {
	synchronized (mouseLock) {
	    return leftMousePressed;
	}
    }

    /**
     * Is the middle mouse button being pressed?
     * @return true or false
     */
    public boolean middleMousePressed() {
	synchronized (mouseLock) {
	    return middleMousePressed;
	}
    }

    /**
     * Is the right mouse button being pressed?
     * @return true or false
     */
    public boolean rightMousePressed() {
	synchronized (mouseLock) {
	    return rightMousePressed;
	}
    }

    /**
     * What is the x-coordinate of the mouse?
     * @return the value of the x-coordinate of the mouse
     */
    public double mouseX() {
	synchronized (mouseLock) {
	    return mouseX;
	}
    }

    /**
     * What is the y-coordinate of the mouse?
     * @return the value of the y-coordinate of the mouse
     */
    public double mouseY() {
	synchronized (mouseLock) {
	    return mouseY;
	}
    }

    /**
     * Attach a new GUI.EventListener to this window. Whenever a keyboard or
     * mouse event is detected, the window will notify the listener by calling
     * the appropriate method, such as mouseClicked() or keyTyped(). It will be
     * notified of all events that occur within the window.
     */
    public void addListener(EventListener listener) {
	listeners.add(listener);
    }

    // Listener for mouse and key events.
    private class InputListener implements MouseListener , MouseMotionListener, KeyListener {

	// Convert screen to user to coordinates, update state, and
	// update child activations.
	private Point.Double track(MouseEvent e) {
	    double x = e.getX();
	    double y = e.getY();
	    synchronized (mouseLock) {
		mouseX = x;
		mouseY = y;
	    }
	    for (Widget child : childListeners) {
		boolean inside = child.containsPoint(x, y);
		boolean activated = childActivated.contains(child);
		if (inside && !activated) {
		    childActivated.add(child);
		    child.mouseEntered(x, y);
		} else if (activated && !inside) {
		    childActivated.remove(child);
		    child.mouseExited(x, y);
		}
	    }
	    return new Point.Double(x, y);
	}

	private String buttonName(int mouseButton) {
	    if (mouseButton == MouseEvent.BUTTON1) return "left";
	    else if (mouseButton == MouseEvent.BUTTON2) return "middle";
	    else if (mouseButton == MouseEvent.BUTTON3) return "right";
	    else return "unknown";
	}

	public void mousePressed(MouseEvent e) {
	    Point.Double p = track(e);
	    String b = buttonName(e.getButton());
	    synchronized (mouseLock) {
		dragX = p.x;
		dragY = p.y;
		if (b.equals("left")) leftMousePressed = true;
		else if (b.equals("middle")) middleMousePressed = true;
		else if (b.equals("right")) rightMousePressed = true;
	    }
	    for (EventListener listener : listeners) {
		listener.mousePressed(p.x, p.y, b);
	    }
	    for (Widget child : childListeners) {
		if (child.containsPoint(p.x, p.y))
		    child.mousePressed(p.x, p.y, b);
	    }
	    refresh();
	}

	public void mouseReleased(MouseEvent e) {
	    Point.Double p = track(e);
	    String b = buttonName(e.getButton());
	    double x, y;
	    synchronized (mouseLock) {
		x = dragX;
		y = dragY;
		if (b.equals("left")) leftMousePressed = false;
		else if (b.equals("middle")) middleMousePressed = false;
		else if (b.equals("right")) rightMousePressed = false;
	    }
	    for (EventListener listener : listeners) {
		listener.mouseReleased(p.x, p.y, b);
	    }
	    for (Widget child : childListeners) {
		if (child.containsPoint(x, y))
		    child.mouseReleased(p.x, p.y, b);
	    }
	    refresh();
	}

	public void mouseDragged(MouseEvent e)  {
	    Point.Double p = track(e);
	    double x, y;
	    synchronized (mouseLock) {
		x = dragX;
		y = dragY;
	    }
	    for (EventListener listener : listeners) {
		listener.mouseDragged(p.x, p.y);
	    }
	    for (Widget child : childListeners) {
		if (child.containsPoint(x, y))
		    child.mouseDragged(p.x, p.y);
	    }
	    refresh();
	}

	public void mouseMoved(MouseEvent e) {
	    track(e); // updates mouseX, mouseY, and childActivated
	}

	public void mouseClicked(MouseEvent e) {
	    Point.Double p = track(e);
	    String b = buttonName(e.getButton());
	    for (EventListener listener : listeners) {
		listener.mouseClicked(p.x, p.y, b);
	    }
	    for (Widget child : childListeners) {
		if (child.containsPoint(p.x, p.y))
		    child.mouseClicked(p.x, p.y, b);
	    }
	    refresh();
	}

	public void mouseEntered(MouseEvent e) { /* unused */ }
	public void mouseExited(MouseEvent e) { /* unused */ }

	public void keyTyped(KeyEvent e) {
	    synchronized (keyLock) {
		keysTyped.addFirst(e.getKeyChar());
	    }
	    for (EventListener listener : listeners)
		listener.keyTyped(e.getKeyChar());
	    refresh();
	}

	public void keyPressed(KeyEvent e) {
	    synchronized (keyLock) {
		keysDown.add(e.getKeyCode());
	    }
	}

	public void keyReleased(KeyEvent e) {
	    synchronized (keyLock) {
		keysDown.remove(e.getKeyCode());
		if (e.isActionKey())
		    for (EventListener listener : listeners)
			listener.specialKeyTyped(KeyEvent.getKeyText(e.getKeyCode()));

	    }
	}

    }

    /*----------------------------------------------------------------
     *  Saving screenshots
     *----------------------------------------------------------------*/

    // Listener for "Save as..." menu events.
    private class SaveAsListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    FileDialog chooser = new FileDialog(frame, "Use a .png or .jpg extension", FileDialog.SAVE);
	    chooser.setVisible(true);
	    String filename = chooser.getFile();
	    String suffix = filename.substring(filename.lastIndexOf('.') + 1);
	    if (filename != null &&
		    !suffix.toLowerCase().equals("png") &&
		    !suffix.toLowerCase().equals("jpg")) {
		JOptionPane.showMessageDialog(frame, 
			"Sorry, the extension must be '.png' or '.jpg'.",
			"Error saving image!", JOptionPane.ERROR_MESSAGE);
	    } else if (filename != null) {
		String path = chooser.getDirectory() + File.separator + filename;
		screenshot(path);
	    }
	}
    }

    public void screenshot(String filename) {
	try {
            if (headless) {
                canvas.reset();
                canvas.clear();
                // First paint the background.
                repaintWindowBackground(canvas); // this draws to offscreenImage
                // Next paint any child widgets.
                for (Widget widget : children) {
                    canvas.setPenColor();
                    canvas.setPenRadius();
                    widget.repaint(canvas); // this draws to offscreenImage
                }
            }
	    Images.save(offscreenImage, filename);
	} catch (IOException err) {
	    String msg = err.getMessage();
	    if (msg == null)
		msg = "Unknown error.";
	    JOptionPane.showMessageDialog(frame, 
		    msg, "Error saving image!", JOptionPane.ERROR_MESSAGE);
	}
    }

    // A test class
    private static class TestWindow extends Window implements EventListener {

	Color fg = Canvas.BLUE, bg = Canvas.WHITE;
	Button quit, spin;
	Box left, middle, right;
	Label label;
	Random random = new Random();
	static final int W = 400;
	static final int H = 400;

	public TestWindow() {
	    super("Test Window", W, H);

	    quit = new Button(W*0.5/4, 40, "Quit");
	    quit.setForegroundColor(Canvas.WHITE);
	    quit.setBackgroundColor(Canvas.BOOK_RED);
	    quit.setBorderColor(Canvas.BOOK_RED.darker());
	    add(quit);

	    spin = new Button(W*1.5/4, 40, "Recolor!");
	    spin.setForegroundColor(Canvas.WHITE);
	    spin.setBackgroundColor(Canvas.BOOK_BLUE);
	    spin.setBorderColor(Canvas.BOOK_BLUE.darker());
	    add(spin);

	    Button b = new Button(W*2.5/4, 40, "");
	    b.setImage("rose.png");
	    b.setBackgroundColor(Canvas.BOOK_RED);
	    b.setBorderColor(Canvas.BOOK_BLUE);
	    b.setBorderWidth(5);
	    b.setCornerRadius(5);
	    add(b);

	    Picture p = new Picture(W*3.5/4, 40, "rose.png");
	    p.setBackgroundColor(null);
	    add(p);

	    left = new Box(W*0.5/3-20, H-60, 40, 40);
	    left.setCornerRadius(0);
	    left.setBorderWidth(3);
	    add(left);

	    middle = new Box(W*1.5/3-20, H-60, 40, 40);
	    middle.setBorderWidth(5);
	    middle.setCornerRadius(7.5);
	    add(middle);

	    right = new Box(W*2.5/3-20, H-60, 40, 40);
	    right.setBorderWidth(5);
	    right.setCornerRadius(10);
	    add(right);

	    label = new Label(W/2, H/2, "Hello, World!");
	    add(label);

	    recolor();
	}

	private void recolor() {
	    setBackgroundColor(bg);

	    left.setBackgroundColor(fg);
	    left.setBorderColor(fg.darker());

	    middle.setBackgroundColor(fg);
	    middle.setBorderColor(null);

	    right.setBackgroundColor(null);
	    right.setBorderColor(fg.darker());

	    label.setForegroundColor(fg);
	    label.setBackgroundColor(bg.darker());
	    label.setBorderColor(null);
	}

	public void repaint(Canvas canvas) {
	    canvas.setPenColor(fg);
	    canvas.setPenRadius();
	    canvas.line(0, 100, 300, 100);
	    canvas.line(150, 0, 150, 200);
	}

	public void mouseClicked (double x, double y, String button) {
	    if (quit.containsPoint(x, y)) {
		hide();
	    } else if (spin.containsPoint(x, y)) {
		int r = random.nextInt(255);
		int g = random.nextInt(255);
		int b = random.nextInt(255);
		fg = new Color(r, g, b);
		bg = new Color(255-r, 255-g, 255-b);
		recolor();
	    }
	}

    }

    /**
     * A main function for testing.
     */
    public static void main(String args[]) {
	Window w = new TestWindow();
	w.show();
	// w.waitForClose();
    }
}
