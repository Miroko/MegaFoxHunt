package net.megafoxhunt.entities;

import net.megafoxhunt.core.GameTextures;

import com.badlogic.gdx.graphics.Texture;

public class Chased extends Entity{
	
	private static final Texture TEXTURE_FOX = GameTextures.DEBUG_TEXTURE;
	
	private static final float MOVEMENT_SPEED = 5;
	
	public Chased(int id, float x, float y) {
		super(id, x, y, MOVEMENT_SPEED, TEXTURE_FOX);
	}





}
