package net.megafoxhunt.core;


import net.megafoxhunt.debug.DebugConsole;
import net.megafoxhunt.entities.StaticEntity;
import net.megafoxhunt.screens.MenuScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;

public class MyGdxGame extends Game {
	
	private static MyGdxGame INSTANCE;
	public static MyGdxGame getInstance(){return INSTANCE;}
	
	@Override	
	public void create() {
		INSTANCE = this;
		initTextures();	
			
		GameNetwork.init();
		GameNetwork.setUsername("TestUser");		
		GameNetwork.connect("localhost", 6666);
		
		INSTANCE.setScreen(new MenuScreen());	
	}	
	private static void initTextures(){
		 StaticEntity.DEBUG_TEXTURE = new Texture("data/libgdx.png");
	}
	private static void disposeTextures(){
		StaticEntity.DEBUG_TEXTURE.dispose();
	}
	public static void shutdown(){
		DebugConsole.msg("Shutdown");
		System.exit(0);
	}
	@Override
	public void dispose() {
		disposeTextures();
	}
}
