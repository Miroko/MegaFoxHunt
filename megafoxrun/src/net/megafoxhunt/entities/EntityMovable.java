package net.megafoxhunt.entities;

import java.util.concurrent.ConcurrentLinkedQueue;

import net.megafoxhunt.core.GameMapClientSide;
import net.megafoxhunt.core.GameNetwork;
import net.megafoxhunt.core.GameResources;
import net.megafoxhunt.shared.KryoNetwork.Move;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class EntityMovable extends Entity{

	public static final int DIRECTION_STOP = 0;	
	public static final int DIRECTION_UP = 1;
	public static final int DIRECTION_RIGHT = 2;
	public static final int DIRECTION_DOWN = 3;
	public static final int DIRECTION_LEFT = 4;
	
	protected int direction = DIRECTION_STOP;
	
	private float movementSpeed;
	private float baseSpeed;
	
	private boolean isMoving = false;
	
	private int destinationX;
	private int destinationY;
	protected int destinationDirection;
	
	// for flip
	protected int lastXDirection = 2;
	
	private Move newMove;

	private GameMapClientSide collisionMap;
	
	private ConcurrentLinkedQueue<Move> movementQueue;
	
	private int lastX = 0;
	private int lastY = 0;
	private int lastDirection = 0;
	
	public EntityMovable(int id, float x, float y, float movementSpeed, Animation[] animations, GameMapClientSide collisionMap) {
		super(id, x, y, animations);
		this.movementSpeed = movementSpeed;
		this.baseSpeed = movementSpeed;
		movementQueue = new ConcurrentLinkedQueue<Move>();
		this.collisionMap = collisionMap;
	}
	
	/**
	 * TODO
	 * Is giving all these parameters really required every frame?
	 */
	public void update(float delta, GameNetwork network){
		float speed = movementSpeed * delta;

		if (collisionMap == null) return;
		
		if (!isMoving) {
			setNewDestination(network);
		}
		if (isMoving) {
			moveTowardsDestination(speed);
			if (isDestinationReached()) {
				snapToTile();
				isMoving = false;
			}
		}
	}

	private void setNewDestination(GameNetwork network) {
		if (network.getLocalUser().getControlledEntity() != this) {
			if (!movementQueue.isEmpty()) {
				newMove = movementQueue.poll();
				if (newMove != null) {
					movementSpeed = baseSpeed + (movementQueue.size() * (baseSpeed / 2));
					x = newMove.x;
					y = newMove.y;
					setDirection(newMove.direction);
					newMove = null;
				}
			} else return;
		}
		
		if (direction == DIRECTION_STOP) {
			destinationDirection = DIRECTION_STOP;
		}
		
		destinationX = (int)x;
		destinationY = (int)y;

		if (direction == DIRECTION_UP) destinationY += 1;
		else if (direction == DIRECTION_RIGHT) destinationX += 1;
		else if (direction == DIRECTION_DOWN) destinationY -= 1;
		else if (direction == DIRECTION_LEFT) destinationX -= 1;
		
		if (!collisionMap.isBlocked(destinationX, destinationY)) {
			isMoving = true;
			destinationDirection = direction;
		} else {
			destinationDirection = DIRECTION_STOP;
			direction = DIRECTION_STOP;
		}
		
		if (network.getLocalUser().getControlledEntity() == this) {
			if (!((int)x == lastX && (int)y == lastY && lastDirection == direction)) {
				Move move = new Move(id, direction, (int)x, (int)y, false);
				network.getKryoClient().sendTCP(move);
				lastX = (int)x;
				lastY = (int)y;
				lastDirection = direction;
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
		if (newMove.force) {
			x = newMove.x;
			y = newMove.y;
			
			isMoving = false;
			setDirection(newMove.direction);
			newMove = null;
			movementQueue.clear();
		} else movementQueue.offer(newMove);
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public synchronized int getDirection() {
		return direction;
	}
}
