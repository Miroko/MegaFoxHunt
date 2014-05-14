package net.megafoxhunt.screens;

import net.megafoxhunt.core.MyGdxGame;

public class ScreenHandler {
	
	private MyGdxGame game;
	
	private LobbyScreen lobbyScreen;
	public LobbyScreen getLobby(){return lobbyScreen;}
	
	private MenuScreen menuScreen;
	private GameScreen gameScreen;
	private CreditsScreen creditsScreen;
	
	public ScreenHandler(MyGdxGame game){
		this.game = game;
		lobbyScreen = new LobbyScreen();
		menuScreen = new MenuScreen();
		gameScreen = new GameScreen();
		creditsScreen = new CreditsScreen();
	}	
	public void setScreenCredits(){
		game.setScreen(creditsScreen);
	}
	public void setScreenLobby(){		
		game.setScreen(lobbyScreen);
	}
	public void setScreenGame(){
		game.setScreen(gameScreen);
	}
	public void setSceenMenu(){
		game.setScreen(menuScreen);
	}
	public void dispose(){
		creditsScreen.dispose();
		lobbyScreen.dispose();
		menuScreen.dispose();
		gameScreen.dispose();
	}

}
