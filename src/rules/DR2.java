package rules;

import sudoku.grid.Cell;
import sudoku.grid.SudokuGrid;

public class DR2 implements DeductionRule {
    // SINGLETON //

    // DR2 ("Hidden Single" rule): If a value can only appear in one cell in a row, column,
    // or 3x3 box, then that cell must contain that value.
    
    private static DR2 instance;

    private DR2() {
    }

    /*
     * Returns the instance of DR2.
     */
    public static DR2 getInstance() {
        if (instance == null) {
            instance = new DR2();
        }
        return instance;
    }

    /*
     * Applies the Hidden Single rule to the grid.
     */
    @Override
    public void applyRule(SudokuGrid grid) {
        // Look for hidden singles in 3x3 boxes
        for (int boxRow = 0; boxRow < 9; boxRow += 3) {
            for (int boxCol = 0; boxCol < 9; boxCol += 3) {
                applyHiddenSingleBox(grid, boxRow, boxCol);
            }
        }

        // Look for hidden singles in rows
        for (int row = 0; row < 9; row++) {
            applyHiddenSingleLine(grid, row, true);
        }

        // Look for hidden singles in columns
        for (int col = 0; col < 9; col++) {
            applyHiddenSingleLine(grid, col, false);
        }
    }

    /*
     * Applies the Hidden Single rule to a row or column.
     */
    private void applyHiddenSingleLine(SudokuGrid grid, int line, boolean isRow) {
        for (int value = 1; value <= 9; value++) {
            int possibleIndex = findUniqueValueIndex(grid, line, value, isRow);
            if (possibleIndex != -1) {
                setValueInGrid(grid, line, possibleIndex, value, isRow);
                return;
            }
        }
    }

    /*
     * Finds the index of a unique value in a row or column.
     */
    private int findUniqueValueIndex(SudokuGrid grid, int line, int value, boolean isRow) {
        int count = 0;
        int possibleLocation = -1;

        for (int i = 0; i < 9; i++) {
            int row = isRow ? line : i;
            int col = isRow ? i : line;
            Cell cell = grid.getCell(row, col);

            if (cell.getValue() == -1 && cell.getCellPossibleValues().contains(value)) {
                count++;
                possibleLocation = i;
                if (count > 1) {
                    return -1; // count > 1, so not a hidden single => stop searching
                }
            }
        }

        return count == 1 ? possibleLocation : -1;
    }

    /*
     * Sets the value in the grid.
     */
    private void setValueInGrid(SudokuGrid grid, int line, int index, int value, boolean isRow) {
        int row = isRow ? line : index;
        int col = isRow ? index : line;
        grid.setValue(row, col, value);
    }

    /*
     * Applies the Hidden Single rule to a 3x3 box.
     */
    private void applyHiddenSingleBox(SudokuGrid grid, int boxRow, int boxCol) {
        for (int value = 1; value <= 9; value++) {
            int count = 0;
            int row = -1;
            int col = -1;

            for (int r = boxRow; r < boxRow + 3; r++) {
                for (int c = boxCol; c < boxCol + 3; c++) {
                    Cell cell = grid.getCell(r, c);
                    if (cell.getValue() == -1 && cell.getCellPossibleValues().contains(value)) {
                        count++;
                        row = r;
                        col = c;
                    }
                }
            }

            if (count == 1) {
                grid.setValue(row, col, value);
                return;
            }
        }
    }
}
