package edu.pk.pj.pong.events;

import java.util.List;

import edu.pk.pj.pong.Entity;
import edu.pk.pj.pong.local.EventHandler;
import edu.pk.pj.pong.server.Player;

public class GameState extends Event {
  
  public Entity.State ball;
  public List<Player.State> players;

  @Override
  public void handle(EventHandler handler) {
    handler.handle(this);
  }
  
  @Override public String toString() {
    return String.format("GameState: ball = %s", ball);
  }
}
