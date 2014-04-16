package net.megafoxhunt.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import net.megafoxhunt.core.GameResources;
import net.megafoxhunt.core.MyGdxGame;

public class PickupItem extends Entity{

	public static final int TIME_SECONDS = 10;
	
	boolean removing = false;
	
	public PickupItem(int id, float x, float y) {
		super(id, x, y, MyGdxGame.resources.PICKUP_ITEM_ANIMATIONS);
	}
	
	@Override
	public void render(Batch batch) {
		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = animations[animationNumber].getKeyFrame(stateTime, true);
		
		if (removing) {
			animationNumber = GameResources.FOURTH_ANIMATION;
			if (animations[animationNumber].isAnimationFinished(stateTime)) shouldBeRemoved = true;
			else batch.draw(currentFrame, x, y, 1, 1);
		} else {
			batch.draw(currentFrame, x, y, 1, 1);
		}
	}	

	@Override
	public void setShouldBeRemoved(boolean shouldBeRemoved) {
		MyGdxGame.resources.BUBBLE.play();
		stateTime = 0;
		removing = true;
	}

}
