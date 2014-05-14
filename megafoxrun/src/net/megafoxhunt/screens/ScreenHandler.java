package net.megafoxhunt.screens;

import net.megafoxhunt.core.MyGdxGame;

public class ScreenHandler {
	
	private MyGdxGame game;
	
	private LobbyScreen lobbyScreen;
	private MenuScreen menuScreen;
	private GameScreen gameScreen;
	
	public ScreenHandler(MyGdxGame game){
		this.game = game;
		lobbyScreen = new LobbyScreen();
		menuScreen = new MenuScreen();
		gameScreen = new GameScreen();
	}	
	public void setScreenLobby(String winner){
		lobbyScreen.lobbyUI.setWinner(winner);
		game.setScreen(lobbyScreen);
	}
	public void setScreenGame(){
		game.setScreen(gameScreen);
	}
	public void setSceenMenu(){
		game.setScreen(menuScreen);
	}
	public void dispose(){
		lobbyScreen.dispose();
		menuScreen.dispose();
		gameScreen.dispose();
	}

}
