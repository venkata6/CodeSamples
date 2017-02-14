package puzzle;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class GraphRandomizedMinCut
{
    /* Makes use of Map collection to store the adjacency list for each vertex.*/
    public  Map<Integer, List<Integer>> Adjacency_List;
    /*
     * Initializes the map to with size equal to number of vertices in a graph
     * Maps each vertex to a given List Object
     */
    public GraphRandomizedMinCut(int number_of_vertices)
    {
        Adjacency_List = new HashMap<Integer, List<Integer>>();
        for (int i = 1 ; i <= number_of_vertices ; i++)
        {
            Adjacency_List.put(i, new LinkedList<Integer>());
        }
    }


    /* Adds nodes in the Adjacency list for the corresponding vertex */
    public void setEdge(int source , int destination)
    {
       /* if (source > Adjacency_List.size() || destination > Adjacency_List.size())
        {
            System.out.println("the vertex entered in not present ");
            return;
        }*/
        List<Integer> slist = Adjacency_List.get(source);
        slist.add(destination);
        //List<Integer> dlist = Adjacency_List.get(destination);
        //dlist.add(source);
    }

    /* Returns the List containing the vertex joining the source vertex */
    public List<Integer> getEdge(int source)
    {
       /* if (source > Adjacency_List.size())
        {
            System.out.println("the vertex entered is not present");
            return null;
        }*/
        return Adjacency_List.get(source);
    }

    /*
     * Main Function reads the number of vertices and edges in a graph.
     * then creates a Adjacency List for the graph and prints it  
     */
    public static void main(String...arg)
    {
    	int mincut = Integer.MAX_VALUE;
    	for ( int i=0; i < 100; i++ ) {
    		System.out.println("in iteration " + i);
            int tmp = mincut();
            if ( tmp < mincut ) {
            	mincut=tmp;
            }
    	}
    	System.out.println("minumun cut is " + mincut);
        
    }


	private static int mincut() {
		int source , destination;
        int number_of_edges,number_of_vertices;
        int count = 1;
        number_of_vertices=200;
        String sep = "\t";
        Random random = new Random();
 
       	GraphRandomizedMinCut adjacencyList = new GraphRandomizedMinCut(number_of_vertices);
        File f = new File ( "/Users/venkat/Desktop/kargerMincut.txt") ; /*kargerMincut*/
        
        try
        {
        	Scanner s = new Scanner(f);
        	
            while ( s.hasNextLine() ) {
            	String line = s.nextLine();
            	String[] tokens = line.split(sep);
            	int i = 0; 
            	int edge =0 ;
            	int vertice =0;
            	for ( String token : tokens ) {
            		if ( i == 0) {
            			vertice = Integer.parseInt(token);
            		} else {
            			edge = Integer.parseInt(token);
            		}
            		if ( i++ != 0) {
            			adjacencyList.setEdge(vertice, edge);
            		}
            	}
            }
            
            /* Prints the adjacency List representing the graph.*/
         /*   System.out.println ("the given Adjacency List for the graph \n");
            for (int i = 1 ; i <= adjacencyList.Adjacency_List.size() ; i++)
            {
                System.out.print(i+"->");
                List<Integer> edgeList = adjacencyList.getEdge(i);
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
            
            while ( adjacencyList.Adjacency_List.size() > 2 ) {
            	int rand1 = random.nextInt(adjacencyList.Adjacency_List.size()) ;
            	Set<Integer> keyset = adjacencyList.Adjacency_List.keySet();
            	int selVertex=0;
            	for ( int i : keyset ) {
            		if ( rand1 == 0) {
            			selVertex=i;
            			break;
            		}
            		rand1--;
            		
            	}
            	
            	List<Integer> sourceVertexEdges = adjacencyList.getEdge(selVertex);
            	//System.out.println("source vertex " + selVertex);
            	
            	int rand = random.nextInt(sourceVertexEdges.size());
            	int selEdge = sourceVertexEdges.get(rand);
            	//sourceVertexEdges.remove(rand);    // remove the selected edge now
            	List<Integer> destVertexEdges = adjacencyList.getEdge(selEdge);
            	//System.out.println("dest vertex " + selEdge);
            	
            	// now first change all the pointers to the new merged node 
            	for ( int i : destVertexEdges ) {
            		if ( i != selVertex && i != selEdge) {
	            		List<Integer> e = adjacencyList.getEdge(i);
	            		int add = 0;
	            		if ( e.contains(selEdge)) {
	            			Iterator it = e.iterator();
	            			while ( it.hasNext()){
	            				int j = (Integer) it.next();
	            				if ( j == selEdge ) {
	            					it.remove(); //  remove(selEdge);
	            					add++;
	            				}
	            			}
	            		}
	            		while ( add > 0 ) {
	            			e.add(selVertex);
	            			add--;
	            		}
            		}
            	}
            	
            	// remove the vertices 
            	//adjacencyList.Adjacency_List.remove(selVertex);
            	adjacencyList.Adjacency_List.remove(selEdge);
            	
            	/*
            	Set<Integer> dup = new HashSet<Integer>();
            	for ( int i : sourceVertexEdges) {
            		if ( i != selVertex && i != selEdge) {
            			dup.add(i);
            		}
            	}
            	for ( int i : destVertexEdges) {
            		if ( i != selVertex && i != selEdge) {
            			dup.add(i);
            		}
            	}
            	sourceVertexEdges.clear();*/
            	List<Integer> dup = new ArrayList<Integer>();
            	for ( int i : destVertexEdges) {
            		if ( i != selVertex && i != selEdge) {
            			dup.add(i);
            		}
            	}
            	for ( int i : sourceVertexEdges) {
            		if ( i != selVertex && i != selEdge) {
            			dup.add(i);
            		}
            	}
            	sourceVertexEdges.clear();
            	for ( int i : dup) {
            		sourceVertexEdges.add(i);
            	}
            	
            	//   Prints the adjacency List representing the graph.
              /*  System.out.println ("the given Adjacency List for the graph \n");
                for (int i : adjacencyList.Adjacency_List.keySet() )
                {
                	
                    System.out.print(i+"->");
                    List<Integer> edgeList = adjacencyList.getEdge(i);
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
                }*/
               
            	
            	
            }
            
            
           //   Prints the adjacency List representing the graph.
            //System.out.println ("the given Adjacency List for the graph \n");
            //for (int i = 1 ; i <= number_of_vertices ; i++)
            int mincut = 0 ;
            for (int i : adjacencyList.Adjacency_List.keySet() )
            {
                //System.out.print(i+"->");
                List<Integer> edgeList = adjacencyList.getEdge(i);
                if ( edgeList.size() > mincut) {
                	mincut = edgeList.size();
                }
                for (int j = 1 ; ; j++ )
                {
                    if (j != edgeList.size())
                    {
                       // System.out.print(edgeList.get(j - 1 )+"->");
                    }else
                    {
                       // System.out.print(edgeList.get(j - 1 ));
                        break;
                    }
                }
                //System.out.println();
                
            }
            s.close();
            System.out.println("mincut is " + mincut);
            return mincut;
            
        } catch(InputMismatchException inputMismatch)
        {
            System.out.println("Error in Input Format. \nFormat : <source index> <destination index>");
        } catch ( FileNotFoundException e ) {
        	
        }
        return -1;
	}
}