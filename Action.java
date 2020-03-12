public class Action {
    private Cell affectedCell;
    private String valueBefore;
    private String valueAfter;

    // Constructor
    public Action (Cell c, String valueBefore, String valueAfter) {
        this.affectedCell = c;
        this.valueBefore = valueBefore;
        this.valueAfter = valueAfter;
    }

    // Undo the action
    public void undo () {
        this.affectedCell.setCageValue(this.valueBefore);
    }

    // Redo the action
    public void redo () {
        this.affectedCell.setCageValue(this.valueAfter);
    }
}