package AVL;

import java.lang.reflect.Array;

import AVL.AVLTree.AVLNode;

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
        if (k < this.min.getKey())
            this.min = new_node;
        else if (k > this.max.getKey())
            this.max = new_node;
        new_node.setSucc(findSucc(new_node));
        AVLNode pred = findPred(new_node);
        if (pred != null)
            pred.setSucc(new_node);
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
        else if (x.hasLeft())
            if (x.hasRight()) {
                AVLNode y = x.getRight();
                if (!y.hasLeft())
                    y.getParent().setRight(y.getRight());
                else {
                    while (y.hasLeft())
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

    private void remove(AVLNode node) {
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
        IntArray marr = new IntArray(this.size());
        if (this.size() > 0)
            drec2(this.getRoot(), marr, 0);
        return (int[]) marr.getArr();
    }

    /**
     * public boolean[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public boolean[] infoToArray() {
        BooleanArray marr = new BooleanArray(this.size());
        if (this.size() > 0)
            drec2(this.getRoot(), marr, 0);
        return (boolean[]) marr.getArr();
    }
    
    private int drec2(AVLNode node, MyArray marr, int ind) {
        if (node.hasLeft())
            ind = drec2(node.getLeft(), marr, ind);
        marr.setAt(ind++, node);
        if (node.hasRight())
            ind = drec2(node.getRight(), marr, ind);
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
        private int size = 1;
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
        public void setLeft(AVLNode node) {
            this.left = node;
            if (node.isRealNode())
                node.parent = this;
            this.update();
        }

        //returns left child (if there is no left child return null)
        public AVLNode getLeft() {
            if (this.hasLeft())
                return this.left;
            return null;
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

        // updates height, BF, size
        public void update() {
            this.height = Math.max(this.left.height, this.right.height) + 1;
            this.size = this.left.size + this.right.size + 1;
        }

        @Override
        public String toString() {
            if (!this.isRealNode())
                return "null";
            return this.value + ":" + Integer.toString(this.key) + " " + "h = " + this.height;
        }
    }
}

abstract class MyArray {
    public Object arr;
    public int len;

    MyArray(int capacity) {
        this.len = capacity;
    }

    public Object getArr() {
        return this.arr;
    }

    abstract void setAt(int ind, AVLNode node);
}

class IntArray extends MyArray {
    public IntArray(int capacity) {
        super(capacity);
        this.arr = new int[this.len];
    }

    @Override
    void setAt(int ind, AVLNode node) {
        Array.setInt(this.arr, ind, node.getKey());
    }
}

class BooleanArray extends MyArray {
    public BooleanArray(int capacity) {
        super(capacity);
        this.arr = new boolean[this.len];
    }

    @Override
    void setAt(int ind, AVLNode node) {
        Array.setBoolean(this.arr, ind, node.getValue());
    }
}
