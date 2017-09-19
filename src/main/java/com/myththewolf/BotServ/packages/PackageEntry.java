package com.myththewolf.BotServ.packages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.myththewolf.BotServ.packages.downloader.HTTPUtils;

public class PackageEntry {
	public String PackageName;
	public String absoluteURL;
	public JSONObject packageMeta;
	public PackageRepo repo;
	public List<PackageEntry> dependencies = new ArrayList<>();
	public PackageEntry(String name,PackageRepo rep) {
		repo = rep;
		PackageName = name;
		try {
		
			packageMeta = HTTPUtils.readJsonFromUrl(repo.getDistroURL()+name+"/"+"plugin.json");
			//Checking dependencies
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public String getPackcageBase() {
		return repo.getDistroURL()+getName()+"/";
	}
	public PackageRelease getRelease(String tag) {
		return new PackageRelease(this,packageMeta.getJSONObject("releases").getJSONObject(tag));
	}
	public List<PackageEntry> getDependencies() throws JSONException, IOException {
		addDependencies(packageMeta.getJSONArray("dependencies"));
		return null;
	}
	public List<PackageRelease> getReleases(){
		List<PackageRelease> rel = new ArrayList<>();
		JSONObject releaseSet = this.packageMeta.getJSONObject("releases");
		Iterator<?> it = releaseSet.keys();
		while(it.hasNext()) {
			String key = (String) it.next();
			JSONObject release = releaseSet.getJSONObject(key);
			rel.add(new PackageRelease(this, release));
		}
		return rel;
	}
	private void addDependencies(JSONArray depen) throws JSONException, IOException {
		for(int i = 0; i<depen.length(); i++) {
			JSONObject main = depen.getJSONObject(i);
			PackageEntry ent = new PackageEntry(main.getString("name"), new PackageRepo(main.getString("repo")));
			System.out.println(ent.packageMeta);
			if(!this.dependencies.contains(ent)) {
				this.dependencies.add(ent);
			}
			if(ent.packageMeta.getJSONArray("dependencies").length() > 0) {
				addDependencies(ent.packageMeta.getJSONArray("dependencies"));
			}else {
				continue;
			}
		}
	}
	public String getName() {
		return this.PackageName;
	}
	@Override
	public boolean equals(Object obj) {
		PackageEntry cast = (PackageEntry) obj;
		return cast.getName().equals(this.getName());
	}
}
