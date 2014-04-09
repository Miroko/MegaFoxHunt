package net.megafoxhunt.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public abstract class Entity {
	
	protected int id;
	protected float x;
	protected float y;
		
	protected float stateTime = 0f;
	protected Animation[] animations;
	protected TextureRegion currentFrame;
	protected int animationNumber = 0;
	
	protected boolean shouldBeRemoved;
	
	public void setAnimation(int animationNumber){
		if(animationNumber < animations.length){
			this.animationNumber = animationNumber;
		}
	}
	public Entity(int id, float x, float y, Animation[] animations){
		this.id = id;
		this.x = x;
		this.y = y;
		this.animations = animations;
		this.shouldBeRemoved = false;
	}	
	public void render(Batch batch){
		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = animations[animationNumber].getKeyFrame(stateTime, true);
		batch.draw(currentFrame, x, y, 1, 1);
	}	
	public int getId() {
		return id;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}

	public boolean getShouldBeRemoved() {
		return shouldBeRemoved;
	}
	
	public void setShouldBeRemoved(boolean shouldBeRemoved) {
		this.shouldBeRemoved = shouldBeRemoved;
	}

}
