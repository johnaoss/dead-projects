package my.finalproject;

import java.awt.Color;
import java.awt.Graphics;

/**
 * @author johnaoss
 * @date 6-12-2016
 * @title Checker.java
 * @purpose Object to be created by Board.java, that handles drawing, and general 
 * use of the Checkers to be played with.
 */

public class Checker{
   
    private final static int DIMENSION = 50;    
    private CheckerType checkerType;
    
    public Checker(CheckerType checkerType){
        
        /*
         * @param    Takes in checker type specified
         * @purpose  Assigns the checkertype to the object Checker.
         */
        
        this.checkerType = checkerType;
    }   
    
    public void draw(Graphics g, int x, int y){
       /*
        * @param   Graphics, and the X and Y position
        * @purpose Handles drawing the checker on the screen.
        */

        g.setColor(checkerType == CheckerType.RED_KING || checkerType == CheckerType.RED_REGULAR ? Color.RED : Color.BLACK);
        g.fillOval(x, y, DIMENSION, DIMENSION);
        g.setColor(Color.WHITE);
        g.drawOval(x, y, DIMENSION, DIMENSION);
        
        if(checkerType == CheckerType.RED_KING || checkerType == CheckerType.BLACK_KING){
            g.drawString("KING",x+10,y+25);
        }
    }
 
    public static int getDimension(){
        /*
         * @param n/a
         * @purpose For reference by Board.java to allow the board to scale with the checkers. 
         */
        
        return DIMENSION;
    }
    
    public CheckerType getCheckerType(){
        /*
         * @param n/a
         * @purpose For Board.java's calculations.  
         */
        
        return this.checkerType;
    }
}
