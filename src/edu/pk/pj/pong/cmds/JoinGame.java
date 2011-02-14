package edu.pk.pj.pong.cmds;

import edu.pk.pj.pong.server.CommandHandler;

public class JoinGame extends Command {
  
  public final String playerName;
  
  public JoinGame(String playerName) {
    this.playerName = playerName;
  }

  @Override
  public void handle(CommandHandler handler) throws Exception {
    handler.handle(this);
  }

}
