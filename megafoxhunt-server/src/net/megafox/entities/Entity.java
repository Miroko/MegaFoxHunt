package net.megafox.entities;

import net.megafox.game.GameMapServerSide;
import net.megafoxhunt.server.PlayerConnection;
import net.megafoxhunt.shared.Shared;

public class Entity {
	
	public enum Visibility {
		CHASER, CHASED, BOTH;
	};
	
	private PlayerConnection player;
		
	private int id;
	
	private int x;
	private int y;
	private int lastX;
	private int lastY;
	private int facingDirection;

	private Visibility visibility;
	
	public Entity(int x, int y, int id, int speed, Visibility visibility, PlayerConnection player){
		this.x = x;
		this.y = y;
		this.id = id;
		this.visibility = visibility;
		this.player = player;
		
		this.lastX = x;
		this.lastY = y;
	}
	/**
	 * Moves to coordinates if possible and sets entity direction
	 * @param x
	 * @param y
	 * @param direction
	 * @param collisionMap
	 */
	public boolean move(int x, int y, int direction, GameMapServerSide map, boolean force) {
		int targetX = x;
		int targetY = y;
		if (direction == Shared.DIRECTION_RIGHT) targetX++;
		else if (direction == Shared.DIRECTION_LEFT) targetX--;
		else if (direction == Shared.DIRECTION_UP) targetY++;
		else if (direction == Shared.DIRECTION_DOWN) targetY--;
		
		if (!force) {
			if (Math.sqrt((targetX - this.x) * (targetX - this.x) + (targetY - this.y) * (targetY - this.y)) > 3) return false;
		}
		
		if(!collidesWithMap(targetX, targetY, map)) {
			if (direction == Shared.DIRECTION_STOP) {
				lastX = targetX;
				lastY = targetY;
			} else {
				lastX = this.x;
				lastY = this.y;
			}
			this.x = targetX;
			this.y = targetY;
			
			if (direction != 0) facingDirection = direction;
			
			return true;
		}
		
		return false;
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
	
	public Visibility getVisibility() {
		return this.visibility;
	}
	public PlayerConnection getPlayer() {		
		return player;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getLastX() {
		return lastX;
	}
	
	public int getLastY() {
		return lastY;
	}
	
	public int getId() {
		return id;
	}
	
	public int getFacingDirection() {
		return facingDirection;
	}

}
