package com.myththewolf.BotServ.lib.API.invoke.manualpages;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class InputParser {
	private String raw;
	private String BUILDER = "";

	public InputParser(String parse) {
		raw = parse;
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> getParamsOf(String key) throws ParseException {
		String in = get(key);
		HashMap<String, Object> params = new HashMap<>();
		boolean READING = false;
		boolean gotKey = false;
		String buildTMp = "";
		String KEY = "";
		String VAL = "";
		
		for (int i = 0; i < in.length(); i++) {
			if (in.charAt(i) == '#') {
				READING = true;
			} else if (READING) {
				if (!gotKey && (in.charAt(i) != '=')) {
					buildTMp += in.charAt(i);
				} else if (!gotKey && ((in.charAt(i) == '=') && in.charAt(i - 1) != '\\')) {
					KEY = buildTMp;
					buildTMp = "";
					gotKey = true;
				} else if (gotKey && in.charAt(i) != ';' || (in.charAt(i) == ';' && in.charAt(i - 1) == '\\')) {
					buildTMp += in.charAt(i);
				} else if (gotKey && ((in.charAt(i) == ';'))) {
					VAL = buildTMp;
					System.out.println("+++" + KEY + ":" + VAL);
					if(params.get(KEY) == null){
						params.put(KEY, new ArrayList<>());
					}
					if(!(params.get(KEY) instanceof List<?>)){
						throw new IllegalStateException("The internal map MUST be a arraylist at the start!");
						
					}
					List<String> TMP = new ArrayList<>(((List<String>) params.get(KEY)));
					TMP.add(VAL);
					params.put(KEY, TMP);
					buildTMp = "";
					KEY = "";
					VAL = "";
					gotKey = false;
					READING = false;
				}

			}
			
		}
		HashMap<String, Object> newparams = new HashMap<>();
		params.forEach((k, v) -> {
			if(v instanceof String){
				newparams.put(k, v);
			}else if((v instanceof List<?> && ((List<?>) v).size() == 1)){
				newparams.put(k, ((String)((List<?>)v).get(0)));
			}else if((v instanceof List<?> && ((List<?>) v).size() > 1)){
				newparams.put(k, v);
			}else{
				System.out.println("Unassigned key: "+k);
			}
		});
		return newparams;
	}
	public boolean paramExists(String key, String name) throws ParseException {
		return getParamsOf(key).containsKey(name);
	}

	public String get(String key) throws ParseException {
		BUILDER = "";
		if (raw.indexOf(key) < 0) {
			throw new ParseException("Key not found: " + key, 0);
		}
		String cut = raw.substring((raw.indexOf(key) + key.length()), raw.indexOf(key) + key.length() + 1);
		int after_key = raw.indexOf(key) + key.length() + 2;
		if (!cut.equals("{")) {
			throw new ParseException("Expected '{" + "', but got '" + cut + "'.", 0);
		}
		String fin = "NOP";
		for (int i = after_key; i < raw.length(); i++) {

			if (raw.charAt(i) == '}' && raw.charAt(i - 1) != '\\') {

				fin = raw.substring(after_key - 1, i);
				break;
			} else if (raw.charAt(i) == '}' && raw.charAt(i - 1) == '\\') {
				StringBuilder SB = new StringBuilder(raw);
				SB.deleteCharAt(SB.indexOf("\\"));
				raw = SB.toString();
			}

		}
		if (fin.equals("NOP")) {
			throw new ParseException("Expected '}', but got to end of file.", 0);
		}
		Arrays.stream(fin.split("\\[n\\]")).forEach(i -> {
			BUILDER += i + "\n";
		});
		System.out.println("OUT--->" + BUILDER);
		return BUILDER;
	}

}