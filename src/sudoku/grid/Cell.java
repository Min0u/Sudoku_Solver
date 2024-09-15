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

        Set<Integer> possibleVal = new HashSet<>();
        for (int val = 1; val <= 9; val++) {
            possibleVal.add(val);
        }

        this.possibleValues = possibleVal;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getValue() {
        return value;
    }

    public void setCellPossibleValues(Set<Integer> integers) {
        this.possibleValues.clear();
        this.possibleValues.addAll(integers);
    }

    public void setValue(int value) {
        this.value = value;
        removePossibleValue(value);
    }

    public void removePossibleValue(int value) {
        possibleValues.remove(value);
    }

    public Set<Integer> getCellPossibleValues() {
        return this.possibleValues;
    }
}