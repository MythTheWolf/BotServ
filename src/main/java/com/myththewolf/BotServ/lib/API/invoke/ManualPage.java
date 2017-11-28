package com.myththewolf.BotServ.lib.API.invoke;

import java.io.File;
import java.text.ParseException;

import com.myththewolf.BotServ.lib.tool.Utils;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

public class ManualPage {
	String RAW = "";

	File manpage;
	InputParser IP;
	DiscordPlugin pl;
	String NAME;

	public ManualPage(DiscordPlugin inst, File manFile) throws ParseException {
		RAW = Utils.readFile(manFile);
		manpage = manFile;
		IP = new InputParser(RAW);
		pl = inst;
		NAME = IP.get("@pagename");
	}

	public String getName() {
		return NAME;
	}

	public String getSyn() throws ParseException {
		return IP.get("@synopsis");
	}

	public String getDescription() throws ParseException {
		return IP.get("@description");
	}

	public String getPage(int num) throws ParseException {
		return IP.get("@page " + num);
	}

	public void instaniate(TextChannel TC) {
		EmbedBuilder EB = new EmbedBuilder();
		EB.setTitle("Manual Page");
		try {
			EB.addField("Synopsis", getSyn(), false);
			EB.addField("Description", getDescription(), false);
			Message theMessage = TC.sendMessage(EB.build()).complete();

			theMessage.addReaction(TC.getGuild().getEmotesByName("arrow_backward", true).get(0)).queue();
			pl.helpEmbeds.add(theMessage);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getPages() {
		int test = 1;
		while (true) {
			try {
				IP.get("@page " + test);
				test++;
			} catch (ParseException e) {
				break;
			}
		}
		return test;
	}
}
