package project4;

import java.util.ArrayList;

/**
 * This class inherits from the BST<E> class as BST<RestStop>. This
 * class represents the mountain itself (therefore, it is not
 * generic and its nodes store data items of type RestStop)
 * 
 * @author Kai Banda
 * 
 */
public class BSTMountain extends BST<RestStop> {

    /**
     * Represents a list of RestStop objects that comprise the tree
     */
    private ArrayList<RestStop> stops = new ArrayList<RestStop>();
    /**
     * Represents a list of paths down the BSTMountain
     */
    private ArrayList<ArrayList<RestStop>> listOfPaths = new ArrayList<ArrayList<RestStop>>();
    /**
     * Helper variable for validating paths given a Hiker object
     */
    private boolean validPath;
    /**
     * Helper variable for validating RestStop objects given a Hiker object
     */
    private boolean validStop;

    /**
     * Constructor creates this BSTMountain using the stops Array,
     * validates the tree for equality of the BST<E> tree, and creates a root
     * node and node.left and node.right references for this BSTMountain
     * 
     * @param stops - collection whose elements will comprise the new tree
     */
    public BSTMountain(RestStop[] stops) {
        /**
         * Using superclass add method to add to this BSTMountain
         */
        for (int i = 0; i < stops.length; i++) {
            this.add(stops[i]);
        }

    }

    /**
     * Returns a two dimensional ArrayList object of RestStop objects (an ArrayList
     * of Arraylist<RestStop> objects) representing all paths down the
     * BSTMountain object
     * 
     * @return ArrayList<ArrayList<RestStop>> - list of all paths down the
     *         BSTMountain
     */
    private ArrayList<ArrayList<RestStop>> getAllPaths() {

        if (getRoot() == null)
            return listOfPaths;
        /**
         * Longest path is the tree's height with each leaf being at height 1, so array
         * length is set to the height of the BST representing this BSTMountain
         */
        helperPaths(getRoot(), new RestStop[height()], 0);

        return listOfPaths;
    }

    /**
     * Recursive helper method for getAllPaths method
     * 
     * @param root   - root node reference
     * @param array  - an array to store each possible path down the BST (without
     *               a hiker involved)
     * @param length - keeps track of index in array
     */
    private void helperPaths(Node root, RestStop[] array, int length) {

        if (root == null)
            return;
        array[length] = root.data;
        length++;
        if (root.left == null && root.right == null) {

            for (int i = 0; i < length; i++) {
                stops.add(array[i]);
            }
            ArrayList<RestStop> stops1 = new ArrayList<RestStop>(stops);
            /**
             * Adds path to listOfPaths
             */
            listOfPaths.add(stops1);

            stops.clear();

            return;

        } else {
            /**
             * Traverses left subtree
             */
            helperPaths(root.left, array, length);
            /**
             * Traverses right subtree
             */
            helperPaths(root.right, array, length);

        }
    }

    /**
     * Returns a two dimensional ArrayList object of RestStop objects (an ArrayList
     * of Arraylist<RestStop> objects) representing all valid paths down the
     * BSTMountain object with a passed Hiker obejct
     * 
     * @param h - Hiker object to be referenced
     * @return ArrayList<ArrayList<RestStop>> - list of valid paths given the passed
     *         Hiker object
     */
    public ArrayList<ArrayList<RestStop>> hike(Hiker h) {

        ArrayList<ArrayList<RestStop>> allPaths = this.getAllPaths();
        ArrayList<ArrayList<RestStop>> allValidPaths = new ArrayList<ArrayList<RestStop>>();
        ArrayList<String> obstacles;
        /**
         * Traverses each path and adds and removes supplies from passed Hiker object
         * for each RestStop object and its obstacle and supply lists
         */
        for (int n = 0; n < allPaths.size(); n++) {
            validPath = true;
            for (int o = 0; o < allPaths.get(n).size(); o++) {

                validStop = true;
                h.addSupplies(allPaths.get(n).get(o));

                obstacles = allPaths.get(n).get(o).getObstacles();
                if (o < allPaths.get(n).size() - 1) {
                    if (!(h.removeSupply("food")))
                        validStop = false;
                }
                if (obstacles.contains("river")) {
                    if (!(h.removeSupply("raft")))
                        validStop = false;
                }
                if (obstacles.contains("fallen tree")) {
                    if (!(h.removeSupply("axe")))
                        validStop = false;
                }
                if (o == allPaths.get(n).size() - 1) {
                    if (o < this.height() - 1)
                        validStop = false;
                }
                if (!(validStop)) {
                    validPath = false;
                }
            }
            /**
             * Clears any supplies stored in the Hiker object after traversing down a path
             */
            h.clearSupplies();
            if (validPath)
                allValidPaths.add(allPaths.get(n));
        }
        return allValidPaths;
    }
}
