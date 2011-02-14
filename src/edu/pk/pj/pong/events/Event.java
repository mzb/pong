package edu.pk.pj.pong.events;

import java.io.Serializable;

import edu.pk.pj.pong.local.EventHandler;

public abstract class Event implements Serializable {
  
  public Event() {
    
  }
  
	public abstract void handle(EventHandler handler);
}
