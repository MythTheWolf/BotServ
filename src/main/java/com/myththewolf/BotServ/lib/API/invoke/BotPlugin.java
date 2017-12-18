package com.myththewolf.BotServ.lib.API.invoke;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;
import com.myththewolf.BotServ.lib.API.command.CommandExecutor;
import com.myththewolf.BotServ.lib.API.command.DiscordCommand;
import com.myththewolf.BotServ.lib.API.event.Interfaces.EventEntry;
import com.myththewolf.BotServ.lib.API.event.Interfaces.EventListener;
import com.myththewolf.BotServ.lib.API.event.Interfaces.EventType;
import com.myththewolf.BotServ.lib.API.invoke.manualpages.ManualPage;
import net.dv8tion.jda.core.JDA;

public interface BotPlugin {
  public List<ManualPage> getManuals();

  public void registerCommand(String command, CommandExecutor execute) throws Exception;

  public File getPLUGIN_DIR();

  public File getConfig();

  public JDA getJDAInstance();

  public File getManualDir();

  public JSONObject getJSONConfig();

  public void saveConfig(JSONObject root);

  public HashMap<String, DiscordCommand> getCommands();

  public String getPluginName();

  public String getPluginAuthor();

  public String getPluginDescription();

  public String getPluginVersion();

  public boolean isEnabled();

  public void registerEvent(EventListener list);

  public List<EventEntry> getEventList(EventType e);
}
