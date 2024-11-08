package solver;

import rules.DeductionRule;
import sudoku.grid.Cell;
import sudoku.grid.SudokuGrid;

public class AutomaticSolverState implements SolverState {
    boolean userCanHelp;

    public AutomaticSolverState(boolean userHelp) {
        this.userCanHelp = userHelp;
    }

    /*
     * Solves the grid using automatic solving mode.
     */
    @Override
    public boolean solve(SudokuSolver solver) {
        printSolvingModeMessage();
        printInitialGrid(solver);

        int iterations = 0;
        boolean progressMade;

        do {
            progressMade = processDeductionRules(solver);
            iterations++;
        } while (progressMade && !solver.getGrid().isSolved());

        if (solver.getGrid().isSolved()) {
            handleSolvedGrid(solver, iterations);
            return true;
        } else {
            return handleUnsolvedGrid(solver, iterations);
        }
    }

    /*
     * Prints a message indicating that the automatic solving mode is active.
     */
    private void printSolvingModeMessage() {
        System.out.println("\u001B[36mAutomatic solving mode...\u001B[0m");
    }

    /*
     * Prints the initial state of the grid.
     */
    private void printInitialGrid(SudokuSolver solver) {
        SudokuGrid grid = solver.getGrid();
        grid.printGrid();
    }

    /*
     * Processes the deduction rules in order until no more progress can be made.
     */
    private boolean processDeductionRules(SudokuSolver solver) {
        boolean progressMade = false;
        Cell[][] previousGrid = solver.copyGrid(solver.getGrid().getGrid());

        for (DeductionRule rule : solver.getDeductionRules()) {
            applyRule(solver, rule);
            if (solver.gridChanged(previousGrid, solver.getGrid().getGrid())) {
                progressMade = true;
                break;  // If progress is made, start over with the first rule
            }
        }

        return progressMade;
    }

    /*
     * Applies a deduction rule to the grid.
     */
    private void applyRule(SudokuSolver solver, DeductionRule rule) {
        SolverContext context = solver.getContext();
        context.setStrategy(rule);
        context.execute(solver.getGrid());
        solver.updateRuleUsage(rule);
    }

    /*
     * Handles the case when the grid is solved.
     */
    private void handleSolvedGrid(SudokuSolver solver, int iterations) {
        solver.classifyDifficulty();
        solver.setState(new FinalState(true, iterations, solver.getGrid()));
        SolverState state = solver.getState();
        state.solve(solver);
    }

    /*
     * Handles the case when the grid is unsolved.
     */
    private boolean handleUnsolvedGrid(SudokuSolver solver, int iterations) {
        if (userCanHelp) {
            solver.setState(new InteractiveSolverState());
            boolean interactiveResult = solver.getState().solve(solver);

            if (!interactiveResult) {
                System.out.println("User chose not to assist. The grid is unsolvable.");
                solver.setState(new FinalState(false, iterations, solver.getGrid()));
                return false;
            }
        } else {
            System.out.println("Automatic solving mode could not complete. The grid is unsolvable.");
            solver.setState(new FinalState(false, iterations, solver.getGrid()));
        }

        return solver.getGrid().isSolved();
    }
}