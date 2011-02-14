package edu.pk.pj.pong.server;

import java.io.Serializable;

import edu.pk.pj.pong.Connection;
import edu.pk.pj.pong.Entity;

public class Player  {

  public static final int SPEED = 10;
  
  private Paddle paddle;
  private long score;
  private Connection connection;
  private String name;
  
  //private State state = new State();
  
  public Player(Connection connection, String name) {
    this.connection = connection;
    this.name = name;
  }
  
  public void move() {
    paddle.move();
  }
  
  public void send(Serializable msg) throws Connection.Error {
    connection.putAndReset(msg);
  }

  public Paddle getPaddle() {
    return paddle;
  }
  
  public State getState() {
    State state = new State();
    state.name = name;
    state.score = score;
    state.paddle = paddle.getState();
    return state;
  }
  
  public Connection getConnection() {
    return connection;
  }

  public String getName() {
    return name;
  }

  public void setPaddle(Paddle paddle) {
   this.paddle = paddle;
  }

  public void increaseScore() {
    score++;
  }
  
  
  public static class State implements Serializable {
    public long score;
    public Entity.State paddle;
    public String name;
  }
}
