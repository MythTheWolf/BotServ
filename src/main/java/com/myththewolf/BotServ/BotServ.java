package com.myththewolf.BotServ;

import com.myththewolf.BotServ.lib.API.event.engines.EventHandlerTest;
import com.myththewolf.BotServ.lib.API.event.engines.EventManager;

public class BotServ  {
	
	private EventManager EM;
	public BotServ() {
		EM = new EventManager();
		EM.registerEvents(new EventHandlerTest());
	}
	public void registerEvents(Object eventHandler) {
	
	}
	public EventManager getEventManager() {
		return EM;
	}
	
}
