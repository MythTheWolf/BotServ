package com.myththewolf.BotServ.lib.API.event.engines;

import java.lang.reflect.InvocationTargetException;

import com.myththewolf.BotServ.lib.API.command.DiscordCommand;
import com.myththewolf.BotServ.lib.API.command.ManCommand;
import com.myththewolf.BotServ.lib.API.event.Interfaces.EventEntry;
import com.myththewolf.BotServ.lib.API.event.Interfaces.EventType;
import com.myththewolf.BotServ.lib.API.invoke.DiscordPlugin;
import com.myththewolf.BotServ.lib.API.invoke.ServerPluginManager;
import com.myththewolf.BotServ.lib.event.Objects.UserSendEvent;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class UserChat extends ListenerAdapter {

	

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		try {
			if(event.getMessage().getContent().startsWith("!man")) {
				ManCommand mC = new ManCommand();
				mC.onCommand(event.getMessage().getContent().split(" "), event);
			}
			for(DiscordPlugin I : ServerPluginManager.getPlugins()) {
				if(I.getCommands().containsKey(event.getMessage().getContent().split(" ")[0])) {
					
					DiscordCommand DC = I.getCommands().get(event.getMessage().getContent().split(" ")[0]);
					DC.setMessageEvent(event);
					DC.runCommand();
				}
				if(I.isEnabled() && I.getEventList(EventType.UserSendEvent).size() > 0) {
					for(EventEntry EE : I.getEventList(EventType.UserSendEvent)) {
						EE.runEvent(new UserSendEvent(event));
					}
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
