package com.myththewolf.BotServ;

import java.io.File;

import javax.security.auth.login.LoginException;

import com.myththewolf.BotServ.lib.API.event.engines.ReactionAdd;
import com.myththewolf.BotServ.lib.API.event.engines.UserChat;
import com.myththewolf.BotServ.lib.API.invoke.ServerPluginManager;
import com.myththewolf.BotServ.lib.tool.Tools;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

/**
 * This class represents all the control mechanisms to correctly start
 * everything
 * 
 * @author MythTheWolf
 *
 */
public class Driver implements Runnable {
	/**
	 * The settings.json file object
	 */
	public static File CONF;
	/**
	 * The {@link JDA Bot instance} when(if) the login is completed
	 */
	protected static JDA runner;

	/**
	 * Auto runs when the runnable is instaniated This will correctly start
	 * everything in the proper order: 1 - Create/load configuration 2 - Load/Invoke
	 * Plugins 3 - Start the bot 4 - Register system evvents 5 - Notify the system
	 * that login is done and we are ready {@link BotServ#ready(JDA)}
	 */
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
				ServerPluginManager man = new ServerPluginManager();
				man.loadDir();
				System.out.println("[BotServ]Config OK,attempting login!");
				runner = new JDABuilder(AccountType.CLIENT)
						.setToken("MjMwMTY2MjI1MTM1NTk5NjE4.DQg-Jw.pkGU4HCvk4WoTB395f2ltB9fX5c").buildBlocking();
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
