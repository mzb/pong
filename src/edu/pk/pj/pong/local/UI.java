package edu.pk.pj.pong.local;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class UI {
  
  public final StatusBar status = new StatusBar();
  public final MenuBar menu = new MenuBar();
  public final GamePanel gamePanel = new GamePanel(400, 300, Color.BLACK);
  public final Window window = new Window("Pong");
  
  private Client client;
  
  public UI(Client client) {
    this.client = client;
  }
  
  public String prompt(String title, String msg, String defaultValue) {
    return (String) JOptionPane.showInputDialog(UI.this.window,
        msg, title, JOptionPane.PLAIN_MESSAGE, null, null, defaultValue);
  }
  
  public void warning(String msg, Object... params) {
    message(JOptionPane.WARNING_MESSAGE, "", msg, params);
  }
  
  public void error(String title, String msg, Object... params) {
    message(JOptionPane.ERROR_MESSAGE, title, msg, params);
  }
  
  public void info(String msg, Object... params) {
    message(JOptionPane.INFORMATION_MESSAGE, "", msg, params);
  }
  
  public void message(int type, String title, String msg, Object... params) {
    JOptionPane.showMessageDialog(UI.this.window, String.format(msg, params), title, type);
  }

  
  public class GamePanel extends JPanel implements KeyListener {
    public Ball ball = new Ball(null);
    public List<Paddle> paddles = new ArrayList<Paddle>();

    public GamePanel(int width, int height, Color backgroundColor) {
      setSize(width, height);
      setPreferredSize(getSize());
      setBackground(backgroundColor);
      setFocusable(true);
      addKeyListener(this);
      
      setDoubleBuffered(true);      
      
      paddles.add(new Paddle(null));
      paddles.add(new Paddle(null));
    }
    
    @Override public void paint(Graphics g) {
      super.paint(g);
      
      if (ball.getState() != null) ball.render(g);
      for (Paddle paddle : paddles) {
        if (paddle.getState() != null) paddle.render(g);
      }
      
      Toolkit.getDefaultToolkit().sync();
      g.dispose();
    }
    
    @Override public void keyPressed(KeyEvent e) {
      switch (e.getKeyCode()) {
        case KeyEvent.VK_UP:
         UI.this.client.playerMovingUp();
          break;
        case KeyEvent.VK_DOWN:
          UI.this.client.playerMovingDown();
          break;
      }
    }

    @Override public void keyReleased(KeyEvent e) {
      switch (e.getKeyCode()) {
      case KeyEvent.VK_UP:
      case KeyEvent.VK_DOWN:
        UI.this.client.playerStopped();
        break;
      }
    }

    @Override public void keyTyped(KeyEvent e) {}
  }
  
  
  public class MenuBar extends JMenuBar implements ActionListener {
    final JMenuItem NEW_GAME = new JMenuItem("Nowa");
    final JMenuItem QUIT_GAME = new JMenuItem("Zakończ");
    public MenuBar() {
      JMenu menu = new JMenu("Gra");
      menu.add(NEW_GAME);
      menu.add(QUIT_GAME);
      add(menu);
      add(Box.createHorizontalGlue());
      add(UI.this.status);
      for (Component item : menu.getMenuComponents()) {
        ((JMenuItem)item).addActionListener(this);
      }
    }
    @Override public void actionPerformed(ActionEvent e) {
      if (NEW_GAME == e.getSource()) {
        newGameSelected();
      }
      if (QUIT_GAME == e.getSource()) {
        UI.this.client.quitGame();
      }
    }
    
    public void newGameSelected() {
      String input = UI.this.prompt(
          "Nowa gra", "<nick>@<server>:<port>",
          "player@localhost:3000");
      if (input != null) {
        String[] inputParts = input.split("@");
        if (inputParts.length > 1) {
          String playerName = inputParts[0];
          String serverAddress = inputParts[1];
          UI.this.client.joinGame(playerName, serverAddress);
        } else {
          UI.this.warning("Adres serwera i nick powinny być w formacie \n" +
              "<nick>@<server>:<port>");
        }
      }
    }
  }
  
  
  public class StatusBar extends JMenuItem {
    public StatusBar(String text) {
      super(text);
      setEnabled(false);
    }
    public void setText(String text, Object... args) {
      setText(String.format(text, args));
    }
    public StatusBar() { this(""); }
  }
  
  
  public class Window extends JFrame {
    public Window(final String title) {
      SwingUtilities.invokeLater(new Runnable() {
        @Override public void run() {
          setTitle(title);
          setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          setJMenuBar(UI.this.menu);
          setContentPane(UI.this.gamePanel);
          setResizable(false);
          pack();
          setVisible(true);
          addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
              UI.this.client.quitGame();
            }
          });
        }
      });
    }
  }
}
