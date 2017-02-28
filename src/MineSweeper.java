import java.util.Random;

/**
 * {@link MineSweeper} represents minesweeper board game.
 *
 * @author Yoel Ivan
 * @version 0.0a
 */
public class MineSweeper {
    final private static Random RAND = new Random(System.currentTimeMillis());
    final private static char BOMB = '*';
    final private static char EMPTY = ' ';

    private enum CellState {
        FLAGGED, REVEALED, UNREVEALED
    }

    private enum GameState {
        WIN, LOSE, ONGOING
    }

    private char[][] board;
    private CellState[][] cellStates;
    private GameState gameState;
    private int unrevealedCellCount;

    private MineSweeper(int row, int col) {
        board = new char[row][col];
        cellStates = new CellState[row][col];
        unrevealedCellCount = row * col;
        gameState = GameState.ONGOING;
        for (int r = 0; r < cellStates.length; r++) {
            for (int c = 0; c < cellStates[0].length; c++) {
                cellStates[r][c] = CellState.UNREVEALED;
                board[r][c] = EMPTY;
            }
        }
    }

    private void setMines(int minesCount) {
        while (minesCount > 0) {
            int r = RAND.nextInt(board.length);
            int c = RAND.nextInt(board[0].length);
            if (board[r][c] == EMPTY) {
                board[r][c] = BOMB;
                minesCount--;
            }
        }
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                if (board[r][c] != BOMB) {
                    board[r][c] = countBomb(r, c);
                }
            }
        }
    }

    private char countBomb(int row, int col) {
        int count = 0;
        int initRow, initCol, endRow, endCol;
        if (row == 0) {
            initRow = 0;
        } else {
            initRow = row - 1;
        }
        if (row == board.length - 1) {
            endRow = row + 1;
        } else {
            endRow = row + 2;
        }
        if (col == 0) {
            initCol = 0;
        } else {
            initCol = col - 1;
        }
        if (col == board.length - 1) {
            endCol = col + 1;
        } else {
            endCol = col + 2;
        }
        for (int r = initRow; r < endRow; r++) {
            for (int c = initCol; c < endCol; c++) {
                if (board[r][c] == BOMB) {
                    count++;
                }
            }
        }
        return Integer.toString(count).charAt(0);
    }

    private void cellInteractionCheck(int row, int col) {
        if (gameState != GameState.ONGOING) {
            throw new IllegalStateException();
        }
        if (row < 0 || col < 0 || row >= board.length
                || col >= board[0].length
                || cellStates[row][col] == CellState.REVEALED) {
            throw new IllegalArgumentException();
        }
    }

    private void winConditionCheck() {
        unrevealedCellCount--;
        if (unrevealedCellCount == 0) {
            gameState = GameState.WIN;
        }
    }

    /**
     * Factory method to create instance of {@link MineSweeper} class.
     *
     * @param row        number of rows of this board
     * @param col        number of columns of this board
     * @param minesCount number of bomb in this board
     * @return instance of {@link MineSweeper} class
     * @throws IllegalArgumentException if either <code>row</code> or
     *                                  <code>col</code> less than 9 or
     *                                  <code>minesCount</code> less than 1
     *                                  or <code>minesCount</code>
     *                                  is greater than <code>.9 * row *
     *                                  col</code>
     */
    public static MineSweeper init(int row, int col, int minesCount) {
        int area = row * col;
        if (row < 9 || col < 9 || minesCount < 1 || minesCount > area * .9) {
            throw new IllegalArgumentException();
        }

        MineSweeper board = new MineSweeper(row, col);
        board.setMines(minesCount);
        return board;
    }

    /**
     * Reveal specified cell.
     *
     * @param row row of the cell to be revealed.
     * @param col column of the cell to be revealed.
     * @throws IllegalArgumentException if the specified cell has been
     *                                  revealed or flagged or is out of bound
     * @throws IllegalStateException    if the game has ended
     */
    public void revealCell(int row, int col) {
        cellInteractionCheck(row, col);
        if (cellStates[row][col] == CellState.FLAGGED) {
            throw new IllegalArgumentException();
        }
        cellStates[row][col] = CellState.REVEALED;
        if (board[row][col] == BOMB) {
            gameState = GameState.LOSE;
        } else {
            winConditionCheck();
        }
    }

    /**
     * Flag or unflag specified cell.
     *
     * @param row row of the cell to be flagged/unflagged
     * @param col column of the cell to be flagged/unflagged
     * @throws IllegalArgumentException if the specified cell has been
     *                                  revealed or is out of bound
     * @throws IllegalStateException    if the game has ended
     */
    public void flagCell(int row, int col) {
        cellInteractionCheck(row, col);
        if (cellStates[row][col] == CellState.FLAGGED) {
            cellStates[row][col] = CellState.UNREVEALED;
        } else {
            cellStates[row][col] = CellState.FLAGGED;
        }
        winConditionCheck();
    }


    /**
     * Check whether the game has ended.
     *
     * @return <code>true</code> if the game has ended <code>false</code>
     * otherwise
     */
    public boolean hasEnded() {
        return gameState != GameState.ONGOING;
    }

    /**
     * Check whether the board has been won.
     *
     * @return <code>true</code> if the board has been won
     * <code>false</code> otherwise
     */
    public boolean hasWon() {
        return gameState == GameState.WIN;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(' ').append(' ');
        for (int c = 0; c < board[0].length; c++) {
            res.append(c).append(' ');
        }
        res.append('\n');
        for (int r = 0; r < board.length; r++) {
            res.append(r).append(' ');
            for (int c = 0; c < board[0].length; c++) {
                if (gameState == GameState.LOSE) {
                    res.append(board[r][c]);
                } else {
                    switch (cellStates[r][c]) {
                        case FLAGGED:
                            res.append('F');
                            break;
                        case REVEALED:
                            res.append(board[r][c]);
                            break;
                        case UNREVEALED:
                            res.append(' ');
                            break;
                    }
                }
                res.append(' ');
            }
            res.append('\n');
        }
        return res.toString();
    }


}
