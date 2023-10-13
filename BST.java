package project4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * An implementation of a binary search tree. The elements are ordered using
 * their natural ordering.
 * 
 * This implementation provides guaranteed O(H) (H is the height of this tree
 * which could be as low as logN for balanced trees, but could be as large as N
 * for unbalanced trees) time cost for the basic operations (add, remove and
 * contains).
 * 
 * This class implements many of the methods provided by the Java framework's
 * TreeSet class.
 * 
 * 
 * @author Kai Banda
 * 
 */
public class BST<E extends Comparable<E>> extends Object implements Iterable<E> {
    /**
     * A nested class to represent a node and a data value within the node in the
     * BST, as well as its parent node, height, left and right node references.
     */
    protected class Node implements Comparable<E> {
        /**
         * Node data value
         */
        E data;
        /**
         * Left Node reference for a Node
         */
        Node left;
        /**
         * Right Node reference for a Node
         */
        Node right;
        /**
         * Height value for a Node as an Integer
         */
        int height;
        /**
         * Parent Node reference for a Node
         */
        Node parent;

        public Node(E data, int height) {
            this.data = data;
            this.height = height;
        }

        /**
         * Overrides the default compareTo method from the Comparable interface
         */
        @Override
        public int compareTo(E other) {
            return data.compareTo(other);

        }
    }

    /**
     * Reference to the root node of the tree
     */
    private Node root;
    /**
     * Reference to the size of the tree
     */
    private int size;
    /**
     * Helper variable used by the remove methods
     */
    private boolean found;

    /**
     * Constructs a new, empty tree, sorted according to the natural ordering of its
     * elements. All elements inserted into the tree must implement the Comparable
     * interface.
     * 
     */
    BST() {

        root = null;

    }

    /**
     * Constructs a new tree containing the elements in the specified collection,
     * sorted according to the natural ordering of its elements. All elements
     * inserted into the tree must implement the Comparable interface.
     * 
     * @param collection - collection whose elements will comprise the new tree
     * @throws NullPointerException - if the specified collection is null
     */
    public BST(E[] collection) throws NullPointerException {
        if (collection == null)
            throw new NullPointerException("collection is null");

        Arrays.sort(collection);
        /**
         * Adding elements to the set so the height of this BST is approximately logN
         */
        binSearchAdd(collection, 0, collection.length - 1);

    }

    /**
     * Recursive method to add elements from the collection to this BST<E> object in
     * order to keep the height of the tree approximately logN
     * 
     * @param collection - collection whose elements will comprise the new tree
     * @param low        - first index reference of the collection
     * @param high       - last index reference of the collection
     * @return root node reference of the tree;
     */
    private Node binSearchAdd(E[] collection, int low, int high) {

        if (low > high) {
            return null;
        }
        int mid = (low + high) / 2;
        this.add(collection[mid]);
        /**
         * Calls on right half
         */
        binSearchAdd(collection, mid + 1, high);
        /**
         * Calls on left half
         */
        binSearchAdd(collection, low, mid - 1);

        return root;

    }

    public int bar (Node n ) {
        if (n == null ) 
            return 0;
        int val = 0;
        if (n.left != null)
           val =  (int)n.left.data; 
        return val + bar(n.left) + bar(n.right); 
    }

    /**
     * Returns the root node of this BST<E> object
     * Must be public to allow access for reference by external classes
     * 
     * @return root node of this BST<E> object
     */
    public Node getRoot() {
        return this.root;
    }

