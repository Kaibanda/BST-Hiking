package project4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This is the class that is the program. This means it has the main method.
 * This class is responsible for parsing and validating the command line
 * arguments, reading and parsing the input file, producing any error messages,
 * handling any exceptions thrown by other classes, and producing output.
 * 
 * @author Kai Banda
 * 
 */
public class MountainClimb {

    /**
     * @param args
     */
    public static void main(String[] args) {

        RestStop[] restStops;

        /**
         * Printing an error message if there is no argument in the command line
         */

        /*
         * if (args.length == 0) {
         * System.err.
         * println("Usage Error: the program expects file name as an argument.\n");
         * System.exit(1);
         * }
         * 
         * /**
         * Creating a new file with the command line argument as the file name and
         * catching any errors if the file does not exist or can't be opened
         */
        File treeFile = new File("Tree.csv");

        if (!treeFile.exists()) {
            System.err.println("Error: the file " + treeFile.getAbsolutePath() + " does not exist.\n");
            System.exit(1);
        }
        if (!treeFile.canRead()) {
            System.err.println("Error: the file " + treeFile.getAbsolutePath() +
                    " cannot be opened for reading.\n");
            System.exit(1);
        }

        /**
         * Creating a scanner object to be implemented in the CSV class and checking if
         * it can be opened for reading
         */
        Scanner inTrees = null;

        try {
            inTrees = new Scanner(treeFile);
        } catch (FileNotFoundException e) {
            System.err.println("Error: the file " + treeFile.getAbsolutePath() +
                    " cannot be opened for reading.\n");
            System.exit(1);
        }

        /**
         * Passing the scanner into the CSV class and iterating over each row
         */
        CSV csv = new CSV(inTrees);
        restStops = new RestStop[csv.getNumOfRows()];
        for (int i = 0; i < csv.getNumOfRows(); i++) {
            /**
             * Creating an ArrayList of strings for each row
             */
            ArrayList<String> line = csv.getNextRow();
            RestStop restStop = new RestStop(line.get(0));
            /**
             * Boolean to check if supplies are allowed to be added (in the order supplies,
             * obstacles)
             */
            boolean supplies = true;
            boolean obstacles = true;
            for (int n = 1; n < line.size(); n++) {

                if (line.get(n).equals("food") || line.get(n).equals("raft") || line.get(n).equals("axe")) {
                    /**
                     * Ignores supplies if obstacles have already been added
                     */
                    if (supplies == true) {
                        restStop.addSupply(line.get(n));
                    }
                }
                if (n + 1 < line.size()) {
                    if (line.get(n).equals("fallen") && line.get(n + 1).equals("tree")) {
                        if (obstacles == true) {
                            restStop.addObstacle("fallen tree");
                        }
                        n++;
                        supplies = false;
                    }
                }
                if (line.get(n).equals("river")) {
                    if (obstacles == true) {
                        restStop.addObstacle("river");
                    }
                    supplies = false;
                }
            }
            restStops[i] = restStop;
        }
        /**
         * Constructs a BSTMountain object with restStops from file read, and traverses
         * the mountain with a Hiker obejct using the BSTMountain's hike method,
         * displaying all valid paths down the BSTMountain with the given Hiker
         */
        BSTMountain mountain = new BSTMountain(restStops);
        Hiker hiker = new Hiker();

        ArrayList<ArrayList<RestStop>> allValidPaths = mountain.hike(hiker);

        for (int n = 0; n < allValidPaths.size(); n++) {
            for (int i = 0; i < allValidPaths.get(n).size(); i++) {

                System.out.print(allValidPaths.get(n).get(i).toString());
                if (i < allValidPaths.get(n).size() - 1)
                    System.out.print(" ");

            }
            System.out.println();

        }




      //  System.out.println(mountain.toStringTreeFormat());

    }

    public static class CSV {

        /**
         * A list of lists to store each row of the file in a list of Strings
         */
        private ArrayList<ArrayList<String>> lines;

        /*
         * Keeps track of what line number should be returned by the next call to
         * getNextRow()
         */
        private int nextLine;

