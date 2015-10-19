import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by cc on 11/10/15.
 */
public class FastCollinearPoints {

    private ArrayList<LineSegment> segmentArrayList;
    private HashMap<String, Boolean> map;

    public FastCollinearPoints(Point[] points) {    // finds all line segments containing 4 or more points
        segmentArrayList = new ArrayList<LineSegment>();
        map = new HashMap<String, Boolean>();
        Point[] originPoints = Arrays.copyOf(points, points.length);
        Arrays.sort(originPoints);

        for (int i = 0; i < originPoints.length; i++) {
            Point origin = originPoints[i];

            if (origin == null)
                throw new NullPointerException();

            Comparator<Point> comparator = origin.slopeOrder();
            Point[] copy = Arrays.copyOfRange(originPoints, i, originPoints.length);

            Arrays.sort(copy, comparator);

            if (copy.length > 1) {
                if (origin.compareTo(copy[1]) == 0)
                    throw new IllegalArgumentException();

                double preSlope = origin.slopeTo(copy[1]);
                int count = 1;
                int start = 1;

                for (int j = 2; j < copy.length; j++) {
                    double curSlope = origin.slopeTo(copy[j]);

                    if (origin.compareTo(copy[j]) == 0)
                        throw new IllegalArgumentException();

                    if (curSlope == preSlope) {
                        count++;
                    } else {
                        if (count >= 3) {
                            boolean flag = true;

                            for (int k = start; k < j - 1; k++) {
                                String key = copy[k].toString() + "," + copy[k + 1].toString();
                                if (map.get(key) != null)
                                    flag = false;
                            }

                            if (flag) {
                                LineSegment seg = new LineSegment(origin, copy[j - 1]);
                                segmentArrayList.add(seg);

                                for (int k = start; k < j - 1; k++) {
                                    String key = copy[k].toString() + "," + copy[k + 1].toString();
                                    map.put(key, true);
                                }
                            }
                        }
                        preSlope = curSlope;
                        count = 1;
                        start = j;
                    }
                }

                if (count >= 3) {
                    boolean flag = true;

                    for (int k = start; k < copy.length - 2; k++) {
                        String key = copy[k].toString() + "," + copy[k + 1].toString();
                        if (map.get(key) != null)
                            flag = false;
                    }

                    if (flag) {
                        LineSegment seg = new LineSegment(origin, copy[copy.length - 1]);
                        segmentArrayList.add(seg);

                        for (int k = start; k < copy.length - 2; k++) {
                            String key = copy[k].toString() + "," + copy[k + 1].toString();
                            map.put(key, true);
                        }

                    }
                }
            }

        }
    }

    public int numberOfSegments() {       // the number of line segments
        return segmentArrayList.size();
    }

    public LineSegment[] segments() {               // the line segments
        LineSegment[] result = new LineSegment[segmentArrayList.size()];

        for (int i = 0; i < segmentArrayList.size(); i++) {
            result[i] = segmentArrayList.get(i);
        }

        return result;
    }

}
