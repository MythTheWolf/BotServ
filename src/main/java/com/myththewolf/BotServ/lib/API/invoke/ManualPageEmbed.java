package com.myththewolf.BotServ.lib.API.invoke;

import java.text.ParseException;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

public class ManualPageEmbed {
	private int page = 0;
	private Message msg;
	private int MAX_PAGES;
	private ManualPage mp;

	public ManualPageEmbed(Message theMessage, ManualPage MP) {
		msg = theMessage;
		MAX_PAGES = MP.getPages();
		mp = MP;
	}

	public void incrementPage() throws IllegalArgumentException, ParseException {
		if (page + 1 > MAX_PAGES) {
			throw new IllegalArgumentException();
		}
		page++;
		EmbedBuilder EB = new EmbedBuilder();
		EB.setTitle("Manual page: " + page + "/" + MAX_PAGES);
		EB.setDescription(mp.getPage(page));
		msg.editMessage(EB.build());
	}
}
