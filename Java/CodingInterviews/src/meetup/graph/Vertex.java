package meetup.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by varjunan on 2/11/17.
 */
public class Vertex {
    String data;
    List<Edge> edges;
    Status visited;

    Vertex( String data )  {
        edges = new ArrayList<Edge>();
        visited = Status.Unvisited;
        this.data = data ;
    }
}
