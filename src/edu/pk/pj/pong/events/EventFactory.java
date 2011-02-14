package edu.pk.pj.pong.events;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

import edu.pk.pj.pong.ObjectCache;
import edu.pk.pj.pong.server.Game;
import edu.pk.pj.pong.server.Player;

public class EventFactory {
  
  private ObjectCache<Event> cache = new ObjectCache<Event>(new ServerError(""));
  
  public ServerError ServerError(String msg) {
    ServerError serverError = (ServerError) cache.get("ServerError");
    serverError.message = msg;
    return serverError;
  }
  
  public GameState GameState(Game game) throws EventFactory.Error {
    GameState gameState;
    try {
      gameState = (GameState) cache.getOrCreate(GameState.class);
    } catch (ObjectCache.Error e) {
      throw new EventFactory.Error(e.toString());
    }
    gameState.ball = game.getBall().getState();
    List<Player.State> playerStates = new ArrayList<Player.State>();
    for (Player player : game.getPlayers()) {
      playerStates.add(player.getState());
    }
    gameState.players = playerStates;
    return gameState;
  }
  
  public PlayerLeft PlayerLeft(Player player) throws EventFactory.Error {
    PlayerLeft event;
    try {
      event = (PlayerLeft) cache.getOrCreate(PlayerLeft.class);
    } catch (ObjectCache.Error e) {
      throw new EventFactory.Error(e.toString());
    }
    event.playerName = player.getName();
    return event;
  }
  
  public PlayerJoined PlayerJoined(Player player) throws EventFactory.Error {
    PlayerJoined event;
    try {
      event = (PlayerJoined) cache.getOrCreate(PlayerJoined.class);
    } catch (ObjectCache.Error e) {
      throw new EventFactory.Error(e.toString());
    }
    event.playerName = player.getName();
    return event;
  }
  
  public PlayerNameTaken PlayerNameTaken(String name) throws EventFactory.Error {
    PlayerNameTaken event;
    try {
      event = (PlayerNameTaken) cache.getOrCreate(PlayerNameTaken.class);
    } catch (ObjectCache.Error e) {
      throw new EventFactory.Error(e.toString());
    }
    event.name = name;
    return event;
  }
  
  
  public static class Error extends Exception {
    public Error(String msg) {
      super(msg);
    }
  }
}
