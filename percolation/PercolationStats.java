import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Created by cc on 10/10/15.
 */
public class PercolationStats {
    private int times;
    private int randomSeed;
    private double[] scores;
    private Percolation[] percolations;

    public PercolationStats(int N, int T) { // perform T independent experiments on an N-by-N grid
        if (N <= 0 || T <= 0)
            throw new IllegalArgumentException();

        percolations = new Percolation[T];

        for (int i = 0; i < percolations.length; i++) {
            percolations[i] = new Percolation(N);
        }
        times = T;
        randomSeed = N;
        scores = new double[T];
    }

    private void calculate() {
        int index = 0;

        while (index < times) {
            calculateOnce(percolations[index], index);
            index++;
        }
    }

    private void calculateOnce(Percolation percolation, int index) {
        int seed = 0;
        double count = 0;

        while (true) {
            int i = StdRandom.uniform(randomSeed) + 1;
            int j = StdRandom.uniform(randomSeed) + 1;

            if (!percolation.isOpen(i, j)) {
                percolation.open(i, j);
                count++;

                if (percolation.percolates()) {
                    break;
                }
            }
        }

        scores[index] = count / (randomSeed * randomSeed);
    }

    public double mean() { // sample mean of percolation threshold
        return StdStats.mean(scores);
    }

    public double stddev() { // sample standard deviation of percolation threshold
        return StdStats.stddev(scores);
    }

    public double confidenceLo() {             // low  endpoint of 95% confidence interval
        double m = mean();
        double dev = stddev();

        return m - (1.96 * dev) / (Math.sqrt(times));
    }

    public double confidenceHi() {          // high endpoint of 95% confidence interval
        double m = mean();
        double dev = stddev();

        return m + (1.96 * dev) / (Math.sqrt(times));
    }

    public static void main(String[] args) {   // test client (described below)
        Integer n = Integer.valueOf(args[0]);
        Integer times = Integer.valueOf(args[1]);

        PercolationStats percolationStats = new PercolationStats(n.intValue(), times.intValue());
        percolationStats.calculate();

        StdOut.println("mean                = " + percolationStats.mean());
        StdOut.println("stddev              = " + percolationStats.stddev());
        StdOut.println("95% confidence interval = " + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi());
    }
}
