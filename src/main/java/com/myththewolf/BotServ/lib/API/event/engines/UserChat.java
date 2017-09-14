package com.myththewolf.BotServ.lib.API.event.engines;

import java.lang.reflect.InvocationTargetException;

import com.myththewolf.BotServ.lib.API.event.Interfaces.EventEntry;
import com.myththewolf.BotServ.lib.API.event.Interfaces.EventType;
import com.myththewolf.BotServ.lib.event.Objects.NOPEvent;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class UserChat extends ListenerAdapter{
	private EventManager EE;
	public UserChat(EventManager e) {
		EE = e;
	}
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getMessage().getContent().equals("NOP")) {
			for(EventEntry ent : EE.getScheduale(EventType.NOPEvent)) {
				try {
					ent.runEvent(new NOPEvent());
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
