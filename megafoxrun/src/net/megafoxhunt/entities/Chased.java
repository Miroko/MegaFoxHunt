package net.megafoxhunt.entities;

import net.megafoxhunt.core.GameResources;
import net.megafoxhunt.core.MyGdxGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Chased extends EntityMovable{
	
	private static final float MOVEMENT_SPEED = 5;
	
	public Chased(int id, float x, float y) {
		super(id, x, y, MOVEMENT_SPEED, MyGdxGame.resources.FOX_ANIMATIONS);
	}

	
	@Override
	public void render(Batch batch){
		if (destinationDirection != DIRECTION_STOP)
			stateTime += Gdx.graphics.getDeltaTime();
		
		currentFrame = animations[animationNumber].getKeyFrame(stateTime, true);
		
		switch (destinationDirection) {
			case DIRECTION_STOP:
				if (lastXDirection == DIRECTION_LEFT) {
					if (currentFrame.isFlipX() == false) currentFrame.flip(true, false);
				} else if (lastXDirection == DIRECTION_RIGHT) {
					if (currentFrame.isFlipX() == true) currentFrame.flip(true, false);
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
			batch.draw(currentFrame, x + 0.05f, y, 0.9f, 1.4f);
		else if (animationNumber == GameResources.BACK_ANIMATION)
			batch.draw(currentFrame, x + 0.05f, y, 0.9f, 1.4f);
		else 
			batch.draw(currentFrame, x - 0.33f, y, 1.6f, 1.0f);
	}
}
