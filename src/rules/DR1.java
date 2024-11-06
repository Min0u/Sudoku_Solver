package rules;

import sudoku.grid.Cell;
import sudoku.grid.CellIterator;
import sudoku.grid.SudokuGrid;

public class DR1 implements DeductionRule {
    // SINGLETON //

    // DR1 ("Naked Single" rule): If a cell has only one possible value,
    // then that value must be the correct value.

    private static DR1 instance;

    private DR1() {
    }

    /*
     * Returns the instance of DR1.
     */
    public static DR1 getInstance() {
        if (instance == null) {
            instance = new DR1();
        }
        return instance;
    }

    /*
     * Applies the Naked Single rule to the grid.
     */
    @Override
    public void applyRule(SudokuGrid grid) {
        CellIterator iterator = grid.iterator();
        while (iterator.hasNext()) {
            Cell cell = iterator.next();
            if (cell.getValue() == -1 && cell.getCellPossibleValues().size() == 1) {
                int value = cell.getCellPossibleValues().iterator().next();
                grid.setValue(cell.getRow(), cell.getCol(), value);
            }
        }
    }
}