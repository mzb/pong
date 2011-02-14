package edu.pk.pj.pong.server;

public class Board {
  
  public int width;
  public int height;
  
  public Board(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }
}
