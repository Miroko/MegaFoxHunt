package net.megafoxhunt.entities;


import net.megafoxhunt.core.GameMap;
import net.megafoxhunt.core.GameNetwork;
import net.megafoxhunt.screens.GameScreen;
import net.megafoxhunt.server.KryoNetwork.Move;

import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Entity extends StaticObject{

	public static final int DIRECTION_STOP = 0;	
	public static final int DIRECTION_UP = 1;
	public static final int DIRECTION_RIGHT = 2;
	public static final int DIRECTION_DOWN = 3;
	public static final int DIRECTION_LEFT = 4;
	
	private int direction = DIRECTION_STOP;
	
	private float movementSpeed;
	
	private boolean isMoving = false;
	
	private int destinationX;
	private int destinationY;
	private int destinationDirection;
	
	private Move newMove;

	private TiledMapTileLayer collisionMap;
	
	public Entity(int id, float x, float y, float movementSpeed, Texture texture) {
		super(id, x, y, texture);
		this.movementSpeed = movementSpeed;
	}

	@Override
	public void update(float delta){
		float speed = movementSpeed * delta;
		collisionMap = GameMap.getCurrentMap().getCollisionLayer();
		
		if (collisionMap == null) return;
		
		if (!isMoving) {
			setNewDestination();
		}
		if (isMoving) {
			moveTowardsDestination(speed);
			if (isDestinationReached()) {
				snapToTile();
				isMoving = false;
			}
		}
		
		if (newMove != null) {
			x = newMove.x;
			y = newMove.y;
			
			isMoving = false;
			setDirection(newMove.direction);
			
			newMove = null;
		}
	}
	
	private void setNewDestination() {
		if (direction == DIRECTION_STOP) {
			if (GameNetwork.getUser().getControlledEntity() == this && destinationDirection != direction) {
				Move move = new Move(id, direction, (int)x, (int)y);
	    		GameNetwork.getClient().sendTCP(move);
			}
			destinationDirection = DIRECTION_STOP;
			return;
		}
		
		destinationX = (int)x;
		destinationY = (int)y;

		if (direction == DIRECTION_UP) destinationY += 1;
		else if (direction == DIRECTION_RIGHT) destinationX += 1;
		else if (direction == DIRECTION_DOWN) destinationY -= 1;
		else if (direction == DIRECTION_LEFT) destinationX -= 1;
		
		if (!collisionMap.getCell(destinationX, destinationY).getTile().getProperties().containsKey("wall")) {
			isMoving = true;
			if (GameNetwork.getUser().getControlledEntity() == this && destinationDirection != direction) {
				Move move = new Move(id, direction, (int)x, (int)y);
	    		GameNetwork.getClient().sendTCP(move);
			}
			destinationDirection = direction;
		} else {
			if (GameNetwork.getUser().getControlledEntity() == this && destinationDirection != DIRECTION_STOP) {
				Move move = new Move(id, direction, (int)x, (int)y);
	    		GameNetwork.getClient().sendTCP(move);
	    		destinationDirection = DIRECTION_STOP;
			}
		}
	}
	
	private void moveTowardsDestination(float speed) {
		if 		(destinationDirection == DIRECTION_UP) 		y += speed;
		else if (destinationDirection == DIRECTION_RIGHT) 	x += speed;
		else if (destinationDirection == DIRECTION_DOWN) 	y -= speed;
		else if (destinationDirection == DIRECTION_LEFT) 	x -= speed;
	}
	
	private boolean isDestinationReached() {
		if 		(destinationDirection == DIRECTION_UP && y > destinationY) return true;
		else if (destinationDirection == DIRECTION_RIGHT && x > destinationX) return true;
		else if (destinationDirection == DIRECTION_DOWN && y < destinationY) return true;
		else if (destinationDirection == DIRECTION_LEFT && x < destinationX) return true;
		
		float distanceX = Math.abs(x - destinationX);
		float distanceY = Math.abs(y - destinationY);
		
		if (distanceX <= 0.04f && distanceY <= 0.04f) {
			return true;
		}
		
		return false;
	}
	
	private void snapToTile() {
		x = destinationX;
		y = destinationY;
	}
	
	public void move(Move newMove) {
		this.newMove = newMove;
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public synchronized int getDirection() {
		return direction;
	}
}