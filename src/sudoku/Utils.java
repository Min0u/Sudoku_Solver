package sudoku;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utils {
    private static final String BOLD_PINK = "\u001B[35m\u001B[1m";
    private static final String TURQUOISE = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";

    private static final String SEPARATOR = "[\\s,;]+";

    private Utils() {
    }

    /*
     * Prints the welcome message and instructions.
     */
    public static void welcome() {
        System.out.println(BOLD_PINK + "Sudoku Solver" + RESET);
        System.out.println("Welcome to the Sudoku Solver!\n");

        printInstructions();
        printExample();
    }

    /*
     * Prints the instructions for the user.
     */
    private static void printInstructions() {
        System.out.println(TURQUOISE + "Instructions:" + RESET);
        System.out.println("Use 0 or -1 to represent empty cells.");
        System.out.println("You can enter the grid in one of the following ways, using commas to separate integers:");
        System.out.println("1. As a single line of 81 integers.");
        System.out.println("2. Row by row, with 9 integers per row.");
        System.out.println("3. From a file, with each grid on a separate line.");
        System.out.println("Type \"quit\" to exit.\n");
    }

    /*
     * Prints an example for row by row input.
     */
    private static void printExample() {
        System.out.println(YELLOW + "Example for a row by row input:" + RESET);
        System.out.println("Enter row 1: 5,3,-1,-1,7,0,-1,-1,-1");
        System.out.println("(For the entire grid, you would enter 81 integers separated by commas.)");
        System.out.println();
    }

    /*
     * Reads the input from the user.
     *
     * Allows the user to enter the entire grid, row by row, or read from a file.
     * Based on the user's choice, it calls the appropriate method to read the input.
     */
    public static List<int[]> readInput() {
        Scanner scanner = new Scanner(System.in);
        String choice = getUserChoice(scanner);

        return switch (choice) {
            case "E" -> readEntireGrid();
            case "R" -> readRowByRow();
            case "F" -> readFromFile();
            default -> new ArrayList<>();
        };
    }

    /*
     * Gets the user's choice for entering the grid.
     */
    private static String getUserChoice(Scanner scanner) {
        while (true) {
            System.out.print("Enter the entire grid, row by row, or read from a file? (E/R/F): ");
            String choice = scanner.nextLine().trim().toUpperCase();
            // choice must be E, R, or F
            if (choice.matches("[ERF]")) {
                return choice;
            } else {
                validateQuitCommand(choice);
                System.out.println(RED + "Invalid choice. Please enter E, R, or F." + RESET);
            }
        }
    }

    /*
     * Reads the grid row by row.
     */
    private static ArrayList<int[]> readRowByRow() {
        ArrayList<int[]> grids = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        int[] linearGrid = new int[81];

        for (int row = 0; row < 9; row++) {
            int[] rowData = getValidRowData(scanner, row);
            System.arraycopy(rowData, 0, linearGrid, row * 9, 9);
        }

        grids.add(linearGrid);
        return grids;
    }

    /*
     * Checks if the row data entered by the user is valid.
     * If not, prompts the user to enter the row again.
     */
    private static int[] getValidRowData(Scanner scanner, int row) {
        while (true) {
            System.out.print("Enter row " + (row + 1) + ": ");
            String input = scanner.nextLine().trim();
            validateQuitCommand(input);

            // Split the input by any whitespace character, comma, or semicolon
            String[] values = input.split(SEPARATOR);
            if (values.length == 9 && isValidInput(values)) {
                return convertToIntArray(values);
            } else {
                System.out.println(RED + "Invalid input. Please enter 9 integers between -1 and 9." + RESET);
            }
        }
    }

    /*
     * Reads the entire grid from the user.
     */
    private static ArrayList<int[]> readEntireGrid() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter the entire grid (81 integers separated by commas): ");
            String input = scanner.nextLine().trim();
            validateQuitCommand(input);

            // Split the input by any whitespace character, comma, or semicolon
            String[] values = input.split(SEPARATOR);
            if (values.length == 81 && isValidInput(values)) {
                ArrayList<int[]> grids = new ArrayList<>();
                grids.add(convertToIntArray(values));
                return grids;
            } else {
                System.out.println(RED + "Invalid input. Please enter 81 integers." + RESET);
            }
        }
    }

    /*
     * Reads the grids from a file.
     */
    private static ArrayList<int[]> readFromFile() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter the file path: ");
            String filePath = scanner.nextLine().trim();
            validateQuitCommand(filePath);

            File file = new File(filePath);
            try (Scanner fileScanner = new Scanner(file)) {
                return parseGridsFromFile(fileScanner);
            } catch (FileNotFoundException e) {
                System.out.println(RED + "File not found. Please try again." + RESET);
            }
        }
    }

    /*
     * Parses the grids from the file.
     */
    private static ArrayList<int[]> parseGridsFromFile(Scanner fileScanner) {
        ArrayList<int[]> grids = new ArrayList<>();

        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine().trim();
            if (line.isEmpty()) continue;

            // Split the line by any whitespace character, comma, or semicolon
            String[] values = line.split(SEPARATOR);
            if (values.length == 81 && isValidInput(values)) {
                grids.add(convertToIntArray(values));
            } else {
                System.out.println(RED + "Invalid grid format in file." + RESET);
            }
        }

        return grids;
    }

    /*
     * Checks if the input values are valid.
     */
    private static boolean isValidInput(String[] values) {
        for (String value : values) {
            try {
                int intValue = Integer.parseInt(value.trim());
                if (intValue < -1 || intValue > 9) return false;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    /*
     * Validates the quit command.
     */
    public static void validateQuitCommand(String input) {
        if (input.equalsIgnoreCase("quit")) {
            System.out.println("\u001B[36mExiting program..." + RESET);
            System.exit(0);
        }
    }

    /*
     * Converts the string array to an integer array.
     */
    private static int[] convertToIntArray(String[] values) {
        int[] intArray = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            intArray[i] = Integer.parseInt(values[i].trim());
        }
        return intArray;
    }
}