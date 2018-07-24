package my.finalproject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 * @author johnaoss
 * @date 6-12-2016
 * @title Checker.java
 * @purpose Object to be created by Board.java, that handles drawing, and type
 * of checker 
 */

public class Board extends JComponent{
    
    /*Is a JComponent because it needs to be nested inside the JFrame.
     *Seemed like a better option to do this rather than do it all in the main class.
     *Sets the dimensions of the board to be about 1.25 times bigger than the checker.
     */
    
   //VARIABLE DECLARATIONS
    private final static int SQUAREDIMENSION = (int) (Checker.getDimension() * 1.25);
    private final int BOARD_DIMENSION = 8 * SQUAREDIMENSION;
    private final int rows = 8, cols = 8;
    private Checker[][] boardState;
    private CheckerType draggedChecker;
    private int currentX = 0, currentY = 0;
    private int finalX = 0, finalY = 0;
    private double deltaX = 0, deltaY = 0;
    private String playerTurn = "Black";
    private String winner = "";
    private boolean nowDrag = false;
   
   public Board(){
        boardState = new Checker[rows][cols];
        setUp();
        System.out.format("It is now %s's turn\n",playerTurn);
        addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent me){
               /*   
                *@param   Mousevent 
                *@purpose Takes in initial position of checker, allowing dragging to be possible. 
                */
               
                draggedChecker = null;
                if(isCheckerThere(me.getX(),me.getY())){
                    currentX = (int)(Math.round(me.getX()/SQUAREDIMENSION));
                    currentY = (int)(Math.round(me.getY()/SQUAREDIMENSION));
                    draggedChecker = boardState[currentX][currentY].getCheckerType(); 
                    if(draggedChecker == CheckerType.BLACK_KING || draggedChecker == CheckerType.BLACK_REGULAR){
                        if(playerTurn.equals("Black"))nowDrag = true;
                    }else{
                        if(playerTurn.equals("Red"))nowDrag = true;
                    }
                }
            }
            @Override
            public void mouseReleased(MouseEvent me){
                /*   
                 *@param   MouseEvent
                 *@purpose Handles moving of checkers by updating boardState and
                 *         calling other functions to see if the move is possible.
                 */
                
                finalX = (int)(Math.round(me.getX()/SQUAREDIMENSION));
                finalY = (int)(Math.round(me.getY()/SQUAREDIMENSION));
                deltaX = (finalX - currentX);
                deltaY = (finalY - currentY);
                if((float)(Math.abs(deltaX / deltaY))!= 1) nowDrag = false;
                if(nowDrag && !isCheckerThere(me.getX(),me.getY()) && isLegalMove(draggedChecker)){
                    nowDrag = false;
                        if(Math.abs(deltaX) == 2 || Math.abs(deltaX) == 2)tryJumping();
                        else move();
                }
                revalidate();
                repaint();

                System.out.format("It is now %s's turn\n",playerTurn);
            } 
        });
   }
   
   public void addChecker(CheckerType checker,int x, int y){
       /*   
       *@param   Takes in the type of and position of the checker.
       *@purpose Adds the checker to the board. 
       */
       
       boardState[y][x] = new Checker(checker);
   }

   @Override
   protected void paintComponent(Graphics g){
       /*
        * @param Takes in the Graphics (don't worry about it)
        * @purpose Calls the paintCheckerBoard function and then draws the checkers.
        */
       
       //Calum insisted on antialiasing
       Graphics2D g2 = (Graphics2D) g;
       g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
       isGameOver();
            paintCheckerBoard(g);
            paintCheckers(g);

   }
   
   private void paintCheckers(Graphics g){
       /*
        * @param Takes in the Graphics (don't worry about it)
        * @purpose Goes through boardState[][] and draws every checker. 
        *          Called from paintComponent()
        */
       
       for (int j = 0; j<boardState[0].length; j++){
            for (int i = 0; i<boardState.length; i++){
                if(boardState[i][j].getCheckerType() != CheckerType.NON_EXISTANT) boardState[i][j].draw(g, (i*SQUAREDIMENSION)+5, (j*SQUAREDIMENSION)+5);
            }
       }
   }
   
   private void paintCheckerBoard(Graphics g){
       /*
        * @param Takes in the Graphics (don't worry about it)
        * @purpose Goes through boardState[][] and draws every checker. 
        *          Called from paintComponent()
        */
       
      for (int i = 0; i < rows; i++){
         g.setColor(((i & 1) != 0) ? Color.BLACK : Color.WHITE);
         
         for (int k = 0; k < cols; k++){
            g.fillRect(k * SQUAREDIMENSION, i * SQUAREDIMENSION, SQUAREDIMENSION, SQUAREDIMENSION);
            g.setColor((g.getColor() == Color.BLACK) ? Color.WHITE : Color.BLACK);
         }
      } 
   }
    
   private boolean isCheckerThere(int x, int y){
       /*
        * @param: X Position of mouse, Y Position of mouse.
        * @purpose Checks to see if the checker is in the given coordinates
        */
       
       int cx = (int)(Math.round(x/SQUAREDIMENSION));
       int cy = (int)(Math.round(y/SQUAREDIMENSION));
       
       return boardState[cx][cy].getCheckerType() != CheckerType.NON_EXISTANT;
   }
        
   private boolean isLegalMove(CheckerType checkertype){
       /*
        * @param deltaX and deltaY, and the type of checker making the move.
        * @purpose Detects if the checker is making a legal
        */
       
       if(Math.abs(deltaX / deltaY)!= 1) return false;
       
       if(checkertype == CheckerType.RED_KING || checkertype == CheckerType.BLACK_KING){
           return!(deltaY < -2 || deltaY > 2 || deltaY == 0);
       }else if(checkertype == CheckerType.RED_REGULAR){
           return!(deltaY > 2 || deltaY <= 0);
       }else if(checkertype == CheckerType.BLACK_REGULAR){
           return!(deltaY < -2 || deltaY >= 0);
       }
       
       return true;
   }
   
   private void move(){
       /*
        * @param n/a
        * @purpose Handles moving of the draggedChecker.
        */
        
        boardState[currentX][currentY] = new Checker(CheckerType.NON_EXISTANT);
        if(finalY == 7 && draggedChecker == CheckerType.RED_REGULAR) draggedChecker = CheckerType.RED_KING;
        if(finalY == 0 && draggedChecker == CheckerType.BLACK_REGULAR) draggedChecker = CheckerType.BLACK_KING;
        boardState[finalX][finalY] = new Checker(draggedChecker);  
        if(playerTurn.equals("Black")) playerTurn = "Red";
        else playerTurn = "Black";
   }
   
   private void tryJumping(){
       /*
        * @param n/a
        * @purpose Handles jumping
        */
       
       int xdiff = 0; int ydiff = 0;
       char cTeam = '\0', fTeam = '\0';
       xdiff = (int)(Math.signum(deltaX));
       ydiff = (int)(Math.signum(deltaY));
       CheckerType check = boardState[currentX + xdiff][currentY + ydiff].getCheckerType();
       
       if(draggedChecker == CheckerType.BLACK_KING || draggedChecker == CheckerType.BLACK_REGULAR)cTeam = 'B';
       else cTeam = 'R';
       
       if(check == CheckerType.BLACK_KING || check == CheckerType.BLACK_REGULAR)fTeam = 'B';
       else fTeam = 'R';
       
       if (check != CheckerType.NON_EXISTANT && cTeam != fTeam) {
           move();
           boardState[currentX + xdiff][currentY + ydiff] = new Checker(CheckerType.NON_EXISTANT);
       }
   }
   
   public int getPreferredDimension(){
      /*
       *@param n\a
       *@purpose Gives the board dimension to the main class for setting the window size
       */
      
       return BOARD_DIMENSION;
   }
   
   private void setUp(){
      /*
       *@param n\a
       *@purpose Sets up the boardState
       */
      
        playerTurn = "Black";
        winner = "";
        for(Checker[] c : boardState){
            Arrays.fill(c,new Checker(CheckerType.NON_EXISTANT));
        }
        
        this.addChecker(CheckerType.RED_REGULAR, 0, 1);
        this.addChecker(CheckerType.RED_REGULAR, 0, 3);
        this.addChecker(CheckerType.RED_REGULAR, 0, 5);
        this.addChecker(CheckerType.RED_REGULAR, 0, 7);
        
        this.addChecker(CheckerType.RED_REGULAR, 1, 0);
        this.addChecker(CheckerType.RED_REGULAR, 1, 2);
        this.addChecker(CheckerType.RED_REGULAR, 1, 4);
        this.addChecker(CheckerType.RED_REGULAR, 1, 6);
        
        this.addChecker(CheckerType.RED_REGULAR, 2, 1);
        this.addChecker(CheckerType.RED_REGULAR, 2, 3);
        this.addChecker(CheckerType.RED_REGULAR, 2, 5);
        this.addChecker(CheckerType.RED_REGULAR, 2, 7);
        
        this.addChecker(CheckerType.BLACK_REGULAR, 7, 0);
        this.addChecker(CheckerType.BLACK_REGULAR, 7, 2);
        this.addChecker(CheckerType.BLACK_REGULAR, 7, 4);
        this.addChecker(CheckerType.BLACK_REGULAR, 7, 6);
        
        this.addChecker(CheckerType.BLACK_REGULAR, 6, 1);
        this.addChecker(CheckerType.BLACK_REGULAR, 6, 3);
        this.addChecker(CheckerType.BLACK_REGULAR, 6, 5);
        this.addChecker(CheckerType.BLACK_REGULAR, 6, 7);
        
        this.addChecker(CheckerType.BLACK_REGULAR, 5, 0);
        this.addChecker(CheckerType.BLACK_REGULAR, 5, 2);
        this.addChecker(CheckerType.BLACK_REGULAR, 5, 4);
        this.addChecker(CheckerType.BLACK_REGULAR, 5, 6);
   }
   
   private boolean isGameOver(){
      /*
       *@param n\a
       *@purpose Checks boardState to see if the game is over. 
       */
      
       int dialogButton = JOptionPane.YES_NO_OPTION;
       int rCount = 0, bCount = 0;
       CheckerType temp;
       for(int i = 0; i < boardState[0].length; i++){
           for(int j = 0; j < boardState.length; j++){
               temp = boardState[i][j].getCheckerType();
               if(temp != CheckerType.NON_EXISTANT){
                   if(temp == CheckerType.BLACK_KING || temp == CheckerType.BLACK_REGULAR){
                       bCount+=1;
                   }else{
                       rCount+=1;
                   }
               }
           }
       }
       if(bCount == 0){
           winner = "Red";
           JOptionPane.showConfirmDialog (null, "The winner was " + winner + ".\nWould you like to play again?\n","Game Over",dialogButton);
           if(dialogButton == JOptionPane.YES_OPTION){
               setUp();
           }else{
               System.exit(0);
           }
           return true;
       }else if(rCount == 0){
           winner = "Black";
           JOptionPane.showConfirmDialog (null, "The winner was " + winner + ".\nWould you like to play again?\n","Game Over",dialogButton);
           if(dialogButton == JOptionPane.YES_OPTION){
               setUp();
           }else{
               System.exit(0);
           }
           return true;
       }
       return false;
   }
   
}   
