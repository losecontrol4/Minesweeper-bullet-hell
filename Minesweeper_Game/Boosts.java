/*----------------------------------------------------------------
 *  Author:   Adam Hall
 *  Email:    arhall21@g.holycross.edu
 *  Written:  Dec 10 2019
 *  
 *  Boosts displays the boost bar on a box on the screen
 *----------------------------------------------------------------*/

import GUI.*;
import java.awt.Color;


public class Boosts extends Widget {

    /**
     * The width of the box as it is shown on the screen.
     */
    public static final int WIDTH = 200;

    /**
     * The height of the box as it is shown on the screen.
     */
    public static final int HEIGHT = 75;

    // The game for which this box will show the boost bard.
    private Game game;

    /**
     * Initialize a new Boosts for the given game.
     * @param g the game for which this box will show statistics.
     * @param x the x coordinate of the location to draw the box.
     * @param y the y coordinate of the location to draw the box.
     */
    public Boosts(Game g, int x, int y) {
        super(x, y, WIDTH, HEIGHT);
        game = g;
    }

    /*
     * Draws the Boost bar
     * @param canvas the canvas on which to draw.
     */
    public void repaint(GUI.Canvas canvas) {
        // Draw a white rectangle with a black border.
        canvas.setPenColor(Canvas.WHITE);
        canvas.filledRectangle(x, y, width, height);
        canvas.setPenColor(Canvas.BLACK);
        canvas.setPenRadius(1.0);
        canvas.rectangle(x+0.5, y+0.5, width-1, height-1);


        canvas.setPenColor(Canvas.WHITE);   
        canvas.setFont(Canvas.BOLD_FONT);
        canvas.setFont(18);
        canvas.text(x + WIDTH/2.0, y - 10, "Boost Bar");

        // Draws the individual boosts bars 
        for (int i = 0; i < 8; i++){    
            if (i < game.getBoostMeter()){//this determines how many are full
                canvas.setPenColor(Canvas.CYAN);
                canvas.filledRectangle(x + 14 + i*((WIDTH - 30)/8.0), y + 10, (WIDTH - 30)/8.0, HEIGHT - 20);
            }
            canvas.setPenColor(Canvas.BLACK);
            canvas.setPenRadius(2.0);
            canvas.rectangle(x + 14 + i*((WIDTH - 30)/8.0), y + 10, (WIDTH - 30)/8.0, HEIGHT - 20);
        }
    }
}
        
