import java.util.ArrayList;
import java.util.Iterator;


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
        // Check that the sum of all the cells is equal or not to the target value
        if (this.operation.equals("+")) {
            int total = 0;
            Iterator<Cell> sumIter = myCells.iterator();
            while (sumIter.hasNext()) {
                total += sumIter.next().getValue();
            }
            if (total == this.targetValue) {
                return true;
            } else {
                return false;
            }
        }
        // Check that the product of all the cells is equal or not to the target value
        if (this.operation.equals("x")) {
            int total = 1;
            Iterator<Cell> mulIter = myCells.iterator();
            while (mulIter.hasNext()) {
                total = total*mulIter.next().getValue();
            }
            if (total == this.targetValue) {
                return true;
            } else {
                return false;
            }
        }
        // Check that the minus operator can be used to reach the target value
        if (this.operation.equals("-")) {
            int total = this.targetValue;
            // WORK THIS OUT
            if (total == 10) {
                return true;
            } else {
                return false;
            }
        }
        // Check that the divide operator can be used to reach the target value
        if (this.operation.equals("÷")) {
            int total = this.targetValue;
            // WORK THIS OUT
            if (total == 10) {
                return true;
            } else {
                return false;
            }
        }
        return false;
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