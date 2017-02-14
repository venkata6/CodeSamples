package meetup.graph;

/**
 * Created by varjunan on 2/11/17.
 */
public class Edge {
    Vertex to;
    int weight;

    Edge(Vertex t, int weight) {
        this.to = t;
        this.weight = weight;

    }
}

