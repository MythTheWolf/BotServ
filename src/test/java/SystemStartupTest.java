import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import com.myththewolf.BotServ.BotServ;
import com.myththewolf.BotServ.lib.tool.Utils;

public class SystemStartupTest {
	@Test
	public void startSystem() throws Exception {
		Thread T = new Thread(() -> {
			String TOKEN = Utils.readFile(new File("JunitTestToken.txt"));
			JSONObject config = new JSONObject();
			config.put("token", TOKEN);
			config.put("repos", new JSONArray());
			File RUN = new File("run");
			RUN.mkdirs();
			Utils.writeToFile(config.toString(), new File("run/settings.json"));
			String[] args = { "--noterm", "--nopkg" };
			BotServ.main(args);
		});
		T.start();
		System.out.println("Waiting for 5 seconds for login.");
		Thread.sleep(5000);
		System.out.println("Checking if we ever connected");
		if(!BotServ.isConnected()) {
			throw new Exception("Could not log as the bot; Test fails.");
		}
	}
}
