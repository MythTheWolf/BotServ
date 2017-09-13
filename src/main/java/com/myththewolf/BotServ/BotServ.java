package com.myththewolf.BotServ;

import com.myththewolf.BotServ.lib.API.EventHandlers.EventManager;

public class BotServ  {
	
	private EventManager EM;
	public BotServ() {
		EM = new EventManager();
	}
	public void registerEvents(Object eventHandler) {
		EM.registerEvents(eventHandler);
	}
	public EventManager getEventManager() {
		return EM;
	}
	
}
