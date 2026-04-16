// Problem 1: Print nodes at distance k from root
class Node {
    int data;
    Node left;
    Node right;
    
    Node(int data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }
}

public class TreeDistance {
    // recursively prints the nodes that are k distance away from the current node
    public static void printKDistanceNodes(Node root, int k) {
        if (root == null) {
            return;
        }
        
        // if k is 0, we reached our target distance, so print the data
        if (k == 0) {
            System.out.print(root.data + " ");
            return;
        }
        
        // go down to the left and right children and reduce k by 1
        printKDistanceNodes(root.left, k - 1);
        printKDistanceNodes(root.right, k - 1);
    }

    public static void main(String[] args) {
        // building a quick sample tree to test the method
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right.left = new Node(6);
        root.right.right = new Node(7);
        
        int k = 2;
        System.out.println("Nodes at distance " + k + " from root:");
        printKDistanceNodes(root, k);
        System.out.println();
    }
}
