import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by cc on 11/10/15.
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
    private int count;
    private Item[] arr;
    private int tail;

    public RandomizedQueue() {                // construct an empty randomized queue
        arr = (Item[]) new Object[1];
        count = 0;
        tail = 0;
    }

    public boolean isEmpty() {                // is the queue empty?
        return count == 0;
    }

    public int size() {                       // return the number of items on the queue
        return count;
    }

    private void resize(int length) {
        Item[] result = (Item[]) new Object[length];

        for (int i = 0; i < count; i++) {
            result[i] = arr[i];
        }

        arr = result;
    }

    public void enqueue(Item item) {          // add the item
        if (item == null)
            throw new NullPointerException();

        if (count == arr.length)
            resize(count * 2);

        arr[tail++] = item;
        count++;
    }

    public Item dequeue() {                   // remove and return a random item
        if (isEmpty())
            throw new NoSuchElementException();

        int index = StdRandom.uniform(count);
        Item result = arr[index];
        arr[index] = arr[--tail];
        arr[tail] = null;

        count--;

        if (count == arr.length / 4 && count > 0) // count should be greater than zero
            resize(arr.length / 2);

        return result;
    }

    public Item sample() {                    // return (but do not remove) a random item
        if (isEmpty())
            throw new NoSuchElementException();

        int index = StdRandom.uniform(count);

        return arr[index];
    }

    public Iterator<Item> iterator() {        // return an independent iterator over items in random order
        return new RandomQueueIterable();
    }

    private class RandomQueueIterable implements Iterator<Item> {
        //随机输出，将初始数组洗牌即可
        private Item[] copy;
        private int index = 0;

        public RandomQueueIterable() {
            copy = (Item[]) new Object[count];

            for (int i = 0; i < count; i++) {
                copy[i] = arr[i];
            }

            if (count != 0)
                StdRandom.shuffle(copy, 0, count - 1);
        }

        public boolean hasNext() {
            return index != count;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            return copy[index++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {  // unit testing
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();

        queue.size();
        queue.enqueue(35);
        queue.dequeue();
        queue.enqueue(405);


        for (int i = 0; i < 10; i++) {
            queue.enqueue(i);
        }

        Iterator<Integer> iterator = queue.iterator();
        Iterator<Integer> iterator1 = queue.iterator();

        while (iterator.hasNext()) {
            StdOut.println(iterator.next());
        }

        StdOut.println("next=======");

        while (iterator1.hasNext()) {
            StdOut.println(iterator1.next());
        }
    }

}
