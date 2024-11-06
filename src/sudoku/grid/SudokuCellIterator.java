package sudoku.grid;

public class SudokuCellIterator implements CellIterator {
    private final SudokuGrid grid;
    private int currentRow;
    private int currentCol;

    public SudokuCellIterator(SudokuGrid grid) {
        this.grid = grid;
        this.currentRow = 0;
        this.currentCol = 0;
    }

    /*
     * Checks if there are more cells to iterate.
     */
    @Override
    public boolean hasNext() {
        return currentRow < 9 && currentCol < 9;
    }

    /*
     * Gets the next cell to iterate.
     */
    @Override
    public Cell next() {
        if (!hasNext()) {
            throw new IllegalStateException("No more cells to iterate.");
        }

        Cell newcell = grid.getCell(currentRow, currentCol);

        currentCol++;
        if (currentCol == 9) {
            currentCol = 0;
            currentRow++;
        }

        return newcell;
    }
}