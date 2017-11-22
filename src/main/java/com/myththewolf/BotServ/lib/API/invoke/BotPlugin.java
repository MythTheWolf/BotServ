package com.myththewolf.BotServ.lib.API.invoke;

public interface BotPlugin {
	/**
	 * Calls when your plugin is enabled
	 * @param yourInstance - Instance of your plugin
	 * @return
	 */
	public boolean onEnable(DiscordPlugin yourInstance);

	public void onDisable();
}
