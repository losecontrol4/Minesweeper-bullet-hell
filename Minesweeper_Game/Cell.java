/*----------------------------------------------------------------
 *  Author:   K. Walsh
 *  Email:    kwalsh@holycross.edu
 *  Written:  7/13/2015
 *  
 *  Each Cell object manages information about and draws a
 *  single "cell" of the game grid. 
 *----------------------------------------------------------------*/

import GUI.*;
import java.awt.Color;

/**
 * A <i>Cell</i> object holds all information about the state of a single cell
 * of the minesweeper game board. This includes:
 *   - whether a mine is hidden in this cell or not
 *   - how many of its neighboring cells contain mines
 *   - whether it has been revealed yet or is still hidden
 * Each Cell object knows how to draw itself in a graphical window, and it will
 * draw itself in different styles depending on all the above state information.
 */
public class Cell extends Widget {

    /**
     * Size of one cell when it is drawn on the screen, in pixels.
     */
    public static final int SIZE = 20;

    /**
     * Whether a mine is hidden in this cell or not.
     */
    protected boolean isMine;

    /**
     * Whether this cell is "revealed" or not.
     */
    protected boolean isRevealed;

    
    /**
     * Count of how many neighboring cells have mines.
     */
    protected int neighborMineCount;
    protected boolean isFlagged;
    protected boolean hasPlayer;
    protected int MonsterCount;
    protected boolean hasBullet;
    protected boolean usedMine;
    protected boolean MineKilledYou;
    protected boolean shotTrailing;
    //protected boolean pastBulletLine; //idea to have a graphic 2 long line behind bullets to indicate their danger zone and direction. I don't think I'll have time for this.

    /**
     * Constructor: Initialize a cell to be drawn at the given x, y coordinates
     * on the screen. The cell will be blank. That is, it will not be a mine,
     * and it will have no neighboring mines so a neighbor mine count of zero.
     */
    
    public Cell(int x, int y) {
        super(x, y, SIZE, SIZE);
        this.isMine = false;
        this.usedMine = false; //for secret game
        this.isRevealed = false;
        this.isFlagged = false;
        this.neighborMineCount = 0;
        this.hasPlayer = false;//
        this.MonsterCount = 0;//for secret game
        this.hasBullet = false;//
        this.MineKilledYou = false;
        this.shotTrailing = false;
        //this.pastBulletLine = false;
    }
    //secret game stuff
    public void blownUp(){
        this.MineKilledYou = true;
    }
    public void MineUsed(){
        this.usedMine = true;
    }
    public boolean MineWasUsed(){
        return this.usedMine;
    }
 
    public void placePlayer() {
        hasPlayer = true;
    }
     public void removePlayer() {
        hasPlayer = false;
    }
     public boolean isPlayer() {
         return hasPlayer;    
    }
     public void incrementMonsterCount() {
         MonsterCount++;
    }
     public void decreaseMonsterCount() {
         MonsterCount--;
    }
     public int MonsterCount() {
         return MonsterCount;    
    }

    public void placeBullet() {
        hasBullet = true;
    }
     public void removeBullet() {
        hasBullet = false;
    }
     public boolean isBullet() {
         return hasBullet;    
    }
    
    /**
     * Hide a mine in this cell by changing the isMine variable to true.
     */
    public void makeFlag() {
        isFlagged = true;
    }
    public void removeFlag() {
        isFlagged = false;
    }
    public boolean isFlag() {
        return isFlagged;
    }
    public void plantMine() {
        isMine = true;
    }

    /**
     * Returns true if a mine is hidden in this cell, otherwise returns false.
     */
    public boolean isMine() {
        return isMine;
    }

    /**
     * Increment the neighbor mine count variable by one. 
     */
    public void incrementNeighborMineCount() {
        neighborMineCount++;
    }

    /**
     * Set the neighbor mine count variable to a given value.
     */
    public void setNeighborMineCount(int count) {
        neighborMineCount = count;
    }

    /**
     * Returns the value of the neighbor mine count variable.
     */
    public int getNeighborMineCount() {
        return neighborMineCount;
    }

    /**
     * Change this cell so that it is "revealed" by setting isRevealed to true.
     */
    public void reveal() {
        isRevealed = true;
    }

    /**
     * Returns true if this cell is "revealed", otherwise returns false.
     */
    public boolean isRevealed() {
        return isRevealed;
    }

