import solver.SudokuSolver;
import sudoku.Utils;

public class Main {
    public static void main(String[] args) {
        Utils.welcome();              // Prints the welcome message and instructions.
        SudokuSolver.solveAllGrids(); // Solves all the grids.
    }
}