package net.megafoxhunt.entities;

import net.megafoxhunt.screens.GameScreen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AliveEntity extends StaticEntity{

	public static final int DIRECTION_STOP = 0;	
	public static final int DIRECTION_UP = 1;
	public static final int DIRECTION_RIGHT = 2;
	public static final int DIRECTION_DOWN = 3;
	public static final int DIRECTION_LEFT = 4;
	
	private int direction = DIRECTION_STOP;
	
	private float movementSpeed;
	
	public AliveEntity(int id, float x, float y, float movementSpeed, Texture texture) {
		super(id, x, y, texture);
		this.movementSpeed = movementSpeed;
	}

	@Override
	public void update(float delta){
		float speed = movementSpeed * delta;		
		switch (direction) {
			case DIRECTION_UP:
				y += speed;
				break;
			case DIRECTION_RIGHT:
				x += speed;
				break;
			case DIRECTION_DOWN:
				y -= speed;
				break;
			case DIRECTION_LEFT:
				x -= speed;
				break;
		}
	}
	
	
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public int getDirection() {
		return direction;
	}
}
