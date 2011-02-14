package edu.pk.pj.pong.server;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.pk.pj.pong.Connection;
import edu.pk.pj.pong.Connection.Error;
import edu.pk.pj.pong.Entity;
import edu.pk.pj.pong.events.EventFactory;
import edu.pk.pj.pong.server.Game.Players.PlayerNameTaken;


public class Game implements Runnable {
  
  private static final Logger log = Logger.getLogger("Game");
  
  private static final int REQUIRED_NUM_PLAYERS = 2;
  public static enum Status { RUNNING, WAITING }
  
  private final CollisionHandler collisionHandler = new CollisionHandler();
  private final EventFactory eventFactory = new EventFactory();
  
  private Players players = new Players();
  private Ball ball;
  private Board board;
  
  private Status status = Status.WAITING;
  
  synchronized public void addPlayer(Player player) throws Connection.Error {
    if (status == Status.WAITING) {
      
      try {
        players.add(player);
        broadcast(eventFactory.PlayerJoined(player));
      } catch (Players.PlayerNameTaken e) {
        try {
          player.send(eventFactory.PlayerNameTaken(player.getName()));
        } catch (EventFactory.Error ee) {
          log.log(Level.SEVERE, "PlayerNameTaken", ee);
          player.send(eventFactory.ServerError(ee.getMessage()));
        }
      } catch (EventFactory.Error e) {
        log.log(Level.SEVERE, "PlayerJoined", e);
        broadcast(eventFactory.ServerError(e.getMessage()));
      }
      
      if (players.count() == REQUIRED_NUM_PLAYERS) {
        start();
      }
    }
  }
  
  synchronized public void removePlayer(String playerName) {
    if (players.exist(playerName)) {
      try {
        broadcast(eventFactory.PlayerLeft(players.get(playerName)));
      } catch (EventFactory.Error e) {
        broadcast(eventFactory.ServerError(e.getMessage()));
      }
      players.remove(playerName);
      
      if (players.count() < REQUIRED_NUM_PLAYERS) {
        status = Status.WAITING;
      }
    }
  }
  
  synchronized public void movePlayer(String playerName, int direction) {
    Player player = players.get(playerName);
    player.getPaddle().getState().directionY = direction * Player.SPEED;
  }
  
  synchronized public void stopPlayer(String playerName) {
    Player player = players.get(playerName);
    player.getPaddle().getState().directionY = 0;
  }
  
  // TODO: Refaktoring
  public void start() {
    board = new Board(400, 300);
    
    final Entity.State ballState = new Entity.State();
    ballState.x = board.getWidth() / 2;
    ballState.y = board.getHeight() / 2;
    ballState.directionX = -1; ballState.directionY = 1;
    ballState.velocity = 10;
    ballState.color = Color.WHITE;
    ballState.width = ballState.height = 10;
    ball = new Ball(ballState);

    boolean leftPlayer = true;
    for (Player player : players.getList()) {
      final Entity.State paddleState = new Entity.State();
      paddleState.height = 40; paddleState.width = 10;
      paddleState.color = Color.WHITE;
      paddleState.y = board.getHeight() / 2 - paddleState.height;
      paddleState.x = leftPlayer ? 20 : board.getWidth() - 20 - paddleState.width;
      leftPlayer = false;
      player.setPaddle(new Paddle(paddleState));
    }
    
    status = Status.RUNNING;
    new Thread(this).start();
  }
  
  @Override public void run() {
    final long DELAY = 50;
    long beforeTime = System.currentTimeMillis();
    log.info("BEFORE game main loop");
    
    while (status == Status.RUNNING) {
      ball.move();
      for (Player player : players.getList()) {
        player.move();
      }
      try {
        collisionHandler.handle(ball, players.getList(), board);
      } catch (CollisionHandler.BallOfBoardOnLeftSide e) {
        players.getList().get(1).increaseScore();
      } catch (CollisionHandler.BallOfBoardOnRightSide e) {
        players.getList().get(0).increaseScore();
      }
      
      //log.info("Sending GameState: " + gameState);
      try {
        broadcast(eventFactory.GameState(this));
      } catch (EventFactory.Error e) {
        broadcast(eventFactory.ServerError(e.getMessage()));
      }
      
      long timeDiff = System.currentTimeMillis() - beforeTime;
      long sleep = DELAY - timeDiff;
      if (sleep < 0) sleep = 2;
      try {
        Thread.sleep(sleep);
      } catch (InterruptedException e) {
        log.log(Level.SEVERE, e.getMessage(), e);
      }
      beforeTime = System.currentTimeMillis();
    }
    
    log.info("AFTER game main loop");
  }
  
  public void broadcast(Serializable msg) {
    players.send(msg);
  }
    

  public Collection<Player> getPlayers() {
    return players.getList();
  }
  
  public Ball getBall() {
    return ball;
  }
  
  
  public static class Players {
    private  Map<String, Player> players = new HashMap<String, Player>();
    
    public void send(Serializable msg) {
      for (Player player : getList()) {
        try {
          player.send(msg);
        } catch (Connection.Error e) {
          log.log(Level.SEVERE, e.getMessage(), e);
        }
      } 
    }
    
    public Player get(String name) {
      return players.get(name);
    }
    
    public boolean exist(String name) {
      return players.containsKey(name);
    }
    
    public void add(Player player) throws PlayerNameTaken {
      if (exist(player.getName())) {
        throw new PlayerNameTaken();
      }
      players.put(player.getName(), player);
    }
    
    public boolean remove(String name) {
      return players.remove(name) != null;
    }
    
    public long count() {
      return players.size();
    }
    
    public List<Player> getList() {
      return new ArrayList<Player>(players.values());
    }
    
    public static class PlayerNameTaken extends Exception {}
  }
}
