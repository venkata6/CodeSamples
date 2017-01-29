package puzzle;

import java.io.*;
import java.util.*;

public class QuickSelect {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int arr[] = new int[s.nextInt()];
        for ( int i=0; i < arr.length; i++){
            arr[i]=s.nextInt();
        }


        System.out.println(quickselect(arr, 0, arr.length - 1, arr.length/2));

    }


private static int quickselect(int[] G, int first, int last, int k) {
  if (first <= last) {
    int pivot = partition(G, first, last);
    if (pivot == k) {
    System.out.println(Arrays.toString(G));
      return G[k];
    }
    if (pivot > k) {
      return quickselect(G, first, pivot - 1, k);
    }
    return quickselect(G, pivot + 1, last, k);
  }
  return Integer.MIN_VALUE;
}

private static int partition(int[] G, int first, int last) {
  int pivot = first + new Random().nextInt(last - first + 1);
  swap(G, last, pivot);
  for (int i = first; i < last; i++) {
    if (G[i] > G[last]) {
      swap(G, i, first);
      first++;
    }
  }
  swap(G, first, last);
  return first;
}

private static void swap(int[] G, int x, int y) {
  int tmp = G[x];
  G[x] = G[y];
  G[y] = tmp;
}

}