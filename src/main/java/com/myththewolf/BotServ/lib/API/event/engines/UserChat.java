package com.myththewolf.BotServ.lib.API.event.engines;

import com.myththewolf.BotServ.lib.API.invoke.DiscordPlugin;
import com.myththewolf.BotServ.lib.API.invoke.ServerPluginManager;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class UserChat extends ListenerAdapter{
	
	public UserChat() {
	
	}
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		try {
			for(DiscordPlugin pl : ServerPluginManager.getPlugins()) {
				
			}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
