package edu.pk.pj.pong.server;

import edu.pk.pj.pong.Entity;

public class Ball extends Entity {
  
  public Ball(State state) {
    super(state);
  }

  public void move() {
    state.x += (int) Math.round(state.directionX * state.velocity);
    state.y += (int) Math.round(state.directionY * state.velocity);
  }
}
