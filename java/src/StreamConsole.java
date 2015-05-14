import javax.swing.*;
import java.awt.*;

public class StreamConsole extends JFrame {
  
  private final static String FONT_NAME = "Consolas";
  private final static int FONT_STYLE = Font.PLAIN;
  private final static int FONT_SIZE = 12;
  private final static Color FOREGROUND_COLOR = Color.WHITE;
  private final static Color BACKGROUND_COLOR = Color.GRAY;
  private final static int WINDOW_HEIGHT = 500;
  private final static int WINDOW_WIDTH = 500;
  private JScrollPane scrollPane;
  private JTextArea textArea;

  public StreamConsole() {
    textArea = new JTextArea();
    scrollPane = new JScrollPane(textArea);
    initConsole();
  }

  private void initConsole() {
    Container c = this.getContentPane();
    c.add(scrollPane);

    initWindow();
    initFont();

    this.validate();
    scrollPane.validate();

    this.log("Console initialized.");
    this.log();
  }

  private void initWindow() {
    this.setTitle("Stream Console");
    this.setSize(WINDOW_HEIGHT, WINDOW_WIDTH);
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
    textArea.setBackground(BACKGROUND_COLOR);
  }

  private void initFont() {
    Font font = new Font(FONT_NAME, FONT_STYLE, FONT_SIZE);
    textArea.setFont(font);
    textArea.setForeground(FOREGROUND_COLOR);
  }

  public void log(String message) {
    textArea.append(message);
    textArea.append("\n");
  }

  public void log() {
    textArea.append("\n");
  }
}
