package com.myththewolf.BotServ.lib.API.invoke;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.myththewolf.BotServ.lib.API.event.Interfaces.EventEntry;
import com.myththewolf.BotServ.lib.API.event.Interfaces.EventListener;
import com.myththewolf.BotServ.lib.API.event.Interfaces.EventType;
import com.myththewolf.BotServ.lib.API.event.engines.CommandExecutor;
import com.myththewolf.BotServ.lib.API.event.engines.DiscordCommand;

public class DiscordPlugin {
	private File PLUGIN_DIR;
	private File CURRENT_DIR;
	private String MAIN;
	private String NAME;
	private String AUTH;
	private String DESC;
	private String SHORTDESC;
	private String WEBSITE;
	private String VERSION;
	private String UPDATECONF;
	private boolean enabled;
	private HashMap<String,DiscordCommand> commandMap = new HashMap<>();
	private HashMap<EventType,List<EventEntry>> events = new HashMap<>();
	public DiscordPlugin(JSONObject runconfig) {
		MAIN = runconfig.getString("main");
		NAME = runconfig.getString("name");
		AUTH = runconfig.getString("author");
		DESC = runconfig.getString("description");
		SHORTDESC = runconfig.getString("shortDescription");
		WEBSITE = runconfig.getString("projectURL");
		VERSION = runconfig.getString("version");
		UPDATECONF = runconfig.getString("update-conf");
	}
	public void setEnabled(boolean en) {
		enabled = en;
	}
	public void registerCommand(String command, CommandExecutor execute) throws Exception {
		this.commandMap.put(command, new DiscordCommand(execute, this));
	}
	public File getPLUGIN_DIR() {
		return PLUGIN_DIR;
	}
	public File getCURRENT_DIR() {
		return CURRENT_DIR;
	}
	public String getMAIN() {
		return MAIN;
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
	public String getSHORTDESC() {
		return SHORTDESC;
	}
	public String getWEBSITE() {
		return WEBSITE;
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
	public void runCommand() {
		
	}
	public void registerEvent(EventListener list) {
		boolean found = false;
		for(Method M : list.getClass().getDeclaredMethods()) {
			if(EventEntry.verify(M)) {
				if(this.events.get(EventEntry.findType(M)) == null) {
					this.events.put(EventEntry.findType(M),new ArrayList<>());
				}
				try {
					this.events.get(EventEntry.findType(M)).add(new EventEntry(list,M));
					found = true;
				} catch (Exception e) {
					System.err.println("Could not register event: " + M.getClass().getName()+"#"+M.getName());
					e.printStackTrace();
				}
			}
			
		}
		if(!found) {
			System.out.println("[BotServ]WARNINIG: Class " + list.getClass().getName() + " does not have any event methods.");
		}
		
	}
	public List<EventEntry> getEventList(EventType e){
		return this.events.get(e);
	}
}
