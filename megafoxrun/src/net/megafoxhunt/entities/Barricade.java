package net.megafoxhunt.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import net.megafoxhunt.core.MyGdxGame;

public class Barricade extends Entity {

	public Barricade(int id, float x, float y) {
		super(id, x, y, MyGdxGame.resources.BARRICADE_ANIMATIONS);
	}
	
	@Override
	public void render(Batch batch){
		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = animations[animationNumber].getKeyFrame(stateTime, true);
		batch.draw(currentFrame, x - 0.15f, y, 1.25f, 1.25f);
	}	
}
