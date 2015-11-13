import java.awt.Color;
import edu.princeton.cs.algs4.Picture;
/**
 * Created by cc on 11/11/15.
 *
 * Class SeamCarver contains methods for performing
 * content-aware image resizing on a Picture object
 */
public class SeamCarver {
    private int width;
    private int height;
    private double[][] matrix;  //store the energy of a pixel
    private int[][] colorInfo;   //store the color info instead of int

    public SeamCarver(Picture picture) {              // create a seam carver object based on the given picture
        if (picture == null)
            throw new NullPointerException();

        this.width = picture.width();
        this.height = picture.height();

        matrix = new double[height][width];
        colorInfo = new int[height][width];

        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                colorInfo[i][j] = picture.get(j, i).getRGB();
            }
        }

        initMatrix();
    }

    private void initMatrix() {
        for (int i = 0; i < height; i++) {
            if (i == 0 || i == height - 1) {
                for (int j = 0; j < width; j++) {
                    matrix[i][j] = 1000;
                }
            } else {
                for (int j = 0; j < width; j++) {
                    if (j == 0 || j == width - 1) {
                        matrix[i][j] = 1000;
                    } else {
                        matrix[i][j] = energy(j, i);
                    }
                }
            }
        }
    }

    private double[][] initWeight() {
        double[][] weight = new double[height][width];
        for (int i = 0; i < height; i++) {
            if (i == 0 || i == height - 1) {
                for (int j = 0; j < width; j++) {
                    weight[i][j] = 1000;
                }
            } else {
                for (int j = 0; j < width; j++) {
                    if (j == 0 || j == width - 1) {
                        weight[i][j] = 1000;
                    } else {
                        weight[i][j] = 0;
                    }
                }
            }
        }

        return weight;
    }

    public Picture picture() {                         // current picture
        Picture picture = new Picture(width, height);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                picture.set(i, j, new Color(colorInfo[j][i]));
            }
        }

        return picture;
    }

    public int width() {                           // width of current picture
        return width;
    }

    public int height() {                          // height of current picture
        return height;
    }

    public double energy(int x, int y) { // energy of pixel at column x and row y
        if (x < 0 || x >= width || y < 0 || y >= height)
            throw new IndexOutOfBoundsException();

        if (x == 0 || x == width - 1 || y == 0 || y == height - 1)
            return 1000;

        double xEnergy = createXEnergy(x, y);
        double yEnergy = createYEnergy(x, y);

        return Math.sqrt(xEnergy + yEnergy);
    }

    private void swapHeightWithWidth() {
        int tmp = width;
        width = height;
        height = tmp;
    }

    private double[][] tranposeMatrix(double[][] array) {
        if (array == null)
            return null;

        int height = height();
        int width = width();

        double[][] trans = new double[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                trans[i][j] = array[j][i];
            }
        }

        return trans;
    }

    private int[][] tranposeMatrix(int[][] array) {
        if (array == null)
            return null;

        int height = height();
        int width = width();

        int[][] trans = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                trans[i][j] = array[j][i];
            }
        }

        return trans;
    }

    private double createXEnergy(int x, int y) {
        Color leftColor = new Color(colorInfo[y][x - 1]);
        Color rightColor = new Color(colorInfo[y][x + 1]);

        double red = Math.pow(leftColor.getRed() - rightColor.getRed(), 2);
        double green = Math.pow(leftColor.getGreen() - rightColor.getGreen(), 2);
        double blue = Math.pow(leftColor.getBlue() - rightColor.getBlue(), 2);

        return red + green + blue;
    }

    private double createYEnergy(int x, int y) {
        Color blewColor = new Color(colorInfo[y - 1][x]);
        Color upperColor = new Color(colorInfo[y + 1][x]);

        double red = Math.pow(blewColor.getRed() - upperColor.getRed(), 2);
        double green = Math.pow(blewColor.getGreen() - upperColor.getGreen(), 2);
        double blue = Math.pow(blewColor.getBlue() - upperColor.getBlue(), 2);

        return red + green + blue;
    }

    public int[] findHorizontalSeam() {  // sequence of indices for horizontal seam
        matrix = tranposeMatrix(matrix);
        colorInfo = tranposeMatrix(colorInfo);
        swapHeightWithWidth();

        int[] result = findVerticalSeam();

        matrix = tranposeMatrix(matrix);
        colorInfo = tranposeMatrix(colorInfo);
        swapHeightWithWidth();

        return result;
    }

    public int[] findVerticalSeam() {   // sequence of indices for vertical seam
        int[][] distTo = new int[height][width];
        double[][] weight = initWeight();

        initMatrix();

        return findVerticalSeamHelper(distTo, weight);
    }

    private int[] findVerticalSeamHelper(int[][] distTo, double[][] weight) {
        for (int i = 1; i < height; i++) {
            for (int j = 0; j < width; j++) {
                distTo[i][j] = getShortestPath(i, j, weight);
            }
        }

        int index = 0;
        double minWeight = weight[height - 1][0];
        for (int col = 1; col < width - 1; col++) {
            if (weight[height - 1][col] < minWeight) {
                index = col;
                minWeight = weight[height -1][col];
            }
        }

        int[] result = new int[height];
        int preIndex = index;
        result[height - 1] = index;
        for (int i = height - 2; i >= 0; i--) {
            result[i] = distTo[i + 1][preIndex];
            preIndex = distTo[i + 1][preIndex];
        }

        return result;
    }

    private int getShortestPath(int row, int col, double[][] weight) {
        int result = 0;
        double minWeight = 0;

        if (col == 0) {
            if (width == 1) {
                result = col;
                minWeight = weight[row - 1][col];
            } else if (weight[row - 1][col] <= weight[row - 1][col + 1]) {
                result = col;
                minWeight = weight[row - 1][col];
            } else {
                result = col + 1;
                minWeight = weight[row - 1][col + 1];
            }
        } else if (col == width - 1) {
            if (weight[row - 1][col] <= weight[row - 1][col - 1]) {
                result = col;
                minWeight = weight[row - 1][col];
            } else {
                result = col - 1;
                minWeight = weight[row - 1][col - 1];
            }
        } else if (weight[row - 1][col - 1] <= weight[row - 1][col]) {
            if (weight[row - 1][col -1] <= weight[row - 1][col + 1]) {
                result = col - 1;
                minWeight = weight[row - 1][col -1];
            } else {
                result = col + 1;
                minWeight = weight[row - 1][col + 1];
            }
        } else {
            if (weight[row - 1][col] <= weight[row - 1][col + 1]) {
                result = col;
                minWeight = weight[row - 1][col];
            } else {
                result = col + 1;
                minWeight = weight[row - 1][col + 1];
            }
        }

        weight[row][col] = matrix[row][col] + minWeight;
        return result;
    }

    public void removeHorizontalSeam(int[] seam) {   // remove horizontal seam from current picture
        if (seam.length != width)
            throw new IllegalArgumentException();

        if (height == 1)
            throw new IllegalArgumentException();

        colorInfo = tranposeMatrix(colorInfo);
        matrix = tranposeMatrix(matrix);

        swapHeightWithWidth();
        removeVerticalSeam(seam);

        colorInfo = tranposeMatrix(colorInfo);
        matrix = tranposeMatrix(matrix);

        swapHeightWithWidth();
    }

    public void removeVerticalSeam(int[] seam) {    // remove vertical seam from current picture
        if (seam == null)
            throw new NullPointerException();

        if (seam.length != height)
            throw new IllegalArgumentException("seam length is wrong");

        if (width == 1)
            throw new IllegalArgumentException();

        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] > width - 1)
                throw new IllegalArgumentException();

            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1)
                throw new IllegalArgumentException();

            System.arraycopy(colorInfo[i], seam[i] + 1, colorInfo[i], seam[i], width - seam[i] - 1);
            System.arraycopy(matrix[i], seam[i] + 1, matrix[i], seam[i], width - seam[i] - 1);

        }

        width--;
    }
}
