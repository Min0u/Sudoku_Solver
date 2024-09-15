package rules;

import sudoku.grid.SudokuGrid;

public interface DeductionRule {
    // Strategy //

    void applyRule(SudokuGrid grid);
}