package net.megafoxhunt.entities;

import net.megafoxhunt.core.GameMapClientSide;
import net.megafoxhunt.core.GameResources;
import net.megafoxhunt.core.MyGdxGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Chased extends EntityMovable{
	
	private static final float MOVEMENT_SPEED = 5;
	
	public Chased(int id, float x, float y, GameMapClientSide gameMap) {
		super(id, x, y, MOVEMENT_SPEED, MyGdxGame.resources.FOX_ANIMATIONS, gameMap);
		rageMode = false;
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
				if (rageMode) animationNumber = GameResources.FIFTH_ANIMATIN;
				else animationNumber = GameResources.FRONT_ANIMATION;
				
				lastXDirection = 0;
				break;
			case DIRECTION_UP:
				if (rageMode) animationNumber = GameResources.SIXTH_ANIMATION;
				else animationNumber = GameResources.BACK_ANIMATION;
				
				lastXDirection = 0;
				break;
			case DIRECTION_LEFT:
				if (rageMode) animationNumber = GameResources.FOURTH_ANIMATION;
				else animationNumber = GameResources.DEFAULT_ANIMATION;
				
				currentFrame = animations[animationNumber].getKeyFrame(stateTime, true);
				if(currentFrame.isFlipX() == false){
					currentFrame.flip(true, false);				
				}
				break;
			case DIRECTION_RIGHT:
				if (rageMode) animationNumber = GameResources.FOURTH_ANIMATION;
				else animationNumber = GameResources.DEFAULT_ANIMATION;
				
				currentFrame = animations[animationNumber].getKeyFrame(stateTime, true);
				if(currentFrame.isFlipX() == true){
					currentFrame.flip(true, false);
				}
				break;
		}
		if (destinationDirection == DIRECTION_LEFT || destinationDirection == DIRECTION_RIGHT){
			lastXDirection = destinationDirection;
		}
		if (animationNumber == GameResources.FRONT_ANIMATION || animationNumber == GameResources.FIFTH_ANIMATIN)
			batch.draw(currentFrame, x + 0.05f, y, 0.9f, 1.4f);
		else if (animationNumber == GameResources.BACK_ANIMATION || animationNumber == GameResources.SIXTH_ANIMATION)
			batch.draw(currentFrame, x + 0.05f, y, 0.9f, 1.4f);
		else 
			batch.draw(currentFrame, x - 0.33f, y, 1.6f, 1.0f);
	}
	
	@Override
	protected boolean isBlocked(int destX, int destY) {
		if (direction == DIRECTION_UP && collisionMap.isHole((int)x, (int)y)) return true;
		
		if (collisionMap.isHole(destX, destY) && direction == DIRECTION_UP) return false;
		
		if (collisionMap.isBlocked(destX, destY)) return true;
		
		return false;
	}
}
