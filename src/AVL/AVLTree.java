package AVL;

import java.lang.reflect.Array;

/**
 * public class AVLNode
 * <p>
 * This class represents an AVLTree with integer keys and boolean values.
 * <p>
 * IMPORTANT: do not change the signatures of any function (i.e. access modifiers, return type, function name and
 * arguments. Changing these would break the automatic tester, and would result in worse grade.
 * <p>
 * However, you are allowed (and required) to implement the given functions, and can add functions of your own
 * according to your needs.
 */

public class AVLTree {
	private final AVLNode virt = new AVLNode();
	private AVLNode root = null;
	private AVLNode min = null;
	private AVLNode max = null;

    /**
     * This constructor creates an empty AVLTree.
     */
    public AVLTree() {
        this.root = this.virt;
    }

    /**
	 * 
	 */
	public AVLTree(boolean value, int key) {
		this.root = new AVLNode(value, key);
	}

	/**
     * public boolean empty()
     * <p>
     * returns true if and only if the tree is empty
     */
    public boolean empty() {
        return (!this.root.isRealNode());
    }
    
    private AVLNode bin_search(int k, boolean b) {
    	AVLNode x = this.root;
    	while (x.isRealNode() && x.getKey() != k)
	    	if (x.getKey() > k) {
	    		if (b && x.getLeft() == null)
	    			return x;
	    		x = x.getLeft();
	    	}
	    	else {
	    		if (b && x.getRight() == null)
	    			return x;
	    		x = x.getRight();
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
    
    private void rotateLeft(AVLNode criminal) {
    	AVLNode B = criminal.getRight();
    	if (this.root == criminal)
    		this.root = B;
	    B.setParent(criminal.getParent());
    	if (B.getLeft() == null)
    		criminal.setRight(this.virt);
    	else
    		criminal.setRight(B.getLeft());
	    B.setLeft(criminal);
    }
    
    private void rotateRight(AVLNode criminal) {
    	AVLNode A = criminal.getLeft();
    	if (this.root == criminal)
    		this.root = A;
	    A.setParent(criminal.getParent());
    	if (A.getRight() == null)
    		criminal.setLeft(this.virt);
    	else
    		criminal.setLeft(A.getRight());
    	A.setRight(criminal);
    }

    /**
     * public int insert(int k, boolean i)
     * <p>
     * inserts an item with key k and info i to the AVL tree.
     * the tree must remain valid (keep its invariants).
	 * returns the number of nodes which require rebalancing operations (i.e. promotions or rotations).
	 * This always includes the newly-created node.
     * returns -1 if an item with key k already exists in the tree.
     */
    public int insert(int k, boolean i) {
    	if (this.empty()) {
    		this.min = this.max = this.root = new AVLNode(i, k);
    		return 1;
    	}
    	AVLNode x = this.bin_search(k, true);
        if (x.getKey() == k)
        	return -1;
    	AVLNode new_node = new AVLNode(i, k);
    	int count = 1;
    	if (x.getKey() < k) {
    		if (x.setRight(new_node))
    			count++;
    	}
    	else {
    		if (x.setLeft(new_node))
    			count++;
    	}
    	if (k < this.min.getKey()) {
    		new_node.setSucc(this.min);
    		this.min = new_node;
    	}
    	else if (k > this.max.getKey()) {
    		this.max.setSucc(new_node);
    		this.max = new_node;
    	}
    	else {
	    	new_node.setSucc(findSucc(new_node));
	    	AVLNode pred = findPred(new_node);
	    	pred.setSucc(new_node);
    	}
    	while (((x = x.getParent()) != null) && (x.update())) {
			if (Math.abs(x.getBF()) == 2) { // criminal has been detected
				if (x.getBF() == -2) {
					if (x.getRight().getBF() == -1) { // case1
						this.rotateLeft(x);
					}
					else { // case2
						this.rotateRight(x.getRight());
						this.rotateLeft(x);
					}
				}
				else if (x.getLeft().getBF() == -1) { // case3
					this.rotateLeft(x.getLeft());
					this.rotateRight(x);
				}
				else { // case4
					this.rotateRight(x);
				}
			}
			count++;
    	}
        return count;
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
        AVLNode x = this.bin_search(k, false);
        if (!x.isRealNode())
        	return -1;
        if (x == this.min) {
    		this.min = x.getSucc();
    	}
    	else {
    		AVLNode pred = findPred(x);
	    	pred.setSucc(x.getSucc());
	    	if (x == this.max)
	    		this.max = pred;
    	}
        if ((this.root == x) && (x.getHeight() == 0)) {
    		this.root = this.virt;
    		return 0;
        }
        x = deleteBST(x);
        int count = 0;
        do {
        	boolean b = x.update();
			if (Math.abs(x.getBF()) == 2) { // criminal has been detected
				if (x.getBF() == -2) {
					if (x.getRight().getBF() <= 0) { // case1
						this.rotateLeft(x);
					}
					else { // case2
						this.rotateRight(x.getRight());
						this.rotateLeft(x);
					}
				}
				else if (x.getLeft().getBF() == -1) { // case3
					this.rotateLeft(x.getLeft());
					this.rotateRight(x);
				}
				else { // case4
					this.rotateRight(x);
				}
				count++;
			}
			else if (b)
				count++;
    	} while ((x = x.getParent()) != null);
        return count;
    }
    
    private AVLNode deleteBST(AVLNode node) {
        if (node.getHeight() == 0) { // node is a leaf
        	AVLNode par = node.getParent();
        	if (par.getLeft() == node)
        		par.setLeft(this.virt);
        	else
        		par.setRight(this.virt);
        	return par;
        }
        else if (node.hasLeft())
        	if (node.hasRight()) { // node has both children
        		AVLNode succ = node.getSucc();
        		AVLNode par = succ.getParent();
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
        		if (par == node)
        			return succ;
        		return par;
        	}
        	else { // node has only left child
        		node.getLeft().setParent(node.getParent());
        		if (this.root == node)
        			this.root = node.getLeft();
        	}
        else { // node has only right child
        	node.getRight().setParent(node.getParent());
        	if (this.root == node)
    			this.root = node.getRight();
        }
        return node.getParent();
    }

    /**
     * public Boolean min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     */
    public Boolean min() {
    	return this.min.getValue();
    }

    /**
     * public Boolean max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     */
    public Boolean max() {
    	return this.max.getValue();
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() {
        int[] arr = new int[this.size()];
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
        boolean[] arr = new boolean[this.size()];
        if (arr.length > 0)
        	drec(this.getRoot(), arr, 0, true);
        return arr;
    }
    
    private int drec(AVLNode node, Object arr, int ind, boolean b) {
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
     * public int size()
     * <p>
     * Returns the number of nodes in the tree.
     */
    public int size() {
        return this.root.getSize();
    }

    /**
     * public int getRoot()
     * <p>
     * Returns the root AVL node, or null if the tree is empty
     */
    public AVLNode getRoot() {
    	if (this.empty())
    		return null;
        return this.root;
    }

    /**
     * public boolean prefixXor(int k)
     *
     * Given an argument k which is a key in the tree, calculate the xor of the values of nodes whose keys are
     * smaller or equal to k.
     *
     * precondition: this.search(k) != null
     *
     */
    public boolean prefixXor(int k) {
    	boolean ret = false;
    	AVLNode node = bin_search(k, false);
    	if (node.hasLeft())
    		ret ^= node.getLeft().getXor();
    	while ((node = node.getParent()) != null)
    		if (node.getKey() < k) {
    			ret ^= node.getValue();
    			if (node.hasLeft())
    				ret ^= node.getLeft().getXor();
    		}
        return ret;
    }

    /**
     * public AVLNode successor
     *
     * given a node 'node' in the tree, return the successor of 'node' in the tree (or null if successor doesn't exist)
     *
     * @param node - the node whose successor should be returned
     * @return the successor of 'node' if exists, null otherwise
     */
    public AVLNode successor(AVLNode node){
        return node.getSucc();
    }
    
    private AVLNode findSucc(AVLNode node) {
    	if (this.max == node)
    		return null;
    	AVLNode succ = node;
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
    
    private AVLNode findPred(AVLNode node) {
    	if (this.min == node)
    		return null;
    	AVLNode pred = node;
    	if (node.hasLeft()) {
    		pred = pred.getLeft();
    		while (pred.hasRight())
    			pred = pred.getRight();
    	} else {
    		pred = pred.getParent();
	    	while (pred.getKey() > node.getKey())
	    		pred = pred.getParent();
    	}
    	return pred;
    }

    /**
     * public boolean succPrefixXor(int k)
     *
     * This function is identical to prefixXor(int k) in terms of input/output. However, the implementation of
     * succPrefixXor should be the following: starting from the minimum-key node, iteratively call successor until
     * you reach the node of key k. Return the xor of all visited nodes.
     *
     * precondition: this.search(k) != null
     */
    public boolean succPrefixXor(int k){
       	boolean ret = false;
    	AVLNode node = this.min;
    	while (node.getKey() < k) {
    		ret ^= node.getValue();
    		node = node.getSucc();
    	}
        return ret;
    }


    /**
     * public class AVLNode
     * <p>
     * This class represents a node in the AVL tree.
     * <p>
     * IMPORTANT: do not change the signatures of any function (i.e. access modifiers, return type, function name and
     * arguments. Changing these would break the automatic tester, and would result in worse grade.
     * <p>
     * However, you are allowed (and required) to implement the given functions, and can add functions of your own
     * according to your needs.
     */
    public class AVLNode {
    	private boolean value;
    	private int key;
    	private int height = 0;
    	private int size = 1;
    	private boolean xor = false;
    	private AVLNode parent = null;
    	private AVLNode left = null;
    	private AVLNode right = null;
    	private AVLNode succ = null;
    	
		public AVLNode() {
			this.key = -1;
			this.height = -1;
			this.size = 0;
		}
		
        /**
		 * @param value
		 * @param key
		 */
		public AVLNode(boolean value, int key) {
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
        public boolean setLeft(AVLNode node) {
        	this.left = node;
        	if (node.isRealNode())
        		node.parent = this;
        	return this.update();
        }

        //returns left child (if there is no left child return null)
        public AVLNode getLeft() {
            if (this.hasLeft())
            	return this.left;
            return null;
        }

        //sets right child
        public boolean setRight(AVLNode node) {
        	this.right = node;
        	if (node.isRealNode())
        		node.parent = this;
        	return this.update();
        }

        //returns right child (if there is no right child return null)
        public AVLNode getRight() {
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
        public void setParent(AVLNode node) {
        	if (node == null) {
        		this.parent = null;
        		return;
        	}
        	if (node.getKey() > this.key)
        		node.left = this;
        	else
        		node.right = this;
            this.parent = node;
            this.parent.update();
        }

        //returns the parent (if there is no parent return null)
        public AVLNode getParent() {
            return this.parent;
        }

        // Returns True if this is a non-virtual AVL node
        public boolean isRealNode() {
            return (this.key != -1);
        }

        // Returns the height of the node (-1 for virtual nodes)
        public int getHeight() {
        	return this.height;
        }

		/**
		 * @return the Balance Factor
		 */
		public int getBF() {
			return this.left.height - this.right.height;
		}

		/**
		 * @return the size
		 */
		public int getSize() {
			return this.size;
		}
        
		/**
		 * @return the xor
		 */
		public boolean getXor() {
			return this.xor;
		}

		/**
		 * @return the succ
		 */
		public AVLNode getSucc() {
			return this.succ;
		}

		/**
		 * @param succ the succ to set
		 */
		public void setSucc(AVLNode succ) {
			this.succ = succ;
		}

		// updates height, size and xor
        public boolean update() {
        	int prev = this.height;
        	this.height = Math.max(this.left.height, this.right.height) + 1;
            this.size = this.left.size + this.right.size + 1;
            this.xor = this.left.xor ^ this.right.xor ^ this.value;
            return (prev != this.height);
        }

		@Override
		public String toString() {
			if (!this.isRealNode())
				return "null";
			return this.value + ":" + Integer.toString(this.key) + " " + "h = " + this.height;
		}
    }
}
