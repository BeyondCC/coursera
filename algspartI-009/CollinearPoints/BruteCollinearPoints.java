import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by cc on 11/10/15.
 */
public class BruteCollinearPoints {

    private ArrayList<LineSegment> segmentList;

    public BruteCollinearPoints(Point[] points) {   // finds all line segments containing 4 points

        segmentList = new ArrayList<LineSegment>();
        Point[] copy = Arrays.copyOf(points, points.length);

        int len = copy.length;

        Arrays.sort(copy);

        for (int i = 0; i < len; i++) {
            if (copy[i] == null)
                throw new NullPointerException();

            for (int j = i + 1; j < len; j++) {
                if (copy[j] == null)
                    throw new NullPointerException();
                if (copy[i].compareTo(copy[j]) == 0)
                    throw new IllegalArgumentException();

                for (int k = j + 1; k < len; k++) {
                    if (copy[k] == null)
                        throw new NullPointerException();
                    if (copy[i].compareTo(copy[k]) == 0 || copy[j].compareTo(copy[k]) == 0)
                        throw new IllegalArgumentException();

                    for (int l = k + 1; l < len; l++) {
                        if (copy[l] == null)
                            throw new NullPointerException();

                        if (copy[i].compareTo(copy[l]) == 0 || copy[j].compareTo(copy[l]) == 0
                                || copy[k].compareTo(copy[l]) == 0)
                            throw new IllegalArgumentException();

                        if ((copy[i].slopeTo(copy[j]) == copy[i].slopeTo(copy[k]))
                                && (copy[i].slopeTo(copy[k]) == copy[i].slopeTo(copy[l]))) {
                            LineSegment seg = new LineSegment(copy[i], copy[l]);
                            segmentList.add(seg);
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {       // the number of line segments
        return segmentList.size();
    }

    public LineSegment[] segments() {               // the line segments
        LineSegment[] seg = new LineSegment[segmentList.size()];

        for (int i = 0; i < segmentList.size(); i++) {
            seg[i] = segmentList.get(i);
        }

        return seg;
    }
}
