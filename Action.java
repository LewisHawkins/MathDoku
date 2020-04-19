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

    public void test () {
        System.out.println("BEFORE: " + valueBefore + ". AFTER: " + valueAfter + ".");
    }

    // Undo the action. This method returns an Action as calling undo is itself performing an action
    public void undo () {
        this.affectedCell.setCageValue(this.valueBefore);
        return new Action(affectedCell, )
    }

    // Redo the action
    public void redo () {
        this.affectedCell.setCageValue(this.valueAfter);
    }
}