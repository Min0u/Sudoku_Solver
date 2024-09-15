package solver;

import sudoku.Utils;
import sudoku.grid.Cell;
import sudoku.grid.SudokuGrid;

import java.util.Scanner;
import java.util.Set;

public class InteractiveSolverState implements SolverState {

    private boolean userInteractionDone = false;

    @Override
    public boolean solve(SudokuSolver solver) {
        if (!userInteractionDone) {
            printInteractiveModeMessage();
            solver.getGrid().printGrid();

            Scanner scanner = new Scanner(System.in);
            while (!userInteractionDone) {
                System.out.print("The solver needs your help. If you don't want to help, type 'quit'.\n");

                int row = getValidLine(scanner, true);
                int col = getValidLine(scanner, false);

                if (isValidValue(scanner, solver, row, col)) {
                    userInteractionDone = true;
                }
            }
        }

        // Return to automatic solving mode after the user interaction
        solver.setState(new AutomaticSolverState(false));
        return solver.getState().solve(solver);
    }

    private void printInteractiveModeMessage() {
        System.out.println("No progress was made in the automatic solving mode.");
        System.out.println("\u001B[36mSwitching to interactive solving mode...\u001B[0m");
        System.out.println("Please select the row and column where you want to fill a value.");
        System.out.println("Here's the current state of the Sudoku grid:");
    }

    private String getUserInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int getValidLine(Scanner scanner, boolean isRow) {
        while (true) {
            String input = getUserInput(scanner, "Enter the " + (isRow ? "row" : "column") + " number (1-9): ");

            Utils.validateQuitCommand(input);

            if (isValidInteger(input)) {
                int line = Integer.parseInt(input);
                if (line >= 1 && line <= 9) {
                    return line;
                }
            }
            System.out.println("\u001B[31mInvalid input. Please enter a number between 1 and 9.\u001B[0m");
        }
    }

    private boolean isValidInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidValue(Scanner scanner, SudokuSolver solver, int row, int col) {
        SudokuGrid grid = solver.getGrid();
        Cell cell = grid.getCell(row - 1, col - 1);
        if (cell.getValue() != -1) {
            System.out.println("\u001B[31mThis cell is already filled. Please select another cell.\u001B[0m");
            return false;
        }

        Set<Integer> possibleValues = cell.getCellPossibleValues();
        System.out.printf("\u001B[33mPossible values for cell (%d, %d): %s\n\u001B[0m", row, col, possibleValues);

        while (true) {
            String input = getUserInput(scanner, "Enter the value you want to fill: ");

            Utils.validateQuitCommand(input);

            if (isValidInteger(input)) {
                int value = Integer.parseInt(input);
                if (possibleValues.contains(value)) {
                    cell.setValue(value);
                    return true;
                }
            }
            System.out.println("\u001B[31mInvalid input. Please enter a valid value from the list above.\u001B[0m");
        }
    }
}