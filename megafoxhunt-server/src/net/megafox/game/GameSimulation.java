package net.megafox.game;

import java.awt.Point;
import java.util.ArrayList;






import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;



import net.megafox.entities.Berry;
import net.megafox.entities.Chased;
import net.megafox.entities.Chaser;
import net.megafox.entities.Entity;
import net.megafox.entities.Hole;
import net.megafox.entities.PickupItem;
import net.megafox.entities.Powerup;
import net.megafox.entities.Entity.Visibility;
import net.megafox.gameroom.PlayerContainer;
import net.megafox.items.Barricade;
import net.megafox.items.Bomb;
import net.megafoxhunt.server.IDHandler;
import net.megafoxhunt.server.PlayerConnection;
import net.megafoxhunt.shared.KryoNetwork.AddChaser;
import net.megafoxhunt.shared.KryoNetwork.AddChased;
import net.megafoxhunt.shared.KryoNetwork.AddPowerup;
import net.megafoxhunt.shared.KryoNetwork.AddBerry;
import net.megafoxhunt.shared.KryoNetwork.ChangeTilesTypes;
import net.megafoxhunt.shared.KryoNetwork.Move;
import net.megafoxhunt.shared.KryoNetwork.PowerupRage;
import net.megafoxhunt.shared.KryoNetwork.PowerupSpeed;
import net.megafoxhunt.shared.KryoNetwork.RemoveEntity;
import net.megafoxhunt.shared.KryoNetwork.AddPickupItem;


import net.megafoxhunt.shared.KryoNetwork.Winner;
import net.megafoxhunt.shared.Shared;

public class GameSimulation {

	private long endTime;	
	
	public GameMapServerSide gameMap;
	
	private ArrayList<Entity> removable;
	
	private ArrayList<Entity> chasers;
	
	private ArrayList<Entity> chaseds;
	private ArrayList<Entity> berries;
	
	private ArrayList<Entity> powerups;
	public int getPowerupsAmount(){return powerups.size();}
	
	private ArrayList<Entity> pickups;
	public int getPickupsAmount(){return pickups.size();}
	public PlayerContainer playerContainer;

	private Random random;
	
	public Timer timer;
	
	public IDHandler idHandler;
	
