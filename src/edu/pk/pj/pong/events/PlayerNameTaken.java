package edu.pk.pj.pong.events;

import edu.pk.pj.pong.local.EventHandler;

public class PlayerNameTaken extends Event {
  
  public String name;

  @Override
  public void handle(EventHandler handler) {
    handler.handle(this);
  }

}
