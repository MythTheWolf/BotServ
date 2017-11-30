package com.myththewolf.BotServ.lib.API.invoke.manualpages;

import java.awt.Color;
import java.io.File;
import java.text.ParseException;

import com.myththewolf.BotServ.lib.API.invoke.DiscordPlugin;
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
		String val = IP.get("@page " + num);
		return val.replaceAll("^(?<!\\\\)[#].*(?<!\\\\)[=].*(?<!\\\\)[;]", "");
	}

	public void instaniate(TextChannel TC) {
		EmbedBuilder EB = new EmbedBuilder();
		EB.setTitle("Manual Page: " + getName());
		try {
			EB.setColor(Color.magenta);
			EB.addField("DESCRIPTION", "```" + getDescription() + "```", false);
			EB.addField("USAGE", "```" + getSyn() + "```", false);
			EB.addField("INDEX", "```" + getIndex() + "```", false);
			Message theMessage = TC.sendMessage(EB.build()).complete();
			theMessage.addReaction("◀").queue();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			theMessage.addReaction("▶").queue();
			pl.helpEmbeds.add(new ManualPageEmbed(theMessage, this));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getIndex() {
		try {
			return IP.get("@index");
		} catch (ParseException e) {
			System.out.println("WARNING: No index for manual page " + getName());
		}
		return "NULL";
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
		return test - 1;
	}
}
