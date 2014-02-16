package net.megafoxhunt.core;


import net.megafoxhunt.debug.DebugConsole;
import net.megafoxhunt.screens.MenuScreen;
import com.badlogic.gdx.Game;

public class MyGdxGame extends Game {
	
	private GameNetwork network;
	public GameNetwork getNetwork(){return network;}
	
	@Override	
	public void create() {		
		
		GameTextures.init();	
		
		network = new GameNetwork(this);
		network.setUsername("TestUser");	
		network.start();			
		network.connect("localhost", 6666);
		
		this.setScreen(new MenuScreen());	
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
