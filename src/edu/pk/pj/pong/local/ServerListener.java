package edu.pk.pj.pong.local;

import java.util.logging.*;

import edu.pk.pj.pong.Connection;
import edu.pk.pj.pong.events.Event;

public class ServerListener implements Runnable {

  private static final Logger log = Logger.getLogger("ServerListener");

  private Connection connection;
  private EventHandler eventHandler;
  private boolean running;

  public ServerListener(Connection connection, EventHandler eventHandler) {
    this.connection = connection;
    this.eventHandler = eventHandler;
  }

  @Override
  public void run() {
    Event event = null;
    while (running) {
      try {
        event = (Event) connection.get();
        //log.info("Received event " + event.getClass().getSimpleName());
        event.handle(eventHandler);
      } catch (Exception e) {
        log.log(Level.WARNING, "", e);
      }
    }
  }

  public void stop() {
    running = false;
    log.info("Stopped.");
  }

  public void start() {
    log.info("Starting");
    running = true;
    new Thread(this).start();
  }
}
