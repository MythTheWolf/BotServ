package com.myththewolf.BotServ.lib.API.invoke;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.json.JSONException;
import org.json.JSONObject;

public class ServerPluginManager {
  private File PLUGIN_DIR;
  private File CURRENT_DIR;
  private HashMap<String, Class<?>> classes = new HashMap<>();
  private static HashMap<String, BotPlugin> pluginMeta = new HashMap<>();

  public ServerPluginManager() throws IOException {
    System.out.println("[BotServ]Starting JarLoader....");
    CURRENT_DIR = new File(System.getProperty("user.dir"));
    PLUGIN_DIR = new File(System.getProperty("user.dir") + "/run/plugins/");
    if (!PLUGIN_DIR.exists()) {
      if (!CURRENT_DIR.canWrite()) {
        throw new IOException("Can't create plugin dir");
      } else {
        PLUGIN_DIR.mkdirs();
      }

    }
  }

  public void loadDir() throws ClassNotFoundException, InstantiationException,
      IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException,
      InvocationTargetException, IOException {
    File[] jars = PLUGIN_DIR.listFiles();
    for (File F : jars) {
      if (F.getName().endsWith(".jar")) {
        loadJar(F);
      }
    }
  }

  @SuppressWarnings("unused")
  public void loadJar(File theJarFile) throws IOException, ClassNotFoundException,
      InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException,
      IllegalArgumentException, InvocationTargetException {
    if (this.getExternalResource(theJarFile, "runconfig.json") == null) {
      System.err.println("[BotServ]Error while importing " + theJarFile.getAbsolutePath()
          + ": No runtime config.");
      return;
    }
    System.out.println("[BotServ]Importing " + theJarFile.getName() + " to classpath...");
    String pathToJar = theJarFile.getAbsolutePath();
    JarFile jarFile = new JarFile(pathToJar);
    Enumeration<JarEntry> e = jarFile.entries();

    URL[] urls = {new URL("jar:file:" + pathToJar + "!/")};
    URLClassLoader cl = URLClassLoader.newInstance(urls);

    while (e.hasMoreElements()) {
      JarEntry je = e.nextElement();
      if (je.isDirectory() || !je.getName().endsWith(".class")) {
        continue;
      }
      // -6 because of .class
      String className = je.getName().substring(0, je.getName().length() - 6);
      className = className.replace('/', '.');
      System.out.println("Loading class: " + className);
      classes.put(className, cl.loadClass(className));
    }
    if (this.getExternalResource(theJarFile, "runconfig.json") == null) {
      System.err.println("Error while importing: " + pathToJar + ": No valid runconfig");
      jarFile.close();
      return;
    }
    JSONObject runconfig;
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(this.getExternalResource(theJarFile, "runconfig.json")));
    String build = "";
    String line = "";

    while ((line = reader.readLine()) != null) {
      build += line;
    }
    try {
      if (empty(build)) {
        jarFile.close();
        throw new JSONException("JSON File must start with `{`");
      }
      runconfig = new JSONObject(build);
      String MAIN = runconfig.getString("main");
      String NAME = runconfig.getString("name");
      String AUTH = runconfig.getString("author");
      String DESC = runconfig.getString("description");
      String VERSION = runconfig.getString("version");
      Class<?> klass = classes.get(MAIN);
      Object runner = klass.newInstance();
      if (!(runner instanceof PluginAdapater)) {
        System.err.println("Error while importing: " + pathToJar + ": Class " + klass.getName()
            + " does not implement " + PluginAdapater.class.getName());
        jarFile.close();
        return;
      }
      classes.put(NAME, klass);

      File pDir = new File(PLUGIN_DIR.getAbsolutePath() + File.separator + NAME);
      if (!pDir.exists()) {
        pDir.mkdirs();
      }
      File manDir = new File(
          PLUGIN_DIR.getAbsolutePath() + File.separator + NAME + File.separator + "manual-pages");
      if (!manDir.exists()) {
        manDir.mkdirs();
      }

      pluginMeta.put(NAME, new ImplBotPlugin(runconfig, theJarFile, pDir));
      enablePlugin(NAME);

    } catch (JSONException ex) {
      System.err.println("Error while importing " + pathToJar + ": Invalid JSON in runconfig.json: "
          + ex.getMessage());
      ex.printStackTrace();
      jarFile.close();
      return;
    }

    jarFile.close();
  }

  protected static BotPlugin forName(String name) {
    return ServerPluginManager.pluginMeta.get(name);
  }

  public void enablePlugin(String name) {
    Thread runner = new Thread(() -> {
      Class<?> RunnerClass = this.classes.get(name);
      try {
        Method M = RunnerClass.getMethod("onEnable", BotPlugin.class);
        Object OB = RunnerClass.newInstance();
        boolean result = (boolean) M.invoke(OB, forName(name));
        if (result) {
          ((ImplBotPlugin) ServerPluginManager.pluginMeta.get(name)).setEnabled(true);
        }
      } catch (NoSuchMethodException | SecurityException e) {
        System.err.println("[BotServ]Error while enabling plugin " + name);
        e.printStackTrace();
      } catch (InstantiationException e) {
        System.err.println("[BotServ]Error while enabling plugin " + name);
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        System.err.println("[BotServ]Error while enabling plugin " + name);
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IllegalArgumentException e) {
        System.err.println("[BotServ]Error while enabling plugin " + name);
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        System.err.println("[BotServ]Error while enabling plugin " + name);
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    });
    runner.start();
  }

  public File getWorkingDir() {
    return this.PLUGIN_DIR;
  }

  public static List<BotPlugin> getPlugins() throws IllegalAccessException {
    List<BotPlugin> pls = new ArrayList<>();
    Iterator<Entry<String, BotPlugin>> it = pluginMeta.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, BotPlugin> pair = (Map.Entry<String, BotPlugin>) it.next();
      pls.add((BotPlugin) pair.getValue());

    }
    return pls;
  }

  public InputStream getExternalResource(File theJar, String pathInJar) {
    try {
      URL url = new URL("jar:file:" + theJar.getAbsolutePath() + "!/" + pathInJar);
      InputStream is = url.openStream();
      return is;
    } catch (Exception e) {
      return null;
    }
  }

  private boolean empty(String e) {
    return (e == null || e.equals("") || e.equals(" "));
  }
}
