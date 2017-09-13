package com.myththewolf.BotServ.lib.API.invoke;

import com.myththewolf.BotServ.BotServ;

public interface BotPlugin {
	public boolean onEnable(BotServ server);

	public void onDisable();
}
