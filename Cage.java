import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;


public class Cage {
    ArrayList<Cell> myCells = new ArrayList<Cell>();
    private String operation;
    private int targetValue;

    // Constructor
    public Cage (ArrayList<Cell> cells, String op, int target) {
        this.myCells = cells;
        this.operation = op;
        this.targetValue = target;
    }

    public void formatCage () {
        // Make the outside of the cage thicker so that it is visible to the user
        // Do this by assuming every cell has all 4 borders thick, then making neighbouring cells have thin borders
        // Set booleans for every cell in the cage
        for (Cell c : this.myCells) {
            boolean[] t = {true, true, true, true};
            c.setAllCages(t);
        }
        // Alter the booleans for neighbour cells
        for (Cell a : this.myCells) {
            for (Cell b : this.myCells) {
                // Check every pair of cells in the cage to see if they neighbour
                if (a.getRow() == b.getRow() && (a.getColumn() - b.getColumn()) == 1) {
                    // If the cell b is above cell a remove the thick border between them
                    a.setTopCage(false);
                    b.setBottomCage(false);
                }
                if (a.getColumn() == b.getColumn() && (a.getRow() - b.getRow()) == 1) {
                    // If the cell b is to the left of cell a remove the thick border between them
                    a.setLeftCage(false);
                    b.setRightCage(false);
                }
            }
        }
        // Apply the changes to every cell
        for (Cell c : this.myCells) {
            c.dispCage();
        }

        // Only the upper left most cell should contain the operation and target value
        Cell topLeftCell = this.getTopLeftCell();
        topLeftCell.setCageValue(" " + Integer.toString(this.targetValue) + this.operation);
    }

    // Check if the entered values in the cells can be used with the operation to reach the target value
    public boolean checkCorrect () {
        // Only highlight the cells if they are completely filled
        boolean completelyFilled = true;
        Iterator<Cell> emptyIter = myCells.iterator();
        while (emptyIter.hasNext()) {
            if (emptyIter.next().getDisplay() == "") {
                completelyFilled = false;
            }
        }
        if (completelyFilled == false) {
            return true;
        }
        if (this.operation.equals("+")) {
            // Check that the sum of all the cells is equal or not to the target value
            int total = 0;
            Iterator<Cell> sumIter = myCells.iterator();
            while (sumIter.hasNext()) {
                total += Integer.valueOf(sumIter.next().getDisplay());
            }
            if (total == this.targetValue) {
                return true;
            } else {
                return false;
            }
        } else if (this.operation.equals("x")) {
            // Check that the product of all the cells is equal or not to the target value
            int total = 1;
            Iterator<Cell> mulIter = myCells.iterator();
            while (mulIter.hasNext()) {
                total = total*Integer.valueOf(mulIter.next().getDisplay());
            }
            if (total == this.targetValue) {
                return true;
            } else {
                return false;
            }
        } else if (this.operation.equals("-")) {
            // Check that the minus operator can be used to reach the target value
            // Create a list of the numbers in the cells so that it can be sorted from smallest to greatest
            ArrayList<Integer> cellNumbers = new ArrayList<Integer>();
            int total = 0;
            Iterator<Cell> minusIter = myCells.iterator();
            while (minusIter.hasNext()) {
                int nextNum = Integer.valueOf(minusIter.next().getDisplay());
                cellNumbers.add(nextNum);
                total += nextNum;
            }
            Collections.sort(cellNumbers);
            // If the target value is equal to the greatest number minus the sum of all the others, the cage is correct
            int sumOfOthers = total - cellNumbers.get(cellNumbers.size() - 1);
            if (cellNumbers.get(cellNumbers.size() - 1) - sumOfOthers == this.targetValue) {
                return true;
            } else {
                return false;
            }
        } else if (this.operation.equals(" ")) {
            // Get the value entered in the only cell
            int enteredValue = Integer.valueOf(this.myCells.get(0).getDisplay());
            if (this.targetValue == enteredValue) {
                return true;
            } else {
                return false;
            }
        } else {
            // Check that the divide operator can be used to reach the target value
            // Create a list of the numbers in the cells so that it can be sorted from smallest to greatest
            ArrayList<Integer> cellNumbers = new ArrayList<Integer>();
            int total = 1;
            Iterator<Cell> divIter = myCells.iterator();
            while (divIter.hasNext()) {
                int nextNum = Integer.valueOf(divIter.next().getDisplay());
                cellNumbers.add(nextNum);
                total = total*nextNum;
            }
            Collections.sort(cellNumbers);
            // If the target value is equal to the greatest number divided by the product of all the others, the cage is correct
            float productOfOthers = total / cellNumbers.get(cellNumbers.size() - 1);
            if (cellNumbers.get(cellNumbers.size() - 1) / productOfOthers == this.targetValue) {
                return true;
            } else {
                return false;
            }
        }
    }

    public Cell getTopLeftCell () {
        Cell topLeft = null;
        for (Cell c : this.myCells) {
            if (topLeft == null) {
                topLeft = c;
            }
            // If the cell is more left and more up than the current 'topLeft' cell, replace it
            if (c.getRow() <= topLeft.getRow()) {
                if (c.getColumn() < topLeft.getColumn()) {
                    topLeft = c;
                }
            }
        }
        return topLeft;
    }

    public boolean areCellsAdjacent () {
        // Create a copy of myCells
        ArrayList<Cell> copy = new ArrayList<Cell>();
        for (Cell c : this.myCells) {
            copy.add(c);
        }
        ArrayList<Cell> cluster = new ArrayList<Cell>();
        if (copy.size() > 0) {
            cluster.add(copy.get(0));
            copy.remove(0);
        } else {
            return false; // Return false as an error has occured if a cage has 0 cells
        }
        // Loop through the cells, add to 'cluster' if they neighbour any cells in the cluster
        // Keep looping until either cells can't be added or no cells are added
        boolean looping = true;
        int loops = -1;
        boolean passWithoutAdding;
        while (looping) {
            passWithoutAdding = true;
            Iterator<Cell> copyIter = copy.iterator();
            Cell checkIfNeighbours;
            while (copyIter.hasNext()) {
                checkIfNeighbours = copyIter.next();
                for (Cell c : cluster) {
                    // If any of the cells in cluster neighbour any of the cells in the copied list
                    // add them to the cluster and remove from the copy
                    if ((c.getColumn() - checkIfNeighbours.getColumn() == 1) || (c.getColumn() - checkIfNeighbours.getColumn() == -1) || (c.getRow() - checkIfNeighbours.getRow() == 1) || (c.getRow() - checkIfNeighbours.getRow() == -1)) {
                        // Add to the cluster, remove from copy
                        cluster.add(checkIfNeighbours);
                        copyIter.remove();
                        passWithoutAdding = false;
                        break;
                    }
                }
            }
            // If the copy list is empty all of the cells have been added to the cluster, return true
            if (copy.size() == 0) {
                return true;
            }
            if (passWithoutAdding) {
                looping = false;
            }
        }
        return false;
    }

    // Getters
    public String getOperation () {
        return this.operation;
    }

    public int getTargetValue () {
        return this.targetValue;
    }

    public ArrayList<Cell> getMyCells() {
        return this.myCells;
    }
}