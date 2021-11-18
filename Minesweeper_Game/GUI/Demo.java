/*----------------------------------------------------------------
 *  Author:   K. Walsh
 *  Email:    kwalsh@holycross.edu
 *  Written:  7/13/2015
 *  
 *  A very simple demo of the GUI package.
 *
 *  Example: java Demo
 *----------------------------------------------------------------*/

import GUI.*;

public class Demo extends Window {

    public static class MyWidget extends Widget {
        double start;
        public MyWidget(double x, double y, double width, double height) {
            super(x, y, width, height);
            start = System.currentTimeMillis();
        }
        public void repaint(Canvas canvas) {
            double t = (System.currentTimeMillis() - start) / 1000.0;

            canvas.setPenColor(Canvas.YELLOW);
            canvas.filledRectangle(x, y, width, height);

            canvas.setPenColor(Canvas.BLACK);
            canvas.text(x + width/2, y + height/2, "Time = " + t);
        }
    }

    Button hello;
    Label message;
    MyWidget thing;

    public Demo() {
        super("I am a title!", 300, 200);

        setBackgroundColor(Canvas.PINK);

        hello = new Button(150, 60, "Click Me!");
        hello.setForegroundColor(Canvas.WHITE);
        hello.setCornerRadius(10);
        add(hello);

        message = new Label(150, 160, "Read Me!");
        message.setBorderWidth(5);
        add(message);

        thing = new MyWidget(75, 100, 150, 50);
        add(thing);
    }

    public void keyTyped(char c) {
        if (c == 'q')
            hide(); // this closes the window
        else
            message.setText("You typed: " + c);
    }

    public void mouseClicked(double x, double y, String button) {
        if (button.equals("left") && hello.containsPoint(x, y))
            hello.setText("Enough!");
    }

    public static void main(String args[]) {
        Demo d = new Demo();
        d.showAndAnimate(30);
        System.out.println("Window was closed... Goodbye!");
    }
}
