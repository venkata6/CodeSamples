package puzzle;

public class Makechange {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("hello world");
		//System.out.println(makeChange(100,25));
		System.out.println(makeChange(10,10));
	}
	public static int makeChange(int n, int denom) {
		int next_denom=0; 
		switch (denom){
		case 25:
			next_denom=10;
			break;
		case 10:
			next_denom=5;
			break;
		case 5:
			next_denom=1;
			break;
		case 1:
			return 1;
			
		}
		int ways = 0; 
		for ( int i=0; i * denom <= n; i++){
			ways += makeChange(n- i*denom, next_denom);
		}
		return ways;
	}
	public static int makeChangeI(int n, int denom) {
		int ways = 0;
		for ( int i=0; i <= (n/25); i++){
			for ( int j=0; j <= (n - (i*25))/10; j++){
				for ( int k=0; k <= (n -(i*25)-(j*10))/5; k++){
					//for ( int l=0; l < (n -(i*25)-(j*10)-(k*5)); l++){
					ways += 1 ; //break;
					//}
				}
			}
		}
		return ways;
	}
	
}
