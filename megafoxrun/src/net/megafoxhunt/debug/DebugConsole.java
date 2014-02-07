package net.megafoxhunt.debug;

public class DebugConsole {
	
	private static boolean STATE = true;
	public static void ON(){STATE = true;}
	public static void OFF(){STATE = false;}
	
	public static boolean msg(String msg){
		if(STATE == true){
			System.out.println(msg);
			return true;
		}
		else{
			return false;
		}
	}
}
