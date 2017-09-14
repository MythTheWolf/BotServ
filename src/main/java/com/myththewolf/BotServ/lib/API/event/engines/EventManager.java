package com.myththewolf.BotServ.lib.API.event.engines;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.myththewolf.BotServ.lib.API.event.Interfaces.EventEntry;
import com.myththewolf.BotServ.lib.API.event.Interfaces.EventListener;
import com.myththewolf.BotServ.lib.API.event.Interfaces.EventType;
public final class EventManager  {
	private HashMap<EventType,List<EventEntry>> EVENTS = new HashMap<>();
	public EventManager() {
		
	}
	
	public void registerEvents(EventListener myHandler) {
		 	addEvent(myHandler);
	}
	private void addEvent(EventListener hand) {
		boolean found = false;
		for(Method M : hand.getClass().getDeclaredMethods()) {
			if(EventEntry.verify(M)) {
				if(this.EVENTS.get(EventEntry.findType(M)) == null) {
					this.EVENTS.put(EventEntry.findType(M),new ArrayList<>());
				}
				try {
					this.EVENTS.get(EventEntry.findType(M)).add(new EventEntry(hand,M));
					found = true;
				} catch (Exception e) {
					System.err.println("Could not register event: " + M.getClass().getName()+"#"+M.getName());
					e.printStackTrace();
				}
			}
			
		}
		if(!found) {
			System.out.println("[BotServ]WARNINIG: Class " + hand.getClass().getName() + " does not have any event methods.");
		}
		
	}
	

	protected List<EventEntry> getScheduale(EventType e) {
		return this.EVENTS.get(e);
	}
	
}