        /**
         * Constructs a CSV object using the input from the provided Scanner object.
         * Closes the provided Scanner object on successful termination.
         * 
         * @param in stream from which the data should be read
         * @throws NullPointerException     when {@code in} is null
         * @throws IllegalArgumentException when no data could be read from {@code in}
         */
        public CSV(Scanner in) {

            /**
             * Validate the parameter
             */
            if (in == null)
                throw new NullPointerException("Scanner in is null");

            /**
             * Read the entire file into a single String
             */
            in.useDelimiter("\\Z"); //
            String csvFile = in.next();

            lines = new ArrayList<ArrayList<String>>();

            parseFile(csvFile);

            if (lines.size() < 1)
                throw new IllegalArgumentException("no valid lines present");

            nextLine = 0;

            in.close();
        }

        /**
         * Returns the number of rows successfully read from the CSV file.
         * 
         * @returns number of rows in this object
         */
        public int getNumOfRows() {
            return lines.size();
        }

        /**
         * Returns a list of entries from the next row.
         * This function provides cyclical access to the rows in this object.
         * The initital call to this function returns the first (index 0) row,
         * the next call returns second row, the next call returns third row, etc, until
         * all the rows are returned. After the last row is requested the following call
         * to this function will return first row again.
         * 
         * @return the next row from this object
         */
        public ArrayList<String> getNextRow() {
            ArrayList<String> next = lines.get(nextLine);
            nextLine = (nextLine + 1) % lines.size();

            return next;
        }

        /**
         * Helper method that parses through the string obtained from the input stream
         * and constructs the list of all the rows.
         * 
         * @param csvFile - file to get parsed
         */
        private void parseFile(String csvFile) {

            int charCounter = 0;
            int charTotal = csvFile.length();

            while (charCounter < charTotal) {

                ArrayList<String> currentLine = new ArrayList<String>();

                StringBuffer nextWord = new StringBuffer();
                char nextChar;
                boolean insideQuotes = false;
                boolean insideEntry = false;
                boolean endOfLine = false;

                /**
                 * iterate over all characters in the textLine
                 */
                while (!endOfLine && charCounter < charTotal) {

                    nextChar = csvFile.charAt(charCounter);

                    charCounter++;

                    /**
                     * handle regular quotes as field separators,
                     * (smart quotes are used within entries)
                     */
                    if (nextChar == '"') {// || nextChar == '\u201C' || nextChar =='\u201D') {

                        /**
                         * change insideQuotes flag when nextChar is a quote
                         */
                        if (insideQuotes) {
                            insideQuotes = false;
                            insideEntry = false;
                        } else {
                            insideQuotes = true;
                            insideEntry = true;
                        }
                    } else if (nextChar == '\n') {
                        /**
                         * if new line outside of quotes, this is the end of this row
                         */
                        if (nextChar == '\n' && !insideQuotes) {

                            endOfLine = true;
                        } else if (insideQuotes || insideEntry) {
                            /**
                             * add it to the current entry
                             */
                            nextWord.append(nextChar);
                        } else {
                            /**
                             * skip all spaces between entries
                             */
                            continue;
                        }
                    } else if (nextChar == ' ') {
                        if (insideQuotes) {
                            /**
                             * comma inside an entry
                             */
                            nextWord.append(nextChar);
                        } else {
                            /**
                             * end of entry found
                             */
                            insideEntry = false;
                            currentLine.add(nextWord.toString());
                            nextWord = new StringBuffer();
                        }
                    } else {
                        /**
                         * add all other characters to the nextWord
                         */
                        nextWord.append(nextChar);
                        insideEntry = true;
                    }

                }
                /**
                 * add the last word ( assuming not empty )
                 * trim the white space before adding to the list
                 */
                if (!nextWord.toString().equals("")) {
                    currentLine.add(nextWord.toString().trim());
                }

                lines.add(currentLine);
            }

        }

        /**
         * Returns a string representation of this object. The string
         * representation consists of a list of each row entries enclosed in square
         * brackets
         * ({@code "[]"}). Adjacent entries are separated by the characters
         * {@code ", "} (comma and space).
         * The rows are separated by a newline character {@code \n}.
         *
         * @return a string representation of this object
         */
        public String toString() {
            StringBuffer sb = new StringBuffer();
            for (ArrayList<String> line : lines) {
                sb.append(line);
                sb.append("\n\n");
            }

            return sb.toString();

        }

    }
}