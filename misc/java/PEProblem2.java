// for the projecteuler.net - 2nd problem 

public class PEProblem2 {
 
    public static void main(String[] args) {
               
	int limit = 4000000;  // limit 4 million for the euler 2nd question
	int n = 2;
	long seq  = 0; 
	long sum = 0 ;	
	while ( seq < limit )
	    {
		n = n + ( n/ 2);
		sum=0; //reset sum for next iteration
		for (int i =1; i < n; i++)
		    {
			seq =  fibonacci(i);
			System.out.print( seq +  " ");
			if ( seq > limit ) break ;
			if ( seq%2 == 0 ) {
			    sum += seq;
			    System.out.println("Sum = " + sum + "\n");
			}
		    }
		System.out.print( "\n ");
    	}
    }
    public static int fibonacci(int n)  {
	if( n <= 2) 
	    return n;
	else
	    return fibonacci(n - 1) + fibonacci(n - 2);
    }

}