import javax.swing.*;
import java.awt.*;

public class StreamConsole extends JFrame {
  
  private JPanel panel;
  private JTextArea textArea;

  public StreamConsole() {
    panel = new JPanel();
    textArea = new JTextArea();
    initConsole();
  }

  private void initConsole() {
    Container c = this.getContentPane();
    c.add(panel);
    c.add(textArea);
    
    this.setTitle("Stream Console");
    this.setSize(500, 500);
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    this.setVisible(true);
    this.validate();

    this.log("Console initialized...");
  }

  public void log(String message) {
    textArea.append(message);
    textArea.append("\n");
  }

  public void log() {
    textArea.append("\n");
  }
}
