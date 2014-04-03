package net.megafoxhunt.entities;

import net.megafoxhunt.core.MyGdxGame;

public class Barricade extends Entity {

	public Barricade(int id, float x, float y) {
		super(id, x, y, MyGdxGame.resources.BARRICADE_ANIMATIONS);
	}
}
