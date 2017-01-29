package puzzle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SumToX {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SumToX s = new SumToX() ;
		int[] a = {1,1,3,4,5,5,5,5,6,7,9,9,5} ;
		ArrayList<int[]> r = s.sumToX2(a,10);
		Iterator i = r.iterator() ;
		while ( i.hasNext()) {
			int[] p = (int[]) i.next() ;
			System.out.printf("pair is %d %d\n",p[0],p[1]);
		}
	}
	public ArrayList<int[]> sumToX ( int[] arr, int x ) {
		ArrayList <int[]> a = new ArrayList<int[]>();
		HashMap <Integer,Integer> h = new HashMap<Integer,Integer>();
		if ( arr == null ) { return null ; }  
		for( int i=0; i < arr.length; i++ ) {
			h.put(arr[i],i) ;
		}
		for( int i=0; i < arr.length; i++ ) {
			if (h.get(x-arr[i])  != i  ) {
				int pair[] = {arr[i],x-arr[i]} ;
				a.add(pair) ;
			}
		}
		return a ;
	}

	public ArrayList<int[]> sumToX2 ( int[] arr, int x ) {
		ArrayList <int[]> a = new ArrayList<int[]>();
		HashMap <Integer,Integer> h = new HashMap<Integer,Integer>();
		if ( arr == null ) { return null ; }  

		for( int i=0; i < arr.length; i++ ) {
			if ( !h.containsKey(arr[i]) ){
				h.put(arr[i],1) ;
			}
			else {
				int v = h.get(arr[i]);
			    h.put(arr[i],++v) ;
			}
		}
		for( int i=0; i < arr.length; i++ ) {
			
			if (h.containsKey(x-arr[i])  ) {
				int v = h.get(x-arr[i]) ;
				if ( v > 0 ) {
					int pair[] = {arr[i],x-arr[i]} ;
					a.add(pair) ;
					h.put(x-arr[i], --v);
				}
		
			}
		}
		return a ;
	}

	
	
}
