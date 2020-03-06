import javafx.application.Application;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;

public class Mathdoku extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start (Stage stage) {
        // Set the title of the GUI
        stage.setTitle("Mathdoku");

        // Make the largest frame that everything will be contained in
        HBox master = new HBox(5);

        // On the left will be the grid for the actual mathdok puzzle, and the options will be on the right
        GridPane puzzle = new GridPane();
        VBox menu = new VBox(5);

        // Puzzle stuff

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

        // Create a scene from the master pane and display it
        stage.setScene(new Scene(master));
        stage.show();
    }

}