package com.myththewolf.BotServ.lib.API.invoke.manualpages;

import java.awt.Color;
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
			throw new IllegalArgumentException("O.U.B");
			
		}
		page++;
		EmbedBuilder EB = new EmbedBuilder();
		EB.setTitle("Manual page: " + page + "/" + MAX_PAGES);
		EB.setDescription(mp.getPage(page));
		msg.editMessage(EB.build()).queue();

	}

	public void decremntPage() throws IllegalArgumentException, ParseException {
		if (page - 1 == 0) {
			EmbedBuilder EB = new EmbedBuilder();
			EB.setTitle("Manual Page: " + mp.getPages());
			EB.setColor(Color.magenta);
			EB.addField("Synopsis", mp.getSyn(), true);
			EB.addField("Description", mp.getDescription(), true);
			msg.editMessage(EB.build()).queue();
			return;
		}
		if ((page - 1 < 0)) {
			throw new IllegalArgumentException("O.U.B: " + (page - 1));
		}
		page--;
		EmbedBuilder EB = new EmbedBuilder();
		EB.setTitle("Manual page: " + page + "/" + MAX_PAGES);
		EB.setDescription(mp.getPage(page));
		msg.editMessage(EB.build()).queue();

	}

	public Message getMessage() {
		return msg;
	}
}
