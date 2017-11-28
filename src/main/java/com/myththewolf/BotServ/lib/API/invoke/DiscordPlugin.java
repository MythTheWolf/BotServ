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

import com.myththewolf.BotServ.lib.API.command.CommandExecutor;
import com.myththewolf.BotServ.lib.API.command.DiscordCommand;
import com.myththewolf.BotServ.lib.API.event.Interfaces.EventEntry;
import com.myththewolf.BotServ.lib.API.event.Interfaces.EventListener;
import com.myththewolf.BotServ.lib.API.event.Interfaces.EventType;
import com.myththewolf.BotServ.lib.tool.Utils;

import net.dv8tion.jda.core.entities.Message;

public class DiscordPlugin {
	private File PLUGIN_DIR;
	private File CURRENT_DIR;
	private String MAIN;
	private String NAME;
	private String AUTH;
	private String DESC;
	private String VERSION;
	private String UPDATECONF;
	private boolean enabled;
	private HashMap<String, DiscordCommand> commandMap = new HashMap<>();
	private HashMap<EventType, List<EventEntry>> events = new HashMap<>();
	private List<ManualPage> manualPages = new ArrayList<>();
	public List<Message> helpEmbeds = new ArrayList<>();
	private File JAR;

	public DiscordPlugin(JSONObject runconfig, File theJarFile, File file) {
		MAIN = runconfig.getString("main");
		NAME = runconfig.getString("name");
		AUTH = runconfig.getString("author");
		DESC = runconfig.getString("description");
		VERSION = runconfig.getString("version");
		JAR = theJarFile;
		PLUGIN_DIR = file;
		for (EventType ET : EventType.values()) {
			events.put(ET, new ArrayList<>());
		}
		Arrays.stream(getManualDir().listFiles()).filter(f -> f.getName().endsWith(".man")).collect(Collectors.toList())
				.forEach(it -> {
					try {
						manualPages.add(new ManualPage(this, it));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
	}

	public void setEnabled(boolean en) {
		enabled = en;
	}
	public List<ManualPage> getManuals(){
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
		Utils.writeToFile(JSON, getConfig());
	}

	public File getCURRENT_DIR() {
		return CURRENT_DIR;
	}

	public String getMAIN() {
		return MAIN;
	}

	public HashMap<String, DiscordCommand> getCommands() {
		return this.commandMap;
	}

	public String getNAME() {
		return NAME;
	}

	public String getAUTH() {
		return AUTH;
	}

	public String getDESC() {
		return DESC;
	}

	public String getVERSION() {
		return VERSION;
	}

	public String getUPDATECONF() {
		return UPDATECONF;
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
					System.err.println("Could not register event: " + M.getClass().getName() + "#" + M.getName());
					e.printStackTrace();
				}
			}
		}
		if (!found) {
			System.err.println(
					"[BotServ]WARNINIG: Class " + list.getClass().getName() + " does not have any event methods.");
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
}
