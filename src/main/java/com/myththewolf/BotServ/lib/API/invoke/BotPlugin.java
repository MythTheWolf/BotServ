package com.myththewolf.BotServ.lib.API.invoke;

public interface BotPlugin {
	public boolean onEnable(DiscordPlugin yourInstance);

	public void onDisable();
}
