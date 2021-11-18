/*----------------------------------------------------------------
 *  Author:   K. Walsh
 *  Email:    kwalsh@holycross.edu
 *  Written:  7/13/2015
 *  
 *  Minesweeper game. This class implements the game window and most
 *  of the game logic.
 *----------------------------------------------------------------*/

import GUI.*;


/**
 * A <i>Game</i> object manages all information about a minesweeper game as it
 * is being played and displayed on the screen. This includes information about
 * all of the cells (this is stored in a 2-D array of Cell objects), how many
 * flags have been planted, how many mines have been deployed, etc. Game extends
 * Window, so it can be drawn on the screen. It also extends EventListener so it
 * can respond to user interaction.
 */
public class Game extends Window implements EventListener {
    /**
     * Number of cells tall the game board will be.
     */
    public static final int NUM_ROWS = 20;
    boolean Teleport = false;
    boolean Secret = false;
    /**
     * Number of cells wide the game board will be.
     */
    public static final int NUM_COLS = 30;

    private int[] PlayerLocation = new int[] {9, 15};
    int turns = 0;
   
    protected int boostMeter;
    // Example game screen layout:
    // +---------------------------------------------------------+
    // |      M A R G I N = 50                                   |
    // | M  + - - - - - - - - - - - - - - - - - - - - - - - + M  |
    // | A  |                                               | A  |
    // | R  |                                               | R  |
    // | G  |                Grid of Cells                  | G  |
    // | I  |                                               | I  |
    // | N  |                                               | N  |
    // | =  |       600 = NUM_COLS * Cell.SIZE wide         | =  |
    // | 50 |                      b                        | 50 |
    // |    |       400 = NUM_ROWS * Cell.SIZE tall         |    |
    // |    |                                               |    |
    // |    |                                               |    |
    // |    |                                               |    |
    // |    + - - - - - - - - - - - - - - - - - - - - - - - +    |
    // |            SPACE     S   SPACE   S    SPACE             |
    // |    + - - - - - - - + P + - - - + P + - - - - - - - +    |
    // |    |    Status     | A | Timer | A |     Help      |    |
    // |    |       Box     | C |       | C |      Box      |    |
    // |    + - - - - - - - + E + - - - + E + - - - - - - - +    |
    // |     M A R G I N = 50                                    |
    // +-- ------------------------------------------------------+

    /**
     * Width of the game window, in pixels.
     * Equal to 2*MARGIN + GRID_WIDTH
     * or 2*MARGIN + 2*SPACE + StatusBox.WIDTH, Timer.WIDTH, HelpBox.WIDTH,
     * whichever is larger.
     */
    public static final int WIDTH = 700;

    /**
     * Height of the game window, in pixels.
     * Equal to 2*MARGIN + SPACE
     *     + GRID_HEIGHT
     *     + max(StatusBox.Height, Timer.HEIGHT, HelpBox.HEIGHT)
     */
    public static final int HEIGHT = 600; 

    /**
     * Width of the grid part of the window, in pixels.
     * Equal to NUM_COLS * Cell.SIZE.
     */
    public static final int GRID_WIDTH = NUM_COLS * Cell.SIZE;
 
    /**
     * Height of the grid part of the window, in pixels.
     * Equal to NUM_ROWS * Cell.SIZE.
     */
    public static final int GRID_HEIGHT = NUM_ROWS * Cell.SIZE;

    /**
     * Margin around the edges of the canvas.
     */
    private static final int MARGIN = 50;

    /**
     * Space between elements on the canvas.
     */
    private static final int SPACE = 25;
  
    // A 2-D array of Cell objects to keep track of the board state.
    private Cell[][] cells = new Cell[NUM_ROWS][NUM_COLS];
    private Monster[] evil = new Monster [101];//I considered making this a list, but there was no noticable lag while playing from my computer with all 101 monsters so I deemed it not needed.
    private int numMines = 0;    // number of mines deployed
    private int numRevealed = 0; // number of cells revealed so far
    private int DeadMonsters = 0;// number of Monsters killed
    // Whether or not the game has been won.
    private boolean gameWon = false;

    // Whether or not the game has been lost
    private boolean gameLost = false;

    // Name of the user playing the game.
    private String username;

