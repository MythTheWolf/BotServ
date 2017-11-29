package com.myththewolf.BotServ.lib.API.event.engines;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.myththewolf.BotServ.lib.API.invoke.ServerPluginManager;
import com.myththewolf.BotServ.lib.API.invoke.manualpages.ManualPageEmbed;

import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.EventListener;

public class ReactionAdd implements EventListener {
	private List<ManualPageEmbed> ALL = new ArrayList<>();

	public void onReaction(User user, GenericGuildMessageReactionEvent e2, boolean isAdd) {
		MessageReaction event = e2.getReaction();
		if (!isAdd && ((GuildMessageReactionRemoveEvent) e2).getUser().isBot()) {
			return;
		}
		ALL = new ArrayList<>();
		// ◀ ▶
		try {
			ServerPluginManager.getPlugins().forEach(plugin -> {
				plugin.helpEmbeds.forEach(entry -> {
					ALL.add(entry);
				});
			});
			ALL.stream().filter(entry -> entry.getMessage().getId().equals(event.getMessageId()))
					.collect(Collectors.toList()).forEach(theEmbed -> {
						ManualPageEmbed MPE = theEmbed;
						if (event.getReactionEmote().getName().equals("▶")) {
							try {
								MPE.incrementPage();

							} catch (Exception e) {
								MPE.getMessage().getTextChannel().sendMessage(e.getMessage()).queue();
							}

						} else if (event.getReactionEmote().getName().equals("◀")) {
							try {
								MPE.decremntPage();

							} catch (Exception e) {
								MPE.getMessage().getTextChannel().sendMessage(e.getMessage()).queue();
							}
						}
						//event.removeReaction(user).queue();
					});
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onEvent(Event arg0) {
		if (arg0 instanceof GenericGuildMessageReactionEvent) {
			if (((GenericGuildMessageReactionEvent) arg0).getUser().isBot()) {
				return;
			}
			if (arg0 instanceof GuildMessageReactionRemoveEvent) {
				return;
			}
			boolean isAdd = (arg0 instanceof GuildMessageReactionAddEvent);
			if (isAdd) {
				// System.out.println("Added reaction: " + ((GenericGuildMessageReactionEvent)
				// arg0));
			}
			User u = ((GenericGuildMessageReactionEvent) arg0).getUser();
			onReaction(u, ((GenericGuildMessageReactionEvent) arg0), isAdd);

		}

	}
}
