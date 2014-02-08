package net.megafoxhunt.entities;

import com.badlogic.gdx.graphics.Texture;

public class Chaser extends AliveEntity{
	
	private static final Texture TEXTURE_DOG = StaticEntity.DEBUG_TEXTURE;
	
	private static final float MOVEMENT_SPEED = 5;
	
	public Chaser(int id, float x, float y) {
		super(id, x, y, MOVEMENT_SPEED, TEXTURE_DOG);
	}


}
