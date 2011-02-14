package edu.pk.pj.pong.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Logger;

import edu.pk.pj.pong.*;

public class Server {
  
  private static final Logger log = Logger.getLogger("Server");
  private boolean listening = true;
  private ServerSocket socket;
  
  private Game game = new Game();

	public Server(int port) throws IOException {
		socket = new ServerSocket(port);
		listen();
	}
	
	public void shutdown() throws IOException {
	  listening = false;
	  socket.close();
	}
	
	protected void listen() {
	  log.info("Listening at " + socket);
	  while (listening) {
      Connection connection;
      try {
        connection = new Connection(socket.accept());
        new ClientListener(connection, game).start();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
	}
}
