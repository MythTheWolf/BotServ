package com.myththewolf.BotServ.lib.API.invoke;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.myththewolf.BotServ.lib.API.command.CommandExecutor;
import com.myththewolf.BotServ.lib.API.command.DiscordCommand;
import com.myththewolf.BotServ.lib.API.event.Interfaces.EventEntry;
import com.myththewolf.BotServ.lib.API.event.Interfaces.EventListener;
import com.myththewolf.BotServ.lib.API.event.Interfaces.EventType;
import com.myththewolf.BotServ.lib.API.invoke.manualpages.ManualPage;
import com.myththewolf.BotServ.lib.API.invoke.manualpages.ManualPageEmbed;
import com.myththewolf.BotServ.lib.tool.Utils;
import net.dv8tion.jda.core.JDA;

public class ImplBotPlugin implements BotPlugin {
  private File PLUGIN_DIR;
  private String MAIN;
  private String NAME;
  private String AUTH;
  private String DESC;
  private String VERSION;
  private boolean enabled;
  private HashMap<String, DiscordCommand> commandMap = new HashMap<>();
  private HashMap<EventType, List<EventEntry>> events = new HashMap<>();
  private List<ManualPage> manualPages = new ArrayList<>();
  private List<ManualPageEmbed> helpEmbeds = new ArrayList<>();
  private File JAR;
  private JDA runner;

  public ImplBotPlugin(JSONObject runconfig, File theJarFile, File file,JDA runner) {
    MAIN = runconfig.getString("main");
    NAME = runconfig.getString("name");
    AUTH = runconfig.getString("author");
    DESC = runconfig.getString("description");
    VERSION = runconfig.getString("version");
    JAR = theJarFile;
    PLUGIN_DIR = file;
    this.runner = runner;
    for (EventType ET : EventType.values()) {
      events.put(ET, new ArrayList<>());
    }
    Arrays.stream(getManualDir().listFiles()).filter(f -> f.getName().endsWith(".man"))
        .collect(Collectors.toList()).forEach(it -> {
          try {
            manualPages.add(new ManualPage(this, it));
          } catch (ParseException e) {
            e.printStackTrace();
          }
        });
  }

  public void setEnabled(boolean en) {
    enabled = en;
  }

  public List<ManualPage> getManuals() {
    return this.manualPages;
  }

  public File getSelfJar() {
    return JAR;
  }

  public void registerCommand(String command, CommandExecutor execute) throws Exception {
    this.commandMap.put(command, new DiscordCommand(execute, this));
  }

  public File getPLUGIN_DIR() {
    return PLUGIN_DIR;
  }

  public File getConfig() {
    return new File(this.PLUGIN_DIR + File.separator + "config.json");
  }

  public File getManualDir() {
    return new File(this.PLUGIN_DIR + File.separator + "manual-pages");
  }

  public JSONObject getJSONConfig() {
    return new JSONObject(Utils.readFile(getConfig()));
  }

  public void saveConfig(JSONObject root) {
    String JSON = root.toString();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    JsonParser jp = new JsonParser();
    JsonElement je = jp.parse(JSON);
    Utils.writeToFile(gson.toJson(je), getConfig());
  }

  public String getMainClass() {
    return MAIN;
  }

  public HashMap<String, DiscordCommand> getCommands() {
    return this.commandMap;
  }

  public String getPluginName() {
    return NAME;
  }

  public String getPluginAuthor() {
    return AUTH;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void registerEvent(EventListener list) {
    boolean found = false;
    for (Method M : list.getClass().getDeclaredMethods()) {

      if (EventEntry.verify(M)) {
        if (this.events.get(EventEntry.findType(M)) == null) {
          this.events.put(EventEntry.findType(M), new ArrayList<>());
        }
        try {

          this.events.get(EventEntry.findType(M)).add(new EventEntry(list, M));
          found = true;
        } catch (Exception e) {
          System.err
              .println("Could not register event: " + M.getClass().getName() + "#" + M.getName());
          e.printStackTrace();
        }
      }
    }
    if (!found) {
      System.err.println("[BotServ]WARNINIG: Class " + list.getClass().getName()
          + " does not have any event methods.");
    }

  }

  public InputStream getInternalResource(File theJar, String pathInJar) {
    try {
      URL url = new URL("jar:file:" + theJar.getAbsolutePath() + "!/" + pathInJar);
      InputStream is = url.openStream();
      return is;
    } catch (Exception e) {
      return null;
    }
  }

  public List<EventEntry> getEventList(EventType e) {
    return this.events.get(e);
  }

  @Override
  public String getPluginDescription() {
    return this.DESC;
  }

  @Override
  public String getPluginVersion() {
    return this.VERSION;
  }

  public List<ManualPageEmbed> getHelpEmbeds() {
    return this.helpEmbeds;
  }

  @Override
  public JDA getJDAInstance() {
    return this.runner;
  }
}

