package net.megafoxhunt.core;
import net.megafoxhunt.screens.MenuScreen;
import net.megafoxhunt.ui.MenuUI;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MyGdxGame extends Game {	
	
	//public static final String IP_SERVER = "54.72.36.237";
	
	public static final String IP_SERVER = "localhost";
	
			
	public static final int VIRTUAL_WIDTH = 800;
	public static final int VIRTUAL_HEIGHT = 600;
	
	public static GameResources resources; 	
	public static GameNetwork network;
	public static GameMapClientSide gameMap;

	@Override	
	public void create() {	
		resources = new GameResources();
		resources.init();
		
		network = new GameNetwork(this);
		
		this.setScreen(new MenuScreen(this));	
	}	
	@Override
	public void dispose() {
		resources.dispose();		
		gameMap.dispose();
	}
	public static void shutdown(){
		Gdx.app.log("INFO", "Shutdown");
		System.exit(0);
	}

}
