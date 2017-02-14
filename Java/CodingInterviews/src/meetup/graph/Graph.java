package meetup.graph;

import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;

import java.util.*;

/**
 * Created by varjunan on 2/11/17.
 */

enum Status {  Unvisited, InProgress,Visited}
public class Graph {

    Set < Vertex> vertices;

    Graph() {
        vertices = new HashSet<Vertex>();
    }

    public static void main ( String[] args) {
        Graph g = new Graph() ;

        Vertex s = new Vertex("s");
        Vertex a = new Vertex("a");
        Vertex b = new Vertex("b");

        Vertex c = new Vertex("c");
        Vertex d = new Vertex("d");

        Vertex e = new Vertex("e");

        g.vertices.add(s);
        g.vertices.add(a);
        g.vertices.add(b);
        g.vertices.add(c);
        g.vertices.add(d);
        g.vertices.add(e);

        s.edges.add(new Edge(a,8));
        a.edges.add(new Edge(b,10));

        s.edges.add(new Edge(b,7));

        b.edges.add(new Edge(c,5));
        c.edges.add(new Edge(d,2));
        s.edges.add(new Edge(e,5));
        e.edges.add(new Edge(c,5));

        //g.DFT(s,b);
        //g.BFT(s,a);

        //for find all the possible paths
        //List<Vertex> l = new ArrayList<>();
        //g.AllPaths(s,c,l);

        //for topological sorting
        Deque<Vertex> stack = new ArrayDeque<>();
        g.TopoSort(g,stack);
        for ( int i=0; i < g.vertices.size(); i++){
            System.out.println(stack.pop().data);
        }

    }

    void TopoSort ( Graph g, Deque<Vertex> stack) {
            for ( Vertex v : g.vertices) {
                if ( v.visited == Status.Visited){
                    continue;
                } else {
                    TopoSortUtil(v,stack);
                }

            }

    }

    void TopoSortUtil ( Vertex v, Deque<Vertex> stack) {
        v.visited = Status.Visited;
        for ( Edge e : v.edges) {
            if ( e.to.visited == Status.Visited){
                continue;
            } else {
                TopoSortUtil(e.to,stack);
            }
        }
        stack.offerFirst(v);

    }

    public void DFT ( Vertex v , Vertex goal ) {
        if ( v.equals(goal)) {
            System.out.println(v.data);
            return ;
        }
        System.out.println(" As we go on - "+ v.data);
        if (  v.visited.equals(Status.Visited)){
            return ;
        } else {
            v.visited = Status.InProgress;
            for (Edge e : v.edges) {
                if (e.to.visited.equals(Status.Unvisited)) {
                    DFT(e.to, goal );
                }
            }
            v.visited = Status.Visited;
            System.out.println(v.data);
        }
    }

    void BFT ( Vertex s, Vertex goal) {
        Queue<Vertex> queue = new ArrayDeque<Vertex>() ;
        queue.clear();
        queue.add(s);
        while (!queue.isEmpty()) {
            Vertex v = queue.remove();

            if ( v.visited == Status.Unvisited){
                System.out.println(v.data);
                if ( v.equals(goal)) {
                    return ;
                }
                v.visited = Status.Visited;
                for ( Edge e : v.edges) {
                    queue.add(e.to);
                }
            }
        }
        System.out.println("Goal not reachable");
    }



    public void AllPaths ( Vertex v , Vertex goal , List<Vertex> list) {
        if ( v.equals(goal)) {
            list.add(v);
            for ( Vertex v1 : list) {
                System.out.print(v1.data);

            }
            System.out.println();
            Vertex root = list.get(0);
            list.clear();
            list.add(root);
            return ;
        }
        System.out.println(" As we go on - "+ v.data);
        if (  v.visited.equals(Status.Visited)){
            return ;
        } else {
            list.add(v);
            v.visited = Status.InProgress;
            for (Edge e : v.edges) {
                    AllPaths(e.to, goal,list );
            }

        }
    }

}
