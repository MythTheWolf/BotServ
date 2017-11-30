import java.io.File;
import java.text.ParseException;
import java.util.List;

import com.myththewolf.BotServ.lib.API.invoke.manualpages.InputParser;
import com.myththewolf.BotServ.lib.tool.Utils;

public class splittest {

	public static void main(String[] args) throws ParseException {
		String IN = Utils.readFile(new File("C:\\Users\\100048201\\git\\BotServ\\run\\plugins\\DJMaster\\manual-pages\\isadmin.man"));
		InputParser IP = new InputParser(IN);
		System.out.println(IP.get("@test"));
		@SuppressWarnings("unchecked")
		List<String> x = ((List<String>) IP.getParamsOf("@test").get("color"));
		x.forEach(y -> {
			System.out.println(y);
		});
		
		/*
		IP.getParamsOf("@page 1").forEach((key, val) -> {
			if(val instanceof String){
				System.out.println("Str: "+val);
			}else{
				System.out.println(key+":"+val.getClass().getName());
			}
		});
		*/
	}

}
