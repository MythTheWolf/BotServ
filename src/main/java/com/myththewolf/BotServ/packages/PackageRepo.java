package com.myththewolf.BotServ.packages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.myththewolf.BotServ.packages.downloader.HTTPUtils;

public class PackageRepo {
	private JSONObject master;
	private List<PackageEntry> packages = new ArrayList<>();
	private String absoluteURL;

	public PackageRepo(String URL) throws JSONException, IOException {
		absoluteURL = URL;
		master = HTTPUtils.readJsonFromUrl(URL);
		JSONArray packs = master.getJSONArray("packages");
		for (int i = 0; i < packs.length(); i++) {
			JSONObject pack = packs.getJSONObject(i);
			packages.add(new PackageEntry(pack.getString("name"), this));
		}
	}

	public PackageEntry getPackage(String name) {
		for (PackageEntry e : this.packages) {
			if (e.getName().equals(name)) {
				return e;
			}
		}
		return null;
	}

	public JSONObject getRepoJSON() {
		return this.master;
	}

	public String getAbsoluteURL() {
		return this.absoluteURL.substring(0, (this.absoluteURL.length() - ("repo.json".length())));
	}

	public String getDistroURL() {
		return getAbsoluteURL() + "" + this.master.getString("repoBase");
	}

}
