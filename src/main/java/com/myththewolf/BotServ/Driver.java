package com.myththewolf.BotServ;

import java.io.File;

import com.myththewolf.BotServ.lib.API.event.engines.UserChat;
import com.myththewolf.BotServ.lib.API.invoke.ServerPluginManager;
import com.myththewolf.BotServ.lib.tool.Tools;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class Driver implements Runnable{
	
	public void run() {
		System.out.println("[BotServ]Loading configuration");
		File RUN_DIR = new File("run/");
		File CONF = new File("run/settings.json");
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
				System.out.println("[BotServ]Config OK,attempting login!");
				JDA runner = new JDABuilder(AccountType.CLIENT).setToken("MjMwMTY2MjI1MTM1NTk5NjE4.DJR0fA.H1inhTyIOB5Hcym7zZR9vUHXVPg").buildBlocking();
				runner.addEventListener(new UserChat());
				ServerPluginManager man = new ServerPluginManager();
				man.loadJar(new File("myJar.jar"));
				man.enablePlugin("MyCoolPlugin");
				System.out.println("[BotServ]System up.");
				BotServ.ready(runner);
				
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("[BotServ]Could not write file(s) to disk. Shutting down.");
			System.exit(0);
		}

	}

}
