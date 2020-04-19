import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javafx.event.EventHandler;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/*
LIST OF THINGS TO CHANGE:
    - Mgake the whole window resize correctly, setHGrow and things
    - Make the menu buttons look nicer (same size / logos)
    -
    - Wrap the gridpane in a group or something so the black doesn't spread
    - Make the number bigger inside of the cell text box
    - Change operation variable in Cage class from string to char
    - Redo the error detection for getCagesFromFile. The errorSum thing was nice but sometimes won't work by fluke number chance
    - User needs to be able to enter the puzzle through appropriate text input control
    - See what happens when tryin to input a number at the start before clicking on a cell for the first time
    - Attempting to select the top left cell at the start of the game doesn't work --> default value?
    - Ensure that the stack size always remains at 10 [no fancy overrides or sub/super classes] just make sure every 'push' is paired with an if size >10 ... remove[0]
 */

public class Mathdoku extends Application {
    // The size (height/width) of the play grid
    private final int size = 6;
    // Nested array of the size^squared cell objects
    private Cell[][] cells = new Cell[size][size];
    // A list of all the cages that group the cells
    private Cage[] cages = new Cage[2];
    // The cell currently selected by the user so that they can highlight cells to enter information
    private Cell selectedCell;
    // A stack of (10) actions from the user to enable the undo/redo buttons
    Stack<Action> actionStack = new Stack<Action>();


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
        undo.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Get the Action on the top of the stack, and call the undo action on it
                Action mostRecentAction = actionStack.peek();
                mostRecentAction.undo();
                // The undo action itself is an action so add it to the stack
            }
        });
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

        // Add the interaction to the buttons so that the buttons can be used
        key1.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Insert the number into the highlighted cell
                keypadNumberPress(1);
            }
        });
        key2.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Insert the number into the highlighted cell
                keypadNumberPress(2);
            }
        });
        key3.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Insert the number into the highlighted cell
                keypadNumberPress(3);
            }
        });
        key4.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Insert the number into the highlighted cell
                keypadNumberPress(4);
            }
        });
        key5.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Insert the number into the highlighted cell
                keypadNumberPress(5);
            }
        });
        key6.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Insert the number into the highlighted cell
                keypadNumberPress(6);
            }
        });
        key7.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Insert the number into the highlighted cell
                keypadNumberPress(7);
            }
        });
        key8.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Insert the number into the highlighted cell
                keypadNumberPress(8);
            }
        });
        key9.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Insert the number into the highlighted cell
                keypadNumberPress(9);
            }
        });
        key0.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Insert the number into the highlighted cell
                keypadNumberPress(0);
            }
        });
        keyC.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // When the clear button is pressed on the keypad, the highlighted cell should be cleared
                Cell cellToChange = getMostRecentSelected();
                // Create a new Action object and add it to the stack
                Action update = new Action(cellToChange, cellToChange.getDisplay(), "");
                cellToChange.setDisplay(0);
                actionStack.push(update);
                System.out.println("STACK IS CURRENTLY: " + actionStack + ". It currently has this many items: " + actionStack.size());
                // Check the stack size always remains at 10
                if (actionStack.size() > 10) {
                    // Take out the oldest element in the stack so that the size always has a maximum of 10
                    actionStack.remove(0);
                }
            }
        });

        // Add all the buttons to the GUI
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

        // Apply the keyboard/mouse  interaction handling
        scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            public void handle(KeyEvent event) {
                int newCellNumber = 0;
                KeyCode keyPressed = event.getCode();
                // Get the key that was pressed, call a function to ensure that it's a valid number key and display it
                newCellNumber = validateKeyPress(keyPressed);
                Cell cellToChange = getMostRecentSelected();
                // Create a new Action object and add it to the stack
                Action update = new Action(cellToChange, cellToChange.getDisplay(), Integer.toString(newCellNumber));
                cellToChange.setDisplay(newCellNumber);
                actionStack.push(update);
                System.out.println("STACK IS CURRENTLY: " + actionStack + ". It currently has this many items: " + actionStack.size());
                // Check the stack size always remains at 10
                if (actionStack.size() == 10) {
                    // Take out the oldest element in the stack so that the size always has a maximum of 10
                    actionStack.remove(0);
                }
            }
        });

        scene.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle (MouseEvent e) {
                //System.out.println("Math call!");
                setSelectedCell(getMostRecentSelected());
            }
        }));

        //scene.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    // Grid/Cage setup
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
            if (cages != null) {
                Iterator<Cage> cageIter = cages.iterator();
                while (cageIter.hasNext()) {
                    Cage next = cageIter.next();
                    next.formatCage();
                }
            } else {
                // Do something error based here
                Label error = new Label ("Error");
                error.setStyle("-fx-background-color: #ff0000");
                //error.setTextFill("#ff0000");
                //p.setRowIndex(error, 2);
                //p.setColumnIndex(error, 3);
                p.add(error, 0, 0);
            }
        } catch (IOException e) {
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
            // For error correction,the sum of all the cell numbers should equal to (1/2 * n * n+1) where n = total cells
            int totalCells = this.size*this.size;
            int errorSum = (totalCells * (totalCells+1))/2;
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
                    // Remove from the error
                    errorSum -= cellNo;
                }
                Cage newCage = new Cage(cageCells, String.valueOf(op), targetInt);

                //
                // Test that the cage is valid
                //
                if (newCage.areCellsAdjacent()) {
                    //System.out.println("Cage added successfully.");
                    allCages.add(newCage);
                } else {
                    System.out.println("Cage failed to load.");
                }

            }
            // After subtracting all of the cell numbers, the errorSum integer will be equal to 0 if each of the cell
            // numbers from 1 to gridSize^2 appears each appears once and only once
            if (errorSum == 0) {
                return allCages;
            } else {
                return null;
            }

        } catch (FileNotFoundException e) {
            System.out.println("That file could not be found!");
            e.printStackTrace();
            return null;
        }
    }


    // Puzzle
    public void clearGrid () {
        for (Cell[] cr : this.cells) {
            for (Cell c : cr) {
                c.setCageValue("");
                // This should either manually set the actual cage value
                // or setCageValue will change the value in the cell class
            }
        }
    }


    // Interaction/Handling
    public int validateKeyPress(KeyCode keyCode) {
        int newNumber = 0;
        // 8 Case statements needed as the largest size that the board can be is 8 by 8
        switch (keyCode) {
            case DIGIT1:
                newNumber = 1;
                break;
            case DIGIT2:
                newNumber = 2;
                break;
            case DIGIT3:
                newNumber = 3;
                break;
            case DIGIT4:
                newNumber = 4;
                break;
            case DIGIT5:
                newNumber = 5;
                break;
            case DIGIT6:
                newNumber = 6;
                break;
            case DIGIT7:
                newNumber = 7;
                break;
            case DIGIT8:
                newNumber = 8;
                break;
            default:
                // If any invalid key is pressed the value 0 should be returned to indiciate this
                newNumber = 0;
        }
        // You can't enter a number into a cell if it is bigger than the grid size
        if (newNumber > this.size) {
            newNumber = 0;
        }
        return newNumber;
    }

    // Is this actually used anywhere???
    public void testFunc () {
        System.out.println("REEE");
    }

    public void keypadNumberPress (int num) {
        if (num > this.size) {
            // An irrelevant number has been pressed, so don't do anything (pop up an alert?)
            System.out.println("Too large pressed.");
        } else {
            Cell cellToChange = getMostRecentSelected();
            // Create a new Action object and add it to the stack
            Action update = new Action(cellToChange, cellToChange.getDisplay(), Integer.toString(num));
            cellToChange.setDisplay(num);
            actionStack.push(update);
            System.out.println("STACK IS CURRENTLY: " + actionStack + ". It currently has this many items: " + actionStack.size());
            // Check the stack size always remains at 10
            if (actionStack.size() > 10) {
                // Take out the oldest element in the stack so that the size always has a maximum of 10
                actionStack.remove(0);
            }
        }
    }

    public Cell getMostRecentSelected () {
        if (this.selectedCell == null) {
            // When first loading select the top left cell by default
            this.selectedCell = this.cells[0][0];
        }
        for (Cell[] cr : this.cells) {
            for (Cell c : cr) {
                if (c.isSelected()) {
                    // Compare with the currently selected, if more recently selected then swap them
                    if (c.getSelectedTime() > this.selectedCell.getSelectedTime()) {
                        this.selectedCell.setSelected(false);
                        this.selectedCell.dispCage();
                        this.selectedCell = c;
                        this.selectedCell.dispCage();
                    }
                }
            }
        }
        return this.selectedCell;
    }

    /*
    public void actionPerformed(ActionEvent e) {
        System.out.println("WOO !!!");
        //text.setText("Button Clicked " + numClicks + " times");
    }

    // Overwrites to get working
    public void windowOpened(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    */

    // Getters
    int getRowForCellNo (int cellNo, int gridSize) {
        return ((cellNo - 1) / gridSize);
    }

    int getColForCellNo (int cellNo, int gridSize) {
        return ((cellNo - 1) % gridSize);
    }

    // Setters
    public void setSelectedCell (Cell c) {
        this.selectedCell = c;
    }


}