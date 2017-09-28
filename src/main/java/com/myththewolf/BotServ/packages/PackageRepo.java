package com.myththewolf.BotServ.packages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.myththewolf.BotServ.packages.downloader.Utils;

public class PackageRepo {
	private JSONObject master;
	private List<PackageEntry> packages = new ArrayList<>();
	private String absoluteURL;

	public PackageRepo(String URL) throws JSONException, IOException {
		absoluteURL = URL;
		master = Utils.readJsonFromUrl(URL);
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
		for (int i = 0; i < this.master.getJSONArray("packages").length(); i++) {
			PackageEntry pack = new PackageEntry(
					this.master.getJSONArray("packages").getJSONObject(i).getString("name"), this);
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
		return this.absoluteURL.substring(0, (this.absoluteURL.length() - ("repo.json".length())));
	}

	public String getDistroURL() {
		return getAbsoluteURL() + "" + this.master.getString("repoBase");
	}

	@Override
	public boolean equals(Object obj) {
		return this.master.equals(((PackageRepo) obj).master);
	}
}
