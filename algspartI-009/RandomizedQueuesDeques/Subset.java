import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

/**
 * Created by cc on 11/10/15.
 */
public class Subset {
    public static void main(String[] args) {
        RandomizedQueue rq = new RandomizedQueue();
        int k = Integer.parseInt(args[0]);
        while (!StdIn.isEmpty())
            rq.enqueue(StdIn.readString());

        for (int i = 0; i < k; i++)
            StdOut.println(rq.dequeue());
    }

}
