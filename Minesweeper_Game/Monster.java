/*----------------------------------------------------------------
 *  Author:   K. Walsh
 *  Email:    kwalsh@holycross.edu
 *  Written:  7/13/2015
 *  
 *  Each Cell object manages information about and draws a
 *  single "cell" of the game grid. 
 *----------------------------------------------------------------*/

import GUI.*;


public class Monster{

    protected boolean alive;
    protected boolean BulletOut;
    protected int Bullet_row_past;
    protected int Bullet_col_past;
    protected int Bullet_row;
    protected int Bullet_col;
    protected int BulletDirection;//0 = up, 1 = down, 2 = left, 3 = right
    protected int row, col;
    
    public Monster(int r, int c) {
        this.row = r;
        this.col = c;
        this.alive = true;
        this.BulletOut = false;
        this.Bullet_row = 0;
        this.Bullet_col = 0;
        this.BulletDirection = 0;
        this.Bullet_row_past = -1;
        this.Bullet_col_past = -1;
    }
    public void killMonster(){
        this.alive = false;
    }
    public boolean isAlive(){
        return this.alive;
      
    }
      public int getBulletDirection(){
          return this.BulletDirection;
        }
    public int MonsterLocation_row (){
        return this.row;        
    }
    public int MonsterLocation_col (){
        return this.col;
            }
    public boolean BulletOut(){
        return this.BulletOut;
    }
    public int BulletLocation_row (){
        return Bullet_row;
    }
    public int BulletLocation_col (){
        return Bullet_col;
    }
    public int BulletLocation_row_past (){
        return Bullet_row_past;
    }
    public int BulletLocation_col_past (){
        return Bullet_col_past;
    }
    public boolean Test (int row_p, int col_p) {
        if(this.alive){
            if (row_p == this.row && col_p == this.col)
                killMonster();
            return true;
        }
        return false;
    }
    public void TakeTurn(int row_p, int col_p){   
         boolean above = !(this.row - row_p > 0);
                boolean right = (this.col - col_p > 0);
                boolean col_zero = (this.col - col_p == 0);
                boolean row_zero =  (this.row - row_p == 0);
        if (!BulletOut){
            this.Bullet_row_past = -1;
            this.Bullet_col_past = -1;
            if (((Math.abs(this.row - row_p) + Math.abs(this.col - col_p)) >= 16) && (StdRandom.uniform(6) == 0)) {//a bullet is fired at a 1.7 chance if the monster is 16 or more spaces away from the player
                BulletOut = true;
                Bullet_row = this.row;
                Bullet_col = this.row;
                if (Math.abs(this.row - row_p) > Math.abs(this.col - col_p)) //
                    if (this.row > row_p)//
                        BulletDirection = 0;//this section determines
                    else
                        BulletDirection = 1;//BulletDirection
                else
                    if (this.col > col_p)//
                        BulletDirection = 2;
                    else
                        BulletDirection = 3;
            } else {
                if (col_zero && row_zero)
                    return;
                if (!col_zero && !row_zero)
                    if (StdRandom.uniform(2) == 0)//this sections determines which direction the monster will go (always towards player);
                        if (above)//these if/else statements is a pathfinding algorithm for the monster to always move towards the player.
                            this.row++;
                        else
                            this.row--;
                    else
                        if (right)
                            this.col--;
                        else
                            this.col++;
                else
                    if (col_zero)
                        if (above)
                            this.row++;
                        else
                            this.row--;
                    else
                        if (right)
                            this.col--;
                        else
                            this.col++;
            }
                                    
        } else {
            switch (this.BulletDirection) {//adds some tracking to the bullets
            case 0:
            case 1:
                if(Math.abs(row_p - this.Bullet_row) == 1 || row_p == this.Bullet_row)
                    if(Math.abs(col_p - this.Bullet_col) > 3)
                        if(col_p < this.Bullet_col)
                            BulletDirection = 2;
                        else
                            BulletDirection = 3;
                   
                
                break;         
            case 2:             
            case 3:
                if(Math.abs(col_p - this.Bullet_col) == 1 || col_p == this.Bullet_col)
                    if(Math.abs(row_p - this.Bullet_row) > 3)
                        if(row_p < this.Bullet_row)
                            BulletDirection = 0;
                        else
                            BulletDirection = 1;
                break;
            }
             this.Bullet_row_past = Bullet_row;
                this.Bullet_col_past = Bullet_col;
            switch (this.BulletDirection) {//out of bounds protection plus removeal
               
            case 0:
                if(this.Bullet_row - 3 < 0){
                    this.BulletOut = false;
                    return;
                }
             
                this.Bullet_row += -3;
                break;
            case 1:
                if(this.Bullet_row + 3 > 19){
                    this.BulletOut = false;
                    return;
                }
                this.Bullet_row += 3;
                break;         
            case 2:
                if(this.Bullet_col - 3 < 0){
                    this.BulletOut = false;
                    return;
                }
                this.Bullet_col += -3;
                break;
            case 3:
                if(this.Bullet_col + 3 > 29){
                    this.BulletOut = false;
                    return;
                }
                this.Bullet_col += 3;
                break;
            }
                
            
        }
        }
    
}
    
            
        
        
    
