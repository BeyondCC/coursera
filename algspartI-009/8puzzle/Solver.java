import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.Comparator;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

/**
 * Created by cc on 14/10/15.
 * 用两个最小堆来判断puzzle是否可解
 * 其中twin为任意交换两个块得到的puzzle
 */
public class Solver {
    private MinPQ<TreeNode> priorityQueue;
    private MinPQ<TreeNode> twinPriorityQueue;
    private TreeNode currentNode;
    private TreeNode twinCurrentNode;
    private Comparator<TreeNode> comparator;

    private class TreeNode {
        private Board current;
        private TreeNode preBoard;
        private int moves;

        public TreeNode(Board cur, TreeNode preBoard, int moves) {
            this.current = cur;
            this.preBoard = preBoard;
            this.moves = moves;
        }

        public Board getCurrent() {
            return current;
        }

        public TreeNode getPreBoard() {
            return preBoard;
        }

        public int getMoves() {
            return moves;
        }
    }

    public Solver(final Board initial) {          // find a solution to the initial board (using the A* algorithm)
        currentNode = new TreeNode(initial, null, 0);
        twinCurrentNode = new TreeNode(initial.twin(), null, 0);

        comparator = new Comparator<TreeNode>() {
            @Override
            public int compare(TreeNode o1, TreeNode o2) {
                if (o1.getCurrent().manhattan() + o1.getMoves() == o2.getCurrent().manhattan() + o2.getMoves()) {
                    return o1.getCurrent().manhattan() - o2.getCurrent().manhattan();
                }

                if (o1.getCurrent().manhattan() + o1.getMoves() < o2.getCurrent().manhattan() + o2.getMoves())
                    return -1;

                return 1;
            }
        };

        priorityQueue = new MinPQ<Solver.TreeNode>(comparator);
        twinPriorityQueue = new MinPQ<TreeNode>(comparator);

        while (!currentNode.getCurrent().isGoal() && !twinCurrentNode.getCurrent().isGoal()) {
            for (Board board : currentNode.getCurrent().neighbors()) {
                TreeNode node = new TreeNode(board, currentNode, currentNode.getMoves() + 1);

                if (currentNode.getPreBoard() != null && !node.getCurrent().equals(currentNode.getPreBoard().getCurrent())) {
                    priorityQueue.insert(node);
                    //StdOut.println(node.getCurrent());
                } else if (currentNode.getPreBoard() == null) {
                    priorityQueue.insert(node);
                    //StdOut.println(node.getCurrent());
                }
            }

            if (!priorityQueue.isEmpty()) {
                //StdOut.println("del the node");
                currentNode = priorityQueue.delMin();
                //StdOut.println(currentNode.getCurrent());
            }
            else
                break;

            for (Board board : twinCurrentNode.getCurrent().neighbors()) {
                TreeNode node = new TreeNode(board, twinCurrentNode, twinCurrentNode.getMoves() + 1);

                if (twinCurrentNode.getPreBoard() != null && node.getCurrent() != twinCurrentNode.getPreBoard().getCurrent()) {
                    twinPriorityQueue.insert(node);
                    //StdOut.println(node.getCurrent());
                } else if (twinCurrentNode.getPreBoard() == null) {
                    twinPriorityQueue.insert(node);
                    //StdOut.println(node.getCurrent());
                }
            }

            if (!twinPriorityQueue.isEmpty()) {
                //StdOut.println("del the node");
                twinCurrentNode = twinPriorityQueue.delMin();
                //StdOut.println(currentNode.getCurrent());
                //twinCountOfMove++;
            }
            else
                break;
        }


    }

    public boolean isSolvable() {           // is the initial board solvable?
        if (twinCurrentNode.getCurrent().isGoal())
            return false;

        return currentNode.getCurrent().isGoal();
    }

    public int moves() {                    // min number of moves to solve initial board; -1 if unsolvable
        if (isSolvable())
            return currentNode.getMoves();

        return -1;
    }

    public Iterable<Board> solution() {     // sequence of boards in a shortest solution; null if unsolvable
        if (isSolvable()) {
            final List<Board> result = new LinkedList<Board>();

            TreeNode visit = currentNode;
            while (visit != null) {
                result.add(0, visit.getCurrent());
                visit = visit.getPreBoard();
            }

            return new Iterable<Board>() {
                @Override
                public Iterator<Board> iterator() {
                    return result.iterator();
                }
            };
        } else {
            return null;
        }

    }

    public static void main(String[] args) { // solve a slider puzzle (given below)
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
