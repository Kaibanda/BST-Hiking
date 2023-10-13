package project4;

import java.util.ArrayList;

/**
 * This class represents a hiker traveling down the mountain. An object of
 * this class is capable of keeping track of all the supplies that the
 * hiker has in their possession. This information is updated as the
 * hiker travels along a trail and consumes supplies (by either eating along the
 * way, or using the tools to clear the path from a broken tree, or cross the
 * river).
 * 
 * @author Kai Banda
 * 
 */
public class Hiker {

    /**
     * Represents the supplies that this Hiker object is carrying
     */
    private ArrayList<String> supplies = new ArrayList<String>();

    /**
     * Constructs a new Hiker object with no supplies
     */
    public Hiker() {

    }

    /**
     * Adds all supplies from a given EestStop object to this Hiker object
     * 
     * @param stop - RestStop which may contain supplies
     */
    public void addSupplies(RestStop stop) {

        supplies.addAll(stop.getSupplies());

    }

    /**
     * Returns true if the ArrayList<String> object of supplies contains the supply
     * (as a String), and removes that supply from the ArrayList<String> object
     * 
     * @param supply - name of supply to be removed
     * @return true if the ArrayList<String> object of supplies contains the supply
     *         (as a String)
     */
    public boolean removeSupply(String supply) {
        if (supplies.contains(supply)) {
            supplies.remove(supplies.indexOf(supply));
            return true;
        } else
            return false;

    }

    /**
     * Clears all supplies from this Hiker object
     */
    public void clearSupplies() {
        supplies.clear();
    }
}