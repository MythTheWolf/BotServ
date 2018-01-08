package com.myththewolf.BotServ.lib.API.invoke.manualpages;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.myththewolf.BotServ.lib.API.invoke.ServerPluginManager;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ManCommand {
    private List<ManualPage> ALL = new ArrayList<>();

    public void onCommand(String[] args, MessageReceivedEvent e) {
        String page = args[1];
        ALL = new ArrayList<>();
        try {
            ServerPluginManager.getPlugins().forEach(pl -> pl.getManuals().forEach(con -> ALL.add(con)));
            List<ManualPage> pages = ALL.stream().filter(pg -> {
                System.out.println(pg.getName().toLowerCase().trim() + "::" + page.toLowerCase().trim());
                return pg.getName().toLowerCase().trim().equals(page.toLowerCase().trim());

            }).collect(Collectors.toList());

            if (pages.isEmpty()) {
                e.getTextChannel().sendMessage("No manual page found").queue();
                ;
                return;
            } else {
                pages.forEach(i -> i.instaniate(e.getTextChannel()));
            }
        } catch (

                IllegalAccessException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }

    }

}
