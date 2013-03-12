// for the projecteuler.net - 2nd problem 
import java.util.ArrayList;

public class PEProblem7 
{
 
    public static void main(String[] args) 
    {
	ArrayList<Long> primes = new ArrayList<Long>();
	long n = 1L ;
	boolean isPrime = true;
	primes.add(2L);
	for ( long i=3L; primes.size() < 10001; i++) 
	    {
		 isPrime = true;
		for(int j=0; j <  primes.size(); j++)
		    {
			long tmp = (long) primes.get(j); 
			if ( (i % tmp)  == 0 ) { isPrime=false; break; }
		    }
		if ( isPrime ) { primes.add(i); System.out.println( " " + i + "\n");  }
	    }
	
    }

} 
