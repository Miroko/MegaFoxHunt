package net.megafoxhunt.entities;

import net.megafoxhunt.core.MyGdxGame;

public class Powerup extends Entity{

	public static final int TIME_SECONDS = 10;
	
	public Powerup(int id, float x, float y) {
		super(id, x, y, MyGdxGame.resources.POWERRUP_ANIMATIONS);
	}

}