    /**
     * Adds the specified element to this set if it is not already present. More
     * formally, adds the specified element e to this tree if the set contains no
     * element e2 such that Objects.equals(e, e2). If this set already contains the
     * element, the call leaves the set unchanged and returns false.
     * 
     * @param e - element to be added to this set
     * @return true if this set did not already contain the specified element
     * @throws NullPointerException - if the specified element is null and this set
     *                              uses natural ordering, or its comparator does
     *                              not permit null elements
     */
    public boolean add(E e) throws NullPointerException {
        if (e == null)
            throw new NullPointerException("added value cannot be null");

        if (root == null) {
            /**
             * Create the first node
             */
            root = new Node(e, 1);
            size++;
            return true;
        }
        Node current = root;
        while (current != null) {

            if (current.compareTo(e) > 0) {
                /**
                 * Add in the left subtree
                 */
                if (current.left == null) {
                    current.left = new Node(e, 1);
                    size++;
                    current.left.parent = current;
                    setHeightAdd(current);
                    return true;
                } else {
                    current = current.left;
                }
            } else if (current.compareTo(e) < 0) {
                /**
                 * Add in the right subtree
                 */
                if (current.right == null) {
                    current.right = new Node(e, 1);
                    size++;
                    current.right.parent = current;
                    setHeightAdd(current);
                    return true;
                } else {
                    current = current.right;
                }
            } else {
                current.data = e;
                if (Objects.equals(current.data, e)) {
                    /**
                     * Duplicate
                     */
                    return false;
                }
            }
        }
        /**
         * We should never get to this line
         */
        return false;

    }

    /**
     * Helper method for add method to set the height of each node
     * 
     * @param n - Parent of leaf node
     */
    private void setHeightAdd(Node n) {
        /**
         * If parent node is null, exit method by returning.
         * Base case
         */
        if (n == null) {
            return;
        }
        /**
         * Recursively goes up the BST and compares left and right child heights to set
         * parent heights additively until base case (root node's parent is null)
         */
        if (n.left != null && n.right != null) {
            if (n.height == n.left.height) {
                n.height++;
                setHeightAdd(n.parent);
            } else if (n.height == n.right.height) {
                n.height++;
                setHeightAdd(n.parent);
            }
        } else if (n.left != null && n.right == null) {
            if (n.height == n.left.height) {
                n.height++;
                setHeightAdd(n.parent);
            }
        } else if (n.left == null && n.right != null) {
            if (n.height == n.right.height) {
                n.height++;
                setHeightAdd(n.parent);
            }
        }

    }

    /**
     * Removes the specified element from this tree if it is present. More formally,
     * removes an element e such that Objects.equals(o, e), if this tree contains
     * such an element. Returns true if this tree contained the element (or
     * equivalently, if this tree changed as a result of the call). (This tree will
     * not contain the element once the call returns.)
     * 
     * @param o - object to be removed from this set, if present
     * @return true if this set contained the specified element
     * @throws ClassCastException   - if the specified object cannot be compared
     *                              with the elements currently in this tree
     * @throws NullPointerException - if the specified element is null
     */
    public boolean remove(Object o) throws ClassCastException, NullPointerException {

        if (o == null)
            throw new NullPointerException("object cannot be null");

        try {
            root = remove(root, o);
            if (found)
                return true;
            else
                return false;
        } catch (ClassCastException ex) {
            throw new ClassCastException("types not the same");
        }

    }

    /**
     * Recursive helper method for remove method
     * Suppresses any unchecked cast warnings
     * 
     * @param n - root node reference
     * @param o - object to be removed from this set, if present
     * @return node that replaces removed object
     * 
     */
    @SuppressWarnings("unchecked")
    private Node remove(Node n, Object o) {

        found = false;

        E val = (E) o;
        Node temp = null;

        if (n == null) {
            return n;
        }

        if (n.compareTo(val) > 0)
            n.left = remove(n.left, o);
        else if (n.compareTo(val) < 0)
            n.right = remove(n.right, o);
        else {
            n.data = val;
            if (Objects.equals(n.data, val)) {
                if (n.left == null) {
                    found = true;
                    size--;
                    return n.right;
                } else if (n.right == null) {
                    found = true;
                    size--;
                    return n.left;
                }
                /**
                 * Successor
                 */
                temp = n.right;
                E min = temp.data;
                while (temp.left != null) {
                    temp = temp.left;
                }
                min = temp.data;

                n.data = min;

                n.right = remove(n.right, n.data);
            }
        }
        /**
         * Sets height for parent node of removed node
         */
        setHeightRemove(n);

        return n;

    }

