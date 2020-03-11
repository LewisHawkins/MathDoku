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

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/*
LIST OF THINGS TO CHANGE:
    - Make the whole window resize correctly, setHGrow and things
    - Make the menu buttons look nicer (same size / logos)
    -
    - Wrap the gridpane in a group or something so the black doesn't spread
    - Make the number bigger inside of the cell text box
    - Change operation variable in Cage class from string to char
    -
 */

public class Mathdoku extends Application {
    private final int size = 6;
    private Cell[][] cells = new Cell[size][size];
    private Cage[] cages = new Cage[2];
    private Cell selectedCell;

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
        //puzzle.setStyle("-fx-border-style: solid; -fx-border-width: 5px; -fx-border-color: black; -fx-background-color: #0000ff; -fx-background-fill: #0000ff; -fx-padding: 5;");
        puzzle.setStyle("-fx-background-color: black; -fx-vgap: 2; -fx-hgap: 2 ; -fx-padding: 4;"); //padding goes around the whole grid, h/vgaps are inbetween cells
        //puzzle.setHgap(5);
        //puzzle.setVgap(5);
        VBox menu = new VBox(6);

        // Puzzle stuff
        this.createGrid(this.size, puzzle);

        // Menu stuff
        // The menu contains buttons for the user to control the game
        Button undo = new Button("Undo");
        Button redo = new Button("Redo");
        Button clear = new Button("Clear");
        Button loadFile = new Button("Load game from file");
        Button loadText = new Button("Load game from text");
        Button showMistakes = new Button("Show mistakes");
        // The menu also contains a keypad so the user can enter numerical input without a keyboard
        GridPane keypad = new GridPane();
        Button key1 = new Button("1");
        Button key2 = new Button("2");
        Button key3 = new Button("3");
        Button key4 = new Button("4");
        Button key5 = new Button("5");
        Button key6 = new Button("6");
        Button key7 = new Button("7");
        Button key8 = new Button("8");
        Button key9 = new Button("9");
        Button keyC = new Button("C");
        Button key0 = new Button("0");
        Button keyX = new Button("X");
        /*
        key1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(">>");
            }
        });
         */
        keypad.add(key1, 0, 0);
        keypad.add(key2, 1, 0);
        keypad.add(key3, 2, 0);
        keypad.add(key4, 0, 1);
        keypad.add(key5, 1, 1);
        keypad.add(key6, 2, 1);
        keypad.add(key7, 0, 2);
        keypad.add(key8, 1, 2);
        keypad.add(key9, 2, 2);
        keypad.add(keyC, 1, 3);
        keypad.add(key0, 0, 3);
        keypad.add(keyX, 2, 3);


        menu.getChildren().addAll(undo, redo, clear, loadFile, loadText, showMistakes, keypad);

        // Add the 2 large components to the master pane
        master.getChildren().addAll(puzzle, menu);

        // Create a scene from the master pane, apply the CSS (?) and display it
        Scene scene = new Scene(master);
        //scene.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    void createGrid (int size, GridPane p)  {
        // Create the cells for the grid
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
        // Create the cages for the grid
        try {
            ArrayList<Cage> cages = this.getCagesFromFile("test.txt");
            Iterator<Cage> cageIter = cages.iterator();
            while (cageIter.hasNext()) {
                Cage next = cageIter.next();
                next.formatCage();
            }
        } catch (IOException e) {
            System.out.println("REE 2");
            e.printStackTrace();
        }
    }

    public ArrayList<Cage> getCagesFromFile (String filename) throws FileNotFoundException, IOException {
        try {
            // Use the contents of a file to make the cages for the puzzle
            ArrayList<Cage> allCages = new ArrayList<Cage>();
            // Get input from file
            File gridFile = new File(filename);
            //FileReader fr = new FileReader(gridFile);
            FileInputStream fis = new FileInputStream(filename);
            InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                // Each line of the file represents a new cage, with the target info and cells split by the space
                String[] lineContents = line.split(" ");
                // The start of the string has the number for the target value, and the operation
                // The operation is the last character before the space
                char op = lineContents[0].charAt(lineContents[0].length() - 1);
                // The rest of the start of the string is the target number for that cage
                String targetStr = lineContents[0].substring(0, lineContents[0].length() - 1);
                int targetInt = Integer.parseInt(targetStr);

                // The end of the string has the cell number for that cage seperated by commas
                String[] cellNumbers = lineContents[1].split(",");
                ArrayList<Cell> cageCells = new ArrayList<Cell>();
                for (String i : cellNumbers) {
                    // For each cell number create a new cell and put cells in the cages
                    int cellNo = Integer.parseInt(i);
                    int cellRow = this.getRowForCellNo(cellNo, this.size);
                    int cellCol = this.getColForCellNo(cellNo, this.size);
                    Cell cellInCage = this.cells[cellRow][cellCol];
                    cageCells.add(cellInCage);
                }
                Cage newCage = new Cage(cageCells, String.valueOf(op), targetInt);
                allCages.add(newCage);
            }
            return allCages;
        } catch (FileNotFoundException e) {
            System.out.println("REE 1");
            e.printStackTrace();
            return null;
        }
    }

    int getRowForCellNo (int cellNo, int gridSize) {
        return ((cellNo - 1) / gridSize);
    }

    int getColForCellNo (int cellNo, int gridSize) {
        return ((cellNo - 1) % gridSize);
    }

    void displayGrid (GridPane p) {
        for (Cell[] cs : this.cells) {
            for (Cell c : cs) {
                System.out.println();
            }
        }
    }

}