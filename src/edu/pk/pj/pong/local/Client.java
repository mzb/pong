package edu.pk.pj.pong.local;

import java.util.logging.*;

import edu.pk.pj.pong.Connection;
import edu.pk.pj.pong.cmds.*;
import edu.pk.pj.pong.events.*;

public class Client extends EventHandler {
  
  private static final Logger log = Logger.getLogger("Client");
  
  private Connection serverConnection;
  private ServerListener serverListener;
  private CommandFactory commandFactory = new CommandFactory();
  private UI ui;
  
  private String playerName;
  private GameState lastGameState;

  
  public Client() {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        quitGame();
      }
    });
  }
  
  @Override public void handle(GameState e) {
    ui.gamePanel.ball.setState(e.ball);
    for (int i = 0; i < e.players.size(); i++) {
      Paddle paddle = ui.gamePanel.paddles.get(i);
      paddle.setState(e.players.get(i).paddle);
    }
    ui.status.setText("%s (%d) VS %s (%d)",
        e.players.get(0).name, e.players.get(0).score,
        e.players.get(1).name, e.players.get(1).score);
    ui.gamePanel.repaint();

    lastGameState = e;
  }
  
  @Override public void handle(PlayerLeft e) {
    ui.status.setText("%s opuścił grę", e.playerName);
    if (lastGameState != null) {
      ui.info("WYNIK:\n%s %d - %s %d", 
          lastGameState.players.get(0).name, lastGameState.players.get(0).score, 
          lastGameState.players.get(1).name, lastGameState.players.get(1).score);
      lastGameState = null;
    }
    if (e.playerName.equals(playerName)) {
      disconnect();
      ui.window.setTitle("Pong");
    }
  }
  
  @Override public void handle(PlayerJoined e) {
    ui.status.setText("%s dolaczyl do gry", e.playerName);
    if (e.playerName.equals(playerName)) {
      ui.window.setTitle(String.format("%s (%s@%s:%d)", 
          ui.window.getTitle(), playerName, 
          serverConnection.getHostName(), serverConnection.getPort()));
    }
  }
  
  @Override public void handle(PlayerNameTaken e) {
    ui.warning("Nick %s jest już zajęty\n - wybierz inny.", e.name);
    disconnect();
    ui.menu.newGameSelected();
  }
  
  @Override public void handle(ServerError e) {
    ui.error(e.getClass().getSimpleName(), e.message);
  }
  
  public void joinGame(String playerName, String serverAddress) {
    if (isConnected()) {
      quitGame();
    }
    
    this.playerName = playerName; 
    String hostName = "";
    int port = 0;
    String[] serverAddressParts = serverAddress.split(":");
    if (serverAddressParts.length >= 2) {
      hostName = serverAddressParts[0];
      try {
        port = Integer.parseInt(serverAddressParts[1]);
      } catch (NumberFormatException e) {
        // Ignorowany - i tak connect wylapie blad
      }
    }
    connect(hostName, port);
    send(new JoinGame(playerName));
  }
  
  public void quitGame() {
    log.info("Quitting game");
    send(new LeaveGame(playerName), false);
  }
  
  public void playerMovingUp() {
    try {
      send(commandFactory.MovePlayer(playerName, MovePlayer.UP));
    } catch (CommandFactory.Error e) {
      log.log(Level.SEVERE, "MovePlayer", e);
    }
  }

  public void playerMovingDown() {
    try {
      send(commandFactory.MovePlayer(playerName, MovePlayer.DOWN));
    } catch (CommandFactory.Error e) {
      log.log(Level.SEVERE, "MovePlayer", e);
    }
  }

  public void playerStopped() {
    try {
      send(commandFactory.StopPlayer(playerName));
    } catch (CommandFactory.Error e) {
      log.log(Level.SEVERE, "StopPlayer", e);
    }
  }
  
  public void createUI() {
    ui = new UI(this);
  }
  
  protected void send(Command command, boolean showError) {
    if (isConnected()) {
      try {
        serverConnection.putAndReset(command);
      } catch (Connection.Error e) {
        if (showError) {
          log.log(Level.SEVERE, "", e);
          ui.error("", "Nie można nawiązać połączenia z serwerem");
        }
      }
    }
  }
  
  protected void send(Command command) {
    send(command, true);
  }

  protected void connect(String host, int port) {
    log.info("Connecting to " + host + ":" + port);
    try {
      serverConnection = new Connection(host, port);
      serverListener = new ServerListener(serverConnection, this);
      serverListener.start();
    } catch (Connection.Error e) {
      log.log(Level.SEVERE, "", e);
      ui.error("", "Nie można nawiązać połączenia z serwerem %s:%d", host, port);
    }
  }
  
  protected void disconnect() {
    if (isConnected()) {
      log.info("Disconnecting...");
      if (serverListener != null) serverListener.stop();
      try {
        serverConnection.close();
      } catch (Connection.Error e) {
        log.log(Level.SEVERE, "", e);
      }
    }
  }
  
  protected boolean isConnected() {
    return serverConnection != null && serverConnection.isOpen();
  }
}
