package net.megafoxhunt.entities;

import net.megafoxhunt.core.MyGdxGame;

public class Bomb extends Entity{

	public Bomb(int id, float x, float y) {
		super(id, x, y, MyGdxGame.resources.BOMB_ANIMATIONS);		
	}

}
