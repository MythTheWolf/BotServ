package com.myththewolf.BotServ;

import java.io.File;

import javax.security.auth.login.LoginException;

import com.myththewolf.BotServ.lib.tool.Tools;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public class Driver {
	
	public static void main(String[] args) throws LoginException, InterruptedException, RateLimitedException {
		System.out.println("[BotServ]Loadings configuration..");
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
		
				JDA runner = new JDABuilder(AccountType.BOT).setToken("MzU2OTUwNzgzNTIwMTQ1NDA4.DJs3sQ.LBVl7WtpGcB95McUwVhLkE08vhM").buildBlocking();
				
				
				//JarFileLoader JJ = new JarFileLoader();
			
				
				
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
