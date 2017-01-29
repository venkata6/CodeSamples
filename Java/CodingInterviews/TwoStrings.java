package puzzle;

import java.io.*;
import java.util.*;

public class TwoStrings {

    public static void main(String[] args) {
        Scanner s = new Scanner (System.in);
        int cnt = s.nextInt();
        Map<Character,Character> m1 = new HashMap<Character,Character>();
        while ( cnt != 0 ){
            m1.clear();
            String s1 = s.nextLine();
            String s2 = s.nextLine();
            System.out.println(s1);
            System.out.println(s2);
            for ( int i=0; i < s1.length() ; i++) {
                m1.put( s1.charAt(i), 'y');
            }
            boolean found=false;
            for ( int i=0; i < s2.length() ; i++) {
               if (m1.get(s2.charAt(i)) != null ) {
                   found=true;
                   break;
               }
            }
            if ( found ) {
                System.out.println("YES");
            } else {
                System.out.println("NO");
            }
            cnt--;
        }
    }
}