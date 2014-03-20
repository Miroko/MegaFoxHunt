package net.megafoxhunt.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import net.megafoxhunt.core.GameResources;
import net.megafoxhunt.core.MyGdxGame;

public class Chaser extends EntityMovable{
		
	private static final float MOVEMENT_SPEED = 6;
	
	public Chaser(int id, float x, float y) {
		super(id, x, y, MOVEMENT_SPEED, MyGdxGame.resources.DOG_ANIMATIONS);
	}

	@Override
	public void render(Batch batch){
		if (destinationDirection != DIRECTION_STOP)
			stateTime += Gdx.graphics.getDeltaTime();
		
		currentFrame = animations[animationNumber].getKeyFrame(stateTime, true);
		
		switch (destinationDirection) {
			case DIRECTION_STOP:
				break;
			case DIRECTION_DOWN:
				animationNumber = GameResources.FRONT_ANIMATION;
				break;
			case DIRECTION_UP:
				animationNumber = GameResources.BACK_ANIMATION;
				break;
			case DIRECTION_LEFT:
				animationNumber = GameResources.DEFAULT_ANIMATION;
				currentFrame = animations[animationNumber].getKeyFrame(stateTime, true);
				if(currentFrame.isFlipX() == false){
					currentFrame.flip(true, false);				
				}
				break;
			case DIRECTION_RIGHT:
				animationNumber = GameResources.DEFAULT_ANIMATION;
				currentFrame = animations[animationNumber].getKeyFrame(stateTime, true);
				if(currentFrame.isFlipX() == true){
					currentFrame.flip(true, false);
				}
				break;
		}
		if (destinationDirection == DIRECTION_LEFT || destinationDirection == DIRECTION_RIGHT){
			lastXDirection = destinationDirection;
		}
		if (animationNumber == GameResources.FRONT_ANIMATION)
			batch.draw(currentFrame, x, y, 1f, 1.51f);
		else if (animationNumber == GameResources.BACK_ANIMATION)
			batch.draw(currentFrame, x, y, 1f, 1.65f);
		else 
			batch.draw(currentFrame, x - 0.65f, y, 2.3f, 1.4f);
	}
}
