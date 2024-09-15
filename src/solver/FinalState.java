package solver;

import sudoku.grid.SudokuGrid;

public class FinalState implements SolverState {
    private final boolean solved;
    private final int iterations;
    private final SudokuGrid grid;

    public FinalState(boolean solved, int iterations, SudokuGrid grid) {
        this.solved = solved;
        this.iterations = iterations;
        this.grid = grid;
    }

    @Override
    public boolean solve(SudokuSolver solver) {
        printSolveStatus();
        printGrid();
        printDeductionRuleUsage(solver);
        return solved;
    }

    private void printSolveStatus() {
        String message = solved ?
                "\u001B[32mSudoku solved after " + iterations + " iterations.\u001B[0m" :
                "\u001B[31mThe Sudoku couldn't be fully solved after " + iterations + " iterations.\u001B[0m";
        System.out.println(message);
    }

    private void printGrid() {
        grid.printGrid();
    }

    private void printDeductionRuleUsage(SudokuSolver solver) {
        System.out.printf("DR1 was used %d times.\n", solver.getDr1Uses());
        System.out.printf("DR2 was used %d times.\n", solver.getDr2Uses());
        System.out.printf("DR3 was used %d times.\n", solver.getDr3Uses());
    }
}