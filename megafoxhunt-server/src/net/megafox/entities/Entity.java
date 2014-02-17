package net.megafox.entities;

import java.util.ArrayList;

import net.megafoxhunt.shared.Shared;

public class Entity {
		
	private int id;
	public int getID(){return id;}
	
	private float x;
	public int getX(){return (int) x;}
	
	private float y;
	public int getY(){return (int) y;}
	
	private float speed;
	
	private int direction = Shared.DIRECTION_STOP;
	
	private int destinationX;
	private int destinationY;
	
	public Entity(int x, int y, int id, int speed){
		this.x = x;
		this.y = y;
		this.destinationX = x;
		this.destinationY = y;
		this.id = id;
		this.speed = speed;
	}
	public void move(int x, int y, int direction, int[][] collisionMap){
		// TODO CHECK COLLISION
		this.x = x;
		this.y = y;
		this.direction = direction;
		
		// SET DESTINATION 
		if (direction == Shared.DIRECTION_UP) destinationY = y + 1;
		else if (direction == Shared.DIRECTION_RIGHT) destinationX = x + 1;
		else if (direction == Shared.DIRECTION_DOWN) destinationY = y - 1;
		else if (direction == Shared.DIRECTION_LEFT) destinationX = x - 1;		
	}
	public void update(float delta){		
		if(x == destinationX && y == destinationY) return;
		else{
			// MOVE TOWARDS DESTINATION
			// delta to seconds
			delta = delta/1000;
			if 		(direction == Shared.DIRECTION_UP) 		y += speed * delta;
			else if (direction == Shared.DIRECTION_RIGHT) 	x += speed * delta;
			else if (direction == Shared.DIRECTION_DOWN) 	y -= speed * delta;
			else if (direction == Shared.DIRECTION_LEFT) 	x -= speed * delta;
			
			// DESTINATION REACHED
			float distanceX = Math.abs(x - destinationX);
			float distanceY = Math.abs(y - destinationY);			
			if (distanceX <= 0.04f && distanceY <= 0.04f) {
				x = destinationX;
				y = destinationY;
			}
		}
		// TODO FIX THIS
		System.out.println(x + "," + y);
	}
	/**
	 * @param collidesWith Entities this entity collides with
	 * @return Entity that collides with this entity or null if no collision
	 */
	public Entity collides(ArrayList<Entity> collidesWith){
		for(Entity entity : collidesWith){
			if(this.x == entity.getX()){
				if(this.y == entity.getY()){
					return entity;
				}
			}
		}
		return null;
	}

}
