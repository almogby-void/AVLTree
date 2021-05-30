/**
 * 
 */
package AVL;

import java.lang.reflect.Array;

/**
 * @author almog
 *
 */
/**
 * public class BST
 * This class represents a BST with integer keys and boolean values.
 */

public class BST {
	private final Node virt = new Node();
	private Node root = null;
	private int size = 0;

    /**
     * This constructor creates an empty BST.
     */
    public BST() {
        this.root = this.virt;
    }

    /**
	 * 
	 */
	public BST(boolean value, int key) {
		this.root = new Node(value, key);
		this.size++;
	}

	/**
     * public boolean empty()
     * returns true if and only if the tree is empty
     */
    public boolean empty() {
        return (!this.root.isRealNode());
    }
    
    private Node bin_search(int k, boolean b) {
    	Node x = this.root;
    	while (x.isRealNode() && x.getKey() != k)
	    	if (x.getKey() > k) {
	    		if (b && x.getLeft() == null)
	    			return x;
	    		if (x.hasLeft())
	    			x = x.getLeft();
	    		else
	    			x = this.virt;
	    	}
	    	else {
	    		if (b && x.getRight() == null)
	    			return x;
	    		if (x.hasRight())
		    		x = x.getRight();
	    		else
	    			x = this.virt;
	    	}
    	return x;
    }

    /**
     * public boolean search(int k)
     * <p>
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     */
    public Boolean search(int k) {
	    return this.bin_search(k, false).getValue();
    }

    /**
     * public int insert(int k, boolean i)
     * inserts an item with key k and info i to the BST.
     * the tree must remain valid (keep its invariants).
	 * returns the number of nodes which require rebalancing operations (i.e. promotions or rotations).
	 * This always includes the newly-created node.
     * returns -1 if an item with key k already exists in the tree.
     */
    public int insert(int k, boolean i) {
    	if (this.empty()) {
    		this.root = new Node(i, k);
    		this.size++;
    		return 1;
    	}
    	Node x = this.bin_search(k, true);
        if (x.getKey() == k)
        	return -1;
    	Node new_node = new Node(i, k);
    	if (x.getKey() < k)
    		x.setRight(new_node);
    	else
    		x.setLeft(new_node);
    	this.size++;
        return 0;
    }

    /**
     * public int delete(int k)
     * <p>
     * deletes an item with key k from the binary tree, if it is there;
     * the tree must remain valid (keep its invariants).
     * returns the number of nodes which required rebalancing operations (i.e. demotions or rotations).
     * returns -1 if an item with key k was not found in the tree.
     */
    public int delete(int k) {
        Node node = this.bin_search(k, false);
        if (!node.isRealNode())
        	return -1;
        if (node.hasLeft())
        	if (node.hasRight()) { // node has both children
        		Node succ = successor(node);
        		Node par = succ.getParent();
        		if (par.getLeft() == succ)
        			if (succ.hasRight())
        				par.setLeft(succ.getRight());
        			else
        				par.setLeft(this.virt);
            	else if (succ.hasRight())
            		par.setRight(succ.getRight());
            	else
            		par.setRight(this.virt);
        		if (node.hasLeft())
        			succ.setLeft(node.getLeft());
        		else
        			succ.setLeft(this.virt);
        		if (node.hasRight())
        			succ.setRight(node.getRight());
        		else
        			succ.setRight(this.virt);
        		succ.setParent(node.getParent());
        		if (this.root == node)
        			this.root = succ;
        	}
        	else { // node has only left child
        		node.getLeft().setParent(node.getParent());
        		if (this.root == node)
        			this.root = node.getLeft();
        	}
        else if (node.hasRight()) { // node has only right child
        	node.getRight().setParent(node.getParent());
        	if (this.root == node)
    			this.root = node.getRight();
        }
        else { // node is a leaf
            if (this.root == node) {
        		this.root = this.virt;
        		return 0;
            }
        	Node par = node.getParent();
        	if (par.getLeft() == node)
        		par.setLeft(this.virt);
        	else
        		par.setRight(this.virt);
        }
        this.size--;
        return 0;
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() {
        int[] arr = new int[this.size];
        if (arr.length > 0)
        	drec(this.getRoot(), arr, 0, false);
        return arr;
    }

    /**
     * public boolean[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public boolean[] infoToArray() {
        boolean[] arr = new boolean[this.size];
        if (arr.length > 0)
        	drec(this.getRoot(), arr, 0, true);
        return arr;
    }
    
    private int drec(Node node, Object arr, int ind, boolean b) {
    	if (node.hasLeft())
    		ind = drec(node.getLeft(), arr, ind, b);
    	if (b)
    		Array.setBoolean(arr, ind++, node.getValue());
    	else
    		Array.setInt(arr, ind++, node.getKey());
    	if (node.hasRight())
    		ind = drec(node.getRight(), arr, ind, b);
    	return ind;
    }

    /**
     * public Node getRoot()
     * Returns the root node, or null if the tree is empty
     */
    public Node getRoot() {
    	if (this.empty())
    		return null;
        return this.root;
    }

    /**
     * public Node successor
     *
     * given a node 'node' in the tree, return the successor of 'node' in the tree (or null if successor doesn't exist)
     *
     * @param node - the node whose successor should be returned
     * @return the successor of 'node' if exists, null otherwise
     */
    public Node successor(Node node){
    	Node succ = node;
    	if (node.hasRight()) {
    		succ = succ.getRight();
    		while (succ.hasLeft())
    			succ = succ.getLeft();
    	} else {
	    	succ = succ.getParent();
	    	while (succ.getKey() < node.getKey())
	    		succ = succ.getParent();
    	}
    	return succ;
    }


    /**
     * public class Node
     * This class represents a node in the BST tree.
     */
    public class Node {
    	private boolean value;
    	private int key;
    	private Node parent = null;
    	private Node left = null;
    	private Node right = null;
    	
		public Node() {
			this.key = -1;
		}
		
        /**
		 * @param value
		 * @param key
		 */
		public Node(boolean value, int key) {
			this.value = value;
			this.key = key;
			this.left = virt;
			this.right = virt;
		}

		//returns node's key (for virtual node return -1)
        public int getKey() {
            return this.key;
        }

		//returns node's value [info] (for virtual node return null)
        public Boolean getValue() {
        	if (!this.isRealNode())
        		return null;
            return this.value;
        }

        //sets left child
        public void setLeft(Node node) {
        	this.left = node;
        	if (node.isRealNode())
        		node.parent = this;
        }

        //returns left child (if there is no left child return null)
        public Node getLeft() {
            if (this.hasLeft())
            	return this.left;
            return null;
        }

        //sets right child
        public void setRight(Node node) {
        	this.right = node;
        	if (node.isRealNode())
        		node.parent = this;
        }

        //returns right child (if there is no right child return null)
        public Node getRight() {
        	if (this.hasRight())
            	return this.right;
            return null;
        }
        
        //returns true if there is a left child
        public boolean hasLeft() {
        	return this.left.isRealNode();
        }
        
        //returns true if there is a right child
        public boolean hasRight() {
        	return this.right.isRealNode();
        }

        //sets parent
        public void setParent(Node node) {
        	if (node == null) {
        		this.parent = null;
        		return;
        	}
        	if (node.getKey() > this.key)
        		node.left = this;
        	else
        		node.right = this;
            this.parent = node;
        }

        //returns the parent (if there is no parent return null)
        public Node getParent() {
            return this.parent;
        }

        // Returns True if this is a non-virtual BST node
        public boolean isRealNode() {
            return (this.key != -1);
        }
    }
}