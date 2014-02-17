package net.megafox.game;

import java.util.ArrayList;

import net.megafox.entities.Berry;
import net.megafox.entities.Chased;
import net.megafox.entities.Chaser;
import net.megafox.entities.Entity;
import net.megafox.gameroom.PlayerContainer;
import net.megafoxhunt.shared.GameMap;
import net.megafoxhunt.shared.KryoNetwork.AddChaser;
import net.megafoxhunt.shared.KryoNetwork.AddChased;
import net.megafoxhunt.shared.KryoNetwork.AddBerry;
import net.megafoxhunt.shared.KryoNetwork.Move;
import net.megafoxhunt.shared.KryoNetwork.RemoveEntity;

public class GameSimulation {
	
	private int[][] collisionMap;
	
	private ArrayList<Entity> removable;
	
	private ArrayList<Entity> chasers;
	private ArrayList<Entity> chaseds;
	private ArrayList<Entity> berries;
	
	private PlayerContainer playerContainer;

	public GameSimulation(PlayerContainer playerContainer, GameMap map){
		this.playerContainer = playerContainer;		
		
		int[][] collisionMap = new int[map.getWidth()][map.getHeight()];		
		// TODO load map collision data from path
		this.collisionMap = collisionMap;	
		
		removable = new ArrayList<>();
		chasers = new ArrayList<>();
		chaseds = new ArrayList<>();
		berries = new ArrayList<>();
	}
	public void update(float delta){
		/*
		 * UPDATE AND CHECK COLLISION
		 */
		for(Entity chaser : chasers){			
			chaser.update(delta);
			Entity chaced = chaser.collides((ArrayList<Entity>) chaseds.clone());
			if(chaced != null){
				removable.add(chaced);
			}
		}
		for(Entity chased : chaseds){		
			chased.update(delta);
			Entity berry = chased.collides((ArrayList<Entity>) berries.clone());
			if(berry != null){
				removable.add(berry);
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
			playerContainer.sendObjectToAll(removeEntity);
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
		playerContainer.sendObjectToAll(new AddBerry(berry.getID(), berry.getX(), berry.getY()));
	}
	public void move(Move move){
		for(Entity chaser : chasers){
			if(chaser.getID() == move.id){
				chaser.move(move.x, move.y, move.direction, collisionMap);
				return;
			}
		}
		for(Entity chased : chaseds){
			if(chased.getID() == move.id){
				chased.move(move.x, move.y, move.direction, collisionMap);
				return;
			}
		}
	}
	
}
