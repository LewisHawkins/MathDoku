import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;

import java.awt.*;
//import java.awt.event.*;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.Date;

public class Cell {//implements ActionListener, WindowListener {
    // A class for each of the cells that the user can input a number into
    //private GridPane container;
    private VBox box;
    private Label cageValue;
    private int value;
    private int row;
    private int column;
    //private HBox displayContainer;
    private Label display;
    private boolean selected;
    private long selectedTime;

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

        // The label for the cage's target
        this.cageValue = new Label(" "); // Keep a space at the start so it looks nice
        this.cageValue.setPrefHeight(10);
        this.value = 0;

        // The cell's co-ords
        this.row = row;
        this.column = col;

        // The text box that the user will use to enter the number
        this.display = new Label("");
        this.display.setAlignment(Pos.TOP_CENTER);
        this.display.setPrefWidth(60);
        this.display.setStyle(" -fx-font: 16 arial; -fx-font-weight: bold;");
        //this.display.setStyle("-fx-border-style: solid; -fx-border-color: #ffffff; -fx-font: 16 arial; -fx-font-weight: bold; -fx-text-box-border: transparent"); // -fx-border-width: 10px; WTFFF

        // Add the functionality to interact with the user
        this.selected = false;
        this.selectedTime = 0;
        // Add the reaction to the cell being clicked
        this.box.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle (MouseEvent e) {
                //System.out.println("Cell call!");
                Date d = new Date();
                setSelectedTime(d.getTime());
                setSelected(true);
                //dispCage();
            }
        }));

        // Put the components together
        this.box.getChildren().addAll(this.cageValue, this.display);//Container);
        // Display the cell
        this.dispCage();
    }

    // Interaction
    /*
    public void actionPerformed(ActionEvent e) {
        System.out.println("WOOO >>>");
        //text.setText("Button Clicked " + numClicks + " times");
    }
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }
    public void windowOpened(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}

     */


    public void dispCage () {
        // Get the borders up to date
        String setCss;
        if (this.selected) {
            //System.out.println("SET ME BLUE");
            setCss = "-fx-background-color: #000000, #29b6f6; -fx-background-insets: 0,";
        } else {
            //System.out.println("SET ME WHITE");
            setCss = "-fx-background-color: #000000, #ffffff; -fx-background-insets: 0,";
        }
        /*
        else {
            String setCss = "-fx-background-color: #000000, #00ff00; -fx-background-insets: 0,"; FOR WHEN A CELL IS CORRECT
        } else {
            String setCss = "-fx-background-color: #000000, #ffbbaa; -fx-background-insets: 0,"; FOR WHEN A CELL IS INCORRECT
        }
         */
        for  (int i : this.getCurrentBorders()) {
            setCss += (" " + i);
        }
        setCss += ";";
        this.box.setStyle(setCss);
        // Get the target label up to date

        // Get the value up to date
    }

    // Getters
    public int getValue () {
        return this.value;
    }

    public String getDisplay () {
        return this.display.getText();
    }

    public VBox getBox () {
        return this.box;
    }

    public int getRow () {
        return this.row;
    }

    public int getColumn () {
        return this.column;
    }

    public long getSelectedTime () {
        return this.selectedTime;
    }

    public boolean isSelected () {
        return this.selected;
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
    public void setLeftCage (boolean b) {
        this.leftCageOn = b;
    }

    public void setRightCage (boolean b) {
        this.rightCageOn = b;
    }

    public void setTopCage (boolean b) {
        this.topCageOn = b;
    }

    public void setBottomCage (boolean b) {
        this.bottomCageOn = b;
    }

    public void setAllCages (boolean[] borders) {
        this.setLeftCage(borders[0]);
        this.setRightCage(borders[1]);
        this.setTopCage(borders[2]);
        this.setBottomCage(borders[3]);
    }

    public void setDisplay (int newNum) {
        if (newNum == 0) {
            this.display.setText("");
        } else {
            this.display.setText(Integer.toString(newNum));
        }
    }

    public void setCageValue (String newText) {
        this.cageValue.setText(newText);
    }

    public void setSelected (Boolean b) {
        this.selected = b;
    }

    public void setSelectedTime (long d) {
        this.selectedTime = d;
    }
}