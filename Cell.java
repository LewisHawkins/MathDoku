import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;

import java.awt.*;
import java.awt.event.*;

public class Cell {// implements ActionListener {
    // A class for each of the cells that the user can input a number into
    //private GridPane container;
    private VBox box;
    private Label cageValue;
    private int value;
    private int row;
    private int column;
    private HBox displayContainer;
    private Label display;
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

        //
        this.cageValue = new Label(" "); // Keep a space at the start so it looks nice
        this.cageValue.setPrefHeight(10);
        //this.cageValue.addActionListener

        // The cell's co-ords
        this.row = row;
        this.column = col;
        this.value = 0;

        // The text box that the user will use to enter the number
        this.displayContainer = new HBox(0);
        this.displayContainer.setPrefWidth(60);
        this.displayContainer.setAlignment(Pos.TOP_CENTER);

        this.display = new Label("_");
        this.display.setAlignment(Pos.TOP_CENTER);
        this.display.setPrefWidth(52); // CENTRE ALIGN + MAKE SIZE EQUAL (RESTRICT SIZE OF STRING TO 1?) TO 1 CHARACTER
        this.display.setPrefHeight(33);
        this.display.setStyle("-fx-border-style: solid; -fx-border-color: #ffffff; -fx-font: 16 arial; -fx-font-weight: bold; -fx-text-box-border: transparent"); // -fx-border-width: 10px; WTFFF

        this.displayContainer.getChildren().add(this.display);
        this.box.getChildren().addAll(this.cageValue, this.displayContainer);

        this.dispCage();
    }

    public void dispCage () {
        // Get the borders up to date
        String setCss = "-fx-background-color: #000000, #ffffff ; -fx-background-insets: 0,";
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

    public void setCageValue(String newText) {
        this.cageValue.setText(newText);
    }
}