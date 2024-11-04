package solver;

import rules.DR1;
import rules.DR2;
import rules.DR3;
import rules.DeductionRule;
import sudoku.grid.Cell;
import sudoku.grid.SudokuGrid;
import sudoku.Utils;

import java.util.*;

public class SudokuSolver {
    private final SudokuGrid grid;
    private final List<DeductionRule> deductionRules;
    private SolverState state;
    private final SolverContext context;
    private int dr1Uses = 0;
    private int dr2Uses = 0;
    private int dr3Uses = 0;
    private static final String RESET = "\u001B[0m";

    public SudokuSolver(SudokuGrid grid) {
        this.grid = grid;
        this.deductionRules = Arrays.asList(DR1.getInstance(), DR2.getInstance(), DR3.getInstance());
        this.state = new AutomaticSolverState(true);
        this.context = new SolverContext();
    }

    public boolean solve() {
        return state.solve(this);
    }

    public SudokuGrid getGrid() {
        return grid;
    }

    public List<DeductionRule> getDeductionRules() {
        return deductionRules;
    }

    public void setState(SolverState state) {
        this.state = state;
    }

    public SolverState getState() {
        return state;
    }

    public SolverContext getContext() {
        return context;
    }

    public int getDr1Uses() {
        return dr1Uses;
    }

    public int getDr2Uses() {
        return dr2Uses;
    }

    public int getDr3Uses() {
        return dr3Uses;
    }

    public void updateRuleUsage(DeductionRule rule) {
        if (rule instanceof DR1) {
            dr1Uses++;
        } else if (rule instanceof DR2) {
            dr2Uses++;
        } else if (rule instanceof DR3) {
            dr3Uses++;
        }
    }

    public DifficultyLevel classifyDifficulty() {
        if (dr1Uses > 0 && dr2Uses == 0 && dr3Uses == 0) {
            return DifficultyLevel.EASY;
        }
        if (dr1Uses > 0 && dr2Uses > 0 && dr3Uses == 0) {
            return DifficultyLevel.MEDIUM;
        }
        return DifficultyLevel.HARD;
    }

    public Cell[][] copyGrid(Cell[][] grid) {
        Cell[][] copy = new Cell[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                copy[row][col] = new Cell(row, col);
                copy[row][col].setValue(grid[row][col].getValue());
                copy[row][col].setCellPossibleValues(grid[row][col].getCellPossibleValues());
            }
        }
        return copy;
    }

    public boolean gridChanged(Cell[][] grid1, Cell[][] grid2) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if ((grid1[row][col].getValue() != grid2[row][col].getValue())
                        || !grid1[row][col].getCellPossibleValues().equals(grid2[row][col].getCellPossibleValues())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void solveAllGrids() {
        List<int[]> allGrids = Utils.readInput();

        if (allGrids != null && !allGrids.isEmpty()) {
            int totalGrids = allGrids.size();
            int solvedCount = 0;
            Map<DifficultyLevel, Integer> difficultyCount = new EnumMap<>(DifficultyLevel.class);

            for (DifficultyLevel level : DifficultyLevel.values()) {
                difficultyCount.put(level, 0);
            }

            for (int i = 0; i < totalGrids; i++) {
                System.out.println("\n\u001B[35m\u001B[1mSolving grid #" + (i + 1) + ":" + RESET);

                SudokuGrid grid = new SudokuGrid(allGrids.get(i));
                SudokuSolver solver = new SudokuSolver(grid);
                boolean solved = solver.solve();

                if (solved) {
                    solvedCount++;
                    DifficultyLevel difficulty = solver.classifyDifficulty();
                    difficultyCount.merge(difficulty, 1, Integer::sum); // Increment the count for the difficulty level
                }
            }

            double percentageSolved = ((double) solvedCount / totalGrids) * 100;
            System.out.println("\nTotal grids: " + totalGrids);
            System.out.println("\u001B[36mSolved grids: " + solvedCount + RESET);
            System.out.println("\u001B[32mEasy: " + difficultyCount.get(DifficultyLevel.EASY) + RESET);
            System.out.println("\u001B[33mMedium: " + difficultyCount.get(DifficultyLevel.MEDIUM) + RESET);
            System.out.println("\u001B[31mHard: " + difficultyCount.get(DifficultyLevel.HARD) + RESET);
            System.out.printf("Percentage solved: %.2f%%\n", percentageSolved);
        } else {
            System.out.println("No valid grids were provided.");
        }
    }
}
