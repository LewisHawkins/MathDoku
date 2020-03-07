import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;

public class Mathdoku extends Application {
    private final int size = 6;
    private Cell[][] cells = new Cell[size][size];

    // Main method
    public static void main (String[] args) {
        launch(args);
    }

    public void start (Stage stage) {
        // Set the title of the GUI
        stage.setTitle("Mathdoku");

        // Make the largest frame that everything will be contained in
        HBox master = new HBox(5);

        // On the left will be the grid for the actual mathdok puzzle, and the options will be on the right
        GridPane puzzle = new GridPane();
        puzzle.setHgap(0);
        puzzle.setVgap(0);
        VBox menu = new VBox(5);

        // Puzzle stuff
        this.createGrid(6, puzzle);

        // Menu stuff
        // The menu contains buttons for the user to control the game
        Button undo = new Button("Undo");
        Button redo = new Button("Redo");
        Button clear = new Button("Clear");
        Button loadFile = new Button("Load game from file");
        Button loadText = new Button("Load game from text");
        Button showMistakes = new Button("Show mistakes");
        menu.getChildren().addAll(undo, redo, clear, loadFile, loadText, showMistakes);

        // Add the 2 large components to the master pane
        master.getChildren().addAll(puzzle, menu);

        // Create a scene from the master pane, apply the CSS and display it
        Scene scene = new Scene(master);
        scene.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    void createGrid (int size, GridPane p) {
        for (int i = 0; i < size; i++) {
            Cell[] newRow = new Cell[this.size];
            for (int j = 0; j < size; j++) {
                // Create the square of cells
                Cell c = new Cell(j, i);
                p.add(c.getBox(), j, i);
                newRow[j] = c;
            }
            this.cells[i] = newRow;
        }
    }





    void displayGrid (GridPane p) {
        for (Cell[] cs : this.cells) {
            for (Cell c : cs) {
                System.out.println();
            }
        }
    }

}