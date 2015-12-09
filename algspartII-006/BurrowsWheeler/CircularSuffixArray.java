/**
 * Created by cc on 5/12/15.
 * use MSD or 3-way quicksort to sort the string
 * while the length of string is short just use insert sort to improve the performance
 */
public class CircularSuffixArray {
    private int[] index;
    private String s;
    private int N;
    private static final int R      = 256;   // extended ASCII alphabet size
    private static final int CUTOFF =  15;   // cutoff to insertion sort 15

    // return dth character of s, -1 if d = length of string
    private int charAt(int i, int d) {
        if (d > s.length())
            return -1;

        return s.charAt((i + d) % N);
    }

    // sort from a[lo] to a[hi], starting at the dth character
    private void sort(int lo, int hi, int d, int[] aux) {

        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(lo, hi, d);
            return;
        }

        // compute frequency counts
        int[] count = new int[R+2];
        for (int i = lo; i <= hi; i++) {
            int c = charAt(index[i], d);
            count[c+2]++;
        }

        // transform counts to indicies
        for (int r = 0; r < R+1; r++)
            count[r+1] += count[r];

        // distribute
        for (int i = lo; i <= hi; i++) {
            int c = charAt(index[i], d);
            aux[count[c+1]++] = index[i];
        }

        // copy back
        for (int i = lo; i <= hi; i++)
            index[i] = aux[i - lo];

        // recursively sort for each character
        for (int r = 0; r < R; r++)
            sort(lo + count[r], lo + count[r+1] - 1, d+1, aux);
    }

    // return dth character of s, -1 if d = length of string
    private void insertion(int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(index[j], index[j-1], d); j--)
                exch(j, j-1);
    }

    // exchange i and j
    private void exch(int i, int j) {
        int temp = index[i];
        index[i] = index[j];
        index[j] = temp;
    }

    // is i-th string less than j-th string, starting at character d
    private boolean less(int i, int j, int d) {
        for (int x = d; x < N; x++)
            if (charAt(i, x) != charAt(j, x))
                return charAt(i, x) < charAt(j, x);

        return false;
    }

    public CircularSuffixArray(String s) {
        this.s = s;
        N = s.length();

        index = new int[N];
        for(int i = 0; i < N; i++) index[i] = i;

        int[] aux = new int[N];
        sort(0, N-1, 0, aux);
    }

    public int length() { return s.length(); }
    public int index(int i) { return index[i]; }

    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < csa.length(); i++)
            System.out.println(csa.index(i));
    }
}