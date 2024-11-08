package sudoku.grid;

public class SudokuGrid {
    private final Cell[][] grid;

    public SudokuGrid(int[] linearGrid) {
        linearGrid = transformLinearGrid(linearGrid);

        this.grid = initializeGrid();

        for (int i = 0; i < 81; i++) {
            int row = i / 9;
            int col = i % 9;
            setValue(row, col, linearGrid[i]);
        }
    }

    /*
     * Gets the iterator for the grid.
     */
    public CellIterator iterator() {
        return new SudokuCellIterator(this);
    }

    /*
     * Initializes the grid with empty cells.
     */
    private Cell[][] initializeGrid() {
        Cell[][] newGrid = new Cell[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                newGrid[row][col] = new Cell(row, col);
            }
        }
        return newGrid;
    }

    /*
     * Removes a possible value from a cell.
     */
    public void removeCellPossibleValue(int row, int col, int value) {
        Cell cell = grid[row][col];
        cell.removePossibleValue(value);
    }


    /*
     * Sets the value of a cell.
     */
    public void setValue(int row, int column, int value) {
        Cell cell = grid[row][column];
        cell.setValue(value);

        for (int i = 0; i < 9; i++) {
            removeCellPossibleValue(row, i, value);
            removeCellPossibleValue(i, column, value);
        }

        int startRow = (row / 3) * 3;
        int startCol = (column / 3) * 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                removeCellPossibleValue(i, j, value);
            }
        }
    }

    /*
     * Gets the cell at the specified row and column.
     */
    public Cell getCell(int row, int col) {
        return grid[row][col];
    }

    /*
     * Gets the grid.
     */
    public Cell[][] getGrid() {
        return grid;
    }

    /*
     * Prints the grid.
     */
    public void printGrid() {
        StringBuilder sb = new StringBuilder();
        CellIterator iterator = iterator();
        for (int row = 0; row < 9; row++) {
            if (row % 3 == 0) {
                sb.append("*-----------------------*\n");
            }
            for (int col = 0; col < 9; col++) {
                if (col % 3 == 0) {
                    sb.append("| ");
                }
                Cell cell = iterator.next();
                sb.append(cell.getValue() == -1 ? "." : cell.getValue());
                sb.append(" ");
            }
            sb.append("|\n");
        }
        sb.append("*-----------------------*\n");
        System.out.print(sb);
    }

    /*
     * Checks if the grid is solved.
     */
    public boolean isSolved() {
        CellIterator iterator = iterator();
        while (iterator.hasNext()) {
            Cell cell = iterator.next();
            if (cell.getValue() == -1) {
                return false;
            }
        }
        return true;
    }

    /*
     * Transforms the linear grid to a grid with -1 for empty cells.
     */
    public int[] transformLinearGrid(int[] linearGrid) {
        int[] transformedGrid = new int[81];
        for (int i = 0; i < 81; i++) {
            transformedGrid[i] = linearGrid[i] == 0 ? -1 : linearGrid[i];
        }
        return transformedGrid;
    }
}