    /**
     * Helper method for setting node heights for the remove method
     * 
     * @param n - parent of removed node
     */
    private void setHeightRemove(Node n) {
        /**
         * Compares left and right child heights to set
         * parent height subtractively, once to ensure order 1 operation
         */
        if (n.left != null && n.right != null) {
            if (n.height - 2 >= n.left.height && n.height - 2 >= n.right.height) {
                n.height--;
            }
        } else if (n.left != null && n.right == null) {
            if (n.height - 2 == n.left.height) {
                n.height--;
            }
        } else if (n.left == null && n.right != null) {
            if (n.height - 2 == n.right.height) {
                n.height--;
            }
        } else if (n.left == null && n.right == null) {
            n.height--;
        }
    }

    /**
     * Removes all of the elements from this set and sets the size of the BST equal
     * to zero
     */
    public void clear() {

        size = 0;
        root = null;

    }

    /**
     * Returns true if this set contains the specified element. More formally,
     * returns true if and only if this set contains an element e such that
     * Objects.equals(o, e).
     * 
     * @param o - object to be checked for containment in this set
     * @return true if this set contains the specified element
     * @throws ClassCastException   - if the specified object cannot be compared
     *                              with the elements currently in the set
     * @throws NullPointerException - if the specified element is null and this set
     *                              uses natural ordering, or its comparator does
     *                              not permit null elements
     */
    public boolean contains(Object o) throws ClassCastException, NullPointerException {

        if (o == null)
            throw new NullPointerException("object cannot be null");

        try {

            Node n = contains(root, o);

            if (n == null)
                return false;
            else
                return true;
        } catch (ClassCastException ex) {
            throw new ClassCastException("types not the same");
        }

    }

    /**
     * Recursive helper method for contains method
     * Suppresses any unchecked cast warnings
     * 
     * @param n - root node reference
     * @param i - object to be checked for containment in this set
     * @return node representing checked object
     */
    @SuppressWarnings("unchecked")
    private Node contains(Node n, Object i) {

        E node = (E) i;

        if (n == null || Objects.equals(node, n.data)) {
            return n;
        }
        /**
         * Compares values in the root's left and right subtree until finding a matching
         * case or returning null
         */
        if (n.compareTo(node) > 0) {
            return contains(n.left, i);
        } else if (n.compareTo(node) < 0) {
            return contains(n.right, i);
        }

        return n;

    }

    /**
     * Returns the number of elements in this tree.
     * 
     * @return the number of elements in this tree
     */
    public int size() {

        return size;
    }

    /**
     * Returns true if this set contains no elements.
     * 
     * @return true if this set contains no elements
     */
    public boolean isEmpty() {
        /**
         * Set is empty if root is null
         */
        if (root == null)
            return true;
        else
            return false;

    }

    /**
     * Returns the height of this tree.
     * 
     * @return the height of this tree or zero if the tree is empty
     */
    public int height() {

        if (root != null)
            return root.height;
        else
            return 0;

    }

    /**
     * Returns an iterator over the elements in this tree in ascending order.
     * Specified by: iterator in interface Iterable<E extends Comparable<E>>
     * 
     * @return an iterator over the elements in this set in ascending order
     */
    @Override
    public Iterator iterator() {
        return new Iterator("inOrder");
    }

    /**
     * Returns an iterator over the elements in this tree in order of the preorder
     * traversal.
     * 
     * @return an iterator over the elements in this tree in order of the preorder
     *         traversal
     */
    public Iterator preorderIterator() {
        return new Iterator("preOrder");
    }

    /**
     * Returns an iterator over the elements in this tree in order of the postorder
     * traversal.
     * 
     * @return an iterator over the elements in this tree in order of the postorder
     *         traversal
     */
    public Iterator postorderIterator() {
        return new Iterator("postOrder");

    }