    // The difficulty level of the game, used for tracking top scores.
    private String difficulty;

    // The status box that appears in the top left.
    private StatusBox status;

    // The timer that appears in the top middle.
    private Timer timer;

    // The help box that appears in the top right.
    private HelpBox help;

    private Boosts boost;

    /**
     * Constructor: Initializes a new game, but does not deploy any mines, plant
     * any flags, etc. The difficulty is either "easy", "medium", or "hard", and
     * will be used to load the proper top scores file. Name is used as the
     * user's name.
     */
    private int spawned = 0;

    public Game(String name, String difficulty) {
        super("Minesweeper!", WIDTH, HEIGHT);
        
        this.username = name;
        this.difficulty = difficulty;
        Secret = difficulty.equals("SecretGame");
        // Create the background
        setBackgroundColor(Canvas.BLACK);

        // Create a border around the grid
        Box border = new Box(MARGIN-1.5, MARGIN-1.5, GRID_WIDTH+3, GRID_HEIGHT+3);
        border.setBackgroundColor(null);
        border.setBorderColor(Canvas.BLACK);
        add(border);
        // Create the info boxes
        if (!Secret){
            help = new HelpBox(
                WIDTH - MARGIN - HelpBox.WIDTH,
                HEIGHT - MARGIN - HelpBox.HEIGHT);
        }
        else {
           boost  = new Boosts(this, WIDTH - MARGIN - Boosts.WIDTH, HEIGHT - MARGIN - Boosts.HEIGHT);
        }
        status = new StatusBox(this, MARGIN, HEIGHT - MARGIN - StatusBox.HEIGHT);    
        timer = new Timer(MARGIN + StatusBox.WIDTH + SPACE, HEIGHT - MARGIN - Timer.HEIGHT);
        if(!Secret)
            add(help);
        else
            add(boost);                                    
        add(timer);add(status);//adds all the info boxes

        boostMeter = 0;
        int size;
        size = 600;
        for (int row = 0; row < NUM_ROWS; row++)
            for (int col = 0; col < NUM_COLS; col++){
                cells[row][col] = new Cell(MARGIN+Cell.SIZE*col, MARGIN+Cell.SIZE*row);
                add(cells[row][col]);
               
            }
    }
    public int getBoostMeter(){
        return boostMeter;
    }
    public void revealAllMines(){
         for (int row = 0; row < NUM_ROWS; row++)
            for (int col = 0; col < NUM_COLS; col++)
                if (cells[row][col].isMine())
                    cells[row][col].reveal();
    }
    public void placeFlags(){
           for (int row = 0; row < NUM_ROWS; row++)
            for (int col = 0; col < NUM_COLS; col++)
                if (cells[row][col].isMine())
                    cells[row][col].makeFlag();
    }
                

    public boolean gameWon(){
        return gameWon;
    }
    
    public boolean gameLost(){
        return gameLost;
    }
    
    public int getNumDeadMonsters(){
        return DeadMonsters;
    }
        
            

    /**
     * Get the number of mines that are deployed.
     */
    public int getNumMinesDeployed() {
        return numMines;
    }

    /**
     * Get the number of hidden cells remaining to be revealed.
     */
    public int getNumCellsRemaining() {
        return NUM_ROWS * NUM_COLS - numRevealed;
    }