	public GameSimulation(PlayerContainer playerContainer, GameMapServerSide gameMap, IDHandler idHandler){
		this.gameMap = gameMap;
		this.playerContainer = playerContainer;
		this.idHandler = idHandler;
		removable = new ArrayList<>();
		chasers = new ArrayList<>();
		chaseds = new ArrayList<>();		
		berries = new ArrayList<>();
		powerups = new ArrayList<>();
		pickups = new ArrayList<>();
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
	public void update(float delta, PlayerContainer players) {
		/*
		 * HANDLE REMOVABLE
		 */
		for(Entity entity : removable){			
			if(entity instanceof Berry){
				berries.remove(entity);
			} else if(entity instanceof Powerup){
				powerups.remove(entity);
			} else if(entity instanceof PickupItem){
				pickups.remove(entity);
			} else if(entity instanceof Chased){
				chaseds.remove(entity);	
			} else if(entity instanceof Chaser){
				chasers.remove(entity);	
			}
			// INFORM ABOUT ENTITY DELETION
			RemoveEntity removeEntity = new RemoveEntity();
			removeEntity.id = entity.getId();
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
			winner.winner = "Chased";
			playerContainer.sendObjectToAll(winner);
			return true;
		}else if(chacersWon() || isTimeFull()){
			Winner winner = new Winner();
			winner.winner = "Chasers";
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
	public void reSpawnChaser(Chaser chaser){	
		Point p = gameMap.getDogSpawn(0);
		chaser.respawn(p, this);
	}
	public void addPowerup(Powerup powerup){
		powerups.add(powerup);		
		playerContainer.sendObjectToAll(new AddPowerup(powerup.getId(), powerup.getX(), powerup.getY()));	
	}
	public void addPickupItem(PickupItem item) {
		pickups.add(item);
		playerContainer.sendObjectToAll(new AddPickupItem(item.getId(), item.getX(), item.getY()));			
	}
	public void addChaser(Chaser chaser){
		chasers.add(chaser);		
		playerContainer.sendObjectToAll(new AddChaser(chaser.getId(), chaser.getX(), chaser.getY()));	
	}	
	public void addChased(Chased chased){
		chaseds.add(chased);	
		playerContainer.sendObjectToAll(new AddChased(chased.getId(), chased.getX(), chased.getY()));		
	}	
	public void addBerry(Berry berry){		
		berries.add(berry);		
		playerContainer.sendObjectToAll(new AddBerry(berry.getId(), berry.getX(), berry.getY()), berry.getVisibility());
	}
	public void addBerryToRemove(Berry berry) {
		removable.add(berry);
		gameMap.removeEntity(berry);
	}
	public void addPowerupToRemove(Powerup powerup){
		removable.add(powerup);
		gameMap.removeEntity(powerup);
	}
	public void addPickupToRemove(PickupItem item){
		removable.add(item);
		gameMap.removeEntity(item);
	}	
	public void addHoleToRemove(Hole hole) {
		removable.add(hole);
		gameMap.removeEntity(hole);
	}
	
	public void addRemovable(Entity entity) {
		removable.add(entity);
	}
	
	public void move(Entity entity, int x, int y, int direction, boolean force) {
			if (entity.move(x, y, direction, gameMap, force)) {
				playerContainer.sendObjectToAllExcept(entity.getPlayer(), new Move(entity.getId(), direction, x, y, force));
			} else {
				Move backMove = new Move(entity.getId(), 0, entity.getX(), entity.getY(), true);
				entity.getPlayer().sendTCP(backMove);
				return;
			}
			checkCollisionStaticEntities(entity, x, y);
			checkCollisionDynamicEntities(entity);
	}
	private void checkCollisionStaticEntities(Entity entity, int x, int y){
		Entity collidedEntity = gameMap.getEntity(x, y);
		
		if (entity instanceof Chased) {
			if (collidedEntity instanceof Berry)  {
				addBerryToRemove((Berry)collidedEntity);
			} else if (collidedEntity instanceof Hole) {
				holeCollisionDetected(entity, (Hole)collidedEntity);
			} 
		}
		if(collidedEntity instanceof Powerup){
			powerupCollisionDetected((Powerup) collidedEntity, entity);						
		} else if(collidedEntity instanceof PickupItem){
			pickupCollision((PickupItem) collidedEntity, entity);
		}
	}
	private void checkCollisionDynamicEntities(Entity entity){		
		// Entity positions
		int entityX = entity.getX();
		int entityY = entity.getY();
		int entityLastX = entity.getLastX();
		int entityLastY = entity.getLastY();
		
		if (entity instanceof Chased) {
			Chased chased = (Chased) entity;
			for (Entity chaserEntity : chasers) {
				Chaser chaser = (Chaser) chaserEntity;
				if(!chaser.isEaten()){
					if (entityX == chaser.getX() && entityY == chaser.getY() ||
						entityX == chaser.getLastX() && entityY == chaser.getLastY() ||
						entityLastX == chaser.getX() && entityLastY == chaser.getY()  ||
						entityLastX == chaser.getLastX() && entityLastY == chaser.getLastY()) {
					
						if (chased.getPlayer().isRageOn()){
							chaser.collisionWithChased(this);
						}
						else removable.add(chased);
					}
				}
			}
		}
		else if (entity instanceof Chaser) {
			Chaser chaser = (Chaser) entity;
			if(!chaser.isEaten()){
				for (Entity chasedEntity : chaseds) {	
					Chased chased = (Chased) chasedEntity;
					if (entityX == chased.getX() && entityY == chased.getY() ||
						entityX == chased.getLastX() && entityY == chased.getLastY() ||
						entityLastX == chased.getX() && entityLastY == chased.getY()  ||
						entityLastX == chased.getLastX() && entityLastY == chased.getLastY()) {
	
						if (chased.getPlayer().isRageOn()){
							chaser.collisionWithChased(this);
						}
						else removable.add(chased);
					}
				}
			}
		}	
	}
	private void powerupCollisionDetected(Powerup powerup, Entity collidedEntity) {	
		if(collidedEntity instanceof Chaser){
			int speedId = idHandler.getFreeID();			
			collidedEntity.getPlayer().activateSpeed(speedId);
			
			PowerupSpeed speed = new PowerupSpeed();
			speed.id = collidedEntity.getId();
			speed.on = true;
			playerContainer.sendObjectToAll(speed);				
			
			timer.schedule(new SpeedDeactivateTask(collidedEntity.getPlayer(), speedId), Powerup.DURATION_SPEED_MS);
		}
		else if(collidedEntity instanceof Chased){	
			int rageId = idHandler.getFreeID();			
			collidedEntity.getPlayer().activateRage(rageId);
			
			PowerupRage rage = new PowerupRage();
			rage.id = collidedEntity.getId();
			rage.on = true;
			playerContainer.sendObjectToAll(rage);			
			
			timer.schedule(new RageDeactivateTask(collidedEntity.getPlayer(), rageId), Powerup.DURATION_RAGE_MS);
		}
		addPowerupToRemove(powerup);		
	}
	private void pickupCollision(PickupItem item, Entity collidedEntity){
		if(collidedEntity.getPlayer().getCurrentItem() == null){
			if(collidedEntity instanceof Chaser){
				collidedEntity.getPlayer().setCurrentItem(new Bomb(this));
			}
			else if(collidedEntity instanceof Chased){	
				collidedEntity.getPlayer().setCurrentItem(new Barricade(this));
			}
			addPickupToRemove(item);	
		}
	}
	
	private void holeCollisionDetected(Entity entity, Hole hole) {
		if (!hole.isHoleCooldown()){
			Entity targetHole = null;
			while (targetHole == hole || targetHole == null) {
				ArrayList<Hole> holes = gameMap.getHoles();
				int i = random.nextInt(holes.size());
				targetHole = holes.get(i);
			}
			
			((Hole)targetHole).setHoleCooldown(true);
			hole.setHoleCooldown(true);

			move(entity, targetHole.getX(), targetHole.getY(), Shared.DIRECTION_DOWN, true);
			playerContainer.sendObjectToAll(new Move(entity.getId(), Shared.DIRECTION_DOWN, targetHole.getX(), targetHole.getY(), true), Visibility.BOTH);
			
			timer.schedule(new HoleTask((Hole)hole, (Hole)targetHole), 5000);
		}		
	}
	
	class HoleTask extends TimerTask {

		private Hole hole1;
		private Hole hole2;
		
		public HoleTask(Hole hole1, Hole hole2) {
			this.hole1 = hole1;
			this.hole2 = hole2;
			
			ChangeTilesTypes changeTileTypes = new ChangeTilesTypes();
			changeTileTypes.addTile(hole1.getX(), hole1.getY(), 67);
			changeTileTypes.addTile(hole2.getX(), hole2.getY(), 67);
			playerContainer.sendObjectToAll(changeTileTypes);
		}
		

		@Override
		public void run() {
			hole1.setHoleCooldown(false);
			hole2.setHoleCooldown(false);
			
			ChangeTilesTypes changeTileTypes = new ChangeTilesTypes();
			changeTileTypes.addTile(hole1.getX(), hole1.getY(), 66);
			changeTileTypes.addTile(hole2.getX(), hole2.getY(), 66);
			playerContainer.sendObjectToAll(changeTileTypes);
		}
	}
	class RageDeactivateTask extends TimerTask{
		
		private PlayerConnection player;	
		private int rageId;

		public RageDeactivateTask(PlayerConnection player, int rageId){
			this.player = player;
			this.rageId = rageId;
		}	
		@Override
		public void run() {
			if(player.deactivateRage(rageId)){
				PowerupRage rage = new PowerupRage();
				rage.id = player.getEntity().getId();
				rage.on = false;
				playerContainer.sendObjectToAll(rage);
			}
		}
		
	}
	class SpeedDeactivateTask extends TimerTask{
		
		private PlayerConnection player;
		private int speedId;

		public SpeedDeactivateTask(PlayerConnection player, int speedId){
			this.player = player;
			this.speedId = speedId;
		}	
		@Override
		public void run() {
			if(player.deactivateSpeed(speedId)){
				PowerupSpeed speed = new PowerupSpeed();
				speed.id = player.getEntity().getId();
				speed.on = false;
				playerContainer.sendObjectToAll(speed);
			}
		}
		
	}
}
