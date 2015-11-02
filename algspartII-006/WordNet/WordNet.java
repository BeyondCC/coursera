import edu.princeton.cs.algs4.Digraph;
import java.util.HashMap;
import java.util.Iterator;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.DirectedCycle;

/**
 * Created by cc on 31/10/15.
 */
public class WordNet {
    private Digraph digraph;
    private HashMap<String, HashMap<Integer, Boolean>> synsetsDict;
    private HashMap<Integer, String> nounsDict;
    private SAP helpSAp;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new NullPointerException();
        }

        In in = new In(synsets);
        synsetsDict = new HashMap<String, HashMap<Integer, Boolean>>();
        nounsDict = new HashMap<Integer, String>();

        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] fields = line.split(",");

            int id = Integer.parseInt(fields[0]);
            String[] nouns = fields[1].split(" ");

            nounsDict.put(id, fields[1]);

            for (String noun : nouns) {
                if (synsetsDict.containsKey(noun)) {
                    HashMap<Integer, Boolean> dict = synsetsDict.get(noun);
                    dict.put(id, true);
                    synsetsDict.put(noun, dict);
                } else {
                    HashMap<Integer, Boolean> dict = new HashMap<Integer, Boolean>();
                    dict.put(id, true);
                    synsetsDict.put(noun, dict);
                }
            }
        }

        digraph = new Digraph(synsetsDict.size());

        in = new In(hypernyms);

        while (in.hasNextLine()) {
            String line = in.readLine();

            String[] fields = line.split(",");
            int firstSetId = Integer.parseInt(fields[0]);

            for (int i = 1; i < fields.length; i++) {
                digraph.addEdge(firstSetId, Integer.parseInt(fields[i]));
            }
        }

        helpSAp = new SAP(digraph);

        if (!isValidRootedDAG()) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isValidRootedDAG() {
        int count = 0;

        for (int i = 0; i < digraph.V(); i++) {
            if (digraph.outdegree(i) == 0 && digraph.indegree(i) != 0) {
                count++;

                if (count > 1)
                    break;
            }
        }

        DirectedCycle cycle = new DirectedCycle(digraph);
        return count == 1 && !cycle.hasCycle();
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return synsetsDict.keySet().iterator();
            }
        };
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new NullPointerException();

        return synsetsDict.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }

        final HashMap<Integer, Boolean> idsOfA = synsetsDict.get(nounA);
        final HashMap<Integer, Boolean> idsOfB = synsetsDict.get(nounB);

        int length = helpSAp.length(new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return idsOfA.keySet().iterator();
            }
        }, new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return idsOfB.keySet().iterator();
            }
        });

        return length;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }

        final HashMap<Integer, Boolean> idsOfA = synsetsDict.get(nounA);
        final HashMap<Integer, Boolean> idsOfB = synsetsDict.get(nounB);

        String res = null;
        int sapId = helpSAp.ancestor(new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return idsOfA.keySet().iterator();
            }
        }, new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return idsOfB.keySet().iterator();
            }
        });

        if (sapId == -1)
            return res;

        res = nounsDict.get(sapId);

        return res;
    }

    // do unit testing of this class
    public static void main(String[] args) {
    }
}
