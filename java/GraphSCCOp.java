package puzzle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class GraphOp {
	//public Map<Integer, List<Integer>> adjacencyList;
	public Map adjacencyList;
	
	public List<Boolean> visited;
	public List<Integer> finishingTimes;
	public List<Integer> leaders;
	public GraphOp(int noOfVertices) {
		//adjacencyList = new HashMap<Integer, List<Integer>>(noOfVertices);
		adjacencyList = new HashMap();
		for (int i = 1 ; i <= noOfVertices ; i++)
		        {
		            //adjacencyList.put(i, new LinkedList<Integer>());
					adjacencyList.put(i, new LinkedList());
		        }
		visited=new ArrayList<Boolean>(noOfVertices+1);  // to account for 1-based index
		for ( int i=0; i <noOfVertices+1; i++ ){
			visited.add(i, false);
		}
		finishingTimes=new ArrayList<Integer>(noOfVertices+1);
		for ( int i=0; i <noOfVertices+1; i++ ){
			finishingTimes.add(i, 0);
		}
		
		leaders=new ArrayList<Integer>(noOfVertices+1);
		for ( int i=0; i <noOfVertices+1; i++ ){
			leaders.add(i, 0);
		}
}
/* Adds nodes in the Adjacency list for the corresponding vertex */
    public void setEdge(int source , int destination)
    {
        if (source > adjacencyList.size() || destination > adjacencyList.size())
        {
            System.out.println("the vertex entered in not present ");
            return;
        }
        //List<Integer> slist = adjacencyList.get(source);
        List slist = (List) adjacencyList.get(source);
        slist.add(destination);
        //List<Integer> dlist = adjacencyList.get(destination);
        //dlist.add(source);
    }

    /* Returns the List containing the vertex joining the source vertex */
    public List getEdge(int source)
    {
        if (source > adjacencyList.size())
        {
            System.out.println("the vertex entered is not present");
            return null;
        }
        return (List)adjacencyList.get(source);
    }
}

public class GraphSCCOp
{
    /* Makes use of Map collection to store the adjacency list for each vertex.*/
	int number_of_vertices;
    GraphOp graph;
    GraphOp graphRev;
    static int finishingTime=0;
	static int currentSourceVertex=0;
	static Deque<Integer> stack ;
    /*
     * Initializes the map to with size equal to number of vertices in a graph
     * Maps each vertex to a given List Object
     */
    public GraphSCCOp(int v)
    {
	    number_of_vertices=v;
	    graph=new GraphOp(number_of_vertices);
	    graphRev=new GraphOp(number_of_vertices);
	    stack=new ArrayDeque<Integer>(v);
    }
    
    public static void DFS ( GraphOp g, int index) {
    	if ( g.visited.get(index) ) {
    		return ; // node already visited 
    	}
    	stack.clear(); // clear stack for new iteration 
    	stack.addFirst(index);  // initialize with the start point
    	g.visited.set(index,true);
    	g.leaders.set(index, currentSourceVertex);
    	while ( !stack.isEmpty()) {
    		int currentNode = stack.peekFirst();
    		List<Integer> edges = g.getEdge(currentNode);
    		boolean allDone=true;
        	for ( int j : edges ) {
        		while ( !g.visited.get(j)){  // not visited 
	        		stack.addFirst(j);
	        		allDone=false;
	        		g.visited.set(j,true);
	            	g.leaders.set(j, currentSourceVertex);
        		}
        	}
        	if ( allDone ){
        		stack.removeFirst();
        		g.finishingTimes.set(currentNode, ++finishingTime);
        	}
        	 	
    		
    	}
    }
    
