package my.finalproject;

import javax.swing.JFrame;

/**
 * @author johnaoss
 * @date 6-9-2016
 * @title Checkers.java
 * @purpose Main Class for the Checkers project. 
 */

public class Checkers extends JFrame{
     
    public Checkers(String title){
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Board board = new Board();
        setResizable(false);
        setSize(board.getPreferredDimension()+5,board.getPreferredDimension()+28);
        setContentPane(board);
        
        //NOT NEEDED DUE TO setSize()
        //pack();
        setVisible(true);
    }
  
    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               new Checkers("Checkers"); 
            }
        }); 
    }
}
