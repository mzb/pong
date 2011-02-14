package edu.pk.pj.pong.cmds;

import edu.pk.pj.pong.ObjectCache;

public class CommandFactory {

  private ObjectCache<Command> cache = new ObjectCache<Command>();
  
  public MovePlayer MovePlayer(String playerName, int direction) throws CommandFactory.Error {
    MovePlayer cmd;
    try {
      cmd = (MovePlayer) cache.getOrCreate(MovePlayer.class);
    } catch (ObjectCache.Error e) {
      throw new CommandFactory.Error(e.toString());
    }
    cmd.playerName = playerName;
    cmd.direction = direction;
    return cmd;
  }
  
  public StopPlayer StopPlayer(String playerName) throws CommandFactory.Error {
    StopPlayer cmd;
    try {
      cmd = (StopPlayer) cache.getOrCreate(StopPlayer.class);
    } catch (ObjectCache.Error e) {
      throw new CommandFactory.Error(e.toString());
    }
    cmd.playerName = playerName;
    return cmd;
  }
  
  public static class Error extends Exception {
    public Error(String msg) { super(msg); }
  }
}
