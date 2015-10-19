import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by cc on 19/10/15.
 */
public class KdTree {

    // node can be point left, right and rect for optional
    private class Node {
        private Point2D point;
        private Node left;
        private Node right;

        public Node(Point2D point) {
            this.point = point;
            this.left = null;
            this.right = null;
        }

        public Point2D point() {
            return this.point;
        }

        public Node left() {
            return this.left;
        }

        public Node right() {
            return this.right;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public void setRight(Node right) {
            this.right = right;
        }
    }

    private int count;
    private Node root;

    public KdTree() {                              // construct an empty set of points
        count = 0;
        root = null;
    }

    public boolean isEmpty() {                     // is the set empty?
        return count == 0;
    }

    public int size() {                        // number of points in the set
        return count;
    }

    public void insert(Point2D p) {             // add the point to the set (if it is not already in the set)
        if (p == null)
            throw new NullPointerException();

        if (root == null) {
            root = new Node(p);
            count = 1;
            return;
        }

        if (contains(p)) {
            return;
        }

        Node visit = root;
        int level = 0;

        while (true) {
            if (level % 2 == 0) { // compare x-coordinate
                if (p.x() < visit.point().x()) {
                    if (visit.left() == null) {
                        visit.setLeft(new Node(p));
                        break;
                    } else {
                        visit = visit.left();
                    }
                } else {
                    if (visit.right() == null) {
                        visit.setRight(new Node(p));
                        break;
                    } else {
                        visit = visit.right();
                    }
                }
            } else {  // compare y-coordinate, if less than p.y, go left
                if (p.y() < visit.point().y()) {
                    if (visit.left() == null) {
                        visit.setLeft(new Node(p));
                        break;
                    } else {
                        visit = visit.left();
                    }
                } else {
                    if (visit.right() == null) {
                        visit.setRight(new Node(p));
                        break;
                    } else {
                        visit = visit.right();
                    }
                }
            }

            level++;
        }

        count++;
    }

    private void traverseTree(Node node) {
        if (node == null)
            return;

        StdOut.println(node.point());

        traverseTree(node.left());
        traverseTree(node.right());
    }

    public boolean contains(Point2D p) {           // does the set contain point p?
        if (p == null)
            throw new NullPointerException();

        Node visit = root;
        int level = 0;

        while (visit != null) {
            if (visit.point().equals(p)) {
                return true;
            }

            if (level % 2 == 0) {
                if (p.x() < visit.point().x()) {
                    visit = visit.left();
                } else {
                    visit = visit.right();
                }
            } else {
                if (p.y() < visit.point().y()) {
                    visit = visit.left();
                } else {
                    visit = visit.right();
                }
            }

            level++;
        }

        return false;
    }

    public void draw() {                        // draw all points to standard draw
        StdDraw.line(0, 0, 0, 1);
        StdDraw.line(0, 0, 1, 0);
        StdDraw.line(1, 0, 1, 1);
        StdDraw.line(0, 1, 1, 1);

        RectHV unitSquare = new RectHV(0, 0, 1, 1);
        recurDraw(root, unitSquare, true);
    }

    private void recurDraw(Node node, RectHV corrRect, boolean isVertical) {
        if (node == null)
            return;

        RectHV leftRect = null;
        RectHV rightRect = null;

        if (isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point().x(), corrRect.ymin(), node.point().x(), corrRect.ymax());
            StdDraw.setPenColor(StdDraw.BLACK);

            leftRect = new RectHV(corrRect.xmin(), corrRect.ymin(), node.point().x(), corrRect.ymax());
            rightRect = new RectHV(node.point().x(), corrRect.ymin(), corrRect.xmax(), corrRect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(corrRect.xmin(), node.point().y(), corrRect.xmax(), node.point().y());
            StdDraw.setPenColor(StdDraw.BLACK);

            leftRect = new RectHV(corrRect.xmin(), corrRect.ymin(), corrRect.xmax(), node.point().y());
            rightRect = new RectHV(corrRect.xmin(), node.point().y(), corrRect.xmax(), corrRect.ymax());
        }

        recurDraw(node.left(), leftRect, !isVertical);
        recurDraw(node.right(), rightRect, !isVertical);
    }

