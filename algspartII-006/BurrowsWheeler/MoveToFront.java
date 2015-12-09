import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/**
 * Created by cc on 5/12/15.
 * The solution for Burrows-Wheeler compression algorithm.
 */
public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] alphabet = new char[256];
        for (int i = 0; i < 256; i++) {
            alphabet[i] = (char) (i);
        }

        while (!BinaryStdIn.isEmpty()) {
            char ch = BinaryStdIn.readChar();

            for (int i = 0; i < 256; i++) {
                if (alphabet[i] == ch) {
                    //System.out.println(i);
                    BinaryStdOut.write((byte) i);
                    BinaryStdOut.flush();
                    for (int j = i - 1; j >= 0; j--) {
                        alphabet[j + 1] = alphabet[j];
                    }
                    alphabet[0] = ch;

                    break;
                }
            }
        }
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] alphabet = new char[256];
        for (int i = 0; i < 256; i++) {
            alphabet[i] = (char) (i);
        }

        while (!BinaryStdIn.isEmpty()) {
            char index = BinaryStdIn.readChar();
            char indexChar = alphabet[index];
            BinaryStdOut.write(alphabet[index]);
            BinaryStdOut.flush();

            for (int j = (int) index - 1; j >= 0; j--) {
                alphabet[j + 1] = alphabet[j];
            }

            alphabet[0] = indexChar;
        }
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-"))
            encode();
        else if (args[0].equals("+"))
            decode();
        else
            throw new IllegalArgumentException("args[0] shoule be - or +");
    }
}
