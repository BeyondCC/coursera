import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by cc on 31/10/15.
 */
public class Outcast {
    private WordNet wordnet;

    public Outcast(WordNet wordnet) {        // constructor takes a WordNet object
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns) {  // given an array of WordNet nouns, return an outcast
        if (nouns == null)
            throw new NullPointerException();

        double maxLen = Double.MIN_VALUE;
        String res = null;

        for (int i = 0; i < nouns.length; i++) {
            double local = 0;
            for (int j = 0; j < nouns.length; j++) {
                local += wordnet.distance(nouns[i], nouns[j]);
            }

            if (maxLen < local) {
                maxLen = local;
                res = nouns[i];
            }
        }

        return res;
    }

    public static void main(String[] args) { // see test client below
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
