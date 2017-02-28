import java.util.Scanner;

public class Main {
    final private static int ROW = 9;
    final private static int COL = 9;
    final private static Scanner consoleRead =  new Scanner(System.in);

    public static void main(String[] args) {
        MineSweeper board = MineSweeper.init(ROW, COL, (int) (.3 * ROW * COL));
        printHelp();
        while (!board.hasEnded()) {
            System.out.println(board);
            getInput(board);
        }
        System.out.println(board);
        if (board.hasWon()) {
            System.out.println("YOU WIN!");
        } else {
            System.out.println("YOU LOST...");
        }
    }

    private static void getInput(MineSweeper board) {
        while (true) {
            try {
                System.out.print("Enter your next action: ");
                String[] input = consoleRead.nextLine().split("\\s+");
                if (input.length == 3) {
                    int row = Integer.parseInt(input[1]);
                    int col = Integer.parseInt(input[2]);
                    if (row >= 0 && row < ROW && col >= 0 && col < COL) {
                        String cmd = input[0].toLowerCase();
                        if (cmd.equals("f")) {
                            board.flagCell(row, col);
                            return;
                        } else if (cmd.equals("r")) {
                            board.revealCell(row, col);
                            return;
                        }
                    }
                }
                throw new IllegalArgumentException();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                printHelp();
            }
        }
    }

    private static void printHelp() {
        System.out.println("command:");
        System.out.println("\t'f'\tflag/unflag specified cell");
        System.out.println("\t'r'\treveal specified cell");
        System.out.println("row: [" + 0 + ", " + ROW + ")");
        System.out.println("col: [" + 0 + ", " + COL + ")");
        System.out.println("format: ${command} ${row} ${col}");
        System.out.println();
    }

}
