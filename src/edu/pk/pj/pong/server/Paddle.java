package edu.pk.pj.pong.server;

import edu.pk.pj.pong.Entity;

public class Paddle extends Entity {

  public Paddle(State state) {
    super(state);
  }

  public void move() {
    state.y += state.directionY;
//    if (newY >= min && newY + getLength() <= max) {
//      setY(newY);
//    }
  }
}
