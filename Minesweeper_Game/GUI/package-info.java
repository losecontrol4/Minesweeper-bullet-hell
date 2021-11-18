/**
 * GUI is a simple Graphical User Interface package, designed to help build
 * interactive graphical programs. 
 *
 * <h2>Alternatives</h2>
 * Standard java includes extensive support for interactive graphical programs,
 * mostly in the form of the java.awt and javax.swing packages. These standard
 * packages are rather complex and often overwhelming for new programmers. The
 * GUI package is far simpler, at the cost of only supporting fairly simple
 * programs.
 * <p>
 * The GUI package was inspired by Sedgewick and Wayne's StdDraw and Draw
 * classes, and it borrows heavily from that code. If you have used the drawing
 * capabilities of StdDraw and Draw, then the GUI.Canvas class should feel
 * familar, for example. The key differences between the GUI package and the
 * StdDraw and Draw classes are:
 * <ul>
 *   <li>GUI has more support for interactive programs.</li>
 *   <li>The StdDraw and Draw classes contain code for both managing a window
 *   and drawing inside the window. The GUI package separates these tasks into
 *   two separate classes: GUI.Window and GUI.Canvas. Gui.Window represents a
 *   window, while GUI.Canvas provides a surface on which things can be
 *   drawn.</li>
 *   <li>The GUI.Canvas class provides a somewhat richer set of drawing
 *   functions than StdDraw or Draw.</li>
 *   <li>GUI provides a set of widgets -- buttons, labels, boxes, and so on --
 *   that can be added to a GUI.Window.</li>
 * </ul>
 *
 *  For documentation Draw and StdDraw, see
 *  <a href="http://introcs.cs.princeton.edu/31datatype">Section 3.1</a> of
 *  <i>Introduction to Programming in Java: An Interdisciplinary Approach</i> by
 *
 * <h2>Quick GUI Tutorial: Windows</h2>
 *
 * Step 1: Make a subclass of GUI.Window. This will represent a window on the
 * screen. 
 * <p>
 * Step 2: In the subclass constructor, you can add labels, buttons, and other
 * widgets by calling the add() function.
 * <p>
 * Step 3: To make it interactive, add mouseClicked() and/or keyTyped() methods
 * to the subclass. These methods define what you want to happen when the user
 * clicks or types a key in the window.
 * <p>
 * You can have a custom window background by adding a repaintWindowBackground()
 * function to your subclass. See the GUI.Window documentation for details.
 * <p>
 * You can respond to other kinds of events besides clicks and typing. See
 * GUI.EventListener for details.
 * <pre>
 * import GUI.*;
 * public class Demo extends GUI.Window {
 * 
 *     // Some member variables
 *     Button hello;
 *     Label message;
 *     MyWidget thing;
 * 
 *     public Demo() {
 *         // Constructor should call GUI.Window constructor with the desired
 *         // title and size of the window.
 *         super("I am a title!", 300, 200);
 * 
 *         // It can call GUI.Window methods...
 *         setBackgroundColor(Canvas.PINK);
 * 
 *         // And it can create and add widgets like buttons...
 *         hello = new Button(150, 60, "Click Me!");
 *         hello.setForegroundColor(Canvas.WHITE);
 *         hello.setCornerRadius(10);
*         add(hello);
* 
*         // ... or labels...
*         message = new Label(150, 160, "Read Me!");
*         message.setBorderWidth(5);
*         add(message);
* 
*         // ... or even custom widgets (see tutorial below)...
*         thing = new MyWidget(75, 100, 150, 50);
*         add(thing);
*     }
* 
*     public void keyTyped(char c) {
    *         // respond to key presses here...
        *         if (c == 'q')
        *             hide(); // this closes the window
    *         else
        *             message.setText("You typed: " + c);
    *     }
    * 
    *     public void mouseClicked(double x, double y, String button) {
        *         // respond to mouse clicks here...
            *         if (button.equals("left") &amp;&amp; hello.containsPoint(x, y))
            *             hello.setText("Enough!");
        *     }
        * 
        *     public static void main(String args[]) {
            *         // Create a demo window
                *         Demo d = new Demo();
            *         // Show it on the screen and wait for the user to close it
                *         d.showAndAnimate(30);
            *         // We are done!
                *         System.out.println("Window was closed... Goodbye!");
            *     }
            * }
            * </pre>
            *
            * <h2>Quick GUI Tutorial: Widgets</h2>
            * 
            * There are only a few basic widgets provided by the GUI class: Box (a
                    * rectangule or rounded rectangle with an outline and a background color),
            * Picture (similar, but with an image), Label (similar, but with some text
                    * too), and Button (similar, but with some color/hilighting effects). Most of
                                                                            * the widgets are customizable to a small degree. You can change the border
                                                                            * colors and thicknesses, for example.
                                                                            * <p>
                                                                            * You can write your own custom widgets by creating a subclass of GUI.Widget
                                                                            * (or a subclass of GUI.Button or any of the other existing widgets). You can
                                                                            * override the repaint() method to give your widget any look you like.
                                                                            * <p>
                                                                            * If your subclass implements the EventListener interface, then you can also
                                                                            * add functions to handle key presses and mouse events, just like window does.
                                                                            * <p>
                                                                            * <pre>
                                                                            *   import GUI.*;
                                                                            *   public class MyWidget extends GUI.Widget {
                                                                                *
                                                                                    *       double start; // a member variable
                                                                                *
                                                                                    *       public MyWidget(double x, double y, double width, double height) {
                                                                                        *           // Constructor can use any of the Widget constructors or methods.
                                                                                            *           // Otherwise, just initialize member variables as usual.
                                                                                            *           super(x, y, width, height);
                                                                                        *           start = System.currentTimeMillis();
                                                                                        *       }
                                                                                        *
                                                                                            *       public void repaint(Canvas canvas) {
                                                                                                *           // Draw whatever we want our widget to look like.
                                                                                                    *           double t = (System.currentTimeMillis() - start) / 1000.0;
                                                                                                *
                                                                                                    *           canvas.setPenColor(Canvas.YELLOW);
                                                                                                *           canvas.filledRectangle(x, y, width, height);
                                                                                                *
                                                                                                    *           canvas.setPenColor(Canvas.BLACK);
                                                                                                *           canvas.text(x + width/2, y + height/2, "Time = " + t);
                                                                                                *       }
                                                                                                *   }
                                                                                                * </pre>
                                                                                                *
                                                                                                */
                                                                                                package GUI;

                                                                                                // This file contains no code. It is here to give a comment about the package.
