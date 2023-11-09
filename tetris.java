import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class Tetris extends JPanel {

    private final int BoardWidth = 10;
    private final int BoardHeight = 20;
    private Timer timer;
    private boolean isFallingFinished = false;
    private int currentX = 0;
    private int currentY = 0;
    private Shape currentPiece;
    private Tetrominoe[] board;

    public Tetris() {
        initBoard();
    }

    private void initBoard() {
        setFocusable(true);
        currentPiece = new Shape();
        timer = new Timer(400, new GameCycle());
        timer.start();

        board = new Tetrominoe[BoardWidth * BoardHeight];
        clearBoard();
        addKeyListener(new TAdapter());
    }

    private void clearBoard() {
        for (int i = 0; i < BoardHeight * BoardWidth; ++i) {
            board[i] = Tetrominoe.NoShape;
        }
    }

    private void pieceDropped() {
        for (int i = 0; i < 4; ++i) {
            int x = currentX + currentPiece.x(i);
            int y = currentY - currentPiece.y(i);
            board[(y * BoardWidth) + x] = currentPiece.getShape();
        }

        removeFullLines();

        if (!isFallingFinished) {
            newPiece();
        }
    }

    private void newPiece() {
        currentPiece.setRandomShape();
        currentX = BoardWidth / 2 + 1;
        currentY = BoardHeight - 1 + currentPiece.minY();

        if (!tryMove(currentPiece, currentX, currentY)) {
            currentPiece.setShape(Tetrominoe.NoShape);
            timer.stop();
        }
    }

    private boolean tryMove(Shape newPiece, int newX, int newY) {
        for (int i = 0; i < 4; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight) {
                return false;
            }
            if (board[(y * BoardWidth) + x] != Tetrominoe.NoShape) {
                return false;
            }
        }

        currentPiece = newPiece;
        currentX = newX;
        currentY = newY;
        repaint();
        return true;
    }

    private void removeFullLines() {
        // go through the board and remove full lines.
    }

    private void dropDown() {
        int newY = currentY;
        while (newY > 0) {
            if (!tryMove(currentPiece, currentX, newY - 1)) {
                break;
            }
            --newY;
        }
        pieceDropped();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        // draw the current state of the board along with the falling piece.
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (currentPiece.getShape() == Tetrominoe.NoShape) {
                return;
            }

            int keycode = e.getKeyCode();

            switch (keycode) {
                case KeyEvent.VK_LEFT:
                    tryMove(currentPiece, currentX - 1, currentY);
                    break;
                case KeyEvent.VK_RIGHT:
                    tryMove(currentPiece, currentX + 1, currentY);
                    break;
                case KeyEvent.VK_DOWN:
                    dropDown();
                    break;
                case KeyEvent.VK_UP:
                    tryMove(currentPiece.rotateRight(), currentX, currentY);
                    break;
            }
        }
    }

    private class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    private void doGameCycle() {
        update();
        repaint();
    }

    private void update() {
        if (isFallingFinished) {
            isFallingFinished = false;
            newPiece();
        } else {
            oneLineDown();
        }
    }

    private void oneLineDown() {
        if (!tryMove(currentPiece, currentX, currentY - 1)) {
            pieceDropped();
        }
    }
}

public class Tetrominoe {
    enum Tetrominoes { NoShape, ZShape, SShape, LineShape, TShape, SquareShape, LShape, MirroredLShape }
    // contain all the different shapes that can be used in the game.
}

public class Shape {
    private Tetrominoe.Tetrominoes pieceShape;
    private int[][] coords;

    public Shape() {
        
    }

    public void setShape(Tetrominoe.Tetrominoes shape) {
        
    }

    public void setRandomShape() {
        
    }

    public int x(int index) {
        // Get x coordinate
        return coords[index][0];
    }

    public int y(int index) {
        // Get y coordinate 
        return coords[index][1];
    }

    public Tetrominoe.Tetrominoes getShape() {
        return pieceShape;
    }

    public Shape rotateRight() {
        // Rotate right
    }

    public Shape rotateLeft() {
        // Rotate left
    }
}

public class TetrisGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Tetris");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(200, 400);
            frame.setLocationRelativeTo(null);
            frame.add(new Tetris());
            frame.setVisible(true);
        });
    }
}
