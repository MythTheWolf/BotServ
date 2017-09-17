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
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerPluginManager {
	private File PLUGIN_DIR;
	private File CURRENT_DIR;
	private HashMap<String, Class<?>> classes = new HashMap<>();
	private static HashMap<String, DiscordPlugin> pluginMeta = new HashMap<>();
	
	public ServerPluginManager() throws IOException {
		System.out.println("[BotServ]Starting JarLoader....");
		CURRENT_DIR = new File(System.getProperty("user.dir"));
		PLUGIN_DIR = new File(System.getProperty("user.dir") + "plugins/");
		if (!PLUGIN_DIR.exists()) {
			if (!CURRENT_DIR.canWrite()) {
				throw new IOException("Can't create plugin dir");
			} else {
				PLUGIN_DIR.mkdirs();
			}

		}
	}

	public void loadDir() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, IOException {
		File[] jars = PLUGIN_DIR.listFiles();
		for (File F : jars) {
			if (F.getName().endsWith(".jar")) {
				loadJar(F);
			}
		}
	}

	public void loadJar(File theJarFile)
			throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException,
			NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		System.out.println("[BotServ]Importing " + theJarFile.getName() + " to classpath...");
		String pathToJar = theJarFile.getAbsolutePath();
		JarFile jarFile = new JarFile(pathToJar);
		Enumeration<JarEntry> e = jarFile.entries();

		URL[] urls = { new URL("jar:file:" + pathToJar + "!/") };
		URLClassLoader cl = URLClassLoader.newInstance(urls);
		Class<?> c;
		while (e.hasMoreElements()) {
			JarEntry je = e.nextElement();
			if (je.isDirectory() || !je.getName().endsWith(".class")) {
				continue;
			}
			// -6 because of .class
			String className = je.getName().substring(0, je.getName().length() - 6);
			className = className.replace('/', '.');
			c = cl.loadClass(className);
			Object obj = c.newInstance();
			JSONObject runconfig;
			if (this.getExternalResource(theJarFile, "runconfig.json") == null) {
				System.err.println("[BotServ]Error while importing " + pathToJar + ": No runtime config.");
				continue;
			}
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(this.getExternalResource(theJarFile, "runconfig.json")));
			String build = "";
			String line = "";
			while ((line = reader.readLine()) != null) {
				build += line;
			}
			try {
				if (build.equals("")) {
					throw new JSONException("");
				}
				runconfig = new JSONObject(build);
				String MAIN = runconfig.getString("main");
				String NAME = runconfig.getString("name");
				String AUTH = runconfig.getString("author");
				String DESC = runconfig.getString("description");
				String SHORTDESC = runconfig.getString("shortDescription");
				String WEBSITE = runconfig.getString("projectURL");
				String VERSION = runconfig.getString("version");
			
				if (empty(MAIN) || empty(NAME) || empty(AUTH) || empty(DESC) || empty(SHORTDESC) || empty(WEBSITE) || empty(VERSION)) {
					System.err.println("Error while importing " + pathToJar + ": Key in runconfig.json is empty or null");
					continue;
				} else {
					if (!(obj instanceof BotPlugin) && !c.getName().equals(MAIN)) {
						continue;
					} else if (!(obj instanceof BotPlugin) && c.getName().equals(MAIN)) {
						System.err.println("Error while importing " + pathToJar + ": Class " + c.getName()
								+ " does not implement " + BotPlugin.class.getName());
						continue;
					}
					
					runconfig.put("ENABLED", false);
					classes.put(NAME, c);
				
					pluginMeta.put(NAME, new DiscordPlugin(runconfig));

				}
			} catch (JSONException ex) {
				ex.printStackTrace();
				System.err.println("Error while importing " + pathToJar + ": Invalid JSON in runconfig.json");
				continue;
			}

		}
		jarFile.close();
	}
	protected static DiscordPlugin forName(String name) {
		return ServerPluginManager.pluginMeta.get(name);
	}
	public void enablePlugin(String name) {
		Class<?> RunnerClass = this.classes.get(name);
		try {
			Method M = RunnerClass.getMethod("onEnable", DiscordPlugin.class);
			Object OB = RunnerClass.newInstance();
			boolean result = (boolean) M.invoke(OB, forName(name));
			if (result) {
				ServerPluginManager.pluginMeta.get(name).setEnabled(true);
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
	}

	public File getWorkingDir() {
		return this.PLUGIN_DIR;
	}
	public static List<DiscordPlugin> getPlugins() throws IllegalAccessException {
		List<DiscordPlugin> pls = new ArrayList<>();
		Iterator it = pluginMeta.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        pls.add((DiscordPlugin) pair.getValue());
	        
	    }
		return pls;
	}
	public InputStream getExternalResource(File theJar, String pathInJar) throws IOException {

		URL url = new URL("jar:file:" + theJar.getAbsolutePath() + "!/" + pathInJar);
		InputStream is = url.openStream();
		return is;
	}
	private boolean empty(String e) {
		return (e == null || e.equals("") || e.equals(" "));
	}
}