package com.myththewolf.BotServ.lib.API.event.engines;

import com.myththewolf.BotServ.lib.API.event.Interfaces.EventListener;
import com.myththewolf.BotServ.lib.event.Objects.NOPEvent;

public class EventHandlerTest implements EventListener {
	
	public void onTest(NOPEvent e) {
		System.out.println("GOT EVENT!");
	}
	
}
