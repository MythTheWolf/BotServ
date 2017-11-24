package com.myththewolf.BotServ.packages;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.myththewolf.BotServ.lib.tool.Utils;

public class PackageRepo {
	private JSONObject master;
	private List<PackageEntry> packages = new ArrayList<>();
	private String absoluteURL;

	public PackageRepo(String URL) throws JSONException, IOException {
		absoluteURL = URL;
	   
		master = Utils.readJsonFromUrl(URL+"?a=repoIndex");

		packages = getPackages();
	}

	public PackageEntry getPackage(String name) {
		for (PackageEntry e : this.packages) {
			
			if (e.getName().equals(name)) {
				return e;
			}
		}
		return null;
	}
	public boolean containsPackage(String name) {
		for(PackageEntry e : this.packages) {
			if(e.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	public List<PackageEntry> getPackages() throws JSONException, IOException {
	    JSONArray packet = Utils.readJsonFromUrl(getAbsoluteURL()+"?a=repoIndex").getJSONArray("packages");
		for (int i = 0; i < packet.length(); i++) {
		   
			PackageEntry pack = new PackageEntry(
					packet.getJSONObject(i).getString("name"), this);
			if(!containsPackage(pack.getName())) {
				this.packages.add(pack);
			}
		}
		return packages;
	}

	public JSONObject getRepoJSON() {
		return this.master;
	}

	public String getAbsoluteURL() {
		return this.absoluteURL;
	}

	public String getDistroURL() {

		return getAbsoluteURL() + "" + this.master.getString("repoBase");
	}

	@Override
	public boolean equals(Object obj) {
		return this.master.equals(((PackageRepo) obj).master);
	}
}
