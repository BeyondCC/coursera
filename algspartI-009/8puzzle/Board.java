import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by cc on 12/10/15.
 */
public class Board {
    private int[][] arr;
    private int dimen;

    public Board(int[][] blocks) {         // construct a board from an N-by-N array of blocks
        this.dimen = blocks.length;

        arr = Arrays.copyOf(blocks, this.dimen);

        for (int i = 0; i < arr.length; i++) {
            arr[i] = Arrays.copyOf(blocks[i], blocks[i].length);
        }
    }

    // (where blocks[i][j] = block in row i, column j)
    public int dimension() {                // board dimension N
        return this.dimen;
    }

    public int hamming() {                  // number of blocks out of place
        int result = 0;

        for (int i = 0; i < this.dimen; i++) {
            int[] row = arr[i];
            for (int j = 0; j < row.length; j++) {
                if (arr[i][j] != 0) {
                    int goalRow = (arr[i][j] - 1) / this.dimen;
                    int goalCol = (arr[i][j] - 1) % this.dimen;

                    if (goalRow != i || goalCol != j)
                        result += 1;
                }
            }
        }

        return result;
    }

    public int manhattan() {                // sum of Manhattan distances between blocks and goal
        int result = 0;

        for (int i = 0; i < this.dimen; i++) {
            int[] row = this.arr[i];
            for (int j = 0; j < row.length; j++) {
                if (arr[i][j] != 0) {
                    int goalRow = (arr[i][j] - 1) / this.dimen;
                    int goalCol = (arr[i][j] - 1) % this.dimen;

                    result = result + Math.abs(goalRow - i) + Math.abs(goalCol - j);
                }
            }
        }

        return result;
    }

    public boolean isGoal() {               // is this board the goal board?
        boolean res = false;

        if (manhattan() == 0)
            res = true;

        return res;
    }

    public Board twin() {                  // a board that is obtained by exchanging any pair of blocks
        int[][] copy = copyArray();

        if (copy[0][0] != 0 && copy[0][1] != 0) {
            int tmp = copy[0][0];
            copy[0][0] = copy[0][1];
            copy[0][1] = tmp;
        } else {
            int tmp = copy[1][0];
            copy[1][0] = copy[1][1];
            copy[1][1] = tmp;
        }

        Board board = new Board(copy);
        return board;
    }

    public boolean equals(Object y) {       // does this board equal y?
        if (y == null)
            return false;

        if (this == y)
            return true;

        return toString().equals(y.toString());
    }

    public Iterable<Board> neighbors() {    // all neighboring boards
        final List<Board> neighborBoards = new ArrayList<Board>();

        for (int i = 0; i < this.dimen; i++) {
            int[] row = arr[i];
            for (int j = 0; j < row.length; j++) {
                if (arr[i][j] == 0) {
                   // StdOut.println("neightbour:" + i + " " + j);
                    swap(neighborBoards, i, j, i, j - 1);
                    swap(neighborBoards, i, j, i, j + 1);
                    swap(neighborBoards, i, j, i - 1, j);
                    swap(neighborBoards, i, j, i + 1, j);
                }
            }
        }

        return new Iterable<Board>() {
            @Override
            public Iterator<Board> iterator() {
                return neighborBoards.iterator();
            }
        };
    }

    private int[][] copyArray() {
        if (arr.length > 0) {
            int[][] copy = new int[arr.length][arr[0].length];

            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[0].length; j++) {
                    copy[i][j] = arr[i][j];
                }
            }

            return copy;
        }

        return null;
    }

    private void swap(List<Board> neigiborBoards, int srcRow, int srcCol, int dstRow, int dstCol) {
        int[][] copy = copyArray();
        //StdOut.println("swap" + srcRow + " " + srcCol + " " + dstRow + " " + dstCol);

        if (dstRow >= 0 && dstRow < dimen && dstCol >= 0 && dstCol < dimen) {
            int tmp = copy[srcRow][srcCol];
            copy[srcRow][srcCol] = copy[dstRow][dstCol];
            copy[dstRow][dstCol] = tmp;

            Board board = new Board(copy);

            //StdOut.println(board.toString());
            neigiborBoards.add(board);
        }
    }

    public String toString() {              // string representation of this board (in the output format specified below)
        StringBuffer sb = new StringBuffer();
        sb.append("\n");
        sb.append(this.dimen);
        sb.append("\n");

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                sb.append(arr[i][j] + " ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public static void main(String[] args) { // unit tests (not graded)
        int[][] arr = new int[2][2];
        arr[0][0] = 3;
        arr[0][1] = 0;
        arr[1][0] = 2;
        arr[1][1] = 1;

        Board board  = new Board(arr);

        StdOut.println(board);
        StdOut.println(board.twin());
    }
}
