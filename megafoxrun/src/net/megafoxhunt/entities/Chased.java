package net.megafoxhunt.entities;

import net.megafoxhunt.core.GameTextures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Chased extends Entity{
	
	private static final float MOVEMENT_SPEED = 5;
	
	public Chased(int id, float x, float y) {
		super(id, x, y, MOVEMENT_SPEED, GameTextures.FOX_ANIMATIONS);
	}

	@Override
	public void render(Batch batch){
		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = animations[animationNumber].getKeyFrame(stateTime, true);
		batch.draw(currentFrame, x - 0.35f, y, 1.7f, 1.7f);
	}



}
