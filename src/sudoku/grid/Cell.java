package sudoku.grid;

import java.util.HashSet;
import java.util.Set;

public class Cell {
    private final int row;
    private final int col;
    private int value;
    private final Set<Integer> possibleValues;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.value = -1;

        // Initialize the possible values for the cell
        Set<Integer> possibleVal = new HashSet<>();
        for (int val = 1; val <= 9; val++) {
            possibleVal.add(val);
        }

        this.possibleValues = possibleVal;
    }

    /*
     * Gets the row of the cell.
     */
    public int getRow() {
        return row;
    }

    /*
     * Gets the column of the cell.
     */
    public int getCol() {
        return col;
    }

    /*
     * Gets the value of the cell.
     */
    public int getValue() {
        return value;
    }

    /*
     * Sets the possible values for the cell.
     */
    public void setCellPossibleValues(Set<Integer> integers) {
        this.possibleValues.clear();
        this.possibleValues.addAll(integers);
    }

    /*
     * Sets the value of the cell.
     */
    public void setValue(int value) {
        this.value = value;
        removePossibleValue(value);
    }

    /*
     * Removes a possible value from the cell.
     */
    public void removePossibleValue(int value) {
        possibleValues.remove(value);
    }

    /*
     * Gets the possible values for the cell.
     */
    public Set<Integer> getCellPossibleValues() {
        return this.possibleValues;
    }
}