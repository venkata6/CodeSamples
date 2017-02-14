package puzzle;

import redis.clients.jedis.* ;
import redis.clients.jedis.Jedis;

import java.util.Calendar;
import java.util.concurrent.*;
import java.util.*;


class RedisTestThread implements Callable<ArrayList> {
	private static final int TOTAL_OPERATIONS = 10000;
	@Override 	
	public ArrayList<String> call() throws Exception {
	
		ArrayList<String> a = new ArrayList<String>() ;
		Jedis jedis = new Jedis("localhost");
	    jedis.connect();
	    jedis.set("foo", "bar");
	    String value = jedis.get("foo");
	    //System.out.println(value);
		
	    long begin = Calendar.getInstance().getTimeInMillis();

        for (int n = 0; n <= TOTAL_OPERATIONS; n++) {
            String key = "ADITI" + n;
            jedis.set(key, "AMEYA" + n);
            a.add(jedis.get(key));
        }

        long elapsed = Calendar.getInstance().getTimeInMillis() - begin;

        jedis.disconnect();
        System.out.println(Thread.currentThread().getName() + "elapsed time = " + elapsed);
        //System.out.println(((1000 * 2 * TOTAL_OPERATIONS) / elapsed) + " ops " + Thread.currentThread().getName());
        long ret =  (1000 * 2 * TOTAL_OPERATIONS) / elapsed ;
		//return the thread name executing this callable task
        //return Thread.currentThread().getName();
		return a  ;
	}
	
}

public class RedisTest {
	
	public static void main(String[] args) {
		
		System.out.println("hello Redis, here i come") ;
		
		//Get ExecutorService from Executors utility class, thread pool size is 10
        ExecutorService executor = Executors.newFixedThreadPool(10);
        //create a list to hold the Future object associated with Callable
        List<Future<ArrayList>> list = new ArrayList<Future<ArrayList>>();
        //Create MyCallable instance
        Callable<ArrayList> callable = new RedisTestThread();
        for(int i=0; i< 20; i++){
            //submit Callable tasks to be executed by thread pool
            Future<ArrayList> future = executor.submit(callable);
            //add Future to the list, we can get return value using Future
            list.add(future);
        }
        for(Future<ArrayList> fut : list){
            try {
                //print the return value of Future, notice the output delay in console
                // because Future.get() waits for task to get completed
            	ArrayList<String> a = fut.get() ;
                System.out.println(new Date()+ ":: Future value "+ a.size() + " " + a.get(a.size()-1)+ "\n");
            } 
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        //shut down the executor service now
        executor.shutdown();
    }
		
		    
}

