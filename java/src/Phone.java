import java.util.*;

    public class Phone {
	
	public static void main ( String[] args) {
	    System.out.println("Hello world \n");
	    ArrayList<String> a = new ArrayList<String>() ;
	    a.add("abc");
	    a.add("def") ;
	    a.add("ghi");
	    StringBuffer strBuf = new StringBuffer() ;
	    generateCombinations(a,strBuf,0);
	}
	static void generateCombinations(ArrayList<String> input, StringBuffer strBuf, int index ) {
	    if ( index == (input.size()-1)) {
		String s = input.get(index);
		for(int i=0; i < s.length(); i++)
		    {
			System.out.println(strBuf.toString() + s.charAt(i));

		    }
	    }
	    else {
		String s = input.get(index++);
		for (int i=0; i < s.length(); i++ ) {
		    strBuf.append(s.charAt(i));
		    generateCombinations(input,strBuf,index);
		    strBuf.deleteCharAt(index-1);
		}

	    }

	}

    }