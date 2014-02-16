package net.megafoxhunt.entities;

import net.megafoxhunt.core.GameNetwork;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class StaticObject {
	
	protected int id;
	protected float x;
	protected float y;
	
	protected Texture texture;

	public StaticObject(int id, float x, float y, Texture texture){
		this.id = id;
		this.x = x;
		this.y = y;
		this.texture = texture;
	}
	
	public abstract void update(float delta, GameNetwork network);
	
	public void render(Batch batch){
		batch.draw(texture, x, y, 1, 1);
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
}
