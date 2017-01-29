package puzzle;


public class CheckBSTBalanced {
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
	
		/*Node left4 = new Node() ;
		left4.value = 4 ;
		head.left.left.left = left4;
	*/
		int maxDepth = maxHeight(head) ;
		int minDepth = minHeight(head) ;
		System.out.println(maxDepth-minDepth);
		insert(head,100) ;
		//System.out.print(checkBST(head)) ;
		
	}
	private static int checkBST(Node head) {
		
		if ( head == null ) { return 1 ; } 
		int lh =  0 ;
		int rh = 0 ;
		if ( head.right != null ) {
			rh = HeightBinaryTree.findHeight(head.right);
		}
		if ( Math.abs(lh-rh) > 1 ) {
			return -1 ;
		}
		
		return 1 * checkBST(head.left)* checkBST (head.right) ;
	}
	public  static int maxHeight(Node head) {
		if ( head == null ){
		 return 0 ;
		}
		
		return Math.max( maxHeight(head.left),maxHeight(head.right)) + 1  ;
	}
	public  static int minHeight(Node head) {
		if ( head == null ){
		 return 0 ;
		}
		
		return Math.min( minHeight(head.left),minHeight(head.right)) + 1  ;
	}
	public static Node insert (Node head, int x ) {
		if ( head == null) {
			Node n  = new Node() ;
			n.value = x ;
			return head ;
		}
		if ( x < head.value  ) {
			if (head.left == null) {
				head.left= new Node() ;
				head.left.value = x ;
			}
			else {
				insert(head.left,x ) ;
			}
		}
		else  {
			if (head.right == null) {
				head.right= new Node() ;
				head.right.value = x ;
			}
			else {
				insert(head.right,x ) ;
			}
		}
		return head ; 
	}
}