import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
/* LIST OF THINGS TO CHANGE:
    - Make the whole window resize correctly, setHGrow and things
    - Make the menu buttons look nicer (same size / logos)
    -

 */
public class Cell {
    // A class for each of the cells that the user can input a number into
    //private GridPane container;
    private VBox box;
    private Label cageValue;
    private int value;
    private int row;
    private int column;
    private HBox displayContainer;
    private TextField display;
    private boolean leftCageOn;
    private boolean rightCageOn;
    private boolean topCageOn;
    private boolean bottomCageOn;

    // Constructor
    public Cell (int row, int col) {
        // The box for each input from the user, set the size to be a square
        this.box = new VBox(5);
        this.box.setPrefWidth(60);
        this.box.setPrefHeight(60);

        this.cageValue = new Label(" x"); // Keep a space at the start so it looks nice
        this.cageValue.setPrefHeight(10);

        // The cell's co-ords
        this.row = row;
        this.column = col;
        this.value = 0;

        // The text box that the user will use to enter the number
        this.displayContainer = new HBox(0);
        this.displayContainer.setPrefWidth(50);
        this.display = new TextField();
        this.display.setAlignment(Pos.TOP_CENTER);
        this.displayContainer.setAlignment(Pos.TOP_CENTER);
        this.display.setPrefWidth(5); // CENTRE ALIGN + MAKE SIZE EQUAL (RESTRICT SIZE OF STRING TO 1?) TO 1 CHARACTER
        this.display.setStyle("-fx-border-style: solid; -fx-border-color: #ff0000"); // -fx-border-width: 10px; WTFFF
        this.displayContainer.getChildren().add(this.display);
        this.box.getChildren().addAll(this.cageValue, this.displayContainer);

        // The cage values
        if (this.row == 1 && this.column == 1) {
            this.leftCageOn = true;
            this.rightCageOn = false;
            this.topCageOn = true;
            this.bottomCageOn = true;
        } else if (this.row == 2 && this.column == 1) {
            this.leftCageOn = false;
            this.rightCageOn = true;
            this.topCageOn = true;
            this.bottomCageOn = true;
        } else {
            this.leftCageOn = false;
            this.rightCageOn = false;
            this.topCageOn = false;
            this.bottomCageOn = false;
        }
        this.box.setStyle(this.dispCage());

    }

    public String dispCage () {
        String setCss = "-fx-background-color: #000000, #ffffff ; -fx-background-insets: 0,";
        for  (int i : this.getCurrentBorders()) {
            setCss += (" " + i);
        }
        setCss += ";";
        //System.out.println(setCss);
        return setCss;
        //this.box.setStyle(setCss);
    }

    // Getters
    public TextField getDisplay () {
        return this.display;
    }

    public VBox getBox() {
        return this.box;
    }

    private int[] getCurrentBorders () {
        int[] borders = {0, 0, 0, 0};
        if (this.topCageOn) {
            borders[0] = 2;
        }
        if (this.rightCageOn) {
            borders[1] = 2;
        }
        if (this.bottomCageOn) {
            borders[2] = 2;
        }
        if (this.leftCageOn) {
            borders[3] = 2;
        }
        return borders;
    }

    // Setters
    public void setLeftCageOn(boolean leftCageOn) {
        this.leftCageOn = leftCageOn;
    }

    public void setRightCageOn(boolean rightCageOn) {
        this.rightCageOn = rightCageOn;
    }

    public void setTopCageOn(boolean topCageOn) {
        this.topCageOn = topCageOn;
    }

    public void setBottomCageOn(boolean bottomCageOn) {
        this.bottomCageOn = bottomCageOn;
    }
}