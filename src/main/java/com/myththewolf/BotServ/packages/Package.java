package com.myththewolf.BotServ.packages;

import org.json.JSONArray;
import org.json.JSONObject;

public class Package {
	private String name;
	private String base_location;
	private JSONArray dependencies;

	public Package(String name, String internetLocation, JSONArray dependencies) {
		this.name = name;
		this.base_location = internetLocation;
		this.dependencies = dependencies;
	}

}
