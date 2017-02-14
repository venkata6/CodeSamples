package puzzle;

public class Permutations {

	public static void main(String[] args) {
	
		//int n = Integer.parseInt(args[0]);
		int n = 5;
		StringBuilder s = new StringBuilder();
		for ( int i =0; i < 10000 ; i++) {
			s.append (i+"");
		}
		String elements = s.substring(0, n);
        perm1(elements);
        System.out.println();
		
	}

	// print n! permutation of the characters of the string s (in order)
    public  static void perm1(String s) { perm1("", s); }
    private static void perm1(String prefix, String s) {
        int n = s.length();
        if (n == 0) System.out.println(prefix);
        else {
            for (int i = 0; i < n; i++)
               perm1(prefix + s.charAt(i), s.substring(0, i) + s.substring(i+1, n));
        }

    }
	
}
