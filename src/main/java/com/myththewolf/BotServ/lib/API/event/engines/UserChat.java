package com.myththewolf.BotServ.lib.API.event.engines;

import java.lang.reflect.InvocationTargetException;

import com.myththewolf.BotServ.lib.API.event.Interfaces.EventType;
import com.myththewolf.BotServ.lib.API.invoke.DiscordPlugin;
import com.myththewolf.BotServ.lib.API.invoke.ServerPluginManager;
import com.myththewolf.BotServ.lib.event.Objects.UserSendEvent;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class UserChat extends ListenerAdapter{
	
	public UserChat() {
	
	}
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getMessage().getContent().equals("poontah")) {
			event.getChannel().sendMessage("POONTAH!").queue();
		}
		try {
			for(DiscordPlugin pl : ServerPluginManager.getPlugins()) {
				System.err.println("IN_LOOP");
				if(pl.getEventList(EventType.UserSendEvent).size() > -1) {
					pl.getEventList(EventType.UserSendEvent).get(0).runEvent(new UserSendEvent(event));
					
				}else {
					
				}
			}
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
