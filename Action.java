public class Action {
    private Cell affectedCell;
    private String valueBefore;
    private String valueAfter;

    // Constructor
    public Action (Cell c, String valueBefore, String valueAfter) {
        this.affectedCell = c;
        if (valueBefore == "") {
            this.valueBefore = "0";
        } else {
            this.valueBefore = valueBefore;
        }
        this.valueAfter = valueAfter;
    }

    // Undo the action. This method returns an Action as calling undo is itself performing an action
    public void undo () {
        System.out.println("An undo has occured.");
        this.affectedCell.setDisplay(Integer.parseInt(this.valueBefore));
    }

    // Redo the action. This is esssentially the same as performing the action again
    public void redo () {
        System.out.println("A redo has occured.");
        if (this.valueAfter == "") {
            this.affectedCell.setDisplay(0);
        } else {
            this.affectedCell.setDisplay(Integer.parseInt(this.valueAfter));
        }
    }
}