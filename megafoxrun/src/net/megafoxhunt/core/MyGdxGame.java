package net.megafoxhunt.core;

import net.megafoxhunt.screens.ScreenHandler;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class MyGdxGame extends Game {	
	
	public static final String IP_SERVER = "localhost";
	//public static final String IP_SERVER = "54.72.36.237";

	public static int VIRTUAL_WIDTH = 800;
	public static int VIRTUAL_HEIGHT = 600;
	
	public static GameResources resources; 	
	public static GameNetwork network;
	public static MapHandler mapHandler;
	public static ScreenHandler screenHandler;
	
	@Override	
	public void create() {
		VIRTUAL_WIDTH = Gdx.graphics.getWidth();
		VIRTUAL_HEIGHT = Gdx.graphics.getHeight();
		
		resources = new GameResources();
		resources.init();
		
		mapHandler = new MapHandler();	
		
		network = new GameNetwork();
		network.init();		
		
		screenHandler = new ScreenHandler(this);		
		screenHandler.setSceenMenu();
	}
	
	
	
	@Override
	public void resize(int width, int height) {
		VIRTUAL_WIDTH = width;
		VIRTUAL_HEIGHT = height;
		super.resize(width, height);
	}



	@Override
	public void dispose() {
		if (network.getKryoClient().isConnected()) network.getKryoClient().stop();
		resources.dispose();		
		mapHandler.dispose();
		screenHandler.dispose();		
	}
	public static void shutdown(){
		Gdx.app.log("INFO", "Shutdown");
		System.exit(0);
	}

}
