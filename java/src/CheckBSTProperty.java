package puzzle;
import puzzle.Node;

public class CheckBSTProperty {
	
	public static void main (String[] args ) {
		//System.out.println("hello world");
		Node head = new Node () ;
		head.value = 10 ;
		Node left1 = new Node() ;
		left1.value = 5 ;
		head.left = left1; 
		
		Node right1 = new Node() ;
		right1.value = 11 ;
		head.right=right1 ;
		
		Node right2 = new Node() ;
		right2.value = 6 ;
		head.left.right = right2;
		
		Node left3 = new Node() ;
		left3.value = 4 ;
		head.left.left = left3;
		
		System.out.print(checkBST(head)) ;
		
	}
	private static int checkBST(Node head) {
		
		if ( head == null ) { return 0 ; } 
		
		if ( head.left  == null && head.right == null ){
		 return head.value ;
		}

		int lc = checkBST(head.left) ;
		if ( lc == -1 ) {
			return -1 ;
		}
		int rc = checkBST(head.right) ;
		if ( rc == -1 ) {
			return -1 ;
		}
		if ( ( lc == 0  || lc < head.value)  && (rc == 0 || head.value < rc )) {
			return Math.max(lc,rc) ;
		}
		else {
			return -1 ;
		}
	}

}