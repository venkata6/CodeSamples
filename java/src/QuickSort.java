package puzzle;

import java.io.*;
import java.util.*;

public class QuickSort {

    public static void main(String[] args) {
    	//Integer[] a = {6,5,4,3,2,1};
		//int[] arr = {2, 4, 1, 6, 3, 5};
		List <Integer> l = new ArrayList<Integer>();
		
		File f = new File ( "/Users/venkat/Desktop/QuickSort.txt") ; /*QuickSort*/
		try {
			Scanner s = new Scanner(f);
			while ( s.hasNext() ) {
				l.add(s.nextInt());
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int[] arr = new int[l.size()];
		//Integer[] ip = new Integer[l.size()];
		int j=0;
		for ( Integer i : l ) {
			arr[j++]=i;
			
		}
		
		List<Long> cnt = new ArrayList<Long>();
		//cnt.add((long) arr.length);
		cnt.add((long)0);
        System.out.println(Arrays.toString(quicksort(arr, 0, arr.length - 1, cnt)));
		//Arrays.toString(quicksort(arr, 0, arr.length - 1, cnt));
		System.out.println(cnt.get(0));
    }


private static int[]  quicksort(int[] G, int first, int last, List<Long> cnt) {
  if (first >= last) { 
	  return G ;
  }
  swap(G,first,last) ;  //only for the last element as pivot problem
  int pivot = partition(G, first, last,cnt);
  
  quicksort(G, first, pivot - 1, cnt);
 /* value = cnt.get(0);
  value += last - pivot -1;
  cnt.remove(0);
  cnt.add(value);*/
  quicksort(G, pivot + 1, last, cnt);
  
  return G;
}

private static boolean checkInMiddle (int i, int j , int k ) {
	if ( i > j && i < k )
		return true ;
	else if ( i > k && i < j )
		return true ;
				
	else return false;
}

private static int partition(int[] G, int first, int last,List<Long> cnt) {
  //int pivot = first + new Random().nextInt(last - first + 1);
  int pivot = first;
  long value = cnt.get(0);
  value += last - first;
  cnt.remove(0);
  cnt.add(value);
  /*int mid = (first+last)/2;
  if ( checkInMiddle(G[first],G[last], G[mid]) ) {
	 pivot = first;  
  } else if ( checkInMiddle(G[last],G[first], G[mid]) ) {
		 pivot = last;
		 swap(G, first, pivot);
		 pivot = first;
  } else  if ( checkInMiddle(G[mid],G[first],G[last]) ) {
		 pivot = mid;  
		 swap(G, first, pivot);
		 pivot = first;

	  }*/
  //pivot = first;
  //swap(G, last, pivot);
   first++; // change
  
  for (int i = first; i <= last; i++) {
   // if (G[i] < G[last]) {
	  if (G[i] < G[pivot]) {
      swap(G, i, first);
      first++;
    }
  }
  //swap(G, first, last);
  swap(G, first-1, pivot);
  return first-1;
}

private static void swap(int[] G, int x, int y) {
  int tmp = G[x];
  G[x] = G[y];
  G[y] = tmp;
}

}