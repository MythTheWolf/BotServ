package com.myththewolf.BotServ.lib.API.command;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.myththewolf.BotServ.lib.API.invoke.BotPlugin;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;



public class DiscordCommand {
  public MessageReceivedEvent e;
  private BotPlugin meta;
  private CommandExecutor ex;

  public DiscordCommand(CommandExecutor CE, BotPlugin meta) {
    this.meta = meta;
    ex = CE;
  }

  public void setMessageEvent(MessageReceivedEvent e) {
    this.e = e;
  }

  public String[] getArgs() {

    String[] orgin = e.getMessage().getContent().split(" ");
    List<String> tmp = new ArrayList<String>(Arrays.asList(orgin));
    tmp.remove(0);
    return tmp.toArray(new String[orgin.length - 1]);
  }

  public User getSender() {
    return e.getAuthor();
  }

  public BotPlugin getPlugin() {
    return this.meta;
  }

  public void deleteTriggerMessage() {
    e.getMessage().delete().queue();
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
    // eb.setAuthor(this.getSender().getName(), "", this.getSender().getAvatarUrl());
    eb.setThumbnail("https://blog.sqlauthority.com/wp-content/uploads/2015/08/erroricon.png");
    eb.setDescription(message);
    eb.setFooter("Moduale " + meta.getPluginName() + " under FrameBot", null);
    e.getChannel().sendMessage(eb.build()).queue();
  }

  public void runCommand() {
    ex.onCommand(this);
  }

}
