package net.megafoxhunt.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import net.megafoxhunt.core.GameMapClientSide;
import net.megafoxhunt.core.GameResources;
import net.megafoxhunt.core.MyGdxGame;

public class Chaser extends EntityMovable{
		
	private static final float MOVEMENT_SPEED = 5.5f;
	
	public Chaser(int id, float x, float y, GameMapClientSide gameMap) {
		super(id, x, y, MOVEMENT_SPEED, MyGdxGame.resources.DOG_ANIMATIONS, gameMap);
	}

	@Override
	public void render(Batch batch){
		if (destinationDirection != DIRECTION_STOP)
			stateTime += Gdx.graphics.getDeltaTime();
		
		currentFrame = animations[animationNumber].getKeyFrame(stateTime, true);
		
		switch (destinationDirection) {
			case DIRECTION_STOP:
				if (lastXDirection == DIRECTION_LEFT) {
					if (currentFrame.isFlipX() == true) currentFrame.flip(true, false);
				} else if (lastXDirection == DIRECTION_RIGHT) {
					if (currentFrame.isFlipX() == false) currentFrame.flip(true, false);
				}
				break;
			case DIRECTION_DOWN:
				animationNumber = GameResources.FRONT_ANIMATION;
				lastXDirection = 0;
				break;
			case DIRECTION_UP:
				animationNumber = GameResources.BACK_ANIMATION;
				lastXDirection = 0;
				break;
			case DIRECTION_LEFT:
				animationNumber = GameResources.DEFAULT_ANIMATION;
				currentFrame = animations[animationNumber].getKeyFrame(stateTime, true);
				if(currentFrame.isFlipX() == true){
					currentFrame.flip(true, false);				
				}
				break;
			case DIRECTION_RIGHT:
				animationNumber = GameResources.DEFAULT_ANIMATION;
				currentFrame = animations[animationNumber].getKeyFrame(stateTime, true);
				if(currentFrame.isFlipX() == false){
					currentFrame.flip(true, false);
				}
				break;
		}
		if (destinationDirection == DIRECTION_LEFT || destinationDirection == DIRECTION_RIGHT){
			lastXDirection = destinationDirection;
		}
		if (animationNumber == GameResources.FRONT_ANIMATION)
			batch.draw(currentFrame, x, y, 1f, 1.9f);
		else if (animationNumber == GameResources.BACK_ANIMATION)
			batch.draw(currentFrame, x, y, 1f, 1.9f);
		else 
			batch.draw(currentFrame, x - 0.65f, y, 2.3f, 1.4f);
	}

}