    /**
     * Hide a mine in this cell by changidng the isMine variable to true.
     */
    public void makeMine() {
        isMine = true;
    }

    /**
     * Change this cell so that it shows the mine that is hiding in it.
     */
    public void showMine() {
        if (isMine)
            isRevealed = true;
    }

    /**
     * Check whether there are neighboring mines.
     */
    public boolean coastIsClear() {
        return (neighborMineCount == 0);
    }
    
    public void shotIsTrailing(){
        this.shotTrailing = true;
    }
    public void shotNotTrailing(){
        this.shotTrailing= false;
    }
    public boolean isShotTrailing() {
        return this.shotTrailing;
    }
  

        
   
    /**
     * Paint this cell on the canvas. Don't call this directly, it is called by
     * the GUI system automatically. This function should draw something on the
     * canvas. Usually the drawing should stay within the bounds (x, y, width,
     * height) which are protected member variables of GUI.Widget, which this
     * class extends.
     * @param canvas the canvas on which to draw.
     */
    public void repaint(GUI.Canvas canvas) {
        // TODO: Add code here to draw this cell. The look of the cell should
        // depend on its current state, e.g. if it has been revealed or not, how
        // many neighbors it has, and so on.
        Color[] numbers = new Color[] {Canvas.BLUE, Canvas.DARK_GREEN,Canvas.DARK_RED, Canvas.MAGENTA, Canvas.DARK_PURPLE, new Color(255, 0, 0), Canvas.PINK, Canvas.ORANGE};//array filled with colors for each number 
     
                int NeighborMine = this.getNeighborMineCount(); 
                canvas.setPenColor(Canvas.WHITE);
                
                if(!this.isRevealed()) {
                    
                    canvas.raisedBevelRectangle(this.x, this.y, 20, 20, 4.0);
 

                    if(this.isFlag()){
          
                        canvas.picture(this.x - 3, this.y - 2, "MineSweeper_Images/Flag.png" , 40 ,20);
                    }
                } else {
                    canvas.setPenColor(Canvas.WHITE);                    
                    canvas.sunkenBevelRectangle(this.x, this.y, 20, 20, 0.1);
                    canvas.setPenColor(Canvas.BLACK);
                    canvas.rectangle(this.x, this.y, 20, 20);
                
                    if (this.isMine()){
                        canvas.setPenColor(new Color(255, 0, 0));
                        if(this.usedMine){
                            canvas.setPenColor(Canvas.GREEN);
                        canvas.filledRectangle(this.x, this.y, 20, 20);
                        canvas.setPenColor(Canvas.BLACK);
                        canvas.rectangle(this.x, this.y, 20, 20);
                        }
                        if(this.MineKilledYou){
                            canvas.filledRectangle(this.x, this.y, 20, 20);
                            canvas.setPenColor(Canvas.BLACK);
                            canvas.rectangle(this.x, this.y, 20, 20);
                        }
                        canvas.picture(this.x - 17, this.y - 3, "MineSweeper_Images/Bomb.png" , 55 ,30);
                    }else if (!this.coastIsClear()){
                        canvas.setPenColor(numbers[NeighborMine - 1]);
                        canvas.setFont(Canvas.DEFAULT_FONT);
                        canvas.text(this.x + 10, this.y + 10, Integer.toString(NeighborMine));

                    }
                    
                         
                }
                if (this.isShotTrailing()){
                    canvas.setPenColor(Canvas.BLACK);
                    canvas.filledRectangle(this.x, this.y, 20, 20);
                }
                 
                if (this.isPlayer()){
                     canvas.picture(this.x - 20, this.y - 10, "MineSweeper_Images/Player.png" , 40 ,30);
                }
                if (this.MonsterCount() > 0){
                    canvas.picture(this.x, this.y, "MineSweeper_Images/Monster_Shooting.png" , 30 ,20);// I planned for having images for multiple monsters and having a shooting pose, put the monster image I drew didn't work well and currently (Dec 6, 2019) I am in the end game of making everything function smoothly so I don't plan on redesigning the Monster Image.
                 

                }
                if (this.isBullet()){
                 canvas.picture(this.x - 7, this.y - 2, "MineSweeper_Images/Bullet.png" , 35 ,25);
                }
                
                
    }
}




