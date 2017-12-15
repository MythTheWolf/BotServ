import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import com.myththewolf.BotServ.BotServ;
import com.myththewolf.BotServ.lib.tool.Utils;

public class SystemStartupTest {
	@Test
	public void startSystem() throws Exception 
	{
		String TOKEN = Utils.readFile(new File("JunitTestToken.txt"));
		JSONObject config = new JSONObject();
		config.put("token", TOKEN);
		config.put("repos", new JSONArray());
		File RUN = new File("run");
		RUN.mkdirs();
		Utils.writeToFile(config.toString(), new File("run/settings.json"));
		String[] args = { "--noterm", "--nopkg" };
		BotServ.main(args);
	}
}