    private void rangeHelper(RectHV rect, List<Point2D> res, RectHV corrRect, Node node, boolean isVertical) {
        if (!rect.intersects(corrRect)) {
            return;
        }

        if (node == null)
            return;

        if (rect.contains(node.point)) {
            res.add(node.point);
        }

        RectHV leftRect = null;
        RectHV rightRect = null;

        if (isVertical) { // vertical split
            leftRect = new RectHV(corrRect.xmin(), corrRect.ymin(), node.point().x(), corrRect.ymax());
            rightRect = new RectHV(node.point().x(), corrRect.ymin(), corrRect.xmax(), corrRect.ymax());
        } else {
            leftRect = new RectHV(corrRect.xmin(), corrRect.ymin(), corrRect.xmax(), node.point().y());
            rightRect = new RectHV(corrRect.xmin(), node.point().y(), corrRect.xmax(), corrRect.ymax());
        }

        rangeHelper(rect, res, leftRect, node.left(), !isVertical);
        rangeHelper(rect, res, rightRect, node.right(), !isVertical);
    }

    public Iterable<Point2D> range(RectHV rect) {            // all points that are inside the rectangle
        if (rect == null)
            throw new NullPointerException();

        final List<Point2D> result = new ArrayList<Point2D>();
        RectHV corrRect = new RectHV(0, 0, 1, 1);

        rangeHelper(rect, result, corrRect, root, true);

        return new Iterable<Point2D>() {
            @Override
            public Iterator<Point2D> iterator() {
                return result.iterator();
            }
        };
    }

    public Point2D nearest(Point2D p) {            // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null)
            throw new NullPointerException();

        RectHV unitSqu = new RectHV(0, 0, 1, 1);

        return nearest(root, p, Double.POSITIVE_INFINITY, unitSqu, true);
    }

    // Find the nearest point that is closer than distance
    private Point2D nearest(Node x, Point2D p, double distance, RectHV rect, boolean isVertical) {
        if (x == null) {
            return null;
        }

        if (rect.distanceTo(p) >= distance) {
            return null;
        }

        Point2D nearestPoint = null;
        double nearestDistance = distance;
        double d;

        RectHV leftRect = null;
        RectHV rightRect = null;

        if (isVertical) { // vertical split
            leftRect = new RectHV(rect.xmin(), rect.ymin(), x.point().x(), rect.ymax());
            rightRect = new RectHV(x.point().x(), rect.ymin(), rect.xmax(), rect.ymax());
        } else {
            leftRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), x.point().y());
            rightRect = new RectHV(rect.xmin(), x.point().y(), rect.xmax(), rect.ymax());
        }


        d = p.distanceTo(x.point());
        if (d < nearestDistance) {
            nearestPoint = x.point();
            nearestDistance = d;
        }

        // Choose subtree that is closer to point.

        Node firstNode = x.left();
        Node secondNode = x.right();

        if (firstNode != null && secondNode != null) {
            if (leftRect.distanceTo(p) > rightRect.distanceTo(p)) {
                Node tmp = firstNode;
                firstNode = secondNode;
                secondNode = tmp;

                RectHV tmpRect = leftRect;
                leftRect = rightRect;
                rightRect = tmpRect;

            }
        }

        Point2D firstNearestPoint = nearest(firstNode, p, nearestDistance, leftRect, !isVertical);
        if (firstNearestPoint != null) {
            d = p.distanceTo(firstNearestPoint);
            if (d < nearestDistance) {
                nearestPoint = firstNearestPoint;
                nearestDistance = d;
            }
        }

        Point2D secondNearestPoint = nearest(secondNode, p, nearestDistance, rightRect, !isVertical);
        if (secondNearestPoint != null) {
            d = p.distanceTo(secondNearestPoint);
            if (d < nearestDistance) {
                nearestPoint = secondNearestPoint;
                nearestDistance = d;
            }
        }

        return nearestPoint;
    }


    public static void main(String[] args) {                 // unit testing of the methods (optional)
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.1, 0.1));

        StdOut.println(tree.nearest(new Point2D(0.2, 0.2)));
    }
}
