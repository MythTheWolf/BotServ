package com.myththewolf.BotServ.lib.API.event.engines;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.myththewolf.BotServ.lib.API.invoke.ServerPluginManager;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ReactionAdd extends ListenerAdapter {
	private List<Message> ALL = new ArrayList<>();

	public void onReaction(GuildMessageReactionAddEvent event) {
		ALL = new ArrayList<>();
		try {
			ServerPluginManager.getPlugins().forEach(plugin -> {
				plugin.helpEmbeds.forEach(embed -> ALL.add(embed));
			});
			ALL.stream().filter(entry -> entry.getId().equals(event.getMessageId())).collect(Collectors.toList())
					.forEach(ent -> {
						System.out.println(event.getReaction().getReactionEmote().getName());
					});
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
