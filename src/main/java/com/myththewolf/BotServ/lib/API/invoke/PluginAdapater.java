package com.myththewolf.BotServ.lib.API.invoke;

public interface PluginAdapater {
  public void onEnable();

  public void onDisable();

  public void reload();
  
  default BotPlugin getPlugin() {
    return ((BotPlugin) this);
  }
}
