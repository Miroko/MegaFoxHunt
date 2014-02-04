package net.megafoxhunt.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StaticEntity {
	
	protected int id;
	protected float x;
	protected float y;
	
	protected Texture texture;

	public StaticEntity(int id, float x, float y){
		this.id = id;
		this.x = x;
		this.y = y;
	}
	
	public void update(float delta) {}
	
	public void render(Batch batch){
		
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
