package net.megafoxhunt.entities;

import net.megafoxhunt.core.MyGdxGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Chased extends EntityMovable{
	
	private static final float MOVEMENT_SPEED = 5;
	
	
	public Chased(int id, float x, float y) {
		super(id, x, y, MOVEMENT_SPEED, MyGdxGame.resources.FOX_ANIMATIONS);
	}

}