    /**
     * Returns the element at the specified position in this tree.
     * 
     * @param index - index of the element to return
     * @return the element at the specified position in this tree
     * @throws IndexOutOfBoundsException - if the index is out of range (index < 0
     *                                   || index >= size)
     */
    public E get(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("index out of bounds");

        return get(root, index);

    }

    /**
     * Recursive helper method for get method
     * 
     * @param n     - root node reference
     * @param index - index of the element to return
     * @return E value of the node at the specified index in this set
     */
    private E get(Node n, int index) {

        ArrayList<BST<E>.Node> stack = new ArrayList<BST<E>.Node>();
        Node current = root;
        int count = 0;
        ArrayList<E> list = new ArrayList<E>();

        if (root != null) {
            /**
             * Traverses the tree in an inorder traversal to get values from smallest to
             * highest until specified index is reached
             */
            boolean done = false;
            while (done == false) {
                if (current != null) {
                    stack.add(current);
                    current = current.left;
                } else if (stack.size() > 0) {
                    current = stack.remove(stack.size() - 1);
                    list.add(current.data);
                    if (count == index)
                        done = true;
                    count++;
                    current = current.right;
                } else
                    done = true;
            }
        }
        return list.get(count - 1);
    }

    /**
     * Returns the least element in this tree greater than or equal to the given
     * element, or null if there is no such element.
     * 
     * @param e - the value to match
     * @return the least element greater than or equal to e, or null if there is no
     *         such element
     * @throws ClassCastException   - if the specified element cannot be compared
     *                              with the elements currently in the set
     * @throws NullPointerException - if the specified element is null
     */
    public E ceiling(E e) throws ClassCastException, NullPointerException {

        if (e == null)
            throw new NullPointerException("root is null");
        try {
            return ceiling(root, e);
        } catch (ClassCastException ex) {

            throw new ClassCastException("types not the same");
        }

    }

    /**
     * Recursive helper method for ceiling method
     * 
     * @param node - root node reference
     * @param e    - the value to match
     * @return matched node value
     */
    private E ceiling(Node node, E e) {

        if (node == null)
            return null;
        if (node.compareTo(e) == 0)
            return e;

        if (node.compareTo(e) < 0) {
            return ceiling(node.right, e);
        }

        E ceiling = ceiling(node.left, e);
        if (ceiling != null && e != null || ceiling != null && e == null)
            return ceiling;
        else
            return node.data;

    }

    /**
     * Returns the greatest element in this set less than or equal to the given
     * element, or null if there is no such element.
     * 
     * @param e - the value to match
     * @return the greatest element less than or equal to e, or null if there is no
     *         such element
     * @throws ClassCastException   - if the specified element cannot be compared
     *                              with the elements currently in the set
     * @throws NullPointerException - if the specified element is null
     */
    public E floor(E e) throws ClassCastException, NullPointerException {

        if (e == null)
            throw new NullPointerException("root is null");
        try {

            return floor(root, e);
        } catch (ClassCastException ex) {
            throw new ClassCastException("types not the same");
        }

    }

    /**
     * Recursive helper method for floor method
     * 
     * @param node - root node reference
     * @param e    - the value to match
     * @return matched node value
     */
    private E floor(Node node, E e) {

        if (node == null)
            return null;

        if (node.compareTo(e) == 0)
            return e;

        if (node.compareTo(e) > 0)
            return floor(node.left, e);

        E floor = floor(node.right, e);
        if (floor == null && e == null || floor != null && e != null)
            return floor;
        else
            return node.data;
    }

    /**
     * Returns the first (lowest) element currently in this tree.
     * This operation must be order H
     * 
     * @return the first (lowest) element currently in this tree
     * @throws NoSuchElementException - if this set is empty
     */
    public E first() throws NoSuchElementException {

        if (root == null) {
            throw new NoSuchElementException("set is empty");
        }

        Node current = root;
        /**
         * Traverses down the left subtree of the root until the left-most (lowest)
         * value is reached
         */
        while (current.left != null) {
            current = current.left;
        }

        return current.data;

    }

