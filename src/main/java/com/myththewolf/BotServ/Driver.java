package com.myththewolf.BotServ;

import java.io.File;

import javax.security.auth.login.LoginException;

import org.json.JSONObject;

import com.myththewolf.BotServ.lib.API.event.engines.ReactionAdd;
import com.myththewolf.BotServ.lib.API.event.engines.UserChat;
import com.myththewolf.BotServ.lib.API.invoke.ServerPluginManager;
import com.myththewolf.BotServ.lib.tool.Tools;
import com.myththewolf.BotServ.lib.tool.Utils;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class Driver implements Runnable {
	public static File CONF;
	protected static JDA runner;

	public void run() {
		System.out.println("[BotServ]Loading configuration");
		File RUN_DIR = new File("run/");
		CONF = new File("run/settings.json");
		try {
			if (!RUN_DIR.exists()) {
				System.err.println("[BotServ]No run dir, making run and config");
				RUN_DIR.mkdirs();
				Tools.ExportResource("settings.json", Driver.class, "run/settings.json");
			}

			if (!CONF.exists()) {
				System.err.println("[BotServ]No config found, generating one for you.");
				Tools.ExportResource("settings.json", Driver.class, "run/settings.json");
			}
			try {
				JSONObject run = new JSONObject(Utils.readFile(CONF));
				ServerPluginManager man = new ServerPluginManager();
				man.loadDir();
				System.out.println("[BotServ]Config OK,attempting login!");
				runner = new JDABuilder(AccountType.BOT)
						.setToken(run.getString("token")).buildBlocking();
				runner.addEventListener(new UserChat());
				runner.addEventListener(new ReactionAdd());
				System.out.println("[BotServ]System up.");
				BotServ.ready(runner);
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (LoginException e) {
				System.err.println("[BotServ]Login failed.. exiting.");

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("[BotServ]Exception in main thread:");
			e.printStackTrace();
		}

	}

}
