package com.myththewolf.BotServ.packages;

import java.io.IOException;

import org.json.JSONException;

public class Test {
	public static void main(String[] args) {
		try {
			PackageRepo rep = new PackageRepo("http://70.139.52.7/pluginrepo/repo.json");
			PackageEntry ent = rep.getPackage("MyTestPackage");
			//System.out.println(ent.getReleases().get(0).getFileURL());
			String build = "";
			ent.getDependencies();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