    /**
     * Returns the last (highest) element currently in this tree.
     * 
     * @return the last (highest) element currently in this tree
     * @throws NoSuchElementException - if this set is empty
     */
    public E last() throws NoSuchElementException {

        if (root == null)
            throw new NoSuchElementException("set is empty");

        Node current = root;
        /**
         * Traverses down the right subtree of the root until the left-most (highest)
         * value is reached
         */
        while (current.right != null) {
            current = current.right;
        }

        return current.data;
    }

    /**
     * Returns the greatest element in this set strictly less than the given
     * element, or null if there is no such element.
     * 
     * @param e - the value to match
     * @return the greatest element less than e, or null if there is no such element
     * @throws ClassCastException   - if the specified element cannot be compared
     *                              with the elements currently in the set
     * @throws NullPointerException - if the specified element is null
     */
    public E lower(E e) throws ClassCastException, NullPointerException {

        if (root == null)
            throw new NullPointerException("root is null");
        try {
            return lower(root, e);
        } catch (ClassCastException ex) {

            throw new ClassCastException("types not the same");
        }

    }

    /**
     * Recursive helper method for lower method
     * 
     * @param node - root node reference
     * @param e    - the value to match
     * @return matched node value
     */
    private E lower(Node node, E e) {

        if (node == null)
            return null;

        if (node.compareTo(e) == 0)
            return lower(node.left, e);

        if (node.compareTo(e) > 0)
            return lower(node.left, e);

        E lower = lower(node.right, e);
        if (lower != null && e != null)
            return lower;
        else
            return node.data;
    }

    /**
     * Returns the least element in this tree strictly greater than the given
     * element, or null if there is no such element.
     * 
     * @param e - the value to match
     * @return the least element greater than e, or null if there is no such element
     * @throws ClassCastException   - if the specified element cannot be compared
     *                              with the elements currently in the set
     * @throws NullPointerException - if the specified element is null
     */
    public E higher(E e) throws ClassCastException, NullPointerException {

        if (e == null)
            throw new NullPointerException("root is null");

        try {

            return higher(root, e);
        } catch (ClassCastException ex) {
            throw new ClassCastException("types not the same");
        }

    }

    /**
     * Recursive helper method for the higher method
     * 
     * @param node - root node reference
     * @param e    - the value to match
     * @return - matched node value
     */
    private E higher(Node node, E e) {

        if (node == null)
            return null;

        if (node.compareTo(e) == 0)
            return higher(node.right, e);

        if (node.compareTo(e) < 0) {
            return higher(node.right, e);
        }

        E higher = higher(node.left, e);
        if (higher != null && e == null || higher != null)
            return higher;
        else
            return node.data;

    }

    /**
     * Compares the specified object with this tree for equality. Returns true if
     * the given object is also a tree, the two trees have the same size, and every
     * member of the given tree is contained in this tree.
     * Overrides equals in class Object
     * Suppresses any unchecked cast warnings
     * 
     * @param obj - object to be compared for equality with this tree
     * @return true if the specified object is equal to this tree
     * 
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        boolean isTrue = true;
        if (!(obj instanceof BST)) {
            return false;
        }
        /**
         * Uses contains method to check if each element in the object is contained in
         * this BST
         */
        BST<E> comp = (BST<E>) obj;
        Iterator it = comp.preorderIterator();
        while (it.hasNext()) {
            if (!(this.contains(it.next()))) {
                isTrue = false;
            }
        }
        if (isTrue && this.size() == comp.size())
            return true;

