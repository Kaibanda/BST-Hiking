package project4;

import java.util.ArrayList;

/**
 * This class represents a single rest stop. It is capable of
 * storing the label of the rest stop along with a list of the supplies that a
 * hiker can collect at this rest-stop and a list of obstacles that a hiker may
 * encounter at this rest-stop. It implements the Comparable interface.
 * 
 * @author Kai Banda
 * 
 */
public class RestStop implements Comparable<RestStop> {

    /**
     * Represents all of the supplies contained in this RestStop object
     */
    private ArrayList<String> supplies = new ArrayList<String>();
    /**
     * Represents all of the obstacles contained in this RestStop object
     */
    private ArrayList<String> obstacles = new ArrayList<String>();
    /**
     * Represents the name of the RestStop obejct
     */
    private String label;

    /**
     * Constructs a new RestStop object with a label assigned
     * 
     * @param label - name of the RestStop object
     */
    RestStop(String label) {

        this.label = label;

    }

    /**
     * Adds a supply to the ArrayList<String> object that represents the supplies at
     * this RestStop
     * 
     * @param supply - name of supply to be added
     */
    public void addSupply(String supply) {

        supplies.add(supply);

    }

    /**
     * Adds an obstacle to the ArrayList<String> object that represents the
     * obstacles at this RestStop
     * 
     * @param obstacle - name of the obstacle to be added
     */
    public void addObstacle(String obstacle) {

        obstacles.add(obstacle);

    }

    /**
     * Returns a list of all supplies at this RestStop
     * 
     * @return ArrayList<String> - list of supplies in this RestStop object
     */
    public ArrayList<String> getSupplies() {
        return supplies;
    }

    /**
     * Returns a list of all obstacles at this RestStop
     * 
     * @return ArrayList<String> - list of obstacles in this RestStop object
     */
    public ArrayList<String> getObstacles() {
        return obstacles;
    }

    /**
     * Returns the name of this RestStop
     * 
     * @return String - label of this RestStop object
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns a string representation of this RestStop object
     * 
     * @return String - representation of this RestStop object
     */
    public String toString() {
        return this.label;
    }

    /**
     * Compares two RestStop objects and returns a comparison between their names
     * Overrides the default compareTo method from the Comparable interface
     * 
     * @param o - RestStop object to be compared
     * @return int - value of compareTo between this RestStop object's label and
     *         the passed RestStop object's label
     */
    @Override
    public int compareTo(RestStop o) {
        return this.getLabel().compareTo(o.getLabel());
    }
}
