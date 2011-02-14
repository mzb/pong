package edu.pk.pj.pong.cmds;

import java.io.Serializable;

import edu.pk.pj.pong.server.CommandHandler;

public abstract class Command implements Serializable {
  
  public Command() {}

  public abstract void handle(CommandHandler handler) throws Exception;
  
  public String toString() {
    return getClass().getSimpleName();
  }
}
