import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by cc on 16/10/15.
 */
public class PointSET {

    private TreeSet<Point2D> treeSet;

    public PointSET() {                              // construct an empty set of points
        treeSet = new TreeSet<Point2D>();
    }

    public boolean isEmpty() {                     // is the set empty?
        return treeSet.isEmpty();
    }

    public int size() {                        // number of points in the set
        return  treeSet.size();
    }

    public void insert(Point2D p) {             // add the point to the set (if it is not already in the set)
        if (p == null)
            throw new NullPointerException();

        if (!contains(p)) {
            treeSet.add(p);
        }
    }

    public boolean contains(Point2D p) {           // does the set contain point p?
        if (p == null) {
            throw new NullPointerException();
        }

        return treeSet.contains(p);
    }

    public void draw() {                        // draw all points to standard draw
        if (isEmpty())
            return;

        Iterator<Point2D> iterator = treeSet.iterator();

        while (iterator.hasNext()) {
            Point2D point = iterator.next();
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {            // all points that are inside the rectangle
        if (rect == null)
            throw new NullPointerException();

        final List<Point2D> resultList = new ArrayList<Point2D>();
        Iterator<Point2D> iterator = treeSet.iterator();

        while (iterator.hasNext()) {
            Point2D point = iterator.next();

            if (rect.contains(point)) {
                resultList.add(point);
            }
        }

        return new Iterable<Point2D>() {
            @Override
            public Iterator<Point2D> iterator() {
                return resultList.iterator();
            }
        };
    }

    public Point2D nearest(Point2D p) {            // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) {
            throw new NullPointerException();
        }

        if (isEmpty())
            return null;

        Iterator<Point2D> iterator = treeSet.iterator();
        Point2D resultPoint = null;
        double distance = Double.MAX_VALUE;

        while (iterator.hasNext()) {
            Point2D point = iterator.next();
            if (p.distanceTo(point) < distance) {
                distance = p.distanceTo(point);
                resultPoint = point;
            }
        }

        return resultPoint;
    }

    public static void main(String[] args) {                 // unit testing of the methods (optional)
    }
}
