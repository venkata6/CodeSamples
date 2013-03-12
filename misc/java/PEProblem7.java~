// for the projecteuler.net - 2nd problem 
import java.util.ArrayList;

public class PEProblem3 
{
 
    public static void main(String[] args) 
    {
               
	long n  = 600851475143L;  // euler 3rd question
	//long n  = 100L;  // euler 3rd question
	factorize(n) ;
	System.out.println("\n");
    }
    public static long factorize(long n)  
    {
	ArrayList factors = new ArrayList();
	long prime = 2L;
	while( n > 1)
	    {
	     while(n%prime == 0) 
		{ 
		   factors.add(prime);
		   System.out.println(" " + prime + " ") ;
	           n /= prime;
	        }
	     prime++;
	    }
	return prime;
    }

} 
