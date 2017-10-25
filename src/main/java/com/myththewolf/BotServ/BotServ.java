package com.myththewolf.BotServ;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import com.myththewolf.BotServ.lib.command.ConsoleCommand;
import com.myththewolf.BotServ.lib.command.Commands.DpmInstall;
import com.myththewolf.BotServ.packages.DiscordPackageManager;
import com.myththewolf.BotServ.packages.downloader.Utils;

import net.dv8tion.jda.core.JDA;

public class BotServ {
    private static DiscordPackageManager dpm;
    private static Scanner keyboard;
    private static Thread server;
    private static boolean noTerm = false;
    private static HashMap<String, ConsoleCommand> commandMap = new HashMap<String, ConsoleCommand>();

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

    public void registerConsoleCommand(String cmd, ConsoleCommand runner) {
        commandMap.put(cmd, runner);
    }

    protected static void ready(JDA event) throws JSONException, IOException {
        JSONObject read = new JSONObject(Utils.readFile(Driver.CONF));
        dpm = new DiscordPackageManager(read);
        registerCommands();
        if (!noTerm) {
            keyboard = new Scanner(System.in);
            scanAgain();
        }
    }
    private static void registerSysConsoleCommand(String cmd,ConsoleCommand runner) {
        commandMap.put(cmd, runner);
    }
    private static void registerCommands() {
       registerSysConsoleCommand("dpm-install", new DpmInstall(dpm));
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

        if (commandMap.containsKey(handle[0])) {
            String[] args = Arrays.copyOfRange(handle, 1, handle.length);
            try {
                commandMap.get(handle[0]).onCommand(args);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
       scanAgain();
    }
}