    /**
     * Deploy the given number of mines. This gets called once during game
     * setup. The game doesn't actually begin officially until the user clicks
     * a cell, so the timer should not start yet.
     */
    public void deployMines(int mines) {
        // TODO: Add code here to deploy mines by calling makeMine() on some of
        // the cells, at random. Use StdRandom() for picking random numbers.
        // Each cell also has a variable to track how many of its neighboring
        // cells contain a mine, so you will need to update that variable too.
        // See the increementNeighborMineCount() and setNeighborMineCount()
        // functions in the Cell class, which are meant for this purpos
        if(mines == 101)
             cells[PlayerLocation[0]][PlayerLocation[1]].placePlayer();
        for (int i = 0; i < mines; i++){
                int row = 0;
                int col = 0;
                while(true){
                    row = StdRandom.uniform(NUM_ROWS-1);
                    col = StdRandom.uniform(NUM_COLS-1);
                    if (!cells[row][col].isMine())
                        break;
                }
                cells[row][col].makeMine();
                numMines++;
                for (int m = -1; m < 2; m++)//nested loop creating a 3 by 3 grid to increase neighbor mine count around the mine.
                    for (int n = -1; n < 2; n++)
                        if  (m != 0 || n != 0)
                            if(!(row + m < 0) && !(col + n < 0) && !(row + m > NUM_ROWS - 1) && !(col + n > NUM_COLS - 1)) 
                                cells[row+m][col+n].incrementNeighborMineCount();//sets neighbor
                          
            
    
            }
    }
    public static void spawn(Monster[] evil,int p_row, int p_col, int turns, int spawned){
            int m_row = 0, m_col = 0;
            int quadrant = 4; //1, 2, 3, or, 4
            if (p_row <= 9)//proud of this setup, two if statements determines which of the 4 quadrants the player is in.
                quadrant += -2;
            if (p_col <= 14)
                quadrant += -1;
            switch (quadrant){
            case 1:
                m_row = 19;
                m_col = 29;  
                break;
            case 2:
                m_row = 19;
                m_col = 0; 
                break;
            case 3:
                m_row = 0;
                m_col = 29; 
                break;
            case 4:
                m_row = 0;
                m_col = 0; 
                break;
            }
            //StdOut.println(m_row + " " + m_col);
            evil[spawned] = new Monster(m_row, m_col);
                
                
        }
                          
        
    public static int uncoverNeighboringBlanks(int row, int col, int num, Cell[][] cells){//this is a recursive function used to uncover all neighboring blanks
        int revealCount = 0;
        if(row < 0 || row > NUM_ROWS - 1 || col < 0 || col > NUM_COLS - 1)//base case to prevent out of bounds errors
            return revealCount;
        if (num == 1)//this skips this check the first pass because the mouse would have already revealed it.
            if (cells[row][col].isRevealed())//base case
                return revealCount;
       
        cells[row][col].reveal();
        if (num == 1)
            revealCount++;
        if (!cells[row][col].coastIsClear())//base case
            return revealCount;
        
  
        revealCount += uncoverNeighboringBlanks(row - 1, col, 1, cells) +  uncoverNeighboringBlanks(row + 1, col, 1, cells) + uncoverNeighboringBlanks(row, col - 1, 1, cells) +uncoverNeighboringBlanks(row, col + 1, 1, cells); //does all needed recursion and adds each revealed to revealCount.
      
        return revealCount;
                    
    }

 

    /**
     * Respond to a mouse click. This function will be called each time the user
     * clicks on the game window. The x, y parameters indicate the screen
     * coordinates where the user has clicked, and the button parameter
     * indicates which mouse button was clicked (either "left", "middle", or
     * "right"). The function should update the game state according to what the
     * user has clicked.
     * @param x the x coordinate where the user clicked, in pixels.
     * @param y the y coordinate where the user clicked, in pixels.
     * @param button either "left", "middle", or "right".
     */
    
 
   
