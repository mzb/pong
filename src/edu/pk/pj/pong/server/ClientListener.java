package edu.pk.pj.pong.server;

import java.util.logging.*;

import edu.pk.pj.pong.Connection;
import edu.pk.pj.pong.Connection.Error;
import edu.pk.pj.pong.cmds.*;
import edu.pk.pj.pong.events.PlayerNameTaken;
import edu.pk.pj.pong.events.ServerError;

public class ClientListener extends CommandHandler implements Runnable {
  
  private static final Logger log = Logger.getLogger("ClientHandler");
  
  private Connection connection;
  private boolean running;
  private Game game;
  
  public ClientListener(Connection connection, Game game) {
    log.info("New connection from " + connection);
    this.connection = connection;
    this.game = game;
  }
  
  @Override public void handle(JoinGame cmd) throws Connection.Error {
    game.addPlayer(new Player(connection, cmd.playerName));
  }
  
  @Override public void handle(LeaveGame cmd) {
    game.removePlayer(cmd.playerName);
  }
  
  @Override public void handle(MovePlayer cmd) {
    game.movePlayer(cmd.playerName, cmd.direction);
  }
  
  @Override public void handle(StopPlayer cmd) {
    game.stopPlayer(cmd.playerName);
  }

  @Override public void run() {
    while (running) {
      Command cmd = null;
      try {
        cmd = (Command) connection.get();
        cmd.handle(this);
      } catch (Exception e) {
        log.log(Level.WARNING, "While handling command " + cmd, e);
        if (e instanceof Connection.Error) {
          stop();
        } else {
          // Daj znac klientowi, ze nie udalo sie wykonac polecenia
          try {
            connection.put(new ServerError(e.getMessage()));
          } catch (Connection.Error ee) {
            log.log(Level.WARNING, "Cannot notify client of ServerError", ee);
          }
        }
      }
    }
  }
  
  public void start() {
    running = true;
    new Thread(this).start();
  }

  public void stop() {
    running = false;
    try {
      connection.close();
      log.info("connection closed.");
    } catch (Connection.Error e) {
      log.log(Level.SEVERE, e.getMessage(), e);
    }
  }

}
