import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/**
 * Created by cc on 5/12/15.
 */
public class BurrowsWheeler {
    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = new StringBuilder();
        int first = 0;

        while (!BinaryStdIn.isEmpty()) {
            sb.append(BinaryStdIn.readChar());
        }

        CircularSuffixArray suffixArray = new CircularSuffixArray(sb.toString());
        int length = suffixArray.length();
        for (int i = 0; i < length; i++) {
            if (suffixArray.index(i) == 0) {
                first = i;
            }

            result.append(sb.charAt((suffixArray.index(i) - 1 + length) % length));
        }

        BinaryStdOut.write(first);
        BinaryStdOut.flush();
        BinaryStdOut.write(result.toString());
        BinaryStdOut.flush();
    }

    // origin j <=> sorted i  => next[i] = origin j + 1 appear the index in sorted array
    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    // just use key-indexed string sort that next[i] is the corresponding next[i]
    public static void decode() {
        int first = BinaryStdIn.readInt();
        String sb = BinaryStdIn.readString();
        int lengh = sb.length();
        int[] next = new int[sb.length()];
        int[] count = new int[256 + 1];

        for (int i = 0; i < lengh; i++) {
            count[sb.charAt(i) + 1]++;
        }

        for (int i = 0; i < 256; i++) {
            count[i + 1] += count[i];
        }

        for (int i = 0; i < lengh; i++) {
            next[count[sb.charAt(i)]++] = i;
        }

        for (int i = next[first], c = 0; c < lengh; i = next[i], c++)
            BinaryStdOut.write(sb.charAt(i));
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        if (args[0].equals("-"))
            encode();
        else if (args[0].equals("+"))
            decode();
    }
}
