package edu.pk.pj.pong.cmds;

import edu.pk.pj.pong.server.CommandHandler;

public class StopPlayer extends Command {
  
  public String playerName;
  
  @Override
  public void handle(CommandHandler handler) throws Exception {
    handler.handle(this);
  }

}
