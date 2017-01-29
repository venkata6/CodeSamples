package puzzle;

import java.util.Stack;

class Node {
	Node right ;
	Node left ;
	int value ;
}

public class PostOrderTraversal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
		postorder(head) ;
		
	}
	static private void postorder(Node head) {
		  if (head == null) {
		    return;
		  }
		  Stack<Node> stack = new Stack<Node>();
		  stack.push(head);

		  while (!stack.isEmpty()) {
		    Node next = stack.peek();

		    if (next.right == head || next.left == head ||
		       (next.left == null && next.right == null)) {
		      stack.pop();
		      System.out.println(next.value);
		      head = next;
		    }
		    else {
		      if (next.right != null) {
		        stack.push(next.right);
		      }
		      if (next.left != null) {
		        stack.push(next.left);
		      }
		    }
		  }
		}
	
}
