package net.megafoxhunt.core;

import net.megafoxhunt.screens.ScreenHandler;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;

public class MyGdxGame extends Game {	
	
	public static final String IP_SERVER = "54.76.186.34";
	//public static final String IP_SERVER = "54.76.186.34";
	
	public static GameResources resources; 	
	public static GameNetwork network;
	public static MapHandler mapHandler;
	public static ScreenHandler screenHandler;
	
	@Override	
	public void create() {
		//DisplayMode displayMode = Gdx.graphics.getDesktopDisplayMode();
		//Gdx.graphics.setDisplayMode(displayMode);
		
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
		Gdx.app.exit();
	}
}
