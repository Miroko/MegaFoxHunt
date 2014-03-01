package net.megafoxhunt.entities;

import net.megafoxhunt.core.GameTextures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Chased extends Entity{
	
	private static final float MOVEMENT_SPEED = 5;
	
	private int lastXDirection = 2;
	
	public Chased(int id, float x, float y) {
		super(id, x, y, MOVEMENT_SPEED, GameTextures.FOX_ANIMATIONS);
	}

	@Override
	public void render(Batch batch){
		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = animations[animationNumber].getKeyFrame(stateTime, true);
		
		if (lastXDirection == DIRECTION_RIGHT && currentFrame.isFlipX()) currentFrame.flip(true, false);
		
		if (lastXDirection == DIRECTION_LEFT && !currentFrame.isFlipX()) currentFrame.flip(true, false);
		
		if (destinationDirection == DIRECTION_LEFT || destinationDirection == DIRECTION_RIGHT) lastXDirection = destinationDirection;
		
		batch.draw(currentFrame, x - 0.35f, y, 1.7f, 1.7f);
	}
}
