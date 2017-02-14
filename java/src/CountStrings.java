import java.util.*;

import static java.lang.Thread.sleep;

/**
 * Created by varjunan on 1/21/17.
 */


public class CountStrings {

    private Map<String,List<Long>> counter;
    CountStrings() {
        counter = new HashMap<String,List<Long>>();
    }
    public static void main ( String[] arg) {

        CountStrings c = new CountStrings();
        c.add("foo");
        c.add("bar");
        long l = c.getTime();
        try {
            sleep(1000);
        } catch ( Exception e) {

        }
        c.add("foo");

        System.out.println(c.getCount("foo", c.getTime()));
        System.out.println("foo at l " + c.getCount("foo", l));
        c.reset("foo");
        try {
            sleep(1000);
        } catch ( Exception e) {

        }
        System.out.println(c.getCount("foo", c.getTime()));
        c.add("foo");
        try {
            sleep(1000);
        } catch ( Exception e) {

        }
        System.out.println(c.getCount("foo", c.getTime()));

        System.out.println(c.getCount("foo", l));

    }
    public int getCount(String str, long timeStamp) {
        List<Long> list =  counter.get(str);
        int count = 0;
        if ( list == null) {
            return count ;
        } else {
            Iterator iter = list.iterator();
            while ( iter.hasNext() ) {
                long l = (long)iter.next();
                if ( l <= timeStamp) {
                    if (l == 0L) {
                        //System.out.println("count " + count);
                        count = 0;  //reset the count
                        //System.out.println("count " + count);
                    }
                    if (l <= timeStamp && l != 0L) {
                        count++;
                    }
                } else {
                    return count ;
                }
            }
        }
        return count;
    }
    public void add (String str) {
        List<Long> list = counter.get(str);
        if ( list == null ) {
            list = new ArrayList<Long>();
        }
        list.add(getTime());
        counter.put(str,list);
    }
    public long getTime(){
        return System.currentTimeMillis();
    }

    public void reset ( String str) {
        List<Long> list = counter.get(str);
        if ( list == null ) {
            list = new ArrayList<Long>();
        }
        list.add(0L);
        counter.put(str,list);

    }

}
