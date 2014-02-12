package net.megafoxhunt.entities;

import net.megafoxhunt.core.GameTextures;

import com.badlogic.gdx.graphics.Texture;

public class Chaser extends Entity{
	
	private static final Texture TEXTURE_DOG = GameTextures.DEBUG_TEXTURE;
	
	private static final float MOVEMENT_SPEED = 5;
	
	public Chaser(int id, float x, float y) {
		super(id, x, y, MOVEMENT_SPEED, TEXTURE_DOG);
	}


}
