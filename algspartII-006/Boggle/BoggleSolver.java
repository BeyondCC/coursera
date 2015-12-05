import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;
import java.util.HashSet;

/**
 * Created by cc on 3/12/15.
 */
public class BoggleSolver {
    private MyTrie<Character> tries;
    private int rows;
    private int cols;
    private HashSet<String> validWords;
    private boolean[][] visited;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null)
            throw new NullPointerException();

        tries = new MyTrie<Character>();
        validWords = new HashSet<String>(1024);
        for (String dict : dictionary) {
            tries.put(dict, '0');
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null)
            throw new NullPointerException();

        rows = board.rows();
        cols = board.cols();
        visited = new boolean[rows][cols];
        validWords = new HashSet<String>(1024);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                //System.out.println("begin:" + i + " " + j);
                dfs(board, i, j, new StringBuilder(), null);

                resetVisited();
            }
        }

        System.out.println(validWords.size());
        return validWords;
    }

    private void resetVisited() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                visited[i][j] = false;
            }
        }
    }

    private void dfs(BoggleBoard board, int row, int col, StringBuilder sb, Node node) {
        if (!(0 <= row && row < rows && 0 <= col && col < cols && !visited[row][col]))
            return;

        Node nextNode = null;
        char ch = board.getLetter(row, col);

        if (sb.length() == 0) {
            sb.append(ch);
            nextNode = tries.getPrefixNode(sb.toString());
        } else {
            sb.append(ch);
            nextNode = node.next[ch - 'A'];
        }

        if (ch == 'Q') {
            if (nextNode != null)
                nextNode = nextNode.next['U' - 'A'];
            sb.append('U');
        }

        if (nextNode != null) {
            visited[row][col] = true;
            if (nextNode.val != null) {
                if (sb.length() > 2)
                    validWords.add(sb.toString());
            }
            dfs(board, row, col + 1, sb, nextNode); // right
            dfs(board, row - 1, col - 1, sb, nextNode); // left up
            dfs(board, row, col - 1, sb, nextNode); // left
            dfs(board, row + 1, col - 1, sb, nextNode); // left down
            dfs(board, row - 1, col, sb, nextNode); // up
            dfs(board, row + 1, col, sb, nextNode); // down
            dfs(board, row - 1, col + 1, sb, nextNode); // right up
            dfs(board, row + 1, col + 1, sb, nextNode);  // right down
            //need to add
            //key to any path
            visited[row][col] = false;
        }

        sb.deleteCharAt(sb.length() - 1);
        if (ch == 'Q')
            sb.deleteCharAt(sb.length() - 1);
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int score = 0;
        if (!tries.contains(word))
            return 0;

        int length = word.length();

        switch (length) {
            case 0:
            case 1:
            case 2:
                score = 0;
                break;
            case 3:
            case 4:
                score = 1;
                break;
            case 5:
                score = 2;
                break;
            case 6:
                score = 3;
                break;
            case 7:
                score = 5;
                break;
            default:
                score = 11;
        }
        return score;
    }

    private  class Node {
        private Object val;
        private Node[] next = new Node[26];
    }

    private  class MyTrie<Value> {
        private  final int R = 26;        // extended ASCII

        private Node root;      // root of trie
        private int N;          // number of keys in trie

        // R-way trie node

        /**
         * Initializes an empty string symbol table.
         */
        public MyTrie() {
        }


        /**
         * Returns the value associated with the given key.
         * @param key the key
         * @return the value associated with the given key if the key is in the symbol table
         *     and <tt>null</tt> if the key is not in the symbol table
         * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
         */
        public Value get(String key) {
            Node x = get(root, key, 0);
            if (x == null) return null;
            return (Value) x.val;
        }

        public Node getPrefixNodeNonRecur(String key) {
            Node node = root;

            int index = 0;
            while (index < key.length() && node != null) {
                node = node.next[key.charAt(index) - 'A'];
                index++;
            }

            return node;
        }

        public Node getPrefixNode(String key) {
            Node x = getPrefixNodeNonRecur(key);

            return x;
        }

        /**
         * Does this symbol table contain the given key?
         * @param key the key
         * @return <tt>true</tt> if this symbol table contains <tt>key</tt> and
         *     <tt>false</tt> otherwise
         * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
         */
        public boolean contains(String key) {
            return get(key) != null;
        }

        private Node get(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) return x;
            char c = key.charAt(d);
            return get(x.next[c - 'A'], key, d+1);
        }

        /**
         * Inserts the key-value pair into the symbol table, overwriting the old value
         * with the new value if the key is already in the symbol table.
         * If the value is <tt>null</tt>, this effectively deletes the key from the symbol table.
         * @param key the key
         * @param val the value
         * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
         */
        public void put(String key, Value val) {
            if (val == null) delete(key);
            else root = put(root, key, val, 0);
        }

        private Node put(Node x, String key, Value val, int d) {
            if (x == null) x = new Node();
            if (d == key.length()) {
                if (x.val == null) N++;
                x.val = val;
                return x;
            }
            char c = key.charAt(d);
            x.next[c - 'A'] = put(x.next[c - 'A'], key, val, d+1);
            return x;
        }

        /**
         * Returns the number of key-value pairs in this symbol table.
         * @return the number of key-value pairs in this symbol table
         */
        public int size() {
            return N;
        }

        /**
         * Is this symbol table empty?
         * @return <tt>true</tt> if this symbol table is empty and <tt>false</tt> otherwise
         */
        public boolean isEmpty() {
            return size() == 0;
        }

        /**
         * Returns all keys in the symbol table as an <tt>Iterable</tt>.
         * To iterate over all of the keys in the symbol table named <tt>st</tt>,
         * use the foreach notation: <tt>for (Key key : st.keys())</tt>.
         * @return all keys in the sybol table as an <tt>Iterable</tt>
         */
        public Iterable<String> keys() {
            return keysWithPrefix("");
        }

        /**
         * Returns all of the keys in the set that start with <tt>prefix</tt>.
         * @param prefix the prefix
         * @return all of the keys in the set that start with <tt>prefix</tt>,
         *     as an iterable
         */
        public Iterable<String> keysWithPrefix(String prefix) {
            Queue<String> results = new Queue<String>();
            Node x = get(root, prefix, 0);
            collect(x, new StringBuilder(prefix), results);
            return results;
        }

        private void collect(Node x, StringBuilder prefix, Queue<String> results) {
            if (x == null) return;
            if (x.val != null) results.enqueue(prefix.toString());
            for (char c = 0; c < R; c++) {
                prefix.append(c);
                collect(x.next[c], prefix, results);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }

        /**
         * Returns all of the keys in the symbol table that match <tt>pattern</tt>,
         * where . symbol is treated as a wildcard character.
         * @param pattern the pattern
         * @return all of the keys in the symbol table that match <tt>pattern</tt>,
         *     as an iterable, where . is treated as a wildcard character.
         */
        public Iterable<String> keysThatMatch(String pattern) {
            Queue<String> results = new Queue<String>();
            collect(root, new StringBuilder(), pattern, results);
            return results;
        }

        private void collect(Node x, StringBuilder prefix, String pattern, Queue<String> results) {
            if (x == null) return;
            int d = prefix.length();
            if (d == pattern.length() && x.val != null)
                results.enqueue(prefix.toString());
            if (d == pattern.length())
                return;
            char c = pattern.charAt(d);
            if (c == '.') {
                for (char ch = 0; ch < R; ch++) {
                    prefix.append(ch);
                    collect(x.next[ch], prefix, pattern, results);
                    prefix.deleteCharAt(prefix.length() - 1);
                }
            }
            else {
                prefix.append(c);
                collect(x.next[c - 'A'], prefix, pattern, results);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }

        /**
         * Returns the string in the symbol table that is the longest prefix of <tt>query</tt>,
         * or <tt>null</tt>, if no such string.
         * @param query the query string
         * @return the string in the symbol table that is the longest prefix of <tt>query</tt>,
         *     or <tt>null</tt> if no such string
         * @throws NullPointerException if <tt>query</tt> is <tt>null</tt>
         */
        public String longestPrefixOf(String query) {
            int length = longestPrefixOf(root, query, 0, -1);
            if (length == -1) return null;
            else return query.substring(0, length);
        }

        // returns the length of the longest string key in the subtrie
        // rooted at x that is a prefix of the query string,
        // assuming the first d character match and we have already
        // found a prefix match of given length (-1 if no such match)
        private int longestPrefixOf(Node x, String query, int d, int length) {
            if (x == null) return length;
            if (x.val != null) length = d;
            if (d == query.length()) return length;
            char c = query.charAt(d);
            return longestPrefixOf(x.next[c], query, d+1, length);
        }

        /**
         * Removes the key from the set if the key is present.
         * @param key the key
         * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
         */
        public void delete(String key) {
            root = delete(root, key, 0);
        }

        private Node delete(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) {
                if (x.val != null) N--;
                x.val = null;
            }
            else {
                char c = key.charAt(d);
                x.next[c] = delete(x.next[c], key, d+1);
            }

            // remove subtrie rooted at x if it is completely empty
            if (x.val != null) return x;
            for (int c = 0; c < R; c++)
                if (x.next[c] != null)
                    return x;
            return null;
        }
    }


    public static void main(String[] args)
    {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();

        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board))
        {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
