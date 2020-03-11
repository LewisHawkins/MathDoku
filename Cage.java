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

    public void doBorders () {
        // Make the outside of the cage thicker so that it is visible to the user
        // Do this by assuming every cell has all 4 borders thick, then making neighbouring cells have thin borders
        // Set booleans for every cell in the cage
        Iterator<Cell> cellIter = myCells.iterator();
        while (cellIter.hasNext()) {
            boolean[] t = {true, true, true, true};
            cellIter.next().setAllCages(t);
        }
        // Alter the booleans for neighbour cells

        // Apply the changes to every cell
        for (Cell c : this.myCells) {
            c.dispCage();
        }
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