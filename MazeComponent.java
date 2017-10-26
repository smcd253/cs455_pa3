// Name: Spencer McDonough
// USC loginid: 6341617166
// CS 455 PA3
// Fall 2017

import java.awt.Graphics;
import javax.swing.JComponent;

// libraries from PA1
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.LinkedList;

/**
   MazeComponent class
   
   A component that displays the maze and path through it if one has been found.
*/
public class MazeComponent extends JComponent
{

   private static final int START_X = 10; // top left of corner of maze in frame
   private static final int START_Y = 10;
   private static final int BOX_WIDTH = 20;  // width and height of one maze "location"
   private static final int BOX_HEIGHT = 20;
   private static final int INSET = 2;  // how much smaller on each side to make entry/exit inner box
   
   private Maze maze = null; 
   
   /**
      Constructs the component.
      @param maze   the maze to display
   */
   public MazeComponent(Maze maze) 
   {   
      this.maze = maze;
   }

   /**
     Draws the current state of maze including the path through it if one has
     been found.
     @param g the graphics context
   */
   public void paintComponent(Graphics g)
   {
    Graphics2D g2 = (Graphics2D)g;
    // loop through maze, draw black rectangles for walls, white for free spaces
    for (int i = 0; i < this.maze.numRows(); i++)
    {
      for (int j = 0; j < this.maze.numCols(); j++)
      {
        // create new rectangle at this location, flip x and y coordinates
        Rectangle box = new Rectangle(this.START_Y + (j * this.BOX_HEIGHT),
                                      this.START_X + (i * this.BOX_WIDTH), 
                                      this.BOX_HEIGHT, this.BOX_WIDTH);

        // create new mazecoord at this location
        MazeCoord location = new MazeCoord(i, j);
        
        // set colors based on 1/0 at location
        if (this.maze.hasWallAt(location))
        {
          g2.setColor(Color.BLACK);       
        }
        else
        {
          g2.setColor(Color.WHITE);            
        } 
        // draw box
        g2.draw(box);
        g2.fill(box);
        
        /**
         * if at entry or exit locations, draw a new, smaller box over the default box
         */
        // if location is entry location, decrease size by inset and color yellow
        if (location.getCol() == this.maze.getEntryLoc().getCol() 
        && location.getRow() == this.maze.getEntryLoc().getRow())
        {
          Rectangle entryBox = new Rectangle(this.START_Y + (j * this.BOX_HEIGHT) + this.INSET/2,
                                            this.START_X + (i * this.BOX_WIDTH) + this.INSET/2, 
                                            this.BOX_HEIGHT - this.INSET, 
                                            this.BOX_WIDTH - this.INSET); 

          g2.setColor(Color.YELLOW);
          g2.draw(entryBox);
          g2.fill(entryBox);          
        }

        // if location is exit location, decrease size by inset and color green        
        else if (location.getCol() == this.maze.getExitLoc().getCol() 
              && location.getRow() == this.maze.getExitLoc().getRow())
        {                   
          Rectangle exitBox = new Rectangle(this.START_Y + (j * this.BOX_HEIGHT) + this.INSET/2,
                                            this.START_X + (i * this.BOX_WIDTH) + this.INSET/2, 
                                            this.BOX_HEIGHT - this.INSET, 
                                            this.BOX_WIDTH - this.INSET);                                    
          g2.setColor(Color.GREEN);
          g2.draw(exitBox);
          g2.fill(exitBox);          
        }

        // draw path
        // -------------------- BUG -----------------
        // drawPath(g2);
        // -------------------- BUG -----------------
        // use simple draw method as backup
        if (this.maze.search())
          drawPath(g2);
      }
    }
  }

