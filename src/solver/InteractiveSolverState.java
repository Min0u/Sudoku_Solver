package solver;

import sudoku.Utils;
import sudoku.grid.Cell;
import sudoku.grid.SudokuGrid;

import java.util.Scanner;
import java.util.Set;

public class InteractiveSolverState implements SolverState {

    private boolean userInteractionDone = false;

    /*
     * Solves the grid using interactive solving mode.
     */
    @Override
    public boolean solve(SudokuSolver solver) {
        if (!userInteractionDone) {
            printInteractiveModeMessage();
            solver.getGrid().printGrid();

            Scanner scanner = new Scanner(System.in);
            while (!userInteractionDone) {
                System.out.print("The solver needs your help. If you don't want to help, type 'no' or 'n' to skip this grid or 'quit' to exit.\n");

                int row = getValidLine(scanner, true); // Get a valid row number
                if (row == -1) {
                    return false;
                }

                int col = getValidLine(scanner, false); // Get a valid column number
                if (col == -1) {
                    return false;
                }

                if (isValidValue(scanner, solver, row, col)) { // Get a valid value for the selected cell
                    userInteractionDone = true;
                }
            }
        }

        // Return to automatic solving mode after the user interaction
        solver.setState(new AutomaticSolverState(false));
        return solver.getState().solve(solver);
    }

    /*
     * Prints a message indicating that it's switching to interactive solving mode.
     */
    private void printInteractiveModeMessage() {
        System.out.println("No progress was made in the automatic solving mode.");
        System.out.println("\u001B[36mSwitching to interactive solving mode...\u001B[0m");
        System.out.println("Here's the current state of the Sudoku grid:");
    }

    /*
     * Gets user input from the console.
     */
    private String getUserInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /*
     * Gets a valid row or column number from the user or allows skipping.
     */
    private int getValidLine(Scanner scanner, boolean isRow) {
        while (true) {
            String input = getUserInput(scanner, "Enter the " + (isRow ? "row" : "column") + " number (1-9), or 'no'/'n' to skip: ");

            if (input.equalsIgnoreCase("no") || input.equalsIgnoreCase("n")) {
                return -1; // Signal to skip the grid
            }

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

    /*
     * Checks if the input is a valid integer.
     */
    private boolean isValidInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /*
     * Checks if the value entered by the user is valid for the selected cell or allows skipping.
     */
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
            String input = getUserInput(scanner, "Enter the value you want to fill, or 'no'/'n' to skip: ");

            if (input.equalsIgnoreCase("no") || input.equalsIgnoreCase("n")) {
                return false; // Signal to mark as unsolvable
            }

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