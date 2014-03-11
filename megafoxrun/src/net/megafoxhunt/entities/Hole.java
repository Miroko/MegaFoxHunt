package net.megafoxhunt.entities;

import net.megafoxhunt.core.MyGdxGame;

public class Hole extends Entity {

	public Hole(int id, float x, float y) {
		super(id, x, y, MyGdxGame.resources.HOLE_ANIMATIONS);
	}
}
