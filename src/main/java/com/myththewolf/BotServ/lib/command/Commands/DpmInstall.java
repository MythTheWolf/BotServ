package com.myththewolf.BotServ.lib.command.Commands;

import java.util.Arrays;

import com.myththewolf.BotServ.lib.command.ConsoleCommand;
import com.myththewolf.BotServ.packages.DiscordPackageManager;

public class DpmInstall implements ConsoleCommand {
    private DiscordPackageManager dpm;

    public DpmInstall(DiscordPackageManager pk) {
        this.dpm = pk;
    }

    @Override
    public void onCommand(String[] handle) throws Exception {
        
        System.out.println("➡Reading database..");
        if (dpm.getPackage(handle[0]) == null) {
            System.out.println("Package not found");
            return;
        } else {
            if (Arrays.asList(handle).contains("--force")) {
                System.out.println("➡Running as if new package");
                dpm.installPackage(handle[0], true);
            } else {
                dpm.installPackage(handle[0], false);
            }
        }
    }

}
