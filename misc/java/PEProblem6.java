// for the projecteuler.net - 2nd problem 
import java.util.ArrayList;

public class PEProblem6 
{
    
    public static void main(String[] args) 
    {
	long sumsq = 0L;
	long sqsum = 0L;
	//for euler 6th question
	for (int i=1; i <= 100; i++)
	    {
		sumsq += i*i;
		sqsum += i;
	    }
	sqsum = (sqsum * sqsum);
        System.out.println("Answer is " + (sqsum - sumsq)+ "\n");    
    }

} 
