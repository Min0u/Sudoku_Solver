package solver;

import rules.DeductionRule;
import sudoku.grid.SudokuGrid;

public class SolverContext {
    // Strategy //

    private DeductionRule rule;

    public void setStrategy(DeductionRule rule) {
        this.rule = rule;
    }

    public void execute(SudokuGrid grid) {
        rule.applyRule(grid);
    }
}
