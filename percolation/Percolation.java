import  edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * N^2 + 2个数组来存储所有块的信息，利用两个虚拟节点来判断整个系统的连通性，
 * 当一个块打开时，最多跟周围的4个节点union
 */
public class Percolation {
   private boolean[] flag;
   private int dimention;
   private WeightedQuickUnionUF uf;

   public Percolation(int N) {              // create N-by-N grid, with all sites blocked
      if (N <= 0)
         throw new IllegalArgumentException();

      flag = new boolean[N * N + 2]; // N * N + two virtual node
      dimention = N;
      uf = new WeightedQuickUnionUF(N * N + 2);

      for (int i = 0; i < flag.length; i++) {
         flag[i] = false;
      }
   }
   
   public void open(int i, int j) {          // open site (row i, column j) if it is not open already
      if (i < 1 || i > dimention || j < 1 || j > dimention)
         throw new IndexOutOfBoundsException();

      int index = (i - 1) * dimention + j;
      //StdOut.println(i + " " + j + " " + index);
      flag[index] = true;

      if (i > 1 && i < dimention) {
         if (isOpen(i - 1, j))
            uf.union(index, (i - 2) * dimention + j);

         if (isOpen(i + 1, j))
            uf.union(index, i * dimention + j);
      }

      if (i == 1) {
         if (isOpen(i + 1, j))
            uf.union(index, (i) * dimention + j);

         if (!uf.connected(0, index)) {
            uf.union(0, index);
         }
      }

      if (i == dimention) {
         if (isOpen(i - 1, j))
            uf.union(index, (i - 2) * dimention + j);

         if (!uf.connected(index, dimention * dimention + 1)) //union the virtual node
            uf.union(index, dimention * dimention + 1);
      }

      if (j > 1 && j < dimention) {
         if (isOpen(i, j - 1))
            uf.union(index, index - 1);

         if (isOpen(i, j + 1))
            uf.union(index, index + 1);
      }

      if (j == 1) {
         if (isOpen(i, j + 1))
            uf.union(index, index + 1);
      }

      if (j == dimention) {
         if (isOpen(i, j - 1))
            uf.union(index, index - 1);
      }

   }

   public boolean isOpen(int i, int j) {    // is site (row i, column j) open?
      if (i < 1 || i > dimention || j < 1 || j > dimention)
         throw new IndexOutOfBoundsException();
      return flag[(i - 1) * dimention + j];
   }

   public boolean isFull(int i, int j) {     // is site (row i, column j) full?
      if (i < 1 || i > dimention || j < 1 || j > dimention)
         throw new IndexOutOfBoundsException();

      int index = (i - 1) * dimention + j;
      return uf.connected(0, index);
   }

   public boolean percolates() {            // does the system percolate?
      return uf.connected(0, dimention * dimention + 1);
   }

   public static void main(String[] args) { // test client (optional)
       
   }
}