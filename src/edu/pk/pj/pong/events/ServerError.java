package edu.pk.pj.pong.events;

import edu.pk.pj.pong.local.EventHandler;

public class ServerError extends Event {

	public String message;
	
	public ServerError() {
	  
	}

	public ServerError(String message) {
		this.message = message;
	}

	@Override
	public void handle(EventHandler handler) {
		
	}

}
