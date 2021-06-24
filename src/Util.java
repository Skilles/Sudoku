import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;

public class Util {
    static int N = 9;
    static final int[][] solvedBoard = new int[N][N];
    static final int[][] ogBoard = new int[N][N];
    static int[][] emptyBoard = new int[N][N];

    static boolean empty = false;

    static void drawTitle() {
        Game.title.removeAll();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel label = new JLabel("Sudoku");
        label.setFont(new Font("Serif", Font.BOLD, 36));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setPreferredSize(new Dimension(200, 30));
        panel.add(label, BorderLayout.CENTER);
        Game.title.add(panel);
        label = new JLabel("by Bilal Madi");
        label.setFont(new Font("Serif", Font.PLAIN, 16));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.TOP);
        label.setPreferredSize(new Dimension(20, 20));
        panel.add(label, BorderLayout.SOUTH);
        Game.title.add(panel);
    }

    static void drawInstructions() {
        Game.instructions.removeAll();
        for(int i = 0; i < 3; i++) {
            JPanel panel = new JPanel();
            JLabel label = switch (i) {
                case 0 -> new JLabel("Space to solve");
                case 1 -> new JLabel("Esc to reset");
                case 2 -> new JLabel("Enter to confirm");
                default -> throw new IllegalStateException("Unexpected value: " + i);
            };
            panel.add(label);
            Game.instructions.add(panel);
        }
    }

    static int getNumber(int[][] board, int row, int column) {
        return board[row][column];
    }

    static void updateLabel(Cell cell, int number) {
        if(cell.getComponents().length != 0) {
            if (number != 0)
                cell.label().setText(Integer.toString(number));
            else
                cell.label().setText("");
        }
        else if(number != 0) {
            JLabel label = new JLabel(Integer.toString(number));
            cell.add(label);
        }
        cell.set();
        if(empty) cell.confirm();
    }

    private static void appendNumber(int row, int column) {
        JLabel label = new JLabel(Integer.toString(getNumber(Game.board, row, column)));
        label.setFont(new Font("Serif", Font.BOLD, 28));
        Game.cells[row][column].setLayout(new FlowLayout());
        Game.cells[row][column].add(label);
    }

    static void unselectOtherPanels(Cell source) {
        for(Cell[] arr : Game.cells) {
            for(Cell cell : arr) {
                if(cell.equals(source)) continue;
                if(cell.getBorder() instanceof CompoundBorder border) cell.setBorder(border.getOutsideBorder());
            }
        }
    }

    static void drawBorder(int row, int col) {
        // first row
        if(row == 0) {
            if(col == 0) {
                Game.cells[row][col].setBorder(BorderFactory.createMatteBorder(5, 5, 1, 1, new Color(0)));
            } else if(col == N - 1) {
                Game.cells[row][col].setBorder(BorderFactory.createMatteBorder(5, 1, 1, 5, new Color(0)));
            } else {
                if ((col + 1) % 3 == 0)
                    Game.cells[row][col].setBorder(BorderFactory.createMatteBorder(5, 1, 1, 2, new Color(0)));
                else
                    Game.cells[row][col].setBorder(BorderFactory.createMatteBorder(5, 1, 1, 1, new Color(0)));
            }
            // first column
        } else if(col == 0) {
            if ((row + 1) % 3 == 0)
                if(row == N - 1)
                    Game.cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 5, 5, 1, new Color(0)));
                else
                    Game.cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 5, 2, 1, new Color(0)));
            else
                Game.cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 5, 1, 1, new Color(0)));
            // last row
        } else if(row == N - 1) {
            if ((col + 1) % 3 == 0) {
                if(col == N - 1)
                    Game.cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 5, 5, new Color(0)));
                else
                    Game.cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 5, 2, new Color(0)));
            }
            else
                Game.cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 5, 1, new Color(0)));
            // last column
        } else if(col == N - 1) {
            if ((row + 1) % 3 == 0)
                Game.cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 2, 5, new Color(0)));
            else
                Game.cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 5, new Color(0)));
            // box border column
        } else if ((col + 1) % 3 == 0) {
            Game.cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 2, new Color(0)));
            if ((row + 1) % 3 == 0) {
                Game.cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 2, 2, new Color(0)));
            }
            // box border row
        } else if ((row + 1) % 3 == 0) {
            Game.cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 2, 1, new Color(0)));
        } else {
            Game.cells[row][col].setBorder(BorderFactory.createLineBorder(new Color(0)));
        }
        if(getNumber(Game.board, row, col) != 0) {
            appendNumber(row, col);
        }
    }

    static boolean checkSolved() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if(Game.board[i][j] == 0) return false;
                if(solvedBoard[i][j] != Game.board[i][j]) return false;
            }
        }
        solve();
        return true;
    }

    static void solve() {
        Game.solved = true;
        System.out.println("Solved!");
    }
}
