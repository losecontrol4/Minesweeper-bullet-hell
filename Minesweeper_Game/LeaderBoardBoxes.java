/*----------------------------------------------------------------
 *  Author:   Adam Hall
 *  Email:    arhall21@holycross.edu
 *  Written:  7/13/2015
 *  
 *  HelpBox just displays some helpful text in a box on the scren.
 *----------------------------------------------------------------*/

import GUI.*;

/**
 * A <i>HelpBox</i> object represents the help menu. It can draw itself on a
 * canvas.
 */
public class LeaderBoardBoxes extends Widget {

 /**
     * The width of the box as it is shown on the screen.
     */
    public static final int WIDTH = 120;

    /**
     * The height of the box as it is shown on the screen.
     */
    public static final int HEIGHT = 30;

    public static final int WIDTH2 = 110;

    public static final int HEIGHT2 = 320;
    public LinkedList a = new LinkedList(); LinkedList b = new LinkedList(); LinkedList c = new LinkedList(); LinkedList d = new LinkedList();
    /**
     * Initialize a new HelpBox object. It will be drawn at the specified
     * position.
     * @param x the x coordinate where the help box will be drawn.
     * @param y the y coordinate where the help box will be drawn.
     */
    public LeaderBoardBoxes(Leaderboard lead, int x, int y) {
    
        super(x, y, WIDTH, HEIGHT);
        a = lead.Easy();
        b = lead.Medium();
        c = lead.Hard();
        d = lead.Secret();
 
        //StdOut.println(a.NumItems());

    }
   
    /**
     * Paint the help box on a canvas. Don't call this directly, it is called by
     * the GUI system automatically. This function should draw something on the
     * canvas. Usually the drawing should stay within the bounds (x, y, width,
     * height) which are protected member variables of GUI.Widget, which this
     * class extends.
     * @param canvas the canvas on which to draw.
     */
    public void writeList(GUI.Canvas canvas, LinkedList a, double x){
        for(int i = 0; i < a.NumItems() && i < 10; i++){//that && is just a safety
            String Name = a.getName(i+1);      
            Name = Name.substring(0, 3);            
            int score = a.getScore(i+1);
   
            if (a.SearchRecent() == i + 1){
                if (Math.abs((int)System.currentTimeMillis())%500 < 249)//allows it to flash. This flash may be janky but I am super proud.
                    canvas.setPenColor(canvas.LIGHT_GRAY);

                else
                    canvas.setPenColor(canvas.BLACK);
                    
                canvas.filledRectangle(x + 6, y + 80 - 15 + 30 * i, WIDTH2-2, 30);
            }
            canvas.setPenColor(canvas.WHITE);
            canvas.textLeft(x+5 + 10, y + 80 + 30 * i, (i + 1) + ". " + Name + " " + score);           
        }
    }
    public void repaint(GUI.Canvas canvas) {
        // Draw a white box with a black outline.
        canvas.setPenColor(Canvas.BLACK);
        canvas.filledRoundedRectangle(x, y, width, height, 50.0);
        canvas.filledRoundedRectangle(x + 5, y + 60, WIDTH2, HEIGHT2, 5.0);
        canvas.filledRoundedRectangle(x*2 + width, y, width, height, 50.0);
        canvas.filledRoundedRectangle(x*2 + width + 5, y + 60, WIDTH2, HEIGHT2, 5.0);
        canvas.filledRoundedRectangle(x*3 + 2*width, y, width, height, 50.0);
        canvas.filledRoundedRectangle(x*3 + 2*width + 5, y + 60, WIDTH2, HEIGHT2, 5.0);
        canvas.filledRoundedRectangle(x*4 + 3*width, y, width, height, 50.0);
        canvas.filledRoundedRectangle(x*4 + 3*width + 5, y + 60, WIDTH2, HEIGHT2, 5.0);
   
        canvas.setPenColor(Canvas.WHITE);
        canvas.setPenRadius(1.0);
        canvas.roundedRectangle(x+0.5, y+0.5, width-1, height-1, 50.0);
        canvas.text(x + width/2.0, y + height/2.0, "Easy");    
        canvas.roundedRectangle(x + 5 +0.5, y + 60 +0.5, WIDTH2 -1, HEIGHT2-1, 5.0);
        canvas.roundedRectangle(x*2 + width + 0.5, y +0.5, width-1, height-1, 50.0);
        canvas.text(x*2 + width + width/2.0, y + height/2.0, "Medium"); 
        canvas.roundedRectangle(x*2 + width + 5 +0.5, y + 60 +0.5, WIDTH2 -1, HEIGHT2-1, 5.0);        
        canvas.setPenRadius(1.0);
        canvas.roundedRectangle(x*3 + 2*width + 0.5, y+0.5, width-1, height-1, 50.0);
        canvas.text(x*3 + 2*width + width/2.0, y + height/2.0, "Hard"); 
        canvas.roundedRectangle(x*3 + 2*width + 5 +0.5, y + 60 +0.5, WIDTH2 -1, HEIGHT2-1, 5.0);
        canvas.setPenRadius(1.0);
        canvas.roundedRectangle(x*4 + 3*width + 0.5, y+0.5, width-1, height-1, 50.0);
        canvas.text(x*4 + 3*width + width/2.0, y + height/2.0, "Secret Game");
        canvas.roundedRectangle(x*4 + 3*width + 5 +0.5, y + 60 +0.5, WIDTH2 -1, HEIGHT2-1, 5.0);
      
        writeList(canvas, a, x);
        writeList(canvas, b, x*2 + width);
        writeList(canvas, c, x*3 + 2*width);
        writeList(canvas, d, x*4 + 3*width);
                
      
    }

}

