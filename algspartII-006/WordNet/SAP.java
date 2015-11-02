import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by cc on 31/10/15.
 */
public class SAP {

    private Digraph graph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new NullPointerException();

        graph = new Digraph(G);
    }

    private boolean isValidVertex(int index) {
        if (0 <= index && index <= graph.V() - 1) {
            return true;
        }

        return false;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (!isValidVertex(v) || !isValidVertex(w)) {
            throw new IndexOutOfBoundsException();
        }

        BreadthFirstDirectedPaths bfsVPath = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsWPath = new BreadthFirstDirectedPaths(graph, w);
        int len = Integer.MAX_VALUE;

        for (int i = 0; i < graph.V(); i++) {
            if (bfsVPath.hasPathTo(i) && bfsWPath.hasPathTo(i)) {
                len = Math.min(bfsVPath.distTo(i) + bfsWPath.distTo(i), len);
            }
        }

        if (len == Integer.MAX_VALUE)
            return -1;

        return len;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (!isValidVertex(v) || !isValidVertex(w)) {
            throw new IndexOutOfBoundsException();
        }

        BreadthFirstDirectedPaths bfsVPath = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsWPath = new BreadthFirstDirectedPaths(graph, w);
        int len = Integer.MAX_VALUE;
        int result = -1;

        for (int i = 0; i < graph.V(); i++) {
            if (bfsVPath.hasPathTo(i) && bfsWPath.hasPathTo(i)) {
                if (bfsVPath.distTo(i) + bfsWPath.distTo(i) < len) {
                    len = bfsVPath.distTo(i) + bfsWPath.distTo(i);
                    result = i;
                }

            }
        }

        return result;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new NullPointerException();
        }

        BreadthFirstDirectedPaths bfsVPath = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsWPath = new BreadthFirstDirectedPaths(graph, w);

        int len = Integer.MAX_VALUE;

        for (int i = 0; i < graph.V(); i++) {
            if (bfsVPath.hasPathTo(i) && bfsWPath.hasPathTo(i)) {
                len = Math.min(bfsVPath.distTo(i) + bfsWPath.distTo(i), len);
            }
        }

        if (len == Integer.MAX_VALUE)
            return -1;

        return len;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w ==  null) {
            throw new NullPointerException();
        }

        BreadthFirstDirectedPaths bfsVPath = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsWPath = new BreadthFirstDirectedPaths(graph, w);
        int len = Integer.MAX_VALUE;
        int result = -1;

        for (int i = 0; i < graph.V(); i++) {
            if (bfsVPath.hasPathTo(i) && bfsWPath.hasPathTo(i)) {
                if (bfsVPath.distTo(i) + bfsWPath.distTo(i) < len) {
                    len = bfsVPath.distTo(i) + bfsWPath.distTo(i);
                    result = i;
                }
            }
        }

        return result;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
