package net.megafox.game;

import java.util.ArrayList;


import net.megafox.entities.Berry;
import net.megafox.entities.Chased;
import net.megafox.entities.Chaser;
import net.megafox.entities.Entity;
import net.megafox.gameroom.PlayerContainer;
import net.megafoxhunt.shared.GameMapSharedConfig;
import net.megafoxhunt.shared.KryoNetwork.AddChaser;
import net.megafoxhunt.shared.KryoNetwork.AddChased;
import net.megafoxhunt.shared.KryoNetwork.AddBerry;
import net.megafoxhunt.shared.KryoNetwork.Move;
import net.megafoxhunt.shared.KryoNetwork.RemoveEntity;

public class GameSimulation {
	
	private GameMapServerSide gameMap;
	
	private ArrayList<Entity> removable;
	
	private ArrayList<Entity> chasers;
	private ArrayList<Entity> chaseds;
	private ArrayList<Entity> berries;
	
	private PlayerContainer playerContainer;

	public GameSimulation(PlayerContainer playerContainer, GameMapServerSide gameMap){
		this.gameMap = gameMap;
		this.playerContainer = playerContainer;				
		removable = new ArrayList<>();
		chasers = new ArrayList<>();
		chaseds = new ArrayList<>();
		berries = new ArrayList<>();
	}
	
	public void update(float delta, PlayerContainer players){
		Entity[][] map = gameMap.getCollisionMap();

		for(Entity chased : chaseds){		
			chased.update(delta, gameMap);
			Entity collidedEntity = map[chased.getX()][chased.getY()];

			if (collidedEntity instanceof Berry) {
				addBerryToRemove((Berry)collidedEntity);
			}
		}
		
		for(Entity chaser : chasers){
			chaser.update(delta, gameMap);
			for (Entity chased : chaseds) {
				if (chaser.getX() == chased.getX() && chaser.getY() == chased.getY()) {
					removable.add(chased);
				}
			}
		}
		
		/*
		 * HANDLE REMOVABLE
		 */
		for(Entity entity : removable){			
			if(entity instanceof Chased){
				chaseds.remove(entity);	
			}
			else if(entity instanceof Berry){
				berries.remove(entity);
			}
			// INFORM ABOUT ENTITY DELETION
			RemoveEntity removeEntity = new RemoveEntity();
			removeEntity.id = entity.getID();
			playerContainer.sendObjectToAll(removeEntity, entity.getVisibility());
		}
		removable.clear();
		/*
		 * IS GAME OVER
		 */
		if(berries.isEmpty()){
			// chased won
			// TODO add kryonet commands
		}
		else if(chaseds.isEmpty()){
			// chaser won
		}
	}
	
	public void addChaser(Chaser chaser){
		chasers.add(chaser);		
		playerContainer.sendObjectToAll(new AddChaser(chaser.getID(), chaser.getX(), chaser.getY()));	
	}
	
	public void addChased(Chased chased){
		chaseds.add(chased);	
		playerContainer.sendObjectToAll(new AddChased(chased.getID(), chased.getX(), chased.getY()));		
	}
	
	public void addBerry(Berry berry){
		berries.add(berry);		
		playerContainer.sendObjectToAll(new AddBerry(berry.getID(), berry.getX(), berry.getY()), berry.getVisibility());
	}
	
	public void addBerryToRemove(Berry berry) {
		removable.add(berry);
		gameMap.removeEntity(berry);
	}
	
	public void move(Entity entity, int x, int y, int direction){
		entity.move(x, y, direction, gameMap);
	}
}
