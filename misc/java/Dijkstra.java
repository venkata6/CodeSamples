import java.util.*;
import java.lang.Math;

// From topcoder tutorial about graph
// this sample shows how we can solve the Dijkstra using heap or priority queue 
// http://community.topcoder.com/stat?c=problem_statement&pm=2288&rd=4725
// nice one !!
// wrote from the pseudo-code from the tutorial 


class NodeComparator implements Comparator <Node> {

    public int compare( Node n1, Node n2) { 
	if ( n1.shots < n2.shots ) return -1; 
	else if ( n1.shots == n2.shots ) return 0;
	else return 1; // n1 > n2  returns true if 'n1' has more shots
    }  

}


class Node implements Comparable <Node> {

    public int weapons;
    public int shots;
    Node(int w,int s) { weapons=w; shots=s;} 
    public int compareTo( Node n) { 
	if ( this.shots < n.shots ) return 1;  // returns true if 'this' has less shots whch is what we want.
	else if ( this.shots == n.shots ) return 0;
	else return -1;
    }  

}


public class Dijkstra {

    public static boolean[] visited = new boolean[32768];
    public static PriorityQueue pq = new PriorityQueue(10, new NodeComparator() );
    public static int numWeapons = 1;
 
    public static void main ( String[] args)
    {
	Dijkstra d = new Dijkstra() ;
	//String[] ds = {"070","500","140" };
	//int[] bh = {150,150,150};

	//String[] ds = {"1542", "7935", "1139", "8882"};
	//int[] bh = {150,150,150,150} ;

	//String[] ds = {"07","40" };
	//int[] bh = {150,10 };

	/* String[] ds = {"198573618294842",
		       "159819849819205",
		       "698849290010992",
		       "000000000000000",
		       "139581938009384",
		       "158919111891911",
		       "182731827381787",
		       "135788359198718",
		       "187587819218927",
		       "185783759199192",
		       "857819038188122",
		       "897387187472737",
		       "159938981818247",
		       "128974182773177",
		       "135885818282838"} ;   */
	//int[] bh = {157, 1984, 577, 3001, 2003, 2984, 5988, 190003,9000, 102930, 5938, 1000000, 1000000, 5892, 38} ;

	String[] ds = {"02111111", "10711111", "11071111", "11104111",
		       "41110111", "11111031", "11111107", "11111210"} ;
        int[] bh = {28,28,28,28,28,28,28,28};

	numWeapons = bh.length;  // set the weapons available 


	int shots = d.leastShots(ds,bh); 
	System.out.println("Answers is " + shots) ;
    }
    
    int leastShots(String[] damageChart, int[] bossHealth) {
	int cnt = 0 ;
	pq.add( new Node(0, 0));

	while (pq.isEmpty() == false) {

	    Node top = (Node) pq.peek();  // take the top element 
	    pq.poll();
	    System.out.println("Calculating ... " + top.weapons + "  " + top.shots + " " + cnt++);
	    // Make sure we don't visit the same configuration twice
	    if (visited[top.weapons]) continue;
	    visited[top.weapons] = true;

	    // A quick trick to check if we have all the weapons, meaning we defeated all the bosses.
	    // We use the fact that (2^numWeapons - 1) will have all the numWeapons bits set to 1.
	    if (top.weapons == (1 << numWeapons) - 1)
		return top.shots;

	    for (int i = 0; i < damageChart.length; i++) {
		// Check if we've already visited this boss, then don't bother trying him again
		if ((top.weapons >> i) == 1 ) continue;

		// Now figure out what the best amount of time that we can destroy this boss is, given the weapons we have.
		// We initialize this value to the boss's health, as that is our default (with our KiloBuster).
		int best = bossHealth[i];
		for (int j = 0; j < damageChart.length; j++) {
		    if (i == j) continue;
		    if ((((top.weapons >> j) & 1) == 1) && (damageChart[j].charAt(i) !=  '0')) {
			// We have this weapon, so try using it to defeat this boss
			int shotsNeeded = bossHealth[i] / (damageChart[j].charAt(i) - '0');
			if (bossHealth[i] % (damageChart[j].charAt(i) - '0') != 0) shotsNeeded++;
			best = Math.min(best, shotsNeeded);
		    }
		}

		// Add the new node to be searched, showing that we defeated boss i, and we used 'best' shots to defeat him.
		pq.add(new Node(top.weapons | (1 << i), top.shots + best));
	    }
	}
	//return ((Node)pq.peek()).shots;
	return 0;
    }

}
