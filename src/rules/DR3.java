package rules;

import sudoku.grid.Cell;
import sudoku.grid.SudokuGrid;
import java.util.HashSet;
import java.util.Set;

public class DR3 implements DeductionRule {
    // SINGLETON //

    // DR3 ("Pointing Pair" rule): If a value can only appear in two cells in a row
    // or column within a 3x3 box, then that value cannot appear in any other cells
    // in the same row or column outside the 3x3 box.

    private static DR3 instance;

    private DR3() {
    }

    public static DR3 getInstance() {
        if (instance == null) {
            instance = new DR3();
        }
        return instance;
    }

    @Override
    public void applyRule(SudokuGrid grid) {
        for (int boxRow = 0; boxRow < 9; boxRow += 3) {
            for (int boxCol = 0; boxCol < 9; boxCol += 3) {
                applyPointingPairBox(grid, boxRow, boxCol);
            }
        }
    }

    private void applyPointingPairBox(SudokuGrid grid, int boxRow, int boxCol) {
        for (int value = 1; value <= 9; value++) {
            Set<Integer> possibleRows = new HashSet<>();
            Set<Integer> possibleCols = new HashSet<>();

            // Find the possible rows and columns where the value can appear
            for (int i = boxRow; i < boxRow + 3; i++) {
                for (int j = boxCol; j < boxCol + 3; j++) {
                    Cell cell = grid.getCell(i, j);
                    if (cell.getValue() == -1 && cell.getCellPossibleValues().contains(value)) {
                        possibleRows.add(i);
                        possibleCols.add(j);
                    }
                }
            }

            // Check if the value can only appear in one row or column within the 3x3 box
            checkPointingPairLine(grid, boxRow, boxCol, value, possibleRows, true);
            checkPointingPairLine(grid, boxRow, boxCol, value, possibleCols, false);
        }
    }

    private void checkPointingPairLine(SudokuGrid grid, int boxRow, int boxCol, int value, Set<Integer> possibleLines, boolean isRow) {
        // If there's only one unique row or column where the value can appear in the 3x3 box
        if (possibleLines.size() == 1) {
            int line = possibleLines.iterator().next();

            // Eliminate the value from the rest of the row or column outside the 3x3 box
            eliminateOutsideBox(grid, boxRow, boxCol, value, line, isRow);
        }
    }

    private void eliminateOutsideBox(SudokuGrid grid, int boxRow, int boxCol, int value, int index, boolean isRow) {
        // Eliminate the value from the rest of the row or column outside the 3x3 box
        if (isRow) {
            for (int col = 0; col < 9; col++) {
                if (col < boxCol || col >= boxCol + 3) {
                    grid.removeCellPossibleValue(index, col, value);
                }
            }
        } else {
            for (int row = 0; row < 9; row++) {
                if (row < boxRow || row >= boxRow + 3) {
                    grid.removeCellPossibleValue(row, index, value);
                }
            }
        }
    }
}