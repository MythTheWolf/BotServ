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
			throw new IllegalArgumentException("O.O.B");

		}
		page++;
		String FOOTER = mp.IP.paramExists("@page " + page, "footer") ? mp.IP.getParamsOf("@page " + page).get("footer")
				: mp.pl.getNAME();
		Color C = (mp.IP.paramExists("@page " + page, "color")
				&& mp.IP.getParamsOf("@page " + page).get("color").split(",").length == 3)
						? new Color(Integer.parseInt(mp.IP.getParamsOf("@page " + page).get("color").split(",")[0]),
								Integer.parseInt(mp.IP.getParamsOf("@page " + page).get("color").split(",")[1]),
								Integer.parseInt(mp.IP.getParamsOf("@page " + page).get("color").split(",")[2]))
						: Color.CYAN;
		String[] fieldNames = mp.IP.paramExists("@page " + page, "fieldnames")
				? mp.IP.getParamsOf("@page " + page).get("fieldnames").split("(?<=[^\\\\\\\\]),")
				: null;
		String[] fieldVals = mp.IP.paramExists("@page " + page, "fieldvalues")
				? mp.IP.getParamsOf("@page " + page).get("fieldvalues").split("(?<=[^\\\\\\\\]),")
				: null;
		boolean shouldDo = (fieldNames != null && fieldVals != null) && (fieldNames.length == fieldVals.length);

		EmbedBuilder EB = new EmbedBuilder();
		EB.setTitle("Manual page: " + page + "/" + MAX_PAGES);
		EB.setDescription(mp.getPage(page));
		EB.setFooter(FOOTER, null);
		EB.setColor(C);
		if (shouldDo) {
			int index = 0;
			for(String name : fieldNames) {
				EB.addField(name,fieldVals[index],false);
				index++;
			}
		}
		msg.editMessage(EB.build()).queue();

	}

	public void decremntPage() throws IllegalArgumentException, ParseException {
		if (page - 1 == 0) {
			page = 0;
			EmbedBuilder EB = new EmbedBuilder();
			EB.setTitle("Manual Page: " + mp.getName());

			EB.setColor(Color.magenta);
			EB.addField("DESCRIPTION", "```" + mp.getDescription() + "```", false);
			EB.addField("USAGE", "```" + mp.getSyn() + "```", false);
			EB.addField("INDEX", "```" + mp.getIndex() + "```", false);
			msg.editMessage(EB.build()).queue();

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
