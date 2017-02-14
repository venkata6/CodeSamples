import java.util.Scanner;

/**
 * Created by varjunan on 12/31/16.
 */
public class Fibonacci {
    public static int [] mem = new int[100];  // for memoization

    public static void main ( String[] arg){
        System.out.println("hello world\n");
        long startTime =0; long stopTime = 0; long elapsedTime=0;
        Scanner keyboard = new Scanner(System.in);
        int fn=0;


        while(true) {
            System.out.println("enter an integer");
            fn = keyboard.nextInt();
            startTime=System.currentTimeMillis();
            System.out.println(f(fn));
            stopTime = System.currentTimeMillis();
            elapsedTime = stopTime - startTime;
            System.out.println("time taken (in ms) = " +elapsedTime);
        }
    }

    public static int f ( int i){

        if ( i == 0 || i == 1) { /*System.out.print(i + ",")*/ mem[i]=1;return 1; }
        //return f ( i-1) + f (i-2) ;   // normal recursive call which grows exponentially

        // memoization which runs in linear time
        int ret = 0;
        if ( mem[i-1] == 0) {
            mem[i-1] = f(i-1);
        }
        ret = mem[i-1];
        if ( mem[i-2] == 0) {
            mem[i-2] = f(i-2);
        }
        ret += mem[i-2];
        return ret;
    }

}