    public void mouseClicked(double x, double y, String button) {
        // User clicked the mouse, see what they want to do.
         
        // If game is over, then ignore the mouse click.
        if (gameWon || gameLost){
            hide();
            return;
        }

        // If the user middle-clicked, ignore it.
        if (!button.equals("left") && !button.equals("right"))
            return;
        
        // If the user clicked outside of the game grid, ignore it.
        if (x < MARGIN || y < MARGIN
            || x >= MARGIN + GRID_WIDTH || y >= MARGIN + GRID_HEIGHT) {
            return;
        }
        if(!timer.isCounting())
        timer.startCounting();
        // Calculate which cell the user clicked.
        int row, col;
        if (Secret){
            row = PlayerLocation[0];
            col = PlayerLocation[1];
            if (Teleport){
                cells[PlayerLocation[0]][PlayerLocation[1]].removePlayer();
                row = (int)((y - MARGIN) / Cell.SIZE);
                col = (int)((x - MARGIN) / Cell.SIZE);
            }
        
        } else {
            row = (int)((y - MARGIN) / Cell.SIZE);
            col = (int)((x - MARGIN) / Cell.SIZE);
        }

        // StdOut.printf("You clicked row %d column %d with button %s\n", row, col, button);
        if (Teleport){
            cells[row][col].placePlayer();
            PlayerLocation[0] = row;
            PlayerLocation[1] = col;       
            boostMeter += -4;
            Teleport = false;
            return;
        }
        
        if (button.equals("left")){
            //StdOut.println(getNumCellsRemaining() - getNumMinesDeployed());
        
            if(cells[row][col].isFlag())
                cells[row][col].removeFlag();
            else{
                if(!cells[row][col].isRevealed()){
                    cells[row][col].reveal();
                    if(!cells[row][col].isMine()){
                        numRevealed++;
                        //StdOut.println(getNumCellsRemaining() - getNumMinesDeployed());                            
                     

                    }
                else{
                    gameLost = true;
                    revealAllMines();
                    cells[row][col].blownUp();
                    timer.stopCounting();
                    StdOut.println("BOOOOOM! You Lost! Click anywhere to view leadboard!");
                    /*show();
                      try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ie) {
                        StdOut.println("Thread sleep issue");
                        }
                        hide();*/
                    //I tried the above method to make a more seemless transition but I couldn't get it to repaint the revealed mines before the starting the sleep and hiding. I thought show should repaint the screen first but I couldn't get this to work.After like an hour and a half of looking online and trying things I decided to accept defeat and move on to something else.
                    
                }
                    if (cells[row][col].coastIsClear() && !cells[row][col].isMine())
                        numRevealed += uncoverNeighboringBlanks(row, col, 0, cells);
                    if (getNumCellsRemaining() - getNumMinesDeployed() == 0){
                        timer.stopCounting();
                        placeFlags();
                        gameWon = true;
                        StdOut.println("CONGRATULATIONS YOU WIN!!!! Click anywhere to view the leadboard!"); 
                    }
                }
            }
        }
        else{
            if (!cells[row][col].isRevealed()){
                if(!cells[row][col].isFlag())
                    cells[row][col].makeFlag();
                else
                    cells[row][col].removeFlag();                                              }
        }
  
    }
    
