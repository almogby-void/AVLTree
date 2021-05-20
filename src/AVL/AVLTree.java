package AVL;
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
    
    public AVLNode bin_search(int k, boolean b) {
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
    		criminal.setRight(virt);
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
    		criminal.setLeft(virt);
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
    		this.root = new AVLNode(i, k);
    		return 0;
    	}
    	AVLNode x = this.bin_search(k, true);
        if (x.getKey() == k)
        	return -1;
        int count = 0;
    	AVLNode new_node = new AVLNode(i, k);
    	if (x.getKey() < k)
    		x.setRight(new_node);
    	else
    		x.setLeft(new_node);
    	while ((x = x.getParent()) != null) {
    		x.update();
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
    			count++;
    		}
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
        int count = 0;
        if (x.getHeight() == 0)
        	remove(x);
        else if (x.getLeft().isRealNode())
        	if (x.getRight().isRealNode()) {
        		AVLNode y = x.getRight();
        		if (!y.getLeft().isRealNode())
        			y.getParent().setRight(y.getRight());
        		else {
	        		while (y.getLeft().isRealNode())
	        			y = y.getLeft();
	        		y.getParent().setLeft(y.getRight());
        		}
        		y.setLeft(x.getLeft());
        		y.setRight(x.getRight());
        		y.setParent(x.getParent());
        	}
        	else
        		x.getLeft().setParent(x.getParent());
        else
        	x.getRight().setParent(x.getParent());
        while ((x = x.getParent()) != null) {
    		x.update();
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
    			count++;
    		}
    	}
        return count;
    }
    
    public void remove(AVLNode node) {
    	if (this.root == node) {
    		this.root = this.virt;
    		return;
    	}
    	AVLNode par = node.getParent();
    	if (par.getLeft() == node)
    		par.setLeft(this.virt);
    	else
    		par.setRight(this.virt);
    }

    /**
     * public Boolean min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     */
    public Boolean min() {
        return null; // to be replaced by student code
    }

    /**
     * public Boolean max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     */
    public Boolean max() {
        return null; // to be replaced by student code
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() {
        int[] arr = new int[42]; // to be replaced by student code
        return arr;              // to be replaced by student code
    }

    /**
     * public boolean[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public boolean[] infoToArray() {
        boolean[] arr = new boolean[42]; // to be replaced by student code
        return arr;                    // to be replaced by student code
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
    public boolean prefixXor(int k){
        return false;
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
        return null;
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
        return false;
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
    	private int BF = 0;
    	private int size = 1;
    	private AVLNode parent = null;
    	private AVLNode left = null;
    	private AVLNode right = null;
    	
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
        	if (!isRealNode())
        		return null;
            return this.value;
        }

        //sets left child
        public void setLeft(AVLNode node) {
        	this.left = node;
        	if (node.isRealNode())
        		node.parent = this;
        	this.update();
        }

        //returns left child (if there is no left child return null)
        public AVLNode getLeft() {
            if (!this.left.isRealNode())
            	return null;
        	return this.left;
        }

        //sets right child
        public void setRight(AVLNode node) {
        	this.right = node;
        	if (node.isRealNode())
        		node.parent = this;
        	this.update();
        }

        //returns right child (if there is no right child return null)
        public AVLNode getRight() {
        	if (!this.right.isRealNode())
            	return null;
        	return this.right;
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

        // sets the height of the node
        public void setHeight(int height) {
        	this.height = height;
        }

		/**
		 * @return the BF
		 */
		public int getBF() {
			return this.BF;
		}

		/**
		 * @param BF – the BF to set
		 */
		public void setBF(int BF) {
			this.BF = BF;
		}

		/**
		 * @return the size
		 */
		public int getSize() {
			return this.size;
		}

		/**
		 * @param size – the size to set
		 */
		public void setSize(int size) {
			this.size = size;
		}
        
		// updates height, BF, size
        public void update() {
        	this.setHeight(Math.max(this.left.height, this.right.height) + 1);
            this.setSize(this.left.size + this.right.size + 1);
            this.setBF(this.left.height - this.right.height);
        }

		@Override
		public String toString() {
			if (!this.isRealNode())
				return "null";
			return this.value + ":" + Integer.toString(this.key) + " " + "h = " + this.height;
		}
    }

}


