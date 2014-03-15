package net.megafoxhunt.debug;

import java.util.Random;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import net.megafoxhunt.core.GameNetwork;
import net.megafoxhunt.core.MyGdxGame;
import net.megafoxhunt.entities.EntityMovable;

public class Robot extends Thread {

	private boolean running;
	
	private int waitTimeInMillis;
	
	private Random rand;
	
	private EntityMovable myEntity;
	private GameNetwork network;
	private TiledMapTileLayer map;
	
	private int previousMove = 0;
	private int counter = 0;
	
	public Robot(GameNetwork network, int waitTimeInMillis) {
		this.waitTimeInMillis = waitTimeInMillis;
		running = true;
		rand = new Random();
		this.network = network;
		
		this.start();
	}
	
	public void shutdown() {
		running = false;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {}
		
		int direction = 0;
		
		while (running) {
			if (myEntity == null) {
				myEntity = network.getLocalUser().getControlledEntity();
			}
			
			if (map == null) {
				map = (TiledMapTileLayer)MyGdxGame.mapHandler.currentMap.getCollisionLayer();
			}
			
			if (map != null) {
				while (true) {
					direction = rand.nextInt(4) + 1;
					
					if (direction == EntityMovable.DIRECTION_LEFT) {
						if (map.getCell((int)myEntity.getX() - 1, (int)myEntity.getY()).getTile().getProperties().containsKey("wall")) continue;
					} else if (direction == EntityMovable.DIRECTION_RIGHT) {
						if (map.getCell((int)myEntity.getX() + 1, (int)myEntity.getY()).getTile().getProperties().containsKey("wall")) continue;
					} else if (direction == EntityMovable.DIRECTION_UP) {
						if (map.getCell((int)myEntity.getX(), (int)myEntity.getY() + 1).getTile().getProperties().containsKey("wall")) continue;
					} else if (direction == EntityMovable.DIRECTION_DOWN) {
						if (map.getCell((int)myEntity.getX(), (int)myEntity.getY() - 1).getTile().getProperties().containsKey("wall")) continue;
					}
					
					break;
				}
			}
			
			counter++;
			if (counter < 5) {  
				if (direction == EntityMovable.DIRECTION_LEFT && previousMove == EntityMovable.DIRECTION_RIGHT) continue;
				else if (direction == EntityMovable.DIRECTION_RIGHT && previousMove == EntityMovable.DIRECTION_LEFT) continue;
				else if (direction == EntityMovable.DIRECTION_UP && previousMove == EntityMovable.DIRECTION_DOWN) continue;
				else if (direction == EntityMovable.DIRECTION_DOWN && previousMove == EntityMovable.DIRECTION_UP) continue;
			}
			
			previousMove = direction;
			counter = 0;
			
			if (myEntity != null) myEntity.setDirection(direction);
			
			try {
				Thread.sleep(waitTimeInMillis);
			} catch (InterruptedException e) {
				shutdown();
			}
		}
	}
}
