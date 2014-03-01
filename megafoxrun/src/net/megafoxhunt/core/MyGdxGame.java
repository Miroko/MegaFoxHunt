package net.megafoxhunt.core;


import net.megafoxhunt.debug.DebugConsole;
import net.megafoxhunt.screens.MenuScreen;
import com.badlogic.gdx.Game;

public class MyGdxGame extends Game {
	
	private static final String IP_SERVER = "54.72.36.237";
	
	private GameNetwork network;
	public GameNetwork getNetwork(){return network;}
	
	private GameMapClientSide gameMap;
	public GameMapClientSide getGameMap(){return gameMap;}
	public void setGameMap(GameMapClientSide gameMap){this.gameMap = gameMap;}
	
	@Override	
	public void create() {			
		GameTextures.init();	
		
		network = new GameNetwork(this);
		network.setUsername("TestUser");	
		network.start();			
	//	network.connect(IP_SERVER, 6666);
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
