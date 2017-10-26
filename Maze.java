// Name:
// USC loginid:
// CS 455 PA3
// Fall 2017

import java.util.Iterator;
import java.util.LinkedList;


/**
   Maze class

   Stores information about a maze and can find a path through the maze
   (if there is one).
   
   Assumptions about structure of the maze, as given in mazeData, startLoc, and endLoc
   (parameters to constructor), and the path:
     -- no outer walls given in mazeData -- search assumes there is a virtual 
        border around the maze (i.e., the maze path can't go outside of the maze
        boundaries)
     -- start location for a path is maze coordinate startLoc
     -- exit location is maze coordinate exitLoc
     -- mazeData input is a 2D array of booleans, where true means there is a wall
        at that location, and false means there isn't (see public FREE / WALL 
        constants below) 
     -- in mazeData the first index indicates the row. e.g., mazeData[row][col]
     -- only travel in 4 compass directions (no diagonal paths)
     -- can't travel through walls

 */

public class Maze {
   
  public static final boolean FREE = false;
  public static final boolean WALL = true;
   
  /**
   * maze in int form to detect cycles in search()
   * 0 = FREE
   * 1 = WALL
   * 2 = VISITED
   * 3 = STARTLOC
   * 4 = ENDLOC
   */
  private int[][] myMaze = null;
  private int numRows = 0;
  private int numCols = 0;

  private MazeCoord startLoc = null;
  private MazeCoord exitLoc = null;
  
  private LinkedList<MazeCoord> testPath = new LinkedList<MazeCoord>();
  private LinkedList<MazeCoord> path = new LinkedList<MazeCoord>();

  private boolean alreadySolved = false;

   /**
      Constructs a maze.
      @param mazeData the maze to search.  See general Maze comments above for what
      goes in this array.
      @param startLoc the location in maze to start the search (not necessarily on an edge)
      @param exitLoc the "exit" location of the maze (not necessarily on an edge)
      PRE: 0 <= startLoc.getRow() < mazeData.length and 0 <= startLoc.getCol() < mazeData[0].length
         and 0 <= endLoc.getRow() < mazeData.length and 0 <= endLoc.getCol() < mazeData[0].length

    */
  public Maze(boolean[][] mazeData, MazeCoord startLoc, MazeCoord exitLoc) {
    numRows = mazeData.length;
    numCols = mazeData[0].length;
    this.myMaze = new int[numRows][numCols];

    // run through input boolean array and create int[][] maze 
    for (int i = 0; i < numRows; i++)
    {
      for (int j = 0; j < numCols; j++)
      {
        if(mazeData[i][j] == true)
          this.myMaze[i][j] = 1;
        else if (mazeData[i][j] == false)
          this.myMaze[i][j] = 0;
        
        if (i == startLoc.getRow() && j == startLoc.getCol())
        {
          //this.myMaze[i][j] = 3;
          this.startLoc = new MazeCoord(i, j);
        }          
        else if (i == exitLoc.getRow() && j == exitLoc.getCol())
        {
          //this.myMaze[i][j] = 4;
          this.exitLoc = new MazeCoord(i, j);
        }
        //System.out.print(this.myMaze[i][j] + " ");
      }
      //System.out.println();
    }
  }


   /**
      Returns the number of rows in the maze
      @return number of rows
   */
   public int numRows() {
      return this.numRows;   
   }

   /**
      Returns the number of columns in the maze
      @return number of columns
   */   
   public int numCols() {
      return this.numCols;   
   } 
 
   
   /**
      Returns true iff there is a wall at this location
      @param loc the location in maze coordinates
      @return whether there is a wall here
      PRE: 0 <= loc.getRow() < numRows() and 0 <= loc.getCol() < numCols()
   */
   public boolean hasWallAt(MazeCoord loc) {  
      if (0 <= loc.getRow() && loc.getRow() < this.numRows && 0 <= loc.getCol() && loc.getCol() < this.numCols)
      {
        if (this.myMaze[loc.getRow()][loc.getCol()] == 1)
          return true;
      }
      return false; 
   }
   

   /**
      Returns the entry location of this maze.
    */
   public MazeCoord getEntryLoc() {
      return this.startLoc;   // DUMMY CODE TO GET IT TO COMPILE
   }
   
   /**
     Returns the exit location of this maze.
   */
   public MazeCoord getExitLoc() {
      return this.exitLoc;   // DUMMY CODE TO GET IT TO COMPILE
   }
   
   /**
      Returns the path through the maze. First element is start location, and
      last element is exit location.  If there was not path, or if this is called
      before a call to search, returns empty list.

      @return the maze path
    */
   public LinkedList<MazeCoord> getPath() {
      // test
      // this.buildTestPath();
      // return this.testPath; 
      // System.out.println(this.path.toString());

      return this.path;
   }

   /**
    * Find out if search() was already called and if it was sucsessful
    * @return whether search() was already performed
    */
    public boolean getAlreadySolved()
    {
      return this.alreadySolved;
    }

   /**
      Find a path from start location to the exit location (see Maze
      constructor parameters, startLoc and exitLoc) if there is one.
      Client can access the path found via getPath method.

      @return whether or not a path was found or already found.
    */
   public boolean search()  {  
      if (!alreadySolved)
      {
        alreadySolved = solveMaze(this.startLoc.getRow(), this.startLoc.getCol());
      }
      return alreadySolved; // DUMMY CODE TO GET IT TO COMPILE
   }

   /**
    * Recursively searches maze from entryLoc to exitLoc until it finds entryLoc
    or cannot find entryLoc
    @return whether or not path was found
    */
    private boolean solveMaze(int row, int col)
    {
      boolean done = false;
      MazeCoord location = new MazeCoord(row, col);

      if (0 <= row && row < this.numRows && 0 <= col && col < this.numCols)
      {
        //System.out.println(this.hasWallAt(location));
        if (this.myMaze[row][col] == 0)
        {
          this.myMaze[row][col] = 3;  // cell has been tried
          if (row == this.exitLoc.getRow() && col == this.exitLoc.getCol())
              done = true;  // maze is solved
          else 
          { // search 4 locations around this location (can't go diagonally)
            done = solveMaze (row + 1, col);  // down
            if (!done)
                done = solveMaze (row, col + 1);  // right
            if (!done)
                done = solveMaze (row - 1, col);  // up
            if (!done)
                done = solveMaze (row, col - 1);  // left
          }
           if (done)
           {  
            path.addLast(location);
            this.myMaze[row][col] = 4;// change values at all locations of the final path (4 = part of path)
           }  
        }
      }
      
      return done;
    }
   /**
    * toString() methodS to test constructor and accessors
    */
    public String toString()
    {
      String mazeString = "";
      mazeString += "Rows: " + this.numRows + " Cols: " + this.numCols + "\n";
      for (int i = 0; i < this.numRows; i++)
      {
        for (int j = 0; j < this.numCols; j++)
        {
          mazeString += this.myMaze[i][j] + " ";
        }
        mazeString += "\n";
      }
      return mazeString;
    }

    // test getPath()
    public void buildTestPath()
    {
     testPath.addLast(new MazeCoord(2,4));
     testPath.addLast(new MazeCoord(1,4));
     testPath.addLast(new MazeCoord(0,4));
     testPath.addLast(new MazeCoord(0,5));
     testPath.addLast(new MazeCoord(0,6));
     testPath.addLast(new MazeCoord(0,7));
     testPath.addLast(new MazeCoord(1,7));
     testPath.addLast(new MazeCoord(2,7));
     testPath.addLast(new MazeCoord(3,7));
     testPath.addLast(new MazeCoord(4,7));
     testPath.addLast(new MazeCoord(4,6));
     testPath.addLast(new MazeCoord(4,5));
     testPath.addLast(new MazeCoord(4,4));
     testPath.addLast(new MazeCoord(4,3));
     testPath.addLast(new MazeCoord(4,2));
     testPath.addLast(new MazeCoord(4,1));
     testPath.addLast(new MazeCoord(3,1));
     testPath.addLast(new MazeCoord(2,1));
     testPath.addLast(new MazeCoord(1,1));
     testPath.addLast(new MazeCoord(0,1));
     testPath.addLast(new MazeCoord(0,0)); 
    }
}