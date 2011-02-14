package edu.pk.pj.pong.events;

import edu.pk.pj.pong.local.EventHandler;
import edu.pk.pj.pong.Connection;

public class PlayerJoined extends Event {
  
  public String playerName;
  
  @Override
  public void handle(EventHandler handler) {
    handler.handle(this);
  }
}
