package com.myththewolf.BotServ;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import org.json.JSONException;
import org.json.JSONObject;
import com.myththewolf.BotServ.lib.API.invoke.ServerPluginManager;
import com.myththewolf.BotServ.lib.command.ConsoleCommand;
import com.myththewolf.BotServ.lib.command.Commands.DpmInstall;
import com.myththewolf.BotServ.lib.tool.Utils;
import com.myththewolf.BotServ.packages.DiscordPackageManager;
import net.dv8tion.jda.core.JDA;

/**
 * This class represents the main functions to start the bot framework
 * 
 * @author MythTheWolf
 *
 */
public class BotServ {
  /**
   * A instance of the PackageManager
   */
  private static DiscordPackageManager dpm;
  /**
   * True if everything loaded OK and we are currently connecetd
   */
  private static boolean isConnected = false;
  /**
   * Used for the console input
   */
  private static Scanner keyboard;
  /**
   * The thread that the server is running in
   */
  private static Thread server;
  /**
   * True if --nogui was specified in the run flags If this is true, the server will run in headless
   * mode/wont take in any console commands
   */
  private static boolean noTerm = false;
  /**
   * A Map of all the {@link ConsoleCommand registered console commands}
   */
  private static HashMap<String, ConsoleCommand> commandMap = new HashMap<String, ConsoleCommand>();
  /**
   * True if --nopkg was specified in the run flags If this is true, the server will not instaniate
   * the PackageManager
   */
  private static boolean nopkg = false;

  /**
   * The main method to start everything
   * 
   * @param args - The flags specified on the JAR run
   */
  public static void main(String[] args) {
    System.out.println(Arrays.toString(args));
    server = new Thread(new Driver());
    server.start();
    if (args == null || args.length <= 0) {
      noTerm = false;
    } else if (args.length > -1 && args[0].equals("--noterm")) {
      noTerm = true;
    } else if (Arrays.asList(args).contains("--nopkg")) {
      nopkg = true;
    }

  }

  /**
   * Notifies the server that the bot has successfully logged in and: * Starts the package manager *
   * Registers console commands * Enables the console command prompt
   * 
   * @param event - The {@link JDA Bot instance}
   * @throws JSONException - If we encountered a error while reading the settings.json
   * @throws IOException - If we can't access settings.json
   * @throws InvocationTargetException 
   * @throws IllegalArgumentException 
   * @throws SecurityException 
   * @throws NoSuchMethodException 
   * @throws IllegalAccessException 
   * @throws InstantiationException 
   * @throws ClassNotFoundException 
   */
  protected static void ready(JDA event) throws JSONException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
    ServerPluginManager man = new ServerPluginManager(event);
    man.loadDir();
    isConnected = true;
    if (!nopkg) {
      JSONObject read = new JSONObject(Utils.readFile(Driver.CONF));
      dpm = new DiscordPackageManager(read);
    }
    registerCommands();
    if (!noTerm) {
      keyboard = new Scanner(System.in);
      scanAgain();
    }
  }

  /**
   * Registers a system console command
   * 
   * @param cmd - The keyword to identify the command
   * @param runner - The {@link ConsoleCommand ConsoleCommand} object to invoke
   */

  private static void registerSysConsoleCommand(String cmd, ConsoleCommand runner) {
    commandMap.put(cmd, runner);
  }

  /**
   * Quick wrapper to register all default console commands
   */
  private static void registerCommands() {
    registerSysConsoleCommand("dpm-install", new DpmInstall(dpm));
  }

  /**
   * Re-enables the console prompt
   */
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

  /**
   * Handles the raw string given when a console command is entered This will also run
   * {@link BotServ#scanAgain()} after it is done handling.
   * 
   * @param handle - The command, split by spaces
   * @throws JSONException - If we couldn't read JSON somewhere down the line
   * @throws IOException - If a file error occurred
   */
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

  /**
   * Checks if the bot is connected and online
   * 
   * @return True if the bot is connected and the JDA instance is active
   */
  public static boolean isConnected() {
    return isConnected;
  }
}
