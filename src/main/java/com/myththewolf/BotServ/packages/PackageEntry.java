package com.myththewolf.BotServ.packages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.myththewolf.BotServ.packages.downloader.Utils;

public class PackageEntry {
	public String PackageName;
	public String absoluteURL;

	public PackageRepo repo;
	public List<PackageEntry> dependencies = new ArrayList<>();

	public PackageEntry(String name, PackageRepo rep) {
		repo = rep;
		PackageName = name;
		

	}

	public String getPackcageBase() {
		return repo.getDistroURL() + getName() + "/";
	}

	public PackageRelease getRelease(String tag) throws JSONException, IOException {
	    JSONObject packet = Utils.readJsonFromUrl(repo.getAbsoluteURL()+"?a=getReleases&name="+this.PackageName);
		return new PackageRelease(this, packet.getJSONObject(tag));
	}

	public List<PackageEntry> getDependencies() throws JSONException, IOException {
	    System.out.println(repo.getAbsoluteURL()+"?a=getDependencies&name="+this.PackageName);
		addDependencies(Utils.readArray(repo.getAbsoluteURL()+"?a=getDependencies&name="+this.PackageName));
		return this.dependencies;
	}

	public List<PackageRelease> getReleases() throws JSONException, IOException {
		List<PackageRelease> rel = new ArrayList<>();
		JSONObject releaseSet = Utils.readJsonFromUrl(repo.getAbsoluteURL()+"?a=getReleases&name="+this.PackageName);
		Iterator<?> it = releaseSet.keys();
		while (it.hasNext()) {
			String key = (String) it.next();
			JSONObject release = releaseSet.getJSONObject(key);
			rel.add(new PackageRelease(this, release));
		}
		return rel;
	}
	public JSONArray getDependenciesJSON() throws IOException {
	   return Utils.readArray(repo.getAbsoluteURL()+"?a=getDependencies&name="+this.PackageName);
	}
	private void addDependencies(JSONArray depen) throws JSONException, IOException {
		for (int i = 0; i < depen.length(); i++) {
			JSONObject main = depen.getJSONObject(i);
			PackageEntry ent = new PackageEntry(main.getString("name"), new PackageRepo(main.getString("repo")));
			if (!this.dependencies.contains(ent)) {
				this.dependencies.add(ent);
			}
			
			if (ent.getDependencies().size()-1 > 0) {
				addDependencies(ent.getDependenciesJSON());
			} else {
				continue;
			}
		}
	}

	public String getName() {
		return this.PackageName;
	}
	public PackageRepo getRepo() {
		return this.repo;
	}
	@Override
	public boolean equals(Object obj) {
		PackageEntry cast = (PackageEntry) obj;
		return cast.getName().equals(this.getName());
	}
}
