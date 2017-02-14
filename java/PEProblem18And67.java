import java.nio.*;
import java.io.*;

// http://projecteuler.net/problem=18
// http://projecteuler.net/problem=67

// These problems asked to find the most expensive path down a tree
// I am using some kind of memoization/dyaminc programming to efficiently store the most expensive path from a node to the root. 
// this structure has two parents and hence i am forced  to keep two arrays for the sum at ANY node, path up through either left and right parents 
// all this i did in half a 4-5 hrs programming.. no googling .. :-)
// analyzing the problem and coming with the DS and how to reference the parents  took most of the time !

public class PEProblem18And67 {
    final static int SIZE = 100 ;    // size is 15 for the problem 18 and 100 for the problem 67
    final private static  boolean  DEBUG = false;

    static int n [][] = new int[SIZE][SIZE];
    static int lp [][] = new int[SIZE][SIZE];   // left parent
    static int rp [][] = new int[SIZE][SIZE];   // right parent 

    public static void main (String[] args){
	try { readFile("triangle.txt"); } 
	catch ( IOException e) { System.err.format("IOException: %s%n",e);} 

	if (DEBUG) {
	for(int i=0;i<SIZE;i++) {
	   for(int j=0;j<SIZE;j++) {
	       System.out.print(n[i][j] + " " );
	   }
	}
	}

	System.out.println("\nNow let us calculate the sum ..\n");
	calculateSum();
	int max = 0 ;
	// for debugging start and calcualte the max path
	for(int i=0;i<SIZE;i++) {
	   for(int j=0;j<SIZE;j++) {
	       if (  DEBUG) { System.out.print(lp[i][j] + " "  ); }
	       if ( max < lp[i][j] ) { max = lp[i][j];  } 
	       if ( DEBUG) { System.out.print(rp[i][j] + " "  );}
	       if ( max < rp[i][j] ) { max = rp[i][j];  } 
	   }
	}
	// for debugging -  end 
       System.out.println( "\nAnd the most expensive path down the tree  is " + max + "\n "  );
    }

    public static void calculateSum( ) {

	for (int i=0;i<SIZE;i++)  // row index 
	{
	    if ( n[i][0] == 0 ) return ;  //let us assume for now if we encounter 0 in the first column, we are done
	    for(int j=0;j<SIZE;j++) //column index
	    {
		 //root
		if ( i == 0 && j==0) { lp[i][j] = rp[i][j]=n[i][j]; break; }

		 //if the column index is '0', we need to update only one parent. let us say right parent.
		if ( j == 0 ) { lp[i][j] = rp[i][j] = n[i][j] + rp[i-1][j]; }

		 //likewise if the column index is the last in that row,ie.(row# == col#)  we need to update only left parent.
		else if ( j == i ) { lp[i][j] = rp[i][j] = n[i][j] + lp[i-1][j-1];  }
		
		//rest we need to update both left and right parent sums !
		else { 
		     // find the sum of the current node 
		    // first for left parent path - find the max of the parent paths , if the left parent is greater we need to use that.
		    if ( lp[i-1][j-1] > rp[i-1][j-1] ) { lp[i][j] = n[i][j] + lp[i-1][j-1];  } 
		    else { lp[i][j] = n[i][j] + rp[i-1][j-1];  } // else use the right parent.
		    //same thing we need to do for the right parent sum of the current node.
		    if ( lp[i-1][j] > rp[i-1][j] )  { rp[i][j] = n[i][j] + lp[i-1][j];  }
		    else { rp[i][j] = n[i][j] + rp[i-1][j];  }
		}
	    }
	      
	 }

    }

    public static void readFile(String file) throws java.io.IOException {

	BufferedReader reader=null;
	int cnt=0;
	try {
	     reader = new BufferedReader(new FileReader(file));
	     String line = null;
	     System.out.println("Reading input ...\n");
	     while ((line = reader.readLine()) != null) {
		 if (DEBUG) { System.out.println(line);}
		    String[] digits = line.split(" ");
		    for ( int i=0; i<digits.length;i++)
			{
			    n[cnt][i]=Integer.parseInt(digits[i]);
			}
		    cnt++;
	     }
	 } 
	catch (IOException x) {
	    System.err.format("IOException: %s%n", x);
	}
	finally {
	    if (reader != null) reader.close();
	}
    }

}