        return false;

    }

    /**
     * The BST<E> class implements Iterable<E> interface. This means that its
     * iterator() method returns an instance of a class that implements the
     * Iterator<E> interface. The iterator() method in the BST<E> class returns an
     * iterator instance that accesses the values in the tree according to the
     * inorder traversal of the binary search tree. The two additional methods
     * preorderIterator() and postOrederIterator() wtihin the BST<E> class return
     * iterators that access the values in the tree according to the preorder
     * and postorder traversals, respectively.
     * 
     */
    public class Iterator extends BST<E> implements java.util.Iterator<E> {

        /**
         * Represents the current node in the iterator, and is the root node reference
         */
        private Node current;

        /**
         * String value of the parameter in the constructor, to determine if the list is
         * to be traversed in reverse or sequential order
         */
        private String order;

        /**
         * Integer value of the index in the next() method of this Iterator object
         */
        private int index = 0;

        /**
         * ArrayList<BST<E>.Node> object representing a inorder traversal of the BST if
         * the constructor is called with "inOrder" as its parameter value
         */
        private ArrayList<BST<E>.Node> inList = new ArrayList<BST<E>.Node>();

        /**
         * ArrayList<BST<E>.Node> object representing a preorder traversal of the BST if
         * the constructor is called with "preOrder" as its parameter value
         */
        private ArrayList<BST<E>.Node> preList = new ArrayList<BST<E>.Node>();

        /**
         * ArrayList<BST<E>.Node> object representing a postorder traversal of the BST
         * if the constructor is called with "postOrder" as its parameter value
         */
        private ArrayList<BST<E>.Node> postList = new ArrayList<BST<E>.Node>();

        /**
         * Constructor sets the current Node reference to the root node of the BST
         * and sets the order String reference to the parameter value
         * 
         * @param current String representation of the order of this Iteraror object, as
         *                an inorder, preorder, or postorder Iterator
         */
        Iterator(String current) {
            this.current = root;
            this.order = current;

            if (order.equals("inOrder"))
                inOrder(root);
            else if (order.equals("preOrder"))
                preOrder(root);
            else if (order.equals("postOrder"))
                postOrder(root);

        }

        /**
         * Fills preList ArrayList<BST<E>.Node> with node values representing a preOrder
         * traversal of the BST
         * 
         * @param n - root node reference
         */
        private void preOrder(Node n) {
            ArrayList<BST<E>.Node> stack = new ArrayList<BST<E>.Node>();
            Node current = n;

            if (root != null) {
                /**
                 * Fills the ArrayList<BST<E>.Node> preList with values in a preorder traversal
                 * of this tree
                 */
                boolean done = false;
                while (done == false) {
                    if (current != null) {
                        stack.add(current);
                        preList.add(current);
                        current = current.left;
                    } else if (stack.size() > 0) {
                        current = stack.remove(stack.size() - 1);
                        current = current.right;
                    } else
                        done = true;
                }
            }

        }

        /**
         * Fills inList ArrayList<BST<E>.Node> with node values representing an inOrder
         * traversal of the BST
         * 
         * @param n - root node reference
         */
        private void inOrder(Node n) {
            ArrayList<BST<E>.Node> stack = new ArrayList<BST<E>.Node>();
            Node current = n;

            if (root != null) {
                /**
                 * Fills the ArrayList<BST<E>.Node> inList with values in an inorder traversal
                 * of this tree
                 */
                boolean done = false;
                while (done == false) {
                    if (current != null) {
                        stack.add(current);
                        current = current.left;
                    } else if (stack.size() > 0) {
                        current = stack.remove(stack.size() - 1);
                        inList.add(current);
                        current = current.right;
                    } else
                        done = true;
                }
            }
        }

        /**
         * Fills postList ArrayList<BST<E>.Node> with node values representing a
         * postOrder traversal of the BST
         * 
         * @param n - root node reference
         */
        private void postOrder(Node n) {
            ArrayList<BST<E>.Node> stack = new ArrayList<BST<E>.Node>();
            Node current = n;

            if (current != null) {
                Node p = null;
                /**
                 * Fills the ArrayList<BST<E>.Node> postList with values in a postorder
                 * traversal of this tree
                 */
                while (current != null || stack.size() > 0) {
                    if (current != null) {
                        stack.add(current);
                        current = current.left;
                    } else {
                        current = stack.get(stack.size() - 1);
                        if (current.right == null || current.right == p) {
                            postList.add(current);
                            stack.remove(stack.size() - 1);
                            p = current;
                            current = null;

                        } else
                            current = current.right;
                    }
                }

            }

        }

        /**
         * Returns true if the iteration has more elements. (In other words, returns
         * true if next() would return an element rather than throwing an exception.)
         * 
         * @return boolean value for if the current node is valid
         */
        @Override
        public boolean hasNext() {

            if (current != null) {
                return true;
            }
            return false;

        }

        /**
         * Returns the next element in the iteration.
         * 
         * @return E data value of the current node
         */
        @Override
        public E next() {
            Node c;

            if (hasNext()) {
                /**
                 * Uses a unique list to traverse depending on the order value of this iterator
                 * class
                 */
                if (order.equals("preOrder") && index == 0) {
                    c = preList.get(index);
                    index++;
                }
                if (order.equals("postOrder") && index == 0) {
                    c = postList.get(index);
                    index++;
                } else if (order == "inOrder" && index == 0) {
                    c = inList.get(index);
                    index++;
                } else
                    c = current;

                if (order.equals("preOrder")) {
                    if (index > preList.size() - 1)
                        current = null;
                    else
                        current = preList.get(index);
                }
                if (order.equals("inOrder")) {
                    if (index > inList.size() - 1)
                        current = null;
                    else
                        current = inList.get(index);

                }
                if (order.equals("postOrder")) {
                    if (index > postList.size() - 1)
                        current = null;
                    else
                        current = postList.get(index);
                }

                index++;
                return c.data;
            }
            return null;
        }

        /**
         * Removes from the underlying collection the last element returned by this
         * iterator (optional operation). This method can be called only once per call
         * to next(). This operation is unsupported by this program.
         * 
         * @throws UnsupportedOperationException - this operation is unsupported
         */
        @Override
        public void remove() throws UnsupportedOperationException {

            throw new UnsupportedOperationException("remove from iterator not supported");

        }

    }

    /**
     * Returns a string representation of this tree.
     * Overrides toString in class Object
     */
    @Override
    public String toString() {

        StringBuffer sb = new StringBuffer();
        toString(sb, root);
        return sb.toString();

    }

    /**
     * Recursive helper method for toStrting method
     * 
     * @param sb   - StringBuffer object
     * @param node - root node reference
     */
    private void toString(StringBuffer sb, Node node) {

        if (node == null) {
            return;
        } else {
            sb.append(node.data + " ");
        }

        /**
         * Display the left subtree
         */
        toString(sb, node.left);
        /**
         * Display the right subtree
         */
        toString(sb, node.right);
    }

    /**
     * Produces tree like string representation of this tree.
     * This operation should be O(N).
     * 
     * @return string containing tree-like representation of this tree.
     */
    public String toStringTreeFormat() {
        StringBuffer sb = new StringBuffer();
        toStringTreeFormat(sb, root, 0);
        return sb.toString();
    }

    /**
     * Recursive helper method for toStringTreeFormat method
     * 
     * @param sb    - StringBuffer object
     * @param node  - root node reference
     * @param level - level of each node
     */
    private void toStringTreeFormat(StringBuffer sb, Node node, int level) {
        /**
         * Display the node
         */
        if (level > 0) {
            for (int i = 0; i < level - 1; i++) {
                sb.append("   ");
            }
            sb.append("|--");
        }
        if (node == null) {
            sb.append("->\n");
            return;
        } else {
            sb.append(node.data + "\n");
        }

        /**
         * Display the left subtree
         */
        toStringTreeFormat(sb, node.left, level + 1);
        /**
         * Display the right subtree
         */
        toStringTreeFormat(sb, node.right, level + 1);
    }
}