import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Game extends JFrame implements MouseListener {
    static int[][] board;
    static final Cell[][] cells = new Cell[Util.N][Util.N];

    static final JPanel panel;
    static final JPanel screen;
    static final JPanel title;
    static final JPanel instructions;
    private static Cell selected;
    static boolean solved = false;

    public Game() {
        super("Sudoku");
        setSize(500, 600); // 500 x (500 grid + 50 title + 50 instructions)
        setResizable(false);
        screen.add(instructions, BorderLayout.SOUTH);
        screen.add(panel, BorderLayout.CENTER);
        screen.add(title, BorderLayout.NORTH);
        setContentPane(screen);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        init();
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(!solved) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        System.out.println("Solving...");
                        if (Solver.solveBoard(board, 0, 0)) {
                            for (Cell[] arr : cells) {
                                for (Cell cell : arr) {
                                    if (cell.custom && !cell.confirmed) {
                                        Util.updateLabel(cell, Util.getNumber(board, cell.row, cell.column));
                                        cell.confirm();
                                    }
                                }
                            }
                            Util.solve();
                        } else {
                            System.out.println("No solutions!");
                        }
                    } else if (selected.custom) {
                        if (((e.getKeyCode() >= 49 && e.getKeyCode() <= 57) || (e.getKeyCode() >= 97 && e.getKeyCode() <= 105)) && !selected.confirmed) {
                            // 0-9 only
                            int key = Integer.parseInt(KeyEvent.getKeyText(e.getKeyCode()));
                            if (Solver.canPlace(board, selected.row, selected.column, key))
                                Util.updateLabel(selected, key);
                        } else if (e.getKeyCode() == 46 || e.getKeyCode() == 8) { // delete
                            Util.updateLabel(selected, 0);
                            board[selected.row][selected.column] = 0;
                            selected.confirmed = false;
                        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) { // enter
                            selected.confirm();
                            Util.checkSolved();
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

    void reset() {
        board = Util.empty ? Util.emptyBoard : Solver.testBoard;
        for(int i = 0; i < Util.N; i++) {
            for (int j = 0; j < Util.N; j++) {
                board[i][j] = Util.ogBoard[i][j];
                if(cells[i][j].getComponents().length != 0) {
                    Util.updateLabel(cells[i][j], board[i][j]);
                    cells[i][j].label().setFont(new Font("Serif", Font.BOLD, 28));
                }
                cells[i][j].confirmed = false;
            }
        }
        solved = false;
    }
    public void paint(Graphics g) {
        super.paint(g);
        drawGrid();
        Util.drawInstructions();
        Util.drawTitle();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Game().setVisible(true));
    }
    public void drawGrid() {
        panel.removeAll();
        for (int i = 0; i < Util.N; i++) {
            for (int j = 0; j < Util.N; j++) {
                if(cells[i][j] == null) {
                    cells[i][j] = new Cell(i, j);
                    Util.drawBorder(i, j);
                    cells[i][j].addMouseListener(this);
                }
                panel.add(cells[i][j]);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getSource() instanceof Cell cell) {
            Util.unselectOtherPanels(cell);
            if(!(cell.getBorder() instanceof CompoundBorder)) {
                cell.setBorder(BorderFactory.createCompoundBorder(cell.getBorder(), BorderFactory.createLineBorder(new Color(0xFF0000))));
                selected = cell;
            }
        }
    }
    private static void init() {
        for (int i = 0; i < Util.N; i++) {
            Util.ogBoard[i] = board[i].clone();
            Util.solvedBoard[i] = board[i].clone();
        }
        if (!Solver.solveBoard(Util.solvedBoard, 0, 0)) System.out.println("Unsolvable board!");
    }

    static {
        board = Util.empty ? Util.emptyBoard : Solver.testBoard;
        assert Util.N == board.length;
        panel = new JPanel();
        panel.setLayout(new GridLayout(Util.N, Util.N));
        screen = new JPanel();
        screen.setLayout(new BorderLayout());
        title = new JPanel();
        title.setSize(500, 50);
        instructions = new JPanel();
        instructions.setSize(500, 50);
        FlowLayout fLayout = new FlowLayout();
        fLayout.setHgap(70);
        instructions.setLayout(fLayout);
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
