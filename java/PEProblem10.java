// for the projecteuler.net - 2nd problem 
import java.util.ArrayList;

//For http://projecteuler.net/problem=10

public class PEProblem10
{
 
    public static void main(String[] args) 
    {
	ArrayList<Long> primes = new ArrayList<Long>();

	boolean isPrime = true;
	primes.add(2L); 	
	long sum = 2L;
	for ( long i=3L;  i  < 2000000; i++) 
	    {
	        isPrime = true;
		for(int j=0; j <  primes.size(); j++)
		    {
			long tmp = (long) primes.get(j);
			if ( tmp > Math.sqrt(i) ) break;  // check only till  sqrt(n) 
			if ( (i % tmp)  == 0 ) { isPrime=false; break; }
		    }
		if ( isPrime ) { primes.add(i); sum += i ;  }
	    }
	System.out.println( "sum = " + sum + "\n");
	
    }

} 