    /**
         * Respond to key presses. This function will be called each time the user
         * presses a key. The parameter indicates the character the user pressed.
         * The function should update the game state according to what character the
     * user has pressed. 
     * @param c the character that was typed.
     */
    public void keyTyped(char c)
    {
        // User pressed a key, see what they want to do.
        if (Secret) {
            if (spawned < 101)
                if(turns%8 == 0 && turns != 0){
                    spawn(evil, PlayerLocation[0], PlayerLocation[1], turns, spawned);
                    spawned++;
                }
            cells[PlayerLocation[0]][PlayerLocation[1]].removePlayer();
            int didMove = turns;
          
            if(!gameWon && !gameLost)
            switch (c) {
             
            case 'W':
                 if(PlayerLocation[0] > 1 && boostMeter > 1){
                    PlayerLocation[0] += -2;                
                    turns++;
                    boostMeter += -2;
                 }
                 break;
            case 'w':
                if(PlayerLocation[0] > 0){
                    PlayerLocation[0] += -1;                
                    turns++;
                    
                }
                break;
            case 'A':
                if(PlayerLocation[1] > 1 && boostMeter > 1){
                    PlayerLocation[1] += -2;                
                    turns++;
                    boostMeter += -2;
                }
                break;
            case 'a':
                if(PlayerLocation[1] > 0){
                    PlayerLocation[1] += -1;
                turns++;
                 }
                break;
            case 'S':
                if(PlayerLocation[0] < NUM_ROWS - 2 && boostMeter > 1){
                    PlayerLocation[0] += 2;                
                    turns++;
                    boostMeter += -2;
                }
                break;
            case 's':
                 if(PlayerLocation[0] < NUM_ROWS - 1){
                PlayerLocation[0] += 1;
                turns++;
                 }
                break;
            case 'D':
                 if(PlayerLocation[1] < NUM_COLS - 2 && boostMeter > 1){
                    PlayerLocation[1] += 2;                
                    turns++;
                    boostMeter += -2;
                }
                break;
            case 'd':
                if(PlayerLocation[1] < NUM_COLS - 1){
                    PlayerLocation[1] += 1;                
                    turns++;
                }
                break;
            case ' ':
                turns++;
                break;
                
                
            case 't':
                if (boostMeter > 3)
                Teleport = true;
                //teleport playerlocation equals where you click next
                break;   
            default:
                StdOut.println(turns);
                break; // anything else is ignored
                
        }
            switch(c){
            case 'q': 
            case 'Q': 
                hide(); // user wants to quit
                break;
            }
            
    
                
            cells[PlayerLocation[0]][PlayerLocation[1]].placePlayer();
       
            if (didMove != turns){
                if(turns%2 == 0 && boostMeter < 8)
                    boostMeter++;
                //need number spawned as a variab
                if(spawned == 0){
                    evil[0] = new Monster(0, 0);
                    spawned++;
                }
                boolean MineBoom = false;
           
                for (int i = 0; i < spawned; i++){
                    int deadCount = 0;
                    if(evil[i].isAlive()){
                        int M_row = evil[i].MonsterLocation_row();//these declarations were made to make the code more readable
                        int M_col = evil[i].MonsterLocation_col();//
                        int B_row = evil[i].BulletLocation_row();//
                        int B_col = evil[i].BulletLocation_col();//
                                
                        cells[M_row][M_col].decreaseMonsterCount();//removes this monster from current location
                        
                        if (evil[i].BulletOut()){
                            cells[B_row][B_col].removeBullet();//removes this monster's bullet from current location
                            if(!((evil[i].BulletLocation_col_past() == -1 || evil[i].BulletLocation_row_past() == -1) || (B_row > NUM_ROWS ) || (B_col > NUM_COLS) || (B_row < 0) || (B_col < 0))) {
                                int colDifference = evil[i].BulletLocation_col_past() - B_col;
                                int rowDifference = evil[i].BulletLocation_row_past() - B_row;
                                if (colDifference != 0) {
                                    int multiplier = Math.abs(colDifference) / colDifference;
                                    for (int trail = multiplier; trail != colDifference + multiplier; trail = trail + multiplier) {
                                        cells[B_row][trail + B_col].shotNotTrailing();
                                    }
                                }
                                else {
                                    int multiplier = Math.abs(rowDifference) / rowDifference;
                                    for (int trail = multiplier; trail != rowDifference + multiplier; trail = trail + multiplier) {
                                        cells[trail + B_row][B_col].shotNotTrailing();
                                    }
                                }
                            }
                        }
                        
                        evil[i].TakeTurn(PlayerLocation[0],PlayerLocation[1]);//takes Monster's turn, see Monster Class
                        
                        M_row = evil[i].MonsterLocation_row();//these need to be redefined after the TakeTurn Method
                        M_col = evil[i].MonsterLocation_col();//
                        B_row = evil[i].BulletLocation_row();//
                        B_col = evil[i].BulletLocation_col();//
                        int B_row_past = evil[i].BulletLocation_row_past();//
                        int B_col_past = evil[i].BulletLocation_col_past();//
                            
                        
                        if (evil[i].BulletOut()){
                            if(!((B_col_past == -1 || B_row_past == -1) || (B_row > NUM_ROWS ) || (B_col > NUM_COLS) || (B_row < 0) || (B_col < 0))) {
                            int colDifference = B_col_past - B_col;
                            int rowDifference = B_row_past - B_row;
                            if (colDifference != 0) {
                                int multiplier = Math.abs(colDifference) / colDifference;
                                for (int trail = multiplier; trail != colDifference + multiplier; trail = trail + multiplier) {
                                    cells[B_row][trail + B_col].shotIsTrailing();
                                    StdOut.println("A " + B_row + " " + (trail + B_col) + " " + B_col + " " + trail);
                                    StdOut.println("B " + B_row + " " + B_col + " " + B_row_past + " " + B_col_past);
                                }
                            }
                            else {
                                int multiplier = Math.abs(rowDifference) / rowDifference;
                                for (int trail = multiplier; trail != rowDifference + multiplier; trail = trail + multiplier) {
                                    cells[trail + B_row][B_col].shotIsTrailing();
                                    StdOut.println("A " + (trail + B_row) + " " + B_col + " " + trail);
                                    StdOut.println("B " + B_row + " " + B_col + " " + B_row_past + " " + B_col_past);
                                }
                            }
                        }
                            cells[B_row][B_col].placeBullet();//Places Bullet in it's new location after taking a turn
                        }
                        
                        cells[M_row][M_col].incrementMonsterCount();//Places this Monster in it's new location after taking it's turn
                        if(B_row_past != -1 && B_col_past != -1){
                            if(evil[i].BulletOut())
                            if(cells[B_row][B_col] == cells[PlayerLocation[0]][PlayerLocation[1]]){
                                gameLost = true;
                                revealAllMines();
                                timer.stopCounting();
                                StdOut.println("You were shot! You Lost! Click anywhere to view leadboard!");
                                
                            }
                            int moving = 1;
                            int Direction = evil[i].getBulletDirection();
                            //StdOut.println(Direction);
                            if ((Direction + 2)%2 == 0)
                                moving = -1;
                            for(int j = 1; j <= 3; j++){
                                if (Direction > 1){
                                    if(B_col_past + j*moving >= 0 && B_col_past + j*moving < 30){
           
                                        if(cells[B_row_past][B_col_past + j * moving] == cells[PlayerLocation[0]][PlayerLocation[1]]){
                                            gameLost = true;
                                            revealAllMines();
                                            timer.stopCounting();
                                            StdOut.println("You were shot! You Lost! Click anywhere to view leadboard!");
                                        }
                                    }
                                   

                                }
                         
                                else{
                                    if(B_row_past + j * moving >= 0 && B_row_past + j*moving < 20 && B_col_past > 0){
                                        if(cells[B_row_past + j * moving][B_col_past] == cells[PlayerLocation[0]][PlayerLocation[1]]){

                                            if(cells[B_row_past + j*moving][B_col_past] == cells[PlayerLocation[0]][PlayerLocation[1]]){
                                                //StdOut.println(B_row_past + " " + B_col_past);
                                                //StdOut.println("row");
                                                gameLost = true;
                                                revealAllMines();
                                                timer.stopCounting();
                                                StdOut.println("You were shot! You Lost! Click anywhere to view leadboard!");
                                            }
                                        

                                    }
                                
                            
                            }
                          }
                        }
                      }
                    
                     
                   
                        if(cells[M_row][M_col] == cells[PlayerLocation[0]][PlayerLocation[1]]){
                      
                            cells[M_row][M_col].reveal();
                            
                            if(cells[PlayerLocation[0]][PlayerLocation[1]].isMine() && !cells[PlayerLocation[0]][PlayerLocation[1]].MineWasUsed()){
                                evil[i].killMonster();
                                DeadMonsters++;
                                MineBoom = true;
                                cells[M_row][M_col].decreaseMonsterCount();
                                
                            }  else{
                             
                                gameLost = true;
                                            revealAllMines();
                                            timer.stopCounting();
                                            StdOut.println("You were mauled by a Monster! You Lost! Click anywhere to view leadboard!");

                            }
                            
                        }
                    }
                }
                
                    
                
                if (MineBoom){
                    cells[PlayerLocation[0]][PlayerLocation[1]].MineUsed();
                    if(DeadMonsters == 101){
                        timer.stopCounting();
                        placeFlags();
                        gameWon = true;
                        StdOut.println("CONGRATULATIONS YOU WIN MINESWEEPER'S HARDEST MODE!!!! Click anywhere to view the leadboard!");
                    }
                }
            
            }
        }  else {  
            switch (c) {
            case 'q': 
            case 'Q': 
                hide(); // user wants to quit
                break;
            default:
                break; // anything else is ignored
            }
        }
    }
    
    
    

    public int getScore(){
        return timer.getElapsedSeconds();
}
    
    /**
     * Paint the background for this window on the canvas. Don't call this
     * directly, it is called by the GUI system automatically. This function
     * should draw something on the canvas, if you like. Or the background can
     * be blank.
     * @param canvas the canvas on which to draw.
     */
    public void repaintWindowBackground(GUI.Canvas canvas) {
        canvas.picture(0, 0, "BackGround.jpg", 2*MARGIN + GRID_WIDTH, 2*MARGIN + SPACE + GRID_HEIGHT   + StatusBox.HEIGHT);
      
    }
    
}

