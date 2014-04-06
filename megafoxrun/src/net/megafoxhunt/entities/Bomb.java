package net.megafoxhunt.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import net.megafoxhunt.core.MyGdxGame;

public class Bomb extends Entity{

	private static final float EXPLOSION_SCALE = 0.45f;
	
	public Bomb(int id, float x, float y) {
		super(id, x, y, MyGdxGame.resources.BOMB_ANIMATIONS);		
	}
	
	@Override
	public void render(Batch batch) {
		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = animations[animationNumber].getKeyFrame(stateTime, true);
		int frameIndex = (int) (stateTime / 0.125);
		
		if (frameIndex >= 13) shouldBeRemoved = true;
		if (shouldBeRemoved) return;
		
		if (frameIndex <= 8) { 
			batch.draw(currentFrame, x, y, 1, 1);	
		} else  {
			batch.draw(currentFrame, x - (float)((frameIndex - 8) * (EXPLOSION_SCALE / 2)), y - (float)((frameIndex - 8) * (EXPLOSION_SCALE / 2)), (float)((frameIndex - 8) * EXPLOSION_SCALE + 1), (float)((frameIndex - 8) * EXPLOSION_SCALE + 1));
		}
	}	
	
	@Override
	public void setShouldBeRemoved(boolean shouldBeRemoved) {
		// do not remove, it will be removed when animation is over
	}
}
