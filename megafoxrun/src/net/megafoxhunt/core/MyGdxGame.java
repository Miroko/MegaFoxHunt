package net.megafoxhunt.core;


import net.megafoxhunt.entities.Chaser;
import net.megafoxhunt.screens.GameScreen;

import com.badlogic.gdx.Game;


public class MyGdxGame extends Game {
		
	@Override
	public void create() {
		this.setScreen(new GameScreen(this));	
		PlayerHandler.setPlayerEntity(new Chaser("Player",0, 0));
	}

}
