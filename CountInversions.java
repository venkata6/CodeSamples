package puzzle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CountInversions {

	public static void main(String[] args)
	{
		//Integer[] a = {6,5,4,3,2,1};
		Integer[] a = {2, 4, 1, 3, 5};
		List <Integer> l = new ArrayList<Integer>();
		
		File f = new File ( "/Users/venkat/Desktop/IntegerArray.txt") ;
		try {
			Scanner s = new Scanner(f);
			while ( s.hasNext() ) {
				l.add(s.nextInt());
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//int[] ip = new int[l.size()];
		Integer[] ip = new Integer[l.size()];
		int j=0;
		for ( Integer i : l ) {
			ip[j++]=i;
			
		}
		
		long inv = mergeSort(a);
		//long inv = mergeSort(ip);
		System.out.println(Arrays.toString(a));
		System.out.println(inv);
	}

	public static long mergeSort(Comparable [ ] a)
	{
		Comparable[] tmp = new Comparable[a.length];
		return mergeSort(a, tmp,  0,  a.length - 1);
	}


	private static long mergeSort(Comparable [ ] a, Comparable [ ] tmp, int left, int right)
	{
		if( left < right )
		{
			int center = (left + right) / 2;
			long c1 = mergeSort(a, tmp, left, center);
			long c2 = mergeSort(a, tmp, center + 1, right);
			return merge(a, tmp, left, center + 1, right, c1+c2);
		}
		return 0;
	}


    private static long merge(Comparable[ ] a, Comparable[ ] tmp, int left, int right, int rightEnd, long c )
    {
        int leftEnd = right - 1;
        int k = left;
        int num = rightEnd - left + 1;
        long count = c;
        while(left <= leftEnd && right <= rightEnd)
            if(a[left].compareTo(a[right]) <= 0)
                tmp[k++] = a[left++];
            else {
            	count += ( leftEnd - left + 1);
                tmp[k++] = a[right++];
                
            }

        while(left <= leftEnd)    // Copy rest of first half
            tmp[k++] = a[left++];

        while(right <= rightEnd)  // Copy rest of right half
            tmp[k++] = a[right++];

        // Copy tmp back
        for(int i = 0; i < num; i++, rightEnd--)
            a[rightEnd] = tmp[rightEnd];
        
        return count;
    }
}
