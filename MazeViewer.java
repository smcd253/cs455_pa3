// Name: Spencer McDonough
// USC loginid: 6341617166
// CS 455 PA3
// Fall 2017

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JFrame;


/**
 * MazeViewer class
 * 
 * Program to read in and display a maze and a path through the maze. At user
 * command displays a path through the maze if there is one.
 * 
 * How to call it from the command line:
 * 
 *      java MazeViewer mazeFile
 * 
 * where mazeFile is a text file of the maze. The format is the number of rows
 * and number of columns, followed by one line per row, followed by the start location, 
 * and ending with the exit location. Each maze location is
 * either a wall (1) or free (0). Here is an example of contents of a file for
 * a 3x4 maze, with start location as the top left, and exit location as the bottom right
 * (we count locations from 0, similar to Java arrays):
 * 
 * 3 4 
 * 0111
 * 0000
 * 1110
 * 0 0
 * 2 3
 * 
 */

public class MazeViewer {
   
   private static final char WALL_CHAR = '1';
   private static final char FREE_CHAR = '0';

   public static void main(String[] args)  {

      String fileName = "";

      try {

         if (args.length < 1) {
            System.out.println("ERROR: missing file name command line argument");
         }
         else {
            fileName = args[0];
            
            JFrame frame = readMazeFile(fileName);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.setVisible(true);
         }
      }
      catch (FileNotFoundException exc) {
         System.out.println("ERROR: File not found: " + fileName);
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
    private static MazeFrame readMazeFile(String fileName) throws IOException 
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

        // // ------------------- TEST THAT WE GRABBED RIGHT VALUES ------------
        // System.out.println("numRows: " + numRows + " numCols: " + numCols);
        // for (int i = 0; i < numRows; i++)
        //     System.out.println("row[" + i + "]" + rawMazeData[i]);
        // if (startPoint != null)
        //     System.out.println("startPoint: " + startPoint.getRow() + ", " + startPoint.getCol());
        // if (endPoint != null)
        //     System.out.println("endPoint: " + endPoint.getRow() + ", " + endPoint.getCol());
        // // ------------------- TEST THAT WE GRABBED RIGHT VALUES ------------

        // construct mazeData boolean array after retrieving input
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

        // // ------------------ TEST BOOLEAN ARRAY ------------------
        // for (int i = 0; i < numRows; i++)
        // {
        //     for (int j = 0; j < numCols; j++)
        //     {
        //         System.out.print(mazeData[i][j] + " ");
        //     }
        //     System.out.println();
        // }
        // // ------------------ TEST BOOLEAN ARRAY ------------------
    
      return new MazeFrame(mazeData, startPoint, endPoint);
    }
}