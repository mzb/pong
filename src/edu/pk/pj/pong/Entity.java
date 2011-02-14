package edu.pk.pj.pong;

import java.awt.Color;
import java.io.Serializable;

public class Entity {

  protected State state;
  
  public Entity(State state) {
    setState(state);
  }
  
  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public static class State implements Serializable {
    public int x;
    public int y;
    public int directionX;
    public int directionY;
    public int width;
    public int height;
    public Color color;
    public int velocity;
    
    @Override public String toString() {
      return String.format("<Entity.State: " +
      		"x=%d y=%d \n" +
      		"directionX=%d directionY=%d \n" +
      		"width=%d height=%d \n" +
      		"velocity=%d color=%s>", 
      		x, y, directionX, directionY, width, height, velocity, color.toString());
    }
  }
}
