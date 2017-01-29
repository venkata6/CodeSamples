package puzzle;


public class HeightBinaryTree {
	
	public static void main (String[] args ) {
		//System.out.println("hello world");
		Node head = new Node () ;
		head.value = 1 ;
		Node left1 = new Node() ;
		left1.value = 2 ;
		head.left = left1; 
		
		Node right1 = new Node() ;
		right1.value = 3 ;
		head.right=right1 ;
		
		Node left2 = new Node() ;
		left2.value = 4 ;
		head.left.left = left2;
		
		Node left3 = new Node() ;
		left3.value = 4 ;
		head.left.left.left = left3;
		
		System.out.print(findHeight(head)) ;
		
	}
	public  static int findHeight(Node head) {
		if ( head == null ){
		 return 0 ;
		}
		
		return Math.max( findHeight(head.left),findHeight(head.right)) + 1  ;
	}

}
