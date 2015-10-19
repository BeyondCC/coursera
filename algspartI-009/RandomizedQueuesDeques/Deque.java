import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by cc on 10/10/15.
 */
public class Deque<Item> implements Iterable<Item> {

    private class Node {
        private Item item;
        private Node pre;
        private Node next;
    }

    private Node sentinel; //哨兵
    private int count;    //节点个数

    public Deque() { // construct an empty deque
        sentinel = new Node();
        sentinel.pre = sentinel;
        sentinel.next = sentinel;
        count = 0;
    }

    public boolean isEmpty() {               // is the deque empty?
        return count == 0;
    }

    public int size() {                        // return the number of items on the deque
        return count;
    }

    public void addFirst(Item item) {         // add the item to the front
        if (item == null) {
            throw new NullPointerException();
        }

        Node node = new Node();
        node.item = item;

        node.next = sentinel.next;
        node.next.pre = node;
        node.pre = sentinel;
        sentinel.next = node;

        count++;
    }

    public void addLast(Item item) {         // add the item to the end
        if (item == null) {
            throw new NullPointerException();
        }

        Node node = new Node();
        node.item = item;

        Node last = sentinel.pre;

        last.next = node;
        node.pre = last;
        node.next = sentinel;
        sentinel.pre = node;

        count++;
    }

    public Item removeFirst() {               // remove and return the item from the front
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Node first = sentinel.next;
        sentinel.next = first.next;
        first.next.pre = sentinel;
        count--;

        return first.item;
    }

    public Item removeLast() {              // remove and return the item from the end
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Node last = sentinel.pre;
        sentinel.pre = last.pre;
        last.pre.next = sentinel;
        count--;

        return last.item;
    }

    public Iterator<Item> iterator() {        // return an iterator over items in order from front to end
        return new DequeIterator();
    }

    private  class DequeIterator implements Iterator<Item> {

        private Node current = sentinel.next;

        @Override
        public boolean hasNext() {
            return current != sentinel;
        }

        @Override
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            Node nextNode = current;
            current = current.next;

            return nextNode.item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }



    public static void main(String[] args) {  // unit testing
        Deque<Integer> deque = new Deque<Integer>();

        deque.addFirst(3);
        deque.addFirst(4);
        deque.addFirst(5);
        deque.addLast(6);
        deque.addLast(7);
        deque.addLast(8);
        deque.addLast(9);

        for (Integer i : deque) {
            StdOut.println(i);
        }
       // StdOut.println(deque.removeFirst());
    }
}
