package net.megafoxhunt.core;


import net.megafoxhunt.debug.DebugConsole;
import net.megafoxhunt.screens.MenuScreen;
import net.megafoxhunt.shared.Shared;

import com.badlogic.gdx.Game;

public class MyGdxGame extends Game {
	
	private static MyGdxGame INSTANCE;
	public static MyGdxGame getInstance(){return INSTANCE;}
	
	@Override	
	public void create() {
		INSTANCE = this;
		GameTextures.init();	
			
		GameNetwork.init();
		GameNetwork.setUsername("TestUser");		
		GameNetwork.connect("localhost", 6666);
		
		INSTANCE.setScreen(new MenuScreen());	
	}	
	public static void shutdown(){
		DebugConsole.msg("Shutdown");
		System.exit(0);
	}
	@Override
	public void dispose() {
		GameTextures.dispose();
	}
}