  /**
   * Returns nothing, draws a small box inside each location on path
   * @param g2 must be initialized by (Graphics2D)g
   */
  private void drawPathSimple(Graphics2D g2)
  {
    // path variables
    LinkedList<MazeCoord> path = null;
    Iterator<MazeCoord> iterator = null;
    MazeCoord pathLoc = null;

    path = this.maze.getPath();
    iterator = path.iterator();
    if (iterator.hasNext())
    {
      pathLoc = iterator.next();
    }   
    
    // ---------- draw path elements -----------
    // loop through path
    while(iterator.hasNext())
    {   
      // loop through entire maze for every path location
      for (int i = 0; i < this.maze.numRows(); i++)
      {
        for (int j = 0; j < this.maze.numCols(); j++)
        {
          MazeCoord location = new MazeCoord(i,j);

            // if we are at a path location, draw a line based on the conditions outlined below
          if (location.getCol() == pathLoc.getCol()
              && location.getRow() == pathLoc.getRow())
          {
            Rectangle lineBeta = new Rectangle(this.START_Y + (j * this.BOX_HEIGHT) + 9,
                                              this.START_X + (i * this.BOX_WIDTH) + 9, 
                                              this.BOX_HEIGHT - 18, 
                                              this.BOX_WIDTH - 18);                          
            
            g2.setColor(Color.BLUE);
            g2.draw(lineBeta);
            g2.fill(lineBeta);
            
            // iterate iterator and store back into local var pathLoc
            if (iterator.hasNext())
              pathLoc = new MazeCoord(iterator.next());
          }
        }
      }
    }
  }

  /**
   * Returns nothing, draws a continuous line from entryLoc to exitLoc
   * BUG: was not able to achieve mutual exclusivity on all draw conditions
   * --> does not properly draw line at corners or in
   * @param g2 must be initialized by (Graphics2D)g
   */

