package edu.pk.pj.pong.local;

import java.awt.Graphics;

import edu.pk.pj.pong.Entity;

public class Ball extends Entity implements Renderable {
  
  public Ball(State state) {
    super(state);
  }

  @Override public void render(Graphics g) {
    g.setColor(state.color);
    g.fillRect(state.x, state.y, state.width, state.height);
  }
}
