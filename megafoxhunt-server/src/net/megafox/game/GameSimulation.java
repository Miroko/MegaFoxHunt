package net.megafox.game;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


import net.megafox.entities.Berry;
import net.megafox.entities.Chased;
import net.megafox.entities.Chaser;
import net.megafox.entities.Entity;
import net.megafox.entities.Hole;
import net.megafox.entities.Entity.Visibility;
import net.megafox.gameroom.PlayerContainer;
import net.megafox.items.Bomb;
import net.megafox.items.Item;
import net.megafoxhunt.shared.KryoNetwork.AddChaser;
import net.megafoxhunt.shared.KryoNetwork.AddChased;
import net.megafoxhunt.shared.KryoNetwork.AddBerry;
import net.megafoxhunt.shared.KryoNetwork.AddHole;
import net.megafoxhunt.shared.KryoNetwork.ChangeTilesTypes;
import net.megafoxhunt.shared.KryoNetwork.Move;
import net.megafoxhunt.shared.KryoNetwork.RemoveEntity;
import net.megafoxhunt.shared.KryoNetwork.Winner;
import net.megafoxhunt.shared.Shared;

public class GameSimulation {

	private long endTime;	
	
	private GameMapServerSide gameMap;
	
	private ArrayList<Entity> removable;
	
	private ArrayList<Entity> chasers;
	private ArrayList<Entity> chaseds;
	private ArrayList<Entity> berries;
	
	private ArrayList<Entity> holes;
	
	private PlayerContainer playerContainer;

	private Random random;
	
	private Timer timer;
	
	public GameSimulation(PlayerContainer playerContainer, GameMapServerSide gameMap){
		this.gameMap = gameMap;
		this.playerContainer = playerContainer;		
		removable = new ArrayList<>();
		chasers = new ArrayList<>();
		chaseds = new ArrayList<>();
		berries = new ArrayList<>();
		holes = new ArrayList<>();
		random = new Random();
		timer = new Timer();
	}
	/**
	 * @param matchLengh Match lenght in seconds
	 */
	public void resetTime(long matchLenght){
		endTime = System.currentTimeMillis() + (matchLenght * 1000);		
	}
	private boolean isTimeFull(){
		if(System.currentTimeMillis() > endTime){
			return true;
		}
		else{
			return false;
		}
	}	
	public void update(float delta, PlayerContainer players){		
		Entity[][] map = gameMap.getCollisionMap();

		for(Entity chased : chaseds){		
			chased.update(delta, gameMap);
			Entity collidedEntity = map[chased.getX()][chased.getY()];

			if (collidedEntity instanceof Berry) {
				addBerryToRemove((Berry)collidedEntity);
			} else if (collidedEntity instanceof Hole) {
				if(chased.getPlayer().isGoingInHole()){
					if (((Hole)collidedEntity).isHoleCooldown() == false){							
						Entity targetHole = null;
						while (targetHole == collidedEntity || targetHole == null) {
							int i = random.nextInt(holes.size());
							targetHole = holes.get(i);
						}
						
						((Hole)targetHole).setHoleCooldown(true);
						((Hole)collidedEntity).setHoleCooldown(true);
						
						move(chased, targetHole.getX(), targetHole.getY(), Shared.DIRECTION_STOP);
						playerContainer.sendObjectToAll(new Move(chased.getID(), Shared.DIRECTION_STOP, targetHole.getX(), targetHole.getY()), Visibility.BOTH);
						
						timer.schedule(new TimerListener(collidedEntity), 5000);
						timer.schedule(new TimerListener(targetHole), 5000);	
					}
					chased.getPlayer().setGoInHole(false);
				}				
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
	}
	/*
	 * Checks for winner
	 * false if no winner found
	 * 
	 * Chased -> berries eaten
	 * Chasers -> chased eaten or time full
	 * 
	 */
	public boolean findWinner(){
		if(chacedWon()){
			Winner winner = new Winner();
			winner.winner = "Chaced";
			playerContainer.sendObjectToAll(winner);
			return true;
		}else if(chacersWon() || isTimeFull()){
			Winner winner = new Winner();
			winner.winner = "Chacers";
			playerContainer.sendObjectToAll(winner);
			return true;
		}
		return false;	
	}
	private boolean chacersWon(){
		if(chaseds.isEmpty()){
			return true;
		}
		else{
			return false;
		}
	}
	private boolean chacedWon(){
		if(berries.isEmpty()){
			return true;
		}
		else{
			return false;
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
	
	public void addHole(Hole hole) {
		holes.add(hole);
		playerContainer.sendObjectToAll(new AddHole(hole.getID(), hole.getX(), hole.getY()), hole.getVisibility());
	}
	
	public void addHoleToRemove(Hole hole) {
		removable.add(hole);
		gameMap.removeEntity(hole);
	}
	
	public void move(Entity entity, int x, int y, int direction){
		entity.move(x, y, direction, gameMap);
	}
	
	class TimerListener extends TimerTask {

		private Entity entity;
		
		public TimerListener(Entity entity) {
			this.entity = entity;
		}

		@Override
		public void run() {
			if (entity instanceof Hole) {
				((Hole)entity).setHoleCooldown(false);
			}
		}
	}

	public void useItem(Item item, int x, int y) {
		if (item instanceof Bomb) {
			ChangeTilesTypes changeTilesTypes = new ChangeTilesTypes();
			
			if (gameMap.isBlocked(x - 1, y)) {
				gameMap.setEmpty(x - 1, y);
				changeTilesTypes.addTile(x - 1, y, 13);
			}
			if (gameMap.isBlocked(x + 1, y)) {
				gameMap.setEmpty(x + 1, y);
				changeTilesTypes.addTile(x + 1, y, 13);
			}
			if (gameMap.isBlocked(x, y - 1)) {
				gameMap.setEmpty(x, y - 1);
				changeTilesTypes.addTile(x, y - 1, 13);
			}
			if (gameMap.isBlocked(x, y + 1)) {
				gameMap.setEmpty(x, y + 1);
				changeTilesTypes.addTile(x, y + 1, 13);
			}
			
			if (!changeTilesTypes.getTiles().isEmpty()) {
				playerContainer.sendObjectToAll(changeTilesTypes);
			}
		}
	}
}