  private void drawPath(Graphics2D g2)
  {
    // path variables
    LinkedList<MazeCoord> path = null;
    Iterator<MazeCoord> iterator = null;
    MazeCoord pathLoc = null;
    MazeCoord nextPathLoc = null;

    // if search() returns true, initialize path variables
    if (/*this.maze.search()*/ true)
    {
      path = this.maze.getPath();
      iterator = path.iterator();
      if (iterator.hasNext())
      {
        pathLoc = iterator.next();
        nextPathLoc = iterator.next();
      }   
      
      // ---------- draw path elements -----------
      // loop through path
      while(iterator.hasNext())
      {
        if (this.maze.mazeVal(pathLoc.getRow(), pathLoc.getCol()) == 4
            && this.maze.mazeVal(nextPathLoc.getRow(), nextPathLoc.getCol()) == 4)
        {
          g2.drawLine(this.START_Y + (pathLoc.getCol() * this.BOX_HEIGHT) + this.BOX_HEIGHT/2,
                      this.START_X + (pathLoc.getRow() * this.BOX_WIDTH) + this.BOX_WIDTH/2,
                      this.START_Y + (nextPathLoc.getCol() * this.BOX_HEIGHT) + this.BOX_HEIGHT/2, 
                      this.START_X + (nextPathLoc.getRow() * this.BOX_WIDTH) + this.BOX_WIDTH/2);
          g2.setColor(Color.BLUE);
        }  
        
        
        // update pathLoc vars
        pathLoc = nextPathLoc;
        // iterate iterator and store back into local var pathLoc
        if (iterator.hasNext())
          nextPathLoc = new MazeCoord(iterator.next());           
        
      }
    }
  }
}
  // private void drawPath(Graphics2D g2)
  // {
  //   // path variables
  //   LinkedList<MazeCoord> path = null;
  //   Iterator<MazeCoord> iterator = null;
  //   MazeCoord pathLoc = null;
  //   MazeCoord prevPathLoc = null;
  //   MazeCoord nextPathLoc = null;

  //   // if search() returns true, initialize path variables
  //   if (/*this.maze.search()*/ true)
  //   {
  //     path = this.maze.getPath();
  //     iterator = path.iterator();
  //     if (iterator.hasNext())
  //     {
  //       pathLoc = iterator.next();
  //       prevPathLoc = pathLoc;
  //       nextPathLoc = iterator.next();
  //     }   
      
  //     // ---------- draw path elements -----------
  //     // loop through path
  //     while(iterator.hasNext())
  //     {
  //       // loop through entire maze for every path location
  //       for (int i = 0; i < this.maze.numRows(); i++)
  //       {
  //         for (int j = 0; j < this.maze.numCols(); j++)
  //         {
  //           MazeCoord location = new MazeCoord(i,j);

  //           // System.out.print("location = " + location.toString() + " : ");
  //           // System.out.println("pathLoc = " + pathLoc.toString());

  //            // if we are at a path location, draw a line based on the conditions outlined below
  //           if (location.getCol() == pathLoc.getCol()
  //               && location.getRow() == pathLoc.getRow())
  //           {
  //             // Rectangle lineBeta = new Rectangle(this.START_Y + (j * this.BOX_HEIGHT) + 9,
  //             //                                   this.START_X + (i * this.BOX_WIDTH) + 9, 
  //             //                                   this.BOX_HEIGHT - 18, 
  //             //                                   this.BOX_WIDTH - 18);
              
  //             g2.drawLine(this.START_Y + (j * this.BOX_HEIGHT) + this.BOX_HEIGHT/2,
  //             this.START_X + (i * this.BOX_WIDTH) + this.BOX_WIDTH/2,
  //             this.START_Y + (j * this.BOX_HEIGHT) + this.BOX_HEIGHT/2, 
  //             this.START_X + (i * this.BOX_WIDTH) + this.BOX_WIDTH);
  //             // if at entry point
  //             if (pathLoc.equals(this.maze.getEntryLoc()))
  //             {
  //               // half line right
  //               if (pathLoc.getCol() == nextPathLoc.getCol() - 1)
  //               {
  //                 g2.drawLine(this.START_Y + (j * this.BOX_HEIGHT) + this.BOX_HEIGHT/2,
  //                             this.START_X + (i * this.BOX_WIDTH) + this.BOX_WIDTH/2,
  //                             this.START_Y + (j * this.BOX_HEIGHT) + this.BOX_HEIGHT/2, 
  //                             this.START_X + (i * this.BOX_WIDTH) + this.BOX_WIDTH);
  //               }
  //               // half line left
  //               else if (pathLoc.getCol() == nextPathLoc.getCol() + 1)
  //               {
  //                 g2.drawLine(this.START_Y + (j * this.BOX_HEIGHT) + this.BOX_HEIGHT/2,
  //                             this.START_X + (i * this.BOX_WIDTH) + this.BOX_WIDTH/2,
  //                             this.START_Y + (j * this.BOX_HEIGHT) + this.BOX_HEIGHT/2,
  //                             this.START_X + (i * this.BOX_WIDTH) - this.BOX_WIDTH);                
  //               }
  //               // half line down
  //               else if (pathLoc.getRow() == nextPathLoc.getRow() - 1)
  //               {
  //                 g2.drawLine(this.START_Y + (j * this.BOX_HEIGHT) + this.BOX_HEIGHT/2,
  //                             this.START_X + (i * this.BOX_WIDTH) + this.BOX_WIDTH/2,
  //                             this.START_Y + (j * this.BOX_HEIGHT) + this.BOX_HEIGHT,
  //                             this.START_X + (i * this.BOX_WIDTH) + this.BOX_WIDTH/2);  
  //               }
  //               // half line up
  //               else if (pathLoc.getRow() == nextPathLoc.getRow() + 1)
  //               {
  //                 g2.drawLine(this.START_Y + (j * this.BOX_HEIGHT) + this.BOX_HEIGHT/2,
  //                             this.START_X + (i * this.BOX_WIDTH) + this.BOX_WIDTH/2,
  //                             this.START_Y + (j * this.BOX_HEIGHT) - this.BOX_HEIGHT,
  //                             this.START_X + (i * this.BOX_WIDTH) + this.BOX_WIDTH/2);  
  //               }
  //               // error case
  //               else
  //                 System.out.println("Error in Entry Loc Path Printing");
  //             }
  //             // if at exit point
  //             else if (pathLoc == this.maze.getExitLoc())
  //             {
  //               // half line right
  //               if (pathLoc.getCol() == prevPathLoc.getCol() - 1)
  //               {
  //                 g2.drawLine(this.START_Y + (j * this.BOX_HEIGHT) + this.BOX_HEIGHT/2,
  //                             this.START_X + (i * this.BOX_WIDTH) + this.BOX_WIDTH/2,
  //                             this.START_Y + (j * this.BOX_HEIGHT) + this.BOX_HEIGHT/2, 
  //                             this.START_X + (i * this.BOX_WIDTH) + this.BOX_WIDTH);
  //               }
  //               // half line left
  //               else if (pathLoc.getCol() == prevPathLoc.getCol() + 1)
  //               {
  //                 g2.drawLine(this.START_Y + (j * this.BOX_HEIGHT) + this.BOX_HEIGHT/2,
  //                             this.START_X + (i * this.BOX_WIDTH) + this.BOX_WIDTH/2,
  //                             this.START_Y + (j * this.BOX_HEIGHT) + this.BOX_HEIGHT/2,
  //                             this.START_X + (i * this.BOX_WIDTH) - this.BOX_WIDTH);                
  //               }
  //               // half line down
  //               else if (pathLoc.getRow() == prevPathLoc.getRow() - 1)
  //               {
  //                 g2.drawLine(this.START_Y + (j * this.BOX_HEIGHT) + this.BOX_HEIGHT/2,
  //                             this.START_X + (i * this.BOX_WIDTH) + this.BOX_WIDTH/2,
  //                             this.START_Y + (j * this.BOX_HEIGHT) + this.BOX_HEIGHT,
  //                             this.START_X + (i * this.BOX_WIDTH) + this.BOX_WIDTH/2);  
  //               }
  //               // half line up
  //               else if (pathLoc.getRow() == prevPathLoc.getRow() + 1)
  //               {
  //                 g2.drawLine(this.START_Y + (j * this.BOX_HEIGHT) + this.BOX_HEIGHT/2,
  //                             this.START_X + (i * this.BOX_WIDTH) + this.BOX_WIDTH/2,
  //                             this.START_Y + (j * this.BOX_HEIGHT) - this.BOX_HEIGHT,
  //                             this.START_X + (i * this.BOX_WIDTH) + this.BOX_WIDTH/2);  
  //               }
  //               // error case
  //               else
  //                 System.out.println("Error in Exit Loc Path Printing");
  //             }
  //             else // all path locations between entry and exit
  //             {
  //               // full vertical line
  //               if (pathLoc.getRow() == prevPathLoc.getRow() + 1
  //                   && pathLoc.getRow() == nextPathLoc.getRow() - 1)
  //               {
  //                 g2.drawLine(this.START_Y + (j * this.BOX_HEIGHT) + this.BOX_HEIGHT/2,
  //                             this.START_X + (i * this.BOX_WIDTH),
  //                             this.START_Y + (j * this.BOX_HEIGHT) + this.BOX_HEIGHT/2,
  //                             this.START_X + (i * this.BOX_WIDTH) + this.BOX_WIDTH);
  //               }
  //               // full horizontal line
  //               else if (pathLoc.getCol() == prevPathLoc.getCol() + 1
  //                       && pathLoc.getCol() == nextPathLoc.getCol() - 1)              
  //               {
  //                 g2.drawLine(this.START_Y + (j * this.BOX_HEIGHT),
  //                             this.START_X + (i * this.BOX_WIDTH) + this.BOX_WIDTH/2,
  //                             this.START_Y + (j * this.BOX_HEIGHT) + this.BOX_HEIGHT,
  //                             this.START_X + (i * this.BOX_WIDTH) + this.BOX_WIDTH/2);
                  
  //               } 
  //               // top right hand corner
  //               else if (pathLoc.getCol() == prevPathLoc.getCol() + 1
  //                       && pathLoc.getRow() == nextPathLoc.getRow() + 1)
  //               {
  //                 System.out.println("Found corner");
  //               }
  //               // top left hand corner
  //               else if (pathLoc.getRow() == prevPathLoc.getRow() + 1
  //                       && pathLoc.getCol() == nextPathLoc.getCol() - 1)
  //               {
  //                System.out.println("Found corner");
                  
  //               }
  //               // bottom right hand corner
  //               else if (pathLoc.getCol() == prevPathLoc.getCol() + 1
  //                       && pathLoc.getRow() == nextPathLoc.getRow() - 1)
  //               {
  //                 System.out.println("Found corner");
                  
  //               }
  //               // bottom left hand corner
  //               else if (pathLoc.getRow() == prevPathLoc.getRow() + 1
  //               && pathLoc.getCol() == nextPathLoc.getCol() - 1)
  //               {
  //                 System.out.println("Found corner");
                  
  //               }
  //               // error case
  //               else
  //                 System.out.println("Error in Mid-Path Printing");
                
  //             }                     
              
  //             //g2.draw(lineBeta);
  //             g2.setColor(Color.BLUE);
  //             //g2.fill(lineBeta);
              
  //             // update pathLoc vars
  //             prevPathLoc = pathLoc;
  //             pathLoc = nextPathLoc;
  //             // iterate iterator and store back into local var pathLoc
  //             if (iterator.hasNext())
  //               nextPathLoc = new MazeCoord(iterator.next());
              
  //             System.out.println(prevPathLoc.toString() + pathLoc.toString() + nextPathLoc.toString());
  //           }
  //         }
  //       }
  //     }
  //   }
  // }



