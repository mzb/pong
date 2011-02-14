package edu.pk.pj.pong.cmds;

import edu.pk.pj.pong.server.CommandHandler;

public class LeaveGame extends Command {

  public String playerName;
  
  public LeaveGame(String playerName) {
    this.playerName = playerName;
  }

  @Override
  public void handle(CommandHandler handler) throws Exception {
    handler.handle(this);
  }
}
