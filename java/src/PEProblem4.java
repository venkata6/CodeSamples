// for the projecteuler.net - 2nd problem 
import java.util.ArrayList;

public class PEProblem4 
{
 
    public static void main(String[] args) 
    {
	//for euler 4th question
	for (int i=999; i > 900; i--)
	    {
		for (int j=999; j > 900; j--)
		    {
			int n = i*j;
			if (checkPalin(n)) { System.out.println(n+"\n"); }
		    }
	    }
    }
    public static boolean checkPalin (int n)  
    {
	String s = Integer.toString(n);
	byte[] b = s.getBytes();
	for (int i=0,j=s.length()-1; i <= j; i++,j--)
	    {
		if ( b[i] != b[j]) { return false ; } 
	    }
	return true;
    }

} 
