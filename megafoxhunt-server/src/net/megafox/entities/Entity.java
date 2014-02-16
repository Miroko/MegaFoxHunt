package net.megafox.entities;

import java.util.ArrayList;

public class Entity {
	
	private int id;
	public int getID(){return id;}
	
	private int x;
	public int getX(){return x;}
	
	private int y;
	public int getY(){return y;}
	
	public Entity(int x, int y, int id){
		this.x = x;
		this.y = y;
		this.id = id;
	}
	public void moveTo(int x, int y){
		this.x = x;
		this.y = y;
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
