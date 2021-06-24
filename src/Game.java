import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Game extends JFrame implements MouseListener {
    static int N = Sudoku.board.length;
    private static final Cell[][] cells = new Cell[N][N];
    private static final JPanel panel;
    private static final JPanel instructions;
    private static Cell selected;
    static boolean solved = false;
    static final int[][] ogBoard = new int[N][N];
    static final int[][] solvedBoard = new int[N][N];

    public Game() {
        super("Sudoku");
        setSize(500, 500);
        getContentPane().add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(!solved) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        System.out.println("Solving...");
                        if (Sudoku.solveBoard(Sudoku.board, 0, 0)) {
                            for (Cell[] arr : cells) {
                                for (Cell cell : arr) {
                                    if (cell.custom && !cell.confirmed) {
                                        updateLabel(cell, getNumber(Sudoku.board, cell.row, cell.column));
                                        cell.confirm();
                                    }
                                }
                            }
                            solve();
                        } else {
                            System.out.println("No solutions!");
                        }
                    } else if (selected.custom) {
                        if (((e.getKeyCode() >= 49 && e.getKeyCode() <= 57) || (e.getKeyCode() >= 97 && e.getKeyCode() <= 105)) && !selected.confirmed) {
                            // 0-9 only
                            int key = Integer.parseInt(KeyEvent.getKeyText(e.getKeyCode()));
                            if (Sudoku.canPlace(Sudoku.board, selected.row, selected.column, key))
                                updateLabel(selected, key);
                        } else if (e.getKeyCode() == 46 || e.getKeyCode() == 8) { // delete
                            updateLabel(selected, 0);
                            selected.confirmed = false;
                        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) { // enter
                            selected.confirm();
                            checkSolved();
                        }
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.out.println("Resetting...");
                    reset();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }
    private boolean checkSolved() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if(Sudoku.board[i][j] == 0) return false;
                if(solvedBoard[i][j] != Sudoku.board[i][j]) return false;
            }
        }
        solve();
        return true;
    }
    private void solve() {
        solved = true;
        panel.setFocusable(false);
        System.out.println("Solved!");
    }
    public void paint(Graphics g) {
        super.paint(g);
        drawGrid();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Game().setVisible(true));
    }
    public void drawGrid() {
        panel.removeAll();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if(cells[i][j] == null) {
                    cells[i][j] = new Cell(i, j);
                    drawBorder(i, j);
                    cells[i][j].addMouseListener(this);
                }
                panel.add(cells[i][j]);
            }
        }
    }
    public static int getNumber(int[][] board, int row, int column) {
        return board[row][column];
    }
    public static void updateLabel(Cell cell, int number) {
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
        if(Sudoku.empty) cell.confirm();
    }
    private static void appendNumber(int row, int column) {
        JLabel label = new JLabel(Integer.toString(getNumber(Sudoku.board, row, column)));
        label.setFont(new Font("Serif", Font.BOLD, 28));
        cells[row][column].setLayout(new FlowLayout());
        cells[row][column].add(label);
    }
    private static void unselectOtherPanels(Cell source) {
        for(Cell[] arr : cells) {
            for(Cell cell : arr) {
                if(cell.equals(source)) continue;
                if(cell.getBorder() instanceof CompoundBorder border) cell.setBorder(border.getOutsideBorder());
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getSource() instanceof Cell cell) {
            unselectOtherPanels(cell);
            if(!(cell.getBorder() instanceof CompoundBorder)) {
                cell.setBorder(BorderFactory.createCompoundBorder(cell.getBorder(), BorderFactory.createLineBorder(new Color(0xFF0000))));
                selected = cell;
            }
        }
    }
    public static void drawBorder(int row, int col) {
        // first row
        if(row == 0) {
            if(col == 0) {
                cells[row][col].setBorder(BorderFactory.createMatteBorder(5, 5, 1, 1, new Color(0)));
            } else if(col == N - 1) {
                cells[row][col].setBorder(BorderFactory.createMatteBorder(5, 1, 1, 5, new Color(0)));
            } else {
                if ((col + 1) % 3 == 0)
                    cells[row][col].setBorder(BorderFactory.createMatteBorder(5, 1, 1, 2, new Color(0)));
                else
                    cells[row][col].setBorder(BorderFactory.createMatteBorder(5, 1, 1, 1, new Color(0)));
            }
            // first column
        } else if(col == 0) {
            if ((row + 1) % 3 == 0)
                if(row == N - 1)
                    cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 5, 5, 1, new Color(0)));
                else
                    cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 5, 2, 1, new Color(0)));
            else
                cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 5, 1, 1, new Color(0)));
            // last row
        } else if(row == N - 1) {
            if ((col + 1) % 3 == 0) {
                if(col == N - 1)
                    cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 5, 5, new Color(0)));
                else
                    cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 5, 2, new Color(0)));
            }
            else
                cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 5, 1, new Color(0)));
            // last column
        } else if(col == N - 1) {
            if ((row + 1) % 3 == 0)
                cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 2, 5, new Color(0)));
            else
                cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 5, new Color(0)));
            // box border column
        } else if ((col + 1) % 3 == 0) {
            cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 2, new Color(0)));
            if ((row + 1) % 3 == 0) {
                cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 2, 2, new Color(0)));
            }
            // box border row
        } else if ((row + 1) % 3 == 0) {
            cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 2, 1, new Color(0)));
        } else {
            cells[row][col].setBorder(BorderFactory.createLineBorder(new Color(0)));
        }
        if(getNumber(Sudoku.board, row, col) != 0) {
            appendNumber(row, col);
        }
    }
    void reset() {
        Sudoku.board = Sudoku.empty ? Sudoku.emptyBoard : Sudoku.testBoard;
        for(int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Sudoku.board[i][j] = ogBoard[i][j];
                if(cells[i][j].getComponents().length != 0) {
                    updateLabel(cells[i][j], Sudoku.board[i][j]);
                    cells[i][j].label().setFont(new Font("Serif", Font.BOLD, 28));
                }
                cells[i][j].confirmed = false;
            }
        }
    }
    static {
        panel = new JPanel();
        panel.setLayout(new GridLayout(N, N));
        instructions = new JPanel();
        for (int i = 0; i < N; i++) {
            System.arraycopy(Sudoku.board[i], 0, ogBoard[i], 0, N);
            System.arraycopy(Sudoku.board[i], 0, solvedBoard[i], 0, N);
        }
        if (!Sudoku.solveBoard(solvedBoard, 0, 0)) System.out.println("Unsolvable board!");
    }
    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
