package com.myththewolf.BotServ;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import com.myththewolf.BotServ.packages.DiscordPackageManager;
import com.myththewolf.BotServ.packages.downloader.Utils;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.User;

public class BotServ {
	private static DiscordPackageManager dpm;
	private static Scanner keyboard;
	private static Thread server;
	private static boolean noTerm = false;

	public static void main(String[] args) {
		System.out.println(Arrays.toString(args));
		server = new Thread(new Driver());
		server.start();
		if (args == null || args.length <= 0) {
			noTerm = false;
		} else if (args.length > -1 && args[0].equals("--nogui")) {
			noTerm = true;
		}
	}

	protected static void ready(JDA event) throws JSONException, IOException {
		JSONObject read = new JSONObject(Utils.readFile(Driver.CONF));
		dpm = new DiscordPackageManager(read);
		if (!noTerm) {
			keyboard = new Scanner(System.in);
			scanAgain();
		}
	}

	private static void scanAgain() {
		System.out.print(">");
		
		try {
			handleInput(keyboard.nextLine().split(" "));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void handleInput(String[] handle) throws JSONException, IOException {

		if (handle[0].equals("dpm-install")) {
			System.out.println("➡Reading database..");
			if (dpm.getPackage(handle[1]) == null) {
				System.out.println("Package not found");
				scanAgain();
				return;
			} else {
				if (Arrays.asList(handle).contains("--force")) {
					System.out.println("➡Running as if new package");
					dpm.installPackage(handle[1], true);
				} else {
					dpm.installPackage(handle[1], false);
				}
			}
		} else if (handle[0].equals("search")) {
			int totalSPam = Integer.parseInt(handle[1]);
			User toSpam = Driver.runner.getUserById(handle[2]);
			for (int i = 0; i < totalSPam; i++) {
				toSpam.openPrivateChannel().complete().sendMessage(handle[3]).queue();
				;
			}
		}
		scanAgain();
	}
}
