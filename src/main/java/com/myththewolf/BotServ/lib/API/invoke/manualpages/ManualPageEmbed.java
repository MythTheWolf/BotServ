package com.myththewolf.BotServ.lib.API.invoke.manualpages;

import java.awt.Color;
import java.text.ParseException;
import java.util.List;

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

    @SuppressWarnings("unchecked")
    public void incrementPage() throws IllegalArgumentException, ParseException {
        if (page + 1 > MAX_PAGES) {
            page = 0;
        } else {
            page++;
        }
        String FOOTER = mp.IP.paramExists("@page " + page, "footer") ? (String) mp.IP.getParamsOf("@page " + page).get("footer")
                : mp.pl.getPluginName();
        Color C = (mp.IP.paramExists("@page " + page, "color")
                && ((String) mp.IP.getParamsOf("@page " + page).get("color")).split(",").length == 3)
                ? new Color(Integer.parseInt(((String) mp.IP.getParamsOf("@page " + page).get("color")).split(",")[0]),
                Integer.parseInt(((String) mp.IP.getParamsOf("@page " + page).get("color")).split(",")[1]),
                Integer.parseInt(((String) mp.IP.getParamsOf("@page " + page).get("color")).split(",")[2]))
                : Color.CYAN;


        EmbedBuilder EB = new EmbedBuilder();
        EB.setTitle("Manual page: " + page + "/" + MAX_PAGES);
        EB.setDescription(mp.getPage(page));
        EB.setFooter(FOOTER, null);
        EB.setColor(C);
        if (mp.IP.getParamsOf("@page " + page).get("field") instanceof List<?>) {
            ((List<String>) mp.IP.getParamsOf("@page " + page).get("field")).forEach(con -> {

            });
        }
        msg.editMessage(EB.build()).queue();

    }

    public void decremntPage() throws IllegalArgumentException, ParseException {
        if (page == 0) {
            page = 0;
            EmbedBuilder EB = new EmbedBuilder();
            EB.setTitle("Manual Page: " + mp.getName());

            EB.setColor(Color.magenta);
            EB.addField("DESCRIPTION", "```" + mp.getDescription() + "```", false);
            EB.addField("USAGE", "```" + mp.getSyn() + "```", false);
            EB.addField("INDEX", "```" + mp.getIndex() + "```", false);
            msg.editMessage(EB.build()).queue();
            return;
        }

        if ((page - 1 < 0)) {
            page = MAX_PAGES;
        } else {
            page--;
        }
        EmbedBuilder EB = new EmbedBuilder();
        EB.setTitle("Manual page: " + page + "/" + MAX_PAGES);
        EB.setDescription(mp.getPage(page));
        msg.editMessage(EB.build()).queue();

    }

    public Message getMessage() {
        return msg;
    }
}
