package net.megafox.entities;

import java.util.ArrayList;

import net.megafox.game.GameMapServerSide;
import net.megafoxhunt.shared.Shared;

public class Entity {
	
	public enum Visibility {
		CHASER, CHASED, BOTH;
	};
	
	private static final int COLLISION_VALUE = 1;
		
	private int id;
	public int getID(){return id;}
	
	private float x;
	public int getX(){return (int) x;}
	
	private float y;
	public int getY(){return (int) y;}
	
	private float speed;
	
	private int currentDirection = Shared.DIRECTION_STOP;
	public int getCurrentDirection(){return currentDirection;}
	
	private int destinationX;
	private int destinationY;
	
	private Visibility visibility;
	
	public Entity(int x, int y, int id, int speed, Visibility visibility){
		this.x = x;
		this.y = y;
		this.destinationX = x;
		this.destinationY = y;
		this.id = id;
		this.speed = speed;
		this.visibility = visibility;
	}
	/**
	 * Moves to coordinates if possible and sets entity direction
	 * @param x
	 * @param y
	 * @param direction
	 * @param collisionMap
	 */
	public void move(int x, int y, int direction, GameMapServerSide map){		
		if(collidesWithMap(x, y, map) == false){
			snapToGrid(x, y);			
			setNewDestination(direction, map);
		}	
	}
	private boolean collidesWithMap(int x, int y, GameMapServerSide map){
		if(x > 0 && x < map.getWidth() && y > 0 && y < map.getHeight()){
			Entity[][] collisionMap = map.getCollisionMap();
			if(collisionMap[x][y].getClass().equals(Wall.class)){
				return true;					
			} else{
				return false;
			}
		}
		return true;
	}
	private void setNewDestination(int direction, GameMapServerSide map){
		currentDirection = direction;
		if(currentDirection == Shared.DIRECTION_STOP) return;
		else{
			int newDestinationX = 0;
			int newDestinationY = 0;
			if		(currentDirection == Shared.DIRECTION_UP){
				newDestinationX = (int) x;
				newDestinationY = (int) (y + 1);
			}
			else if	(currentDirection == Shared.DIRECTION_RIGHT){
				newDestinationX = (int) (x + 1);
				newDestinationY = (int) y;
			}
			else if	(currentDirection == Shared.DIRECTION_DOWN){
				newDestinationX = (int) x;
				newDestinationY = (int) (y - 1);
			}
			else if	(currentDirection == Shared.DIRECTION_LEFT){
				newDestinationX = (int) (x - 1);
				newDestinationY = (int) y;
			}
			if(collidesWithMap(newDestinationX, newDestinationY, map) == false){
				destinationX = newDestinationX;
				destinationY = newDestinationY;
			}
			else{
				currentDirection = Shared.DIRECTION_STOP;				
			}
		}
	}
	
	private void snapToGrid(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	private void moveTowardsDirection(float delta){		
		float deltaInSeconds = delta/1000;
		if		(currentDirection == Shared.DIRECTION_STOP) return;
		else if	(currentDirection == Shared.DIRECTION_UP) 		y += speed * deltaInSeconds;
		else if (currentDirection == Shared.DIRECTION_RIGHT) 	x += speed * deltaInSeconds;
		else if (currentDirection == Shared.DIRECTION_DOWN) 	y -= speed * deltaInSeconds;
		else if (currentDirection == Shared.DIRECTION_LEFT) 	x -= speed * deltaInSeconds;	
	}
	/**
	 * @return True if destination reached
	 */
	private boolean destinationReached(){
		float distanceX = Math.abs(x - destinationX);
		float distanceY = Math.abs(y - destinationY);			
		if (distanceX <= 0.04f && distanceY <= 0.04f) {
			snapToGrid(destinationX, destinationY);
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * Updates entity position towards direction if possible
	 * @param delta
	 */
	public void update(float delta, GameMapServerSide map){	
		moveTowardsDirection(delta);
		if(destinationReached()){
			setNewDestination(currentDirection, map);
		}		
	}
	
	public Visibility getVisibility() {
		return this.visibility;
	}
}
