package net.megafoxhunt.core;

import net.megafoxhunt.screens.ScreenHandler;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class MyGdxGame extends Game {	
<<<<<<< HEAD

	
	//public static final String IP_SERVER = "54.72.36.237";
	public static final String IP_SERVER = "54.72.36.237";

=======
	
	//public static final String IP_SERVER = "54.72.36.237";
	public static final String IP_SERVER = "54.72.36.237";
			
>>>>>>> master
	public static final int VIRTUAL_WIDTH = 800;
	public static final int VIRTUAL_HEIGHT = 600;
	
	public static GameResources resources; 	
	public static GameNetwork network;
	public static MapHandler mapHandler;
	public static ScreenHandler screenHandler;

	@Override	
	public void create() {	
		resources = new GameResources();
		resources.init();
		
		mapHandler = new MapHandler();	
		
		network = new GameNetwork();
		network.init();		
		
		screenHandler = new ScreenHandler(this);		
		screenHandler.setSceenMenu();		
	}	
	@Override
	public void dispose() {
		resources.dispose();		
		mapHandler.dispose();
		screenHandler.dispose();
	}
	public static void shutdown(){
		Gdx.app.log("INFO", "Shutdown");
		System.exit(0);
	}

}
