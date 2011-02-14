package edu.pk.pj.pong.cmds;

import edu.pk.pj.pong.server.CommandHandler;

public class MovePlayer extends Command {
  
  public static final int UP = -1;
  public static final int DOWN = +1;
  
  public String playerName;
  public int direction;
  
  @Override
  public void handle(CommandHandler handler) throws Exception {
    handler.handle(this);
  }

}
