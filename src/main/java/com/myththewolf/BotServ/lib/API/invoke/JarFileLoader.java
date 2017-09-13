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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.json.JSONException;
import org.json.JSONObject;

import com.myththewolf.BotServ.BotServ;
import com.myththewolf.BotServ.Driver;

public class JarFileLoader {
	private File PLUGIN_DIR;
	private File CURRENT_DIR;
	private HashMap<String, Class<?>> classes = new HashMap<>();
	private HashMap<String, String> pluginMeta = new HashMap<>();
	private long order = 1;

	public JarFileLoader() throws IOException {
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
				JSONObject runconfig = new JSONObject(build);
				String MAIN = runconfig.getString("main");
				String NAME = runconfig.getString("name");

				if (MAIN == null || NAME == null || MAIN.equals("") || NAME.equals("") || MAIN.equals(" ")
						|| NAME.equals(" ")) {
					System.err.println("Error while importing " + pathToJar + ": Main class or Name is NULL");
					continue;
				} else {
					if (!(obj instanceof BotPlugin) && !c.getName().equals(MAIN)) {
						continue;
					} else if (!(obj instanceof BotPlugin) && c.getName().equals(MAIN)) {
						System.err.println("Error while importing " + pathToJar + ": Class " + c.getName()
								+ " does not implement " + BotPlugin.class.getName());
						continue;
					}
					JSONObject meta = new JSONObject();

					classes.put(NAME, c);
					meta.put("enabled", false);
					meta.put("position", order);
					meta.put("main", MAIN);
					order++;
					pluginMeta.put(NAME, meta.toString());

				}
			} catch (JSONException ex) {
				ex.printStackTrace();
				System.err.println("Error while importing " + pathToJar + ": Invalid JSON in runconfig.json");
				continue;
			}

		}
		jarFile.close();
	}

	public void enablePlugin(String name) {
		Class<?> RunnerClass = this.classes.get(name);
		try {
			Method M = RunnerClass.getMethod("onEnable", BotServ.class);
			Object OB = RunnerClass.newInstance();
			boolean result = (boolean) M.invoke(OB, Driver.main);
			if (result) {
				JSONObject ob = new JSONObject(pluginMeta.get(name));
				ob.put("enabled", true);
				pluginMeta.put(name, ob.toString());
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

	public InputStream getExternalResource(File theJar, String pathInJar) throws IOException {

		URL url = new URL("jar:file:" + theJar.getAbsolutePath() + "!/" + pathInJar);
		InputStream is = url.openStream();
		return is;
	}

}