package com.myththewolf.BotServ.lib.API.EventHandlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.myththewolf.BotServ.lib.API.EventInterfaces.EventType;

public final class EventManager  {
	private HashMap<EventType,List<Object>> EVENTS = new HashMap<>();
	public EventManager() {
		
	}
	public void registerEvents(Object eventHandler) {
		
	}
	private void addEvent(EventType type,Object client) {
		List<Object> tmp = this.EVENTS.get(type);
		if(tmp == null) {
			tmp = new ArrayList<Object>();
		}
		tmp.add(client);
		this.EVENTS.put(type,tmp);
	}
	protected List<Object> getTodoList(EventType type) {
		List<Object> pointer = EVENTS.get(type);
		return pointer;
	}
}
