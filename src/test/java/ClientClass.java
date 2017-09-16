import com.myththewolf.BotServ.lib.API.invoke.ServerPluginManager;

public class ClientClass {
	public static void main(String[] args) {
		ServerClass SC = new ServerClass();
		try {
			System.out.println(ServerPluginManager.getPlugins());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
