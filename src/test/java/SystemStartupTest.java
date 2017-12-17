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
		System.out.println("[UNIT TEST]Waiting for 10 seconds for login.");
		Thread.sleep(10000);
		System.out.println("[UNIT TEST]Checking if we ever connected");
		if(!BotServ.isConnected()) {
			throw new Exception("[UNIT TEST]Could not log as the bot; Test fails.");
		}else {
			System.out.println("[UNIT TEST] Login OK, test succedded!");
		}
	}
}
