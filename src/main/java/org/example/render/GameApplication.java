package org.example.render;

import org.example.Events.GameOverEvent;
import org.example.Events.UIUpdateEvent;
import org.example.game.Grid;
import org.example.game.TetrisGame;
import org.example.input.EventManager;
import org.example.input.KeyHandler;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GameApplication extends JFrame {

    private KeyHandler keyHandler;

    private final int CHARACTER_HEIGHT = 100;
    private final int CHARACTER_WIDTH = 50;

    private final StyledDocument doc;

    private final float baseFontSize = 3f;

    //    private final JFrame frame;
    private final JTextPane textPane;
    private boolean update = true;

    private final SimpleAttributeSet[] colorStyles;

    public GameApplication(TetrisGame game) {
        EventManager.subscribe(GameOverEvent.class, this::gameOverScreen);
        EventManager.subscribe(UIUpdateEvent.class, this::updateFrame);
        keyHandler = new KeyHandler(game);
        setTitle("Window");
        setSize(600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;

        setLocation(x, y);

        textPane = new JTextPane();
        doc = textPane.getStyledDocument();
        textPane.setBackground(Color.BLACK);
        textPane.setFont(new Font("Monospaced", Font.PLAIN, 30));
        textPane.setEditable(false);
        textPane.addKeyListener(keyHandler);

        add(new JScrollPane(textPane));
        setVisible(true);

        textPane.requestFocusInWindow();

        colorStyles = new SimpleAttributeSet[7];
        Color[] pieceColor = {Color.CYAN, Color.BLUE, Color.ORANGE, Color.yellow, Color.GREEN, Color.MAGENTA, Color.RED};
        for (int i = 0; i < 7; i++) {
            colorStyles[i] = new SimpleAttributeSet();
            StyleConstants.setForeground(colorStyles[i], pieceColor[i]);
        }


        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateTextSize();
            }
        });


    }

    private void updateTextSize() {

        int windowHeight = getHeight();
        int windowWidth = getWidth();

        float newFontWidth = baseFontSize * windowWidth / CHARACTER_WIDTH;

        float newFontHeight = baseFontSize * windowHeight / CHARACTER_HEIGHT;

        float newFontSize = Math.min(newFontHeight, newFontWidth);

        Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setFontSize(defaultStyle, (int) newFontSize);

        doc.setParagraphAttributes(0, doc.getLength(), defaultStyle, false);

    }

    public void gameOverScreen(GameOverEvent event) {
        textPane.setText("GAME OVER");
        update = false;
        textPane.removeKeyListener(keyHandler);
        textPane.removeKeyListener(keyHandler);
    }

    private void updateFrame(UIUpdateEvent event) {
        if (update) {
            SwingUtilities.invokeLater(() -> renderGrid(event.getWindow()));
        }
    }

    private void renderGrid(Window window) {
        Cell[][] grid = window.getVisual();
        try {
            doc.remove(0, doc.getLength());
            for (int y = 0; y < grid.length; y++) {
                for (int x = 0; x < grid[0].length; x++) {
                    Cell cell = grid[y][x];
                    SimpleAttributeSet style = getColorForPiece(cell);
                    if (cell == null) {
                        doc.insertString(doc.getLength(), " ", style);
                    } else {
                        doc.insertString(doc.getLength(), String.valueOf(cell.getVal()), style);
                    }
                }
                doc.insertString(doc.getLength(), "\n", null);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private SimpleAttributeSet getColorForPiece(Cell cell) {
        SimpleAttributeSet style = new SimpleAttributeSet();
        if (cell != null) {
            StyleConstants.setForeground(style, cell.getColor());
        }
        return style;
    }

}
