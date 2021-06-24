import org.jetbrains.annotations.TestOnly;

public class Solver {
    public static final int[][] testBoard =
            {{ 8, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 3, 6, 0, 0, 0, 0, 0 },
            { 0, 7, 0, 0, 9, 0, 2, 0, 0 },

            { 0, 5, 0, 0, 0, 7, 0, 0, 0 },
            { 0, 0, 0, 0, 4, 5, 7, 0, 0 },
            { 0, 0, 0, 1, 0, 0, 0, 3, 0 },

            { 0, 0, 1, 0, 0, 0, 0, 6, 8 },
            { 0, 0, 8, 5, 0, 0, 0, 1, 0 },
            { 0, 9, 0, 0, 0, 0, 4, 0, 0 } };

    @TestOnly
    public static void main(String[] args) {
        printBoard(Game.board);
        System.out.println("\nSolution:");
        if(solveBoard(Game.board, 0, 0)) {
            printBoard(Game.board);
        } else {
            System.out.println("No solutions!");
        }
    }
    public static void printBoard(int[][] board) {
        for(int i = 0; i < board.length; i++) {
            if(i % 3 == 0) System.out.println("-------------");
            for(int j = 0; j < board[i].length; j++) {
                if(j % 3 == 0) System.out.print("|");
                System.out.print(board[i][j]);
            }
            System.out.print("|");
            System.out.println();
        }
    }
    public static boolean solveBoard(int[][] board, int column, int row) {
        // checks if on last row to disable backtracking
        // checks if on last column to finish
        if(row == board.length - 1 && column == board.length) return true;
        if(column == board.length) {
            row++; // next row
            column = 0; // set back to first column
        }
        // check if number is not zero and does not need to be solved
        if(board[row][column] != 0) return solveBoard(board, column + 1, row);
        // backtracking algo
        for(int num = 1; num <= 9; num++) {
            // checks if number can be placed
            if(canPlace(board, row, column, num)) {
                // sets the number
                board[row][column] = num;
                // recursion for next space
                if(solveBoard(board, column + 1, row)) return true;
            }
            // set to zero since we were wrong
            board[row][column] = 0;
        }
        return false;
    }
    public static boolean canPlace(int[][] board, int row, int column, int value) {
        // Check if number is in a row
        for(int i = 0; i < board.length; i++) {
            if(board[row][i] == value) return false;
        }
        // Check if number is in a column
        for (int[] rRow : board) {
            if (rRow[column] == value) return false;
        }
        int searchRow = row - row % 3; // if row is 5, this would be 3
        int searchColumn = column - column % 3;
        for(int i = 0; i < 3; i++) {
            // goes through each box
            for(int j = 0;  j < 3; j++) {
                // goes through each place in each box
                // searchRow + i is equal to the row to check
                // searchColumn + j is equal to the column to check
                if(board[searchRow + i][searchColumn + j] == value) return false;
            }
        }
        return true;
    }
}
