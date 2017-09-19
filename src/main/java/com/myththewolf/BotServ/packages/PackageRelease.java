package com.myththewolf.BotServ.packages;

import org.json.JSONObject;

public class PackageRelease {
	public JSONObject releaseMeta;
	public PackageEntry thePackage;
	public PackageRelease(PackageEntry entry,JSONObject relaseObject) {
		this.releaseMeta = relaseObject;
		thePackage = entry;
	}
	public String getDescription() {
		return releaseMeta.getString("description");
	}
	public String getFileURL() {
		return thePackage.getPackcageBase()+"releases/"+this.releaseMeta.getString("tag")+".jar";
	}
	public String getCommitURL() {
		return this.releaseMeta.getString("commit");
	}
}
