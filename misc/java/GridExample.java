import java.util.*;

// For topcoder problem 
// http://community.topcoder.com/stat?c=problem_statement&pm=2998&rd=5857
// Sample of how we can use DFS and Grid to solve problems 
// nice one !!
// wrote from the pseudo-code from the tutorial 
// http://community.topcoder.com/tc?module=Static&d1=tutorials&d2=graphsDataStrucs2

class Node {

    public int x,y;
    Node(int x,int y) { this.x=x; this.y=y;} 

}

public class GridExample {

    public static boolean fill[][] = new boolean[400][600];
 
    public static void main ( String[] args)
    {
	for(int i=0; i<400; i++) 
	    for (int j=0; j<600; j++) 
		fill[i][j]=false;
	String[] ip = { "0 292 399 307"} ;
	//String[] ip = {"48 192 351 207", "48 392 351 407", "120 52 135 547", "260 52 275 547"};
	//String[] ip = {"0 192 399 207", "0 392 399 407", "120 0 135 599", "260 0 275 599"};
	//String[] ip = {"50 300 199 300", "201 300 350 300", "200 50 200 299", "200 301 200 550"};
	/*String[] ip = {"0 20 399 20", "0 44 399 44", "0 68 399 68", "0 92 399 92",
		       "0 116 399 116", "0 140 399 140", "0 164 399 164", "0 188 399 188",
		       "0 212 399 212", "0 236 399 236", "0 260 399 260", "0 284 399 284",
		       "0 308 399 308", "0 332 399 332", "0 356 399 356", "0 380 399 380",
		       "0 404 399 404", "0 428 399 428", "0 452 399 452", "0 476 399 476",
		       "0 500 399 500", "0 524 399 524", "0 548 399 548", "0 572 399 572",
		       "0 596 399 596", "5 0 5 599", "21 0 21 599", "37 0 37 599",
		       "53 0 53 599", "69 0 69 599", "85 0 85 599", "101 0 101 599",
		       "117 0 117 599", "133 0 133 599", "149 0 149 599", "165 0 165 599",
		       "181 0 181 599", "197 0 197 599", "213 0 213 599", "229 0 229 599",
		       "245 0 245 599", "261 0 261 599", "277 0 277 599", "293 0 293 599",
		       "309 0 309 599", "325 0 325 599", "341 0 341 599", "357 0 357 599",
		       "373 0 373 599", "389 0 389 599"};
	*/
	int[] r = sortedAreas(ip);
	for ( int i=0; i < r.length; i++)
	    System.out.println("Answers is "+ r[i]) ;
    }
    
    public static int[] sortedAreas( String[] rectangles ) {
	ArrayList<Integer>  result = new ArrayList<Integer>();
	int cnt=0;
	for(int i=0; i < rectangles.length; i++)
	    {
		String rect = rectangles[i] ;
		String[] xy = rect.split(" ");
		int x1 = Integer.parseInt(xy[0]);
		int y1 = Integer.parseInt(xy[1]);
		int x2 = Integer.parseInt(xy[2]);
		int y2 = Integer.parseInt(xy[3]);
		
		for ( int j=x1; j<=x2; j++)
		    for (int k=y1; k<=y2; k++)
			fill[j][k]=true;
	    }
	for(int i=0; i < 400; i++)
	    for(int j=0; j < 600; j++)
		if ( fill[i][j] == false)
		    result.add(doFill(i,j));

	//surprised there is no easy way to convert Integer[] to int[] !!
	int [] r = new int[result.size()];
	Iterator<Integer> iter = result.iterator();

	for( int i=0 ; i < result.size(); i++ )
	    r[i] = iter.next().intValue();

	Arrays.sort(r) ;
	return r;
    }
    
    public static int doFill(int x, int y) {
	int result = 0;

	// Declare our stack of nodes, and push our starting node onto the stack
	Stack s = new Stack();
	s.push(new Node(x, y));

	while (s.isEmpty() == false) {
	    Node top = (Node) s.peek();
	    s.pop();

	    // Check to ensure that we are within the bounds of the grid, if not, continue
	    if (top.x < 0 || top.x >= 400) continue;
	    // Similar check for y
	    if (top.y < 0 || top.y >= 600) continue;
	    // Check that we haven't already visited this position, as we don't want to count it twice
	    if (fill[top.x][top.y]) continue;

	    fill[top.x][top.y] = true; // Record that we have visited this node

	    // We have found this node to be empty, and part
	    // of this connected area, so add 1 to the result
	    result++;

	    // Now we know that we have at least one empty square, then we will attempt to
	    // visit every node adjacent to this node.
	    s.push(new Node(top.x + 1, top.y));
	    s.push(new Node(top.x - 1, top.y));
	    s.push(new Node(top.x, top.y + 1));
	    s.push(new Node(top.x, top.y - 1));
	}

	return result;
    }

}
