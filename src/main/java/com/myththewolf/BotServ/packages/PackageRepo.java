package com.myththewolf.BotServ.packages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PackageRepo {
	private JSONObject master;
	private List<Package> packages = new ArrayList<>();
	public PackageRepo(String URL) throws JSONException, IOException {
		master = readJsonFromUrl(URL);
		JSONArray packs = master.getJSONArray("plugins");
		for(int i =0; i<packs.length(); i++) {
			JSONObject pack = packs.getJSONObject(i);
			packages.add(new Package(pack.getString("name"),pack.getString("location"), pack.getJSONArray("dependencies")));
		}
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			
			return json;
		} finally {
			is.close();
		}
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
	public Package getPackage(String name,String version) {
		return null;
		
	}
	
}