    public static int  secondDFS ( GraphOp g, int index) {
    	if ( g.visited.get(index) ) {
    		return 0; // node already visited 
    	}
    	int cnt=0;
    	stack.clear(); // clear stack for new iteration 
    	stack.addFirst(index);  // initialize with the start point
    	g.visited.set(index,true);
    	cnt++;
    	while ( !stack.isEmpty()) {
    		int currentNode = stack.peekFirst();
    		List<Integer> edges = g.getEdge(currentNode);
    		boolean allDone=true;
        	for ( int j : edges ) {
        		while ( !g.visited.get(j)){  // not visited 
	        		stack.addFirst(j);
	        		allDone=false;
	        		g.visited.set(j,true);
	        		cnt++;
	    		}
        	}
        	if ( allDone ){
        		stack.removeFirst();
        		//g.finishingTimes.set(currentNode, ++finishingTime);
        	}
    	}
    	return cnt;
    }
    
    
    /*
     * Main Function reads the number of vertices and edges in a graph.
     * then creates a Adjacency List for the graph and prints it  
     */
    public static void main(String...arg)
    {
        int noOfVertices=875714;
    	//int noOfVertices=9;
        String sep = " ";
       	GraphSCCOp graphSCC = new GraphSCCOp(noOfVertices);
        File f = new File ( "/Users/venkat/Desktop/SCC.txt") ;   // input is like  "1 2"  vertex 1 connected to vertex 2 
        Scanner s=null;
        try
        {
        	s = new Scanner(f);
        	long start = System.currentTimeMillis();
            while ( s.hasNextLine() ) {
            	String line = s.nextLine();
            	String[] tokens = line.split(sep);
            	int edge =0 ;
            	int vertice =0;
    			vertice = Integer.parseInt(tokens[0]);
    			edge = Integer.parseInt(tokens[1]);
    			graphSCC.graph.setEdge(vertice, edge);
    			graphSCC.graphRev.setEdge(edge,vertice);
            }
            // KosaRaju's algorithm
            long algostart = System.currentTimeMillis();
            List<Boolean> visited=graphSCC.graphRev.visited;
            for ( int i=graphSCC.graphRev.adjacencyList.size(); i > 0; i-- ){
            	if ( !visited.get(i) ){
            		currentSourceVertex=i;
            		//System.out.println("in node " + i);
            		DFS(graphSCC.graphRev,i);
            	}
            }
            GraphOp finalGraph = new GraphOp(noOfVertices);
            for ( int i=1; i <= graphSCC.graph.adjacencyList.size(); i++){
            	List edges = (List)graphSCC.graph.adjacencyList.get(i);
            	//System.out.println(" original graph " + i  + " --> "+  Arrays.toString(edges.toArray()));
            	
            	List edgesNew= (List) finalGraph.getEdge(graphSCC.graphRev.finishingTimes.get(i));
            	for ( int k=0; k < edges.size(); k++){
            		int j = (Integer)edges.get(k);
            		edgesNew.add(graphSCC.graphRev.finishingTimes.get(j));
            	}
            	//System.out.println(graphSCC.graphRev.finishingTimes.get(i) + " "+  Arrays.toString(edgesNew.toArray()));
            	finalGraph.adjacencyList.put(graphSCC.graphRev.finishingTimes.get(i), edgesNew);
            }
            
            // 2nd DFS call
            List<Integer> sccs = new ArrayList<Integer>();
            for ( int i=finalGraph.adjacencyList.size(); i > 0; i-- ){
            	if ( !finalGraph.visited.get(i) ){
            		int scc=secondDFS(finalGraph,i);
            		if ( scc > 1) {
            			sccs.add(scc);
            		}
            	}
            }
            sccs.sort(null);
            Collections.reverse(sccs);
            long end = System.currentTimeMillis();
            System.out.println("Took : " + ((end - start) / 1000));
            System.out.println("Algo Took : " + ((end - algostart) / 1000));
            System.out.println("total SCCs" + Arrays.toString(sccs.toArray()));
            
        /* Prints the adjacency List representing the graph.*/
       // System.out.println ("the given Adjacency List for the graph \n");
        /* for (int i = 1 ; i <= graphSCC.graph.adjacencyList.size() ; i++)
        {
            System.out.print(i+"->");
            List<Integer> edgeList = graphSCC.graph.getEdge(i);
            for (int j = 1 ; ; j++ )
            {
                if (j != edgeList.size())
                {
                    System.out.print(edgeList.get(j - 1 )+"->");
                }else
                {
                    System.out.print(edgeList.get(j - 1 ));
                    break;
                }
            }
            System.out.println();
        }
        
        for (int i = 1 ; i <= graphSCC.graphRev.adjacencyList.size() ; i++)
        {
            System.out.print(i+"->");
            List<Integer> edgeList = graphSCC.graphRev.getEdge(i);
            for (int j = 1 ; ; j++ )
            {
                if (j != edgeList.size())
                {
                    System.out.print(edgeList.get(j - 1 )+"->");
                }else
                {
                    System.out.print(edgeList.get(j - 1 ));
                    break;
                }
            }
            System.out.println();
        }
        */
        
      }  

        catch(InputMismatchException inputMismatch)
        {
            System.out.println("Error in Input Format. \nFormat : <source index> <destination index>");
        } catch ( FileNotFoundException ex) {
        	
        }finally {
        	s.close();
        }
    }
}