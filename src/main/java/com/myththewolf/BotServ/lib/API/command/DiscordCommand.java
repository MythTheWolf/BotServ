package com.myththewolf.BotServ.lib.API.command;

import java.awt.Color;

import com.myththewolf.BotServ.lib.API.invoke.DiscordPlugin;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class DiscordCommand {
	public MessageReceivedEvent e;
	private DiscordPlugin meta;
	private CommandExecutor ex;
	
	public DiscordCommand(CommandExecutor CE, DiscordPlugin meta) {
		this.meta = meta;
		ex = CE;
	}
	protected void setMessageEvent(MessageReceivedEvent e) {
		this.e = e;
	}
	public String[] getArgs() {
		String[] pop = new String[e.getMessage().getContent().split(" ").length - 1];
		int pos = 0;
		for (String I : e.getMessage().getContent().split(" ")) {
			pop[pos] = I;
			pos++;
		}
		return pop;
	}

	public User getSender() {
		return e.getAuthor();
	}

	public void deleteTriggerMessage() {
		e.getMessage().delete();
	}

	public void reply(String message) {
		e.getChannel().sendMessage(message).queue();
	}

	public void reply(EmbedBuilder em) {
		e.getChannel().sendMessage(em.build()).queue();

	}

	public void failed(String message) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Command Failed");
		eb.setColor(Color.RED);
		eb.setAuthor(this.getSender().getName(), "", this.getSender().getAvatarUrl());
		eb.setFooter("", "");
		eb.setThumbnail("https://blog.sqlauthority.com/wp-content/uploads/2015/08/erroricon.png");
		eb.setFooter("_Moduale "+meta.getNAME()+" under BotServ", null);
		e.getChannel().sendMessage(eb.build()).queue();
	}
	protected void runCommand() {
		ex.onCommand(this);
	}
	
}
