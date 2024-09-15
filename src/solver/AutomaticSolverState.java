package solver;

import rules.DeductionRule;
import sudoku.grid.Cell;
import sudoku.grid.SudokuGrid;

public class AutomaticSolverState implements SolverState {
    boolean userCanHelp;

    public AutomaticSolverState(boolean userHelp) {
        this.userCanHelp = userHelp;
    }

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
            handleUnsolvedGrid(solver, iterations);
            return false;
        }
    }

    private void printSolvingModeMessage() {
        System.out.println("\u001B[36mAutomatic solving mode...\u001B[0m");
    }

    private void printInitialGrid(SudokuSolver solver) {
        SudokuGrid grid = solver.getGrid();
        grid.printGrid();
    }

    private boolean processDeductionRules(SudokuSolver solver) {
        boolean progressMade = false;
        Cell[][] previousGrid = solver.copyGrid(solver.getGrid().getGrid());

        for (DeductionRule rule : solver.getDeductionRules()) {
            applyRule(solver, rule);
            if (solver.gridChanged(previousGrid, solver.getGrid().getGrid())) {
                progressMade = true;
                break;  // If progressed, start over with DR1
            }
        }

        return progressMade;
    }

    private void applyRule(SudokuSolver solver, DeductionRule rule) {
        SolverContext context = solver.getContext();
        context.setStrategy(rule);
        context.execute(solver.getGrid());
        solver.updateRuleUsage(rule);
    }

    private void handleSolvedGrid(SudokuSolver solver, int iterations) {
        solver.classifyDifficulty();
        solver.setState(new FinalState(true, iterations, solver.getGrid()));
        SolverState state = solver.getState();
        state.solve(solver);
    }

    private void handleUnsolvedGrid(SudokuSolver solver, int iterations) {
        if (userCanHelp) {
            solver.setState(new InteractiveSolverState());
        } else {
            solver.setState(new FinalState(false, iterations, solver.getGrid()));
        }
        SolverState state = solver.getState();
        state.solve(solver);
    }
}