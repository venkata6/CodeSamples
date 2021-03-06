import java.nio.*;
import java.io.*;

// http://projecteuler.net/problem=15

// I am using some kind of memoization/dyaminc programming to efficiently store the # of paths 

public class PEProblem15 {
    static int DIM = 20 ;  
    final private static  boolean  DEBUG = false;

    static long grid [][] = null;

    public static void main (String[] args){

	DIM = Integer.parseInt(args[0]);
	grid = new long[DIM][DIM];
	System.out.println("\nNow let us calculate the sum ..\n");
	calculateSum();
	// for debugging start and calcualte the max path
       System.out.println( "\nAnd the number of the paths  " + grid[DIM-1][DIM-1] + "\n "  );
    }

    public static void calculateSum( ) {

	for (int i=0;i<DIM;i++)  // row index 
	{
	    for(int j=0;j<DIM;j++) //column index
	    {
		if (i==0 || j==0 ) { grid[i][j] = 1;  }
		else { grid[i][j] = grid[i][j-1] + grid[i-1][j];  }
	    }
	      
	 }

    }

}