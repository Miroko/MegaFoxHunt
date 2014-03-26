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
import net.megafox.entities.Powerup;
import net.megafox.entities.Entity.Visibility;
import net.megafox.gameroom.PlayerContainer;
import net.megafox.items.Bomb;
import net.megafox.items.Item;
import net.megafoxhunt.server.PlayerConnection;
import net.megafoxhunt.shared.KryoNetwork.ActivatePowerup;
import net.megafoxhunt.shared.KryoNetwork.AddChaser;
import net.megafoxhunt.shared.KryoNetwork.AddChased;
import net.megafoxhunt.shared.KryoNetwork.AddPowerup;
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
	
	private ArrayList<Entity> powerups;
	
	private ArrayList<Entity> holes;
	
	private PlayerContainer playerContainer;

	private Random random;
	
	private Timer timer;
	
	private boolean powerupActive = false;
	
	public GameSimulation(PlayerContainer playerContainer, GameMapServerSide gameMap){
		this.gameMap = gameMap;
		this.playerContainer = playerContainer;		
		removable = new ArrayList<>();
		chasers = new ArrayList<>();
		chaseds = new ArrayList<>();
		berries = new ArrayList<>();
		powerups = new ArrayList<>();
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
			Entity collidedEntity = map[chased.getRoundedX()][chased.getRoundedY()];			

			if(collidedEntity instanceof Powerup){
				addPowerupToRemove((Powerup) collidedEntity);
				ActivatePowerup activatePowerup = new ActivatePowerup();
				playerContainer.sendObjectToAll(activatePowerup);
				
				powerupActive = true;
		
				Timer powerupTimer = new Timer();
				powerupTimer.schedule(new TimerTask() {					
					@Override
					public void run() {
						powerupActive = false;			
					}
				}, Powerup.TIME_SECONDS * 1000);						
			}
		}
		
		int chaserX = 0;
		int chaserY = 0;
		int chaserLastX = 0;
		int chaserLastY = 0;
		
		for(Entity chaser : chasers){
			chaser.update(delta, gameMap);
			chaserX = Math.round(chaser.getX() + 0.4f);
			chaserY = Math.round(chaser.getY() + 0.4f);
			chaserLastX = chaser.getLastX();
			chaserLastY = chaser.getLastY();
			
			for (Entity chased : chaseds) {
				if (chaserX == Math.round(chased.getX() + 0.4f) && chaserY == Math.round(chased.getY() + 0.4f)  ||
					chaserX == chased.getLastX() && chaserY == chased.getLastY() ||
					chaserLastX == Math.round(chased.getX() + 0.4f)  && chaserLastY == Math.round(chased.getY() + 0.4f)  ||
					chaserLastX == chased.getLastX() && chaserLastY == chased.getLastY()) {
					// Powerup active
					if(powerupActive == true){						
						reSpawnChaser((Chaser) chaser);
					}
					else{					
						removable.add(chased);
					}
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
			else if(entity instanceof Powerup){
				powerups.remove(entity);
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
	private void reSpawnChaser(Chaser chaser){		
		move(chaser, 33, 12, Shared.DIRECTION_STOP, true);
		playerContainer.sendObjectToAll(new Move(chaser.getID(), Shared.DIRECTION_STOP, chaser.getRoundedX(), chaser.getRoundedY(), true), Visibility.BOTH);
	}
	public void addPowerup(Powerup powerup){
		powerups.add(powerup);		
		playerContainer.sendObjectToAll(new AddPowerup(powerup.getID(), powerup.getRoundedX(), powerup.getRoundedY()));	
	}
	public void addChaser(Chaser chaser){
		chasers.add(chaser);		
		playerContainer.sendObjectToAll(new AddChaser(chaser.getID(), chaser.getRoundedX(), chaser.getRoundedY()));	
	}
	
	public void addChased(Chased chased){
		chaseds.add(chased);	
		playerContainer.sendObjectToAll(new AddChased(chased.getID(), chased.getRoundedX(), chased.getRoundedY()));		
	}
	
	public void addBerry(Berry berry){		
		berries.add(berry);		
		playerContainer.sendObjectToAll(new AddBerry(berry.getID(), berry.getRoundedX(), berry.getRoundedY()), berry.getVisibility());
	}
	
	public void addBerryToRemove(Berry berry) {
		removable.add(berry);
		gameMap.removeEntity(berry);
	}
	public void addPowerupToRemove(Powerup powerup){
		removable.add(powerup);
		gameMap.removeEntity(powerup);
	}
	
	public void addHole(Hole hole) {
		holes.add(hole);
		playerContainer.sendObjectToAll(new AddHole(hole.getID(), hole.getRoundedX(), hole.getRoundedY()), hole.getVisibility());
	}
	
	public void addHoleToRemove(Hole hole) {
		removable.add(hole);
		gameMap.removeEntity(hole);
	}
	
	public void move(Entity entity, int x, int y, int direction, boolean force) {
		// Move accepted, check for collisions
		if (entity.move(x, y, direction, gameMap, force)) {
			Entity e = gameMap.getEntity(x, y);
			if (e instanceof Berry) addBerryToRemove((Berry)e);
			else if (e instanceof Hole) {
				holeCollisionDetected(entity, (Hole)e);
			}
		}
	}
	
	public void goToHole(PlayerConnection connection) {
		connection.setGoInHole(true);
		Entity playerEntity = connection.getEntity();
		Entity targetEntity = gameMap.getEntity((int)playerEntity.getX(), (int)playerEntity.getY());
		if (targetEntity instanceof Hole) {
			holeCollisionDetected(playerEntity, (Hole)targetEntity);
		}
	}
	
	private void holeCollisionDetected(Entity entity, Hole hole) {
		if(entity.getPlayer().isGoingInHole()){
			if (!hole.isHoleCooldown()){
				Entity targetHole = null;
				while (targetHole == hole || targetHole == null) {
					int i = random.nextInt(holes.size());
					targetHole = holes.get(i);
				}
				
				((Hole)targetHole).setHoleCooldown(true);
				hole.setHoleCooldown(true);
				
				move(entity, targetHole.getRoundedX(), targetHole.getRoundedY(), Shared.DIRECTION_STOP, true);
				playerContainer.sendObjectToAll(new Move(entity.getID(), Shared.DIRECTION_STOP, targetHole.getRoundedX(), targetHole.getRoundedY(), true), Visibility.BOTH);
				
				timer.schedule(new TimerListener(hole), 5000);
				timer.schedule(new TimerListener(targetHole), 5000);	
			}
			entity.getPlayer().setGoInHole(false);
		}				
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
