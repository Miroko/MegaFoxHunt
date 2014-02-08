package net.megafoxhunt.entities;

import com.badlogic.gdx.graphics.Texture;

public class Chased extends AliveEntity{
	
	private static final Texture TEXTURE_FOX = null;
	
	private static final float MOVEMENT_SPEED = 5;
	
	public Chased(int id, float x, float y) {
		super(id, x, y, MOVEMENT_SPEED, StaticEntity.DEBUG_TEXTURE);
	}





}
