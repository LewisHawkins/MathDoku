import javafx.application.Application;

import javafx.geometry.Pos;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.*;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.swing.JFileChooser;

import java.lang.Math;

import java.util.ArrayList;
import java.util.Collections;
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

public class Mathdoku extends Application {
    // The area where the user plays the game
    private GridPane puzzlePane;
    // The size (height/width) of the play grid
    private int size;
    // Nested array of the cell objects
    private Cell[][] cells;
    // A list of all the cages that group the cells
    private ArrayList<Cage> cages;
    // The cell currently selected by the user so that they can highlight cells to enter information
    private Cell selectedCell;
    // Stacks of (10) actions from the user to enable the undo/redo buttons
    Stack<Action> actionStack = new Stack<Action>();
    Stack<Action> redoStack = new Stack<Action>();

    // Main method
    public static void main (String[] args) {
        launch(args);
    }

    public void start (Stage stage) {
        // Set the title of the GUI
        stage.setTitle("Mathdoku");

        // Make the main frame that everything will be contained in
        HBox master = new HBox(5);
        master.setStyle("-fx-background-color: #000000;"); // The thin black line to seperate the two sections

        // On the left will be the grid for the mathdoku puzzle and the options will be on the right
        HBox puzzleContainer = new HBox(5);
        GridPane puzzle = new GridPane();
        this.puzzlePane = puzzle;
        puzzle.setStyle("-fx-background-color: #999999; -fx-padding: 30; -fx-vgap: 1; -fx-hgap: 1");
        puzzleContainer.setStyle("-fx-background-color: #FF0000;");

        HBox menuContainer = new HBox(5);
        VBox menu = new VBox(5);
        menu.setStyle("-fx-background-color: #444444; -fx-padding: 30;");

        // The menu contains buttons for the user to control the game
        Button undo = new Button("Undo");
        Button redo = new Button("Redo");
        Button clear = new Button("Clear");
        Button loadFile = new Button("Load Game From File");
        Button loadText = new Button("Load game From Text");
        ToggleButton showMistakes = new ToggleButton("Show Mistakes");
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
        // There are three buttons for the font size
        Label fontLabel = new Label("Font Size:");
        fontLabel.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 5;");
        HBox fontSize = new HBox(10);
        fontSize.setStyle("-fx-background-color: #FFFFFF;  -fx-padding: 10;");
        RadioButton smallFont = new RadioButton("Small");
        smallFont.setSelected(true);
        RadioButton mediumFont = new RadioButton("Medium");
        RadioButton largeFont = new RadioButton("Large");
        ToggleGroup fontGroup = new ToggleGroup();
        smallFont.setToggleGroup(fontGroup);
        mediumFont.setToggleGroup(fontGroup);
        largeFont.setToggleGroup(fontGroup);
        fontSize.getChildren().addAll(smallFont, mediumFont, largeFont);

        // Inner classes
        class UndoHandler {
            public UndoHandler (Stack stackToCheck) {
                // Whenever an undo action is made, check if the button needs to be disabled
                if (stackToCheck.size() == 0) {
                    // Disable the undo button when the stack of actions is empty
                    undo.setDisable(true);
                } else {
                    undo.setDisable(false);
                }
            }
        }
        class RedoHandler {
            public RedoHandler (Stack stackToCheck) {
                // Whenever a redo action is made, check if the button needs to be disabled
                if (stackToCheck.size() == 0) {
                    // Disable the undo button when the stack of actions is empty
                    redo.setDisable(true);
                } else {
                    redo.setDisable(false);
                }
            }
        }
        class LoadHandler {
            public LoadHandler (Button b1, Button b2) {
                b1.setDisable(true);
                b2.setDisable(true);
            }
        }

        // Add functionality to the undo button
        undo.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Get the Action on the top of the stack, call the undo action on it and then remove it
                Action mostRecentAction = actionStack.peek();
                mostRecentAction.undo();
                actionStack.pop();
                // The undo action is then added to the redo stack
                redoStack.push(mostRecentAction);
                // Disable/Enable the buttons if necessary
                UndoHandler u = new UndoHandler(actionStack);
                RedoHandler r = new RedoHandler(redoStack);
                checkGameWin(showMistakes.isSelected());
            }
        });
        // Add functionality to the redo button
        redo.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Get the Action on the top of the stack, and redo it
                Action mostRecentAction = redoStack.peek();
                mostRecentAction.redo();
                redoStack.pop();
                // The redone action is then added back to the undo stack
                actionStack.push(mostRecentAction);
                // Disable/Enable the buttons if necessary
                UndoHandler u = new UndoHandler(actionStack);
                RedoHandler r = new RedoHandler(redoStack);
                checkGameWin(showMistakes.isSelected());
            }
        });
        // Add functionality to the 'clear board' button
        clear.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Create a dialog box that appears to the user asking them to confirm the action
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirm");
                alert.setHeaderText("Are you sure you want to clear the board?");
                alert.showAndWait().ifPresent(choice -> {
                    if (choice == ButtonType.OK) {
                        // Clear all cells in the board
                        for (Cell[] c : cells) {
                            for (Cell cellToClear : c) {
                                cellToClear.setDisplay(0);
                            }
                        }
                        // Clearing the board should also reset both stacks of actions taken by the user
                        actionStack.clear();
                        redoStack.clear();
                        UndoHandler u = new UndoHandler(actionStack);
                        RedoHandler r = new RedoHandler(redoStack);
                    } else if (choice == ButtonType.CANCEL) {
                        System.out.println("Pressed cancel.");
                    }
                });
                checkGameWin(showMistakes.isSelected());
            }
        });
        // Add functionality to the two load buttons
        loadFile.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Open a dialog box that lets the user select the file for the puzzle
                JFileChooser userChooseFile = new JFileChooser();
                userChooseFile.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = userChooseFile.showOpenDialog(userChooseFile);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = userChooseFile.getSelectedFile();
                    String selectedFilename = selectedFile.getAbsolutePath();
                    // Get the contents of the file selected by the user
                    try {
                        ArrayList<String> selectedFileContents = getCagesFromFile(selectedFilename);
                        // Use the contents to create the game for the user
                        // returns 0 if the number was wrong
                        int newGameSize = checkCellData(selectedFileContents);
                        if (newGameSize != 0) {
                            setNewGameData(newGameSize);
                            GridPane puzzlePane = getPuzzlePane();
                            new LoadHandler(loadFile, loadText);
                            createGrid(selectedFileContents, puzzlePane);
                        }
                    } catch (FileNotFoundException e) {
                        System.out.println("There was an error!");
                        e.printStackTrace();
                    } catch (IOException e) {
                        System.out.println("There was an error!");
                        e.printStackTrace();
                    }
                }
            }
        });
        loadText.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Open a new window with a text box that will let the user input the cage information
                Stage loadTextWindow = new Stage();
                loadTextWindow.initOwner(stage);
                // The window contains a label, area to enter the text and a confirmation button
                VBox loadTextFrame = new VBox(5);
                Label loadTextLabel = new Label("Please enter the text to load the puzzle:");
                TextArea textEntryBox = new TextArea();
                Button loadTextButton = new Button("Done");
                // When the button is pressed use the text entered by the user to make the puzzle and close the window
                loadTextButton.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
                    public void handle(MouseEvent event) {
                        String enteredText = textEntryBox.getText();
                        String[] enteredTextLines = enteredText.split("\n");
                        ArrayList<String> enteredTextLinesArray = new ArrayList<String>();
                        for (String line : enteredTextLines) {
                            enteredTextLinesArray.add(line);
                        }
                        // Use the contents to create the game for the user
                        // 0 is returned if the number was wrong
                        int newGameSize = checkCellData(enteredTextLinesArray);
                        if (newGameSize != 0) {
                            setNewGameData(newGameSize);
                            GridPane puzzlePane = getPuzzlePane();
                            new LoadHandler(loadFile, loadText);
                            loadTextWindow.close();
                            createGrid(enteredTextLinesArray, puzzlePane);
                        }
                    }
                });

                //
                loadTextFrame.getChildren().addAll(loadTextLabel, textEntryBox, loadTextButton);
                Scene loadTextScene = new Scene(loadTextFrame);
                loadTextWindow.setScene(loadTextScene);
                loadTextWindow.show();
            }
        });
        // Add functionality to the button to show mistakes
        showMistakes.setOnMousePressed(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // This occurs before the button's state changes, so call the check win function with the opposite
                checkGameWin(!showMistakes.isSelected());
            }
        });
        // Add the interaction to the keypad buttons so that they can be used
        key1.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Insert the number into the highlighted cell
                keypadNumberPress(1);
                UndoHandler u = new UndoHandler(actionStack);
                RedoHandler r = new RedoHandler(redoStack);
                checkGameWin(showMistakes.isSelected());
            }
        });
        key2.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Insert the number into the highlighted cell
                keypadNumberPress(2);
                UndoHandler u = new UndoHandler(actionStack);
                RedoHandler r = new RedoHandler(redoStack);
                checkGameWin(showMistakes.isSelected());
            }
        });
        key3.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Insert the number into the highlighted cell
                keypadNumberPress(3);
                UndoHandler u = new UndoHandler(actionStack);
                RedoHandler r = new RedoHandler(redoStack);
                checkGameWin(showMistakes.isSelected());
            }
        });
        key4.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Insert the number into the highlighted cell
                keypadNumberPress(4);
                UndoHandler u = new UndoHandler(actionStack);
                RedoHandler r = new RedoHandler(redoStack);
                checkGameWin(showMistakes.isSelected());
            }
        });
        key5.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Insert the number into the highlighted cell
                keypadNumberPress(5);
                UndoHandler u = new UndoHandler(actionStack);
                RedoHandler r = new RedoHandler(redoStack);
                checkGameWin(showMistakes.isSelected());
            }
        });
        key6.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Insert the number into the highlighted cell
                keypadNumberPress(6);
                UndoHandler u = new UndoHandler(actionStack);
                RedoHandler r = new RedoHandler(redoStack);
                checkGameWin(showMistakes.isSelected());
            }
        });
        key7.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Insert the number into the highlighted cell
                keypadNumberPress(7);
                UndoHandler u = new UndoHandler(actionStack);
                RedoHandler r = new RedoHandler(redoStack);
                checkGameWin(showMistakes.isSelected());
            }
        });
        key8.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Insert the number into the highlighted cell
                keypadNumberPress(8);
                UndoHandler u = new UndoHandler(actionStack);
                RedoHandler r = new RedoHandler(redoStack);
                checkGameWin(showMistakes.isSelected());
            }
        });
        key9.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Insert the number into the highlighted cell
                keypadNumberPress(9);
                UndoHandler u = new UndoHandler(actionStack);
                RedoHandler r = new RedoHandler(redoStack);
                checkGameWin(showMistakes.isSelected());
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
                // Check the stack size always remains at 10
                if (actionStack.size() >= 10) {
                    // Take out the oldest element in the stack so that the size always has a maximum of 10
                    actionStack.remove(0);
                }
                checkGameWin(showMistakes.isSelected());
            }
        });
        // Add all the buttons to the keypad
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
        // Add interaction for the font size buttons
        smallFont.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Set all font in the grid to large
                setFontSize(0);
            }
        });
        mediumFont.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Set all font in the grid to medium
                setFontSize(1);
            }
        });
        largeFont.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                // Set all font in the grid to large
                setFontSize(2);
            }
        });

        // Add all the buttons to the menu section of the window
        keypad.setAlignment(Pos.CENTER);
        menu.getChildren().addAll(undo, redo, clear, loadFile, loadText, showMistakes, keypad, fontLabel, fontSize);

        // Edit the 2 large sections
        puzzleContainer.getChildren().add(puzzle);
        puzzleContainer.setHgrow(puzzle, Priority.ALWAYS);
        puzzle.setAlignment(Pos.CENTER);
        menuContainer.getChildren().add(menu);
        menu.setAlignment(Pos.CENTER);

        // Add the largest components to the main frame and set the growth of the window to scale with the puzzle section
        master.getChildren().addAll(puzzleContainer, menuContainer);
        master.setHgrow(puzzleContainer, Priority.ALWAYS);

        // Set the minimum size of the window
        stage.setMinWidth(850);
        stage.setMinHeight(600);

        // Create a scene from the master pane
        Scene scene = new Scene(master);

        // Apply the keyboard and mouse interaction handling
        scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            public void handle(KeyEvent event) {
                int newCellNumber = 0;
                // Get the key that was pressed and call a function to ensure that it's a valid key
                KeyCode keyPressed = event.getCode();
                newCellNumber = validateKeyPress(keyPressed);
                // The number 10 is used to indicate something going wrong, so the cell should not be changed from this key press
                if (newCellNumber < 10) {
                    Cell cellToChange = getMostRecentSelected();
                    // Create a new Action object and add it to the stack
                    Action update = new Action(cellToChange, cellToChange.getDisplay(), Integer.toString(newCellNumber));
                    cellToChange.setDisplay(newCellNumber);
                    actionStack.push(update);
                    // Check the stack size always remains at 10
                    if (actionStack.size() >= 10) {
                        // Take out the oldest element in the stack so that the size always has a maximum of 10
                        actionStack.remove(0);
                    }
                    // See if the buttons should be disabled
                    UndoHandler u = new UndoHandler(actionStack);
                    RedoHandler r = new RedoHandler(redoStack);
                    checkGameWin(showMistakes.isSelected());
                }
            }
        });

        scene.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle (MouseEvent e) {
                // Highlight the cell that the user clicked on so that they know they have selected it
                setSelectedCell(getMostRecentSelected());
                checkGameWin(showMistakes.isSelected());
            }
        }));

        // Initially check if the buttons should be disabled
        UndoHandler u = new UndoHandler(actionStack);
        RedoHandler r = new RedoHandler(redoStack);

        stage.setScene(scene);
        stage.show();
    }

    // Grid/Cage setup
    void createGrid (ArrayList<String> gridContents, GridPane p) {
        // Create the cells for the grid
        for (int i = 0; i < this.size; i++) {
            Cell[] newRow = new Cell[this.size];
            for (int j = 0; j < this.size; j++) {
                // Create a new cell object for the row/col co-ordiante and add it to the gridpane
                Cell c = new Cell(j, i);
                p.add(c.getBox(), j, i);
                newRow[j] = c;
            }
            this.cells[i] = newRow;
        }

        // Get the cages that group the cells for the puzzle
        ArrayList<Cage> cages = getCagesFromArray(gridContents);
        // getCagesFromArray returns null if there was an error loading the puzzle
        if (cages != null) {
            Iterator<Cage> cageIter = cages.iterator();
            while (cageIter.hasNext()) {
                Cage next = cageIter.next();
                next.formatCage();
                this.cages = cages;
            }
        } else {
            // Alert the user that there was an error loading the file
            System.out.println("There was an error loading that file!");
        }
        getMostRecentSelected();
    }

    int checkCellData (ArrayList<String> cellInfo) {
        // The first check should ensure that the file contains only valid characters
        for (String line : cellInfo) {
            for (char c : line.toCharArray()) {
                // Each character should be in the below string, or it is invalid
                if ("0 1 2 3 4 5 6 7 8 9 + - x ,  ".contains(String.valueOf(c)) == false) {
                    if ((int) c != 247) {
                        if ((int) c != 32) {
                            // If an incorrect character is detected alert the user
                            Alert alert = new Alert(AlertType.WARNING);
                            alert.setTitle("Load Game Error");
                            alert.setHeaderText("An incorrect character was detected.");
                            alert.show();
                            return 0;
                        }
                    }
                }
            }
        }

        // Work out the size of the puzzle
        ArrayList<Integer> fileCellNumbers = new ArrayList<Integer>();
        for (String line : cellInfo) {
            String[] lineContents = line.split(" ");
            String[] newCellNumbers = lineContents[1].split(",");
            for (String cellNo : newCellNumbers) {
                fileCellNumbers.add(Integer.parseInt(cellNo));
            }
        }

        // Sort the cells to get the largest cell number, which is the square of the grid width and height
        Collections.sort(fileCellNumbers);
        int largestNumber = fileCellNumbers.size();
        // If correct the biggest cell number should be a square number
        int gridSize = (int) Math.sqrt(largestNumber);
        if (Math.pow(gridSize, 2) != fileCellNumbers.size()) {
            // An incorrect number of cells was counted
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Load Game Error");
            alert.setHeaderText("An incorrect number of cells was detected.");
            alert.show();
            return 0;
        }

        // Check that a cell each cell appears only once
        ArrayList<Integer> duplicateChecker = new ArrayList<Integer>();
        for (Integer i : fileCellNumbers) {
            // Add cell numbers to a new list until either all numbers are added or there is a duplicate
            if (duplicateChecker.contains(i)) {
                // If an incorrect character is detected alert the user
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Load Game Error");
                alert.setHeaderText("Cell duplication detected.");
                alert.show();
                return 0;
            } else {
                duplicateChecker.add(i);
            }
        }

        // Check that every cell from 1 to the size of the grid squared is included
        for (int i = 0; i < fileCellNumbers.size() - 1; i++) {
            // Check that the difference between each of the cell numbers is 1
            if (fileCellNumbers.get(i+1) - fileCellNumbers.get(i) != 1) {
                // If an incorrect character is detected alert the user
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Load Game Error");
                alert.setHeaderText("Missing cell number detected.");
                alert.show();
                return 0;
            }
        }
        return gridSize;
    }

    ArrayList<Cage> getCagesFromArray (ArrayList<String> gridContents) {
        // Use the contents to make the cages for the puzzle
        ArrayList<Cage> allCages = new ArrayList<Cage>();
        // Create an iterator to loop through the contents
        Iterator<String> contentIter = gridContents.iterator();
        while (contentIter.hasNext()) {
            String line = contentIter.next();
            // Each line of the file represents a new cage, with the target info and cells split by the space
            String[] lineContents = line.split(" ");
            // If the line does not have an operator (+, x...) before the space the cage has only one cell
            if ("0 1 2 3 4 5 6 7 8 9".contains(String.valueOf(lineContents[0].charAt(lineContents[0].length() - 1)))) {
                // The start of the string has the number for the target value, and the operation
                // Get the target value for the cage
                String targetStr = lineContents[0];
                int targetInt = Integer.parseInt(targetStr);
                char op = ' ';
                // Get the cell
                ArrayList<Cell> cageCells = new ArrayList<Cell>();
                int cellNo = Integer.parseInt(lineContents[1]);
                int cellRow = this.getRowForCellNo(cellNo, this.size);
                int cellCol = this.getColForCellNo(cellNo, this.size);
                Cell cellInCage = this.cells[cellRow][cellCol];
                cageCells.add(cellInCage);
                // Create the cage object
                Cage newCage = new Cage(cageCells, " ", targetInt);
                allCages.add(newCage);
            } else {
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
                    int cellRow = getRowForCellNo(cellNo, this.size);
                    int cellCol = getColForCellNo(cellNo, this.size);
                    Cell cellInCage = this.cells[cellRow][cellCol];
                    cageCells.add(cellInCage);
                }
                Cage newCage = new Cage(cageCells, String.valueOf(op), targetInt);
                // Test that the cage is valid
                if (newCage.areCellsAdjacent()) {
                    allCages.add(newCage);
                } else {
                        /*
                        THIS NEEDS SOME MORE ERROR HANDLING ---> CHECK FAQ'S ABOUT WHAT TO DO
                        IF THE FILE BREAKS ITSELF WHILE LOADING
                         */
                    System.out.println("Cage failed to load.");
                }
            }
        }
        return allCages;
    }

    ArrayList<String> getCagesFromFile (String filename) throws FileNotFoundException, IOException {
        // Get the contents of the file and return it in an ArrayList of strings
        ArrayList<String> fileContents = new ArrayList<String>();
        //File gridFile = new File(filename);
        try {
            FileInputStream fis = new FileInputStream(filename);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                fileContents.add(line);
            }
            return fileContents;
        } catch (FileNotFoundException e) {
            System.out.println("That file could not be found!");
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Cell> getMistakes () {
        // Check the 3 puzzle constraints
        ArrayList<Cell> allMistakes = new ArrayList<Cell>();
        // Check each row
        for (Cell[] cr : this.cells) {
            boolean cellRowIncorrect = checkForDuplicates(cr);
            if (cellRowIncorrect) {
                for (Cell c : cr) {
                    allMistakes.add(c);
                }
            }
        }
        // Check each column
        for (int i = 0; i < this.size; i++) {
            Cell[] columnOfCells = new Cell[this.size];
            for (int j = 0; j < this.size; j++) {
                columnOfCells[j] = this.cells[j][i];
            }
            // Check the column for duplicates
            boolean cellColumnIncorrect = checkForDuplicates(columnOfCells);
            if (cellColumnIncorrect) {
                for (Cell c : columnOfCells) {
                    allMistakes.add(c);
                }
            }
        }
        // Check each of the cages
        for (Cage c : this.cages) {
            boolean cageCorrect = c.checkCorrect();
            if (cageCorrect == false) {
                //System.out.println("Gets here!");
                ArrayList<Cell> cellsToAdd = c.getMyCells();
                Iterator<Cell> cellIter = cellsToAdd.iterator();
                while (cellIter.hasNext()) {
                    Cell cellToAdd = cellIter.next();
                    allMistakes.add(cellToAdd);
                }
            }
        }
        return allMistakes;
    }

    public boolean checkForDuplicates (Cell[] collection) {
        ArrayList<String> rowContents = new ArrayList<String>();
        boolean rowIsIncorrect = false;
        // The row should contain all of the numbers from 1 to the size of the row
        for (Cell c : collection) {
            if (c.getDisplay() == "") {
                // One of the cells is empty, so mark the row as incorrect
                rowIsIncorrect = true;
                break;
            } else if (rowContents.contains(c.getDisplay())) {
                // The row contains a duplicate, so mark the row as incorrect
                rowIsIncorrect = true;
                break;
            } else {
                // The contents of the cell has not appeared before so add it to the list
                rowContents.add(c.getDisplay());
            }
        }
        if (rowIsIncorrect) {
            return true;
        } else {
            return false;
        }
    }

    public void checkGameWin (boolean showingMistakes) {
        ArrayList<Cell> currentMistakes = getMistakes();
        if (currentMistakes.size() == 0) {
            // The user has won if there are no mistakes detected and every cell is filled
            boolean everyCellFilled = true;
            for (Cell[] cr : this.cells) {
                for (Cell c : cr) {
                    if (c.getDisplay() == "") {
                        everyCellFilled = false;
                    }
                }
            }
            // If the boolean now has the value false there was a cell that wasn't filled
            if (everyCellFilled) {
                // The user has won
                for (Cell[] cr : this.cells) {
                    for (Cell c : cr) {
                        c.setShowingMistakes(true);
                        c.setCorrect(true);
                        c.dispCage();
                    }
                }
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Congratulations!");
                alert.setGraphic(null);
                alert.setHeaderText("You solved the puzzle!");
                alert.show();
            } else {
                // Reset the board before colouring the incorrect cells red
                for (Cell[] cr : this.cells) {
                    for (Cell c : cr) {
                        c.setShowingMistakes(false);
                        c.setCorrect(true);
                        c.dispCage();
                    }
                }
                for (Cell c : currentMistakes) {
                    c.setShowingMistakes(true);
                    c.setCorrect(false);
                    c.dispCage();
                }
            }
        } else {
            if (showingMistakes) {
                // Reset the board before colouring the incorrect cells red
                for (Cell[] cr : this.cells) {
                    for (Cell c : cr) {
                        c.setShowingMistakes(false);
                        c.setCorrect(true);
                        c.dispCage();
                    }
                }
                for (Cell c : currentMistakes) {
                    c.setShowingMistakes(true);
                    c.setCorrect(false);
                    c.dispCage();
                }
            } else {
                // If not showing mistakes, it should still be checked that all cells should be turned off/reset so that
                // the cells return to how they were at the start when the show mistakes button was been turned off
                for (Cell[] cr : this.cells) {
                    for (Cell c : cr) {
                        c.setShowingMistakes(false);
                        c.dispCage();
                    }
                }
            }
        }
    }

    public int validateKeyPress (KeyCode keyCode) {
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
            case BACK_SPACE:
                newNumber = 0;
                break;
            default:
                // If any invalid key is pressed the value 10 should be returned to indiciate this
                newNumber = 10;
        }
        // You can't enter a number into a cell if it is bigger than the grid size
        if (newNumber > this.size) {
            newNumber = 10;
        }
        return newNumber;
    }

    public void keypadNumberPress (int num) {
        if (num <= this.size) {
            Cell cellToChange = getMostRecentSelected();
            // Create a new Action object and add it to the stack
            Action update = new Action(cellToChange, cellToChange.getDisplay(), Integer.toString(num));
            cellToChange.setDisplay(num);
            actionStack.push(update);
            // Check the stack size always remains at 10
            if (actionStack.size() >= 10) {
                // Take out the oldest element in the stack so that the size always has a maximum of 10
                actionStack.remove(0);
            }
        }
    }

    public Cell getMostRecentSelected () {
        if (this.selectedCell == null) {
            // When first loading select the top left cell by default
            this.selectedCell = this.cells[0][0];
            this.selectedCell.setSelected(true);
            this.selectedCell.dispCage();
        }
        for (Cell[] cr : this.cells) {
            for (Cell c : cr) {
                if (c.isSelected()) {
                    // Compare with the currently selected, if more recently selected then swap them
                    if (c.getSelectedTime() > this.selectedCell.getSelectedTime()) {
                        this.selectedCell.setSelected(false);
                        this.selectedCell.dispCage();
                        this.selectedCell = c;
                        this.selectedCell.setSelected(true);
                        this.selectedCell.dispCage();
                    }
                }
            }
        }
        return this.selectedCell;
    }

    public void setFontSize (int size) {
        // Change the font size in every cell in the grid
        for (Cell[] cr : this.cells) {
            for (Cell c : cr) {
                c.setFontSize(size);
            }
        }
    }

    // Getters
    int getRowForCellNo (int cellNo, int gridSize) {
        return ((cellNo - 1) / gridSize);
    }

    int getColForCellNo (int cellNo, int gridSize) {
        return ((cellNo - 1) % gridSize);
    }

    GridPane getPuzzlePane () {
        return this.puzzlePane;
    }

    // Setters
    void setNewGameData (int gameSize) {
        this.size = gameSize;
        this.cells = new Cell[gameSize][gameSize];
    }

    public void setSelectedCell (Cell c) {
        this.selectedCell = c;
    }

}