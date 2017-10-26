import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;

public class MazeTester {

   public static void main(String[] args)  {

      String file0 = "";
      String file1 = "";
      String file2 = "";
      
      try {

         if (args.length < 1) {
            System.out.println("ERROR: missing file name command line argument");
         }
         else {
            // ------------------ main test implementation -----------------
            file0 = args[0];
            file1 = args[1];
            file2 = args[2];
            
            Maze maze0 = readMazeFile(file0);
            // System.out.println(maze0.mazeToString());
            // System.out.println("startLoc: " + maze0.locToString(maze0.getEntryLoc()));
            // System.out.println("exitLoc: " + maze0.locToString(maze0.getExitLoc()));
            
            // System.out.println("Check for Walls at Arbitrary Locations");
            // System.out.println();
            // MazeCoord wall0 = new MazeCoord(1,0);
            // System.out.println("wall0: " + maze0.locToString(wall0) + " wall = " + maze0.hasWallAt(wall0));
            // MazeCoord free0 = new MazeCoord(0,0);
            // System.out.println("free0: " + maze0.locToString(free0) + " wall = " + maze0.hasWallAt(free0));          
            // MazeCoord wall1 = new MazeCoord(20,20);
            // System.out.println("wall1: " + maze0.locToString(wall1) + " wall = " + maze0.hasWallAt(wall1));
            // MazeCoord free1 = new MazeCoord(17,18);
            // System.out.println("free1: " + maze0.locToString(free1) + " wall = " + maze0.hasWallAt(free1));
            // System.out.println();

            // Maze maze1 = readMazeFile(file1);
            // System.out.println(maze1.mazeToString());
            // System.out.println("startLoc: " + maze1.locToString(maze1.getEntryLoc()));
            // System.out.println("exitLoc: " + maze1.locToString(maze1.getExitLoc()));
            // System.out.println();
            
            // Maze maze2 = readMazeFile(file2);
            // System.out.println(maze2.mazeToString());
            // System.out.println("startLoc: " + maze2.locToString(maze2.getEntryLoc()));
            // System.out.println("exitLoc: " + maze2.locToString(maze2.getExitLoc()));
            // System.out.println();
            
            // ------------------ main test implementation -----------------
         }
      }
      catch (FileNotFoundException exc) {
         System.out.println("ERROR: File not found: " + file0);
      }
      catch (IOException exc) {
         exc.printStackTrace();
      }
   }

   /**
    readMazeFile reads in maze from the file whose name is given and 
    returns a MazeFrame created from it.
   
   @param fileName
             the name of a file to read from (file format shown in class comments, above)
   @returns a MazeFrame containing the data from the file.
        
   @throws FileNotFoundException
              if there's no such file (subclass of IOException)
   @throws IOException
              (hook given in case you want to do more error-checking --
               that would also involve changing main to catch other exceptions)
   */
  private static Maze readMazeFile(String fileName) throws IOException 
  {
    // maze data
    int numRows = 0;
    int numCols = 0;
    boolean grabbedSize = false;
    boolean grabbedMaze = false;
    String[] rawMazeData;
    boolean[][] mazeData;
    MazeCoord startPoint = null;
    MazeCoord endPoint = null;

    // create new file and read data into boolean array
    File inFile = new File(fileName);
    try (Scanner in = new Scanner(inFile))
    {
        // grab maze size
        if (in.hasNextInt())
            numRows = in.nextInt();
        if (in.hasNextInt())
            numCols = in.nextInt();
        
        // iterate to begining of maze data
        in.nextLine();
        
        // create maze data and put into array of strings
        rawMazeData = new String[numRows];
        for (int i = 0; i < numRows; i++)
        {
            if (in.hasNextLine())
                rawMazeData[i] = in.nextLine();
        }

        // grab start/end coordinates
        if (in.hasNextLine())
            startPoint = new MazeCoord(in.nextInt(), in.nextInt());
        if (in.hasNextLine())
            endPoint = new MazeCoord(in.nextInt(), in.nextInt());
    }
    mazeData = new boolean[numRows][numCols];
      
    for (int i = 0; i < numRows; i++)
    {
        for (int j = 0; j < numCols; j++)
        {
            if(rawMazeData[i].charAt(j) == '0')
                mazeData[i][j] = false;
            else if (rawMazeData[i].charAt(j) == '1')
                mazeData[i][j] = true;
            else
                System.out.println("ERROR: mazeData parsing unsuccessful"); 
        }
    }
      return new Maze(mazeData, startPoint, endPoint);
    }
}