import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class Cell {
    // A class for each of the cells that the user can input a number into
    //private GridPane container;
    private VBox box;
    private Label cageValue;
    private int value;
    private int row;
    private int column;
    private TextField display;

    // Constructor
    public Cell (int row, int col) {
        this.box = new VBox(5);
        this.cageValue = new Label(" ree"); // Keep a space at the start so it looks nice
        this.row = row;
        this.column = col;
        this.value = 0;
        // The text box that the user will use to enter the number
        this.display = new TextField();
        this.display.setPrefWidth(40);
        this.display.setStyle("-fx-border-style: solid; -fx-border-width: 2px; -fx-border-color: white");
        this.box.getChildren().addAll(this.cageValue, this.display);
        this.box.setStyle("-fx-background-color: #ffffff; -fx-border-style: solid; -fx-border-width: 2px; -fx-border-color: black");
    }

    // Getters
    public TextField getDisplay () {
        return this.display;
    }

    public VBox getBox() {
        return this.box;
    }
}