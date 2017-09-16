
public class ServerClass {
	
	public String getCallerCallerClassName() {
		return new Exception().getStackTrace()[1].getClassName(); 
	   
	 }
}
