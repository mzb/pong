package edu.pk.pj.pong.server;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException,
			ClassNotFoundException {
	  int port = 3000;
	  if (args.length > 0) {
  	  try {
  	    port = Integer.parseInt(args[0]);
  	  } catch (NumberFormatException e) {}
	  }
	  
		new Server(port);
	}
}
