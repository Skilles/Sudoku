import javax.swing.*;
import java.awt.*;

public class Cell extends JPanel {

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
        custom = Util.ogBoard[row][column] == 0;
    }

    final int row;
    final int column;

    boolean confirmed = false;
    JLabel label() {
        return (JLabel) getComponent(0);
    }
    void set() {
        label().setFont(new Font("Serif", Font.ITALIC, 16));
        label().setVerticalAlignment(SwingConstants.TOP);

    }
    void confirm() {
        if(getComponents().length != 0) {
            confirmed = true;
            Game.board[row][column] = Integer.parseInt(label().getText());
            label().setFont(new Font("Serif", Font.PLAIN, 28));
            label().setVerticalAlignment(SwingConstants.CENTER);
        }
    }
    final boolean custom;
}
