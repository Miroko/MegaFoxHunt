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
import net.megafoxhunt.shared.KryoNetwork.AddHole;
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
	
	private ArrayList<Entity> pickups;
	
	private ArrayList<Entity> holes;
	
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
	public void update(float delta, PlayerContainer players) {
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
		playerContainer.sendObjectToAll(new Move(chaser.getId(), Shared.DIRECTION_STOP, chaser.getX(), chaser.getY(), true), Visibility.BOTH);
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
	public void addHole(Hole hole) {
		holes.add(hole);
		playerContainer.sendObjectToAll(new AddHole(hole.getId(), hole.getX(), hole.getY()), hole.getVisibility());
	}
	
	public void addHoleToRemove(Hole hole) {
		removable.add(hole);
		gameMap.removeEntity(hole);
	}
	
	public void move(Entity entity, int x, int y, int direction, boolean force) {
		if (entity.move(x, y, direction, gameMap, force)) {
			playerContainer.sendObjectToAllExcept(entity.getPlayer(), new Move(entity.getId(), direction, x, y, force));	
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
			
			int entityX = entity.getX();
			int entityY = entity.getY();
			int entityLastX = entity.getLastX();
			int entityLastY = entity.getLastY();
			
			if (entity instanceof Chased) {
				for (Entity chaser : chasers) {
					if (entityX == chaser.getX() && entityY == chaser.getY() ||
						entityX == chaser.getLastX() && entityY == chaser.getLastY() ||
						entityLastX == chaser.getX() && entityLastY == chaser.getY()  ||
						entityLastX == chaser.getLastX() && entityLastY == chaser.getLastY()) {
						
						if (entity.getPlayer().isRageOn()) reSpawnChaser((Chaser) chaser);
						else removable.add(entity);
					}
				}
			} else if (entity instanceof Chaser) {
				for (Entity chased : chaseds) {
					if (entityX == chased.getX() && entityY == chased.getY() ||
						entityX == chased.getLastX() && entityY == chased.getLastY() ||
						entityLastX == chased.getX() && entityLastY == chased.getY()  ||
						entityLastX == chased.getLastX() && entityLastY == chased.getLastY()) {
						
						if (chased.getPlayer().isRageOn()) reSpawnChaser((Chaser) entity);
						else removable.add(chased);
					}
				}
			}
		} else {
			Move backMove = new Move(entity.getId(), 0, entity.getX(), entity.getY(), true);
			entity.getPlayer().sendTCP(backMove);
		}
	}
	
	private void powerupCollisionDetected(Powerup powerup, Entity collidedEntity) {	
		if(collidedEntity instanceof Chaser){
			collidedEntity.getPlayer().activateSpeed();
			
			PowerupSpeed speed = new PowerupSpeed();
			speed.id = collidedEntity.getId();
			speed.on = true;
			playerContainer.sendObjectToAll(speed);				
			
			timer.schedule(new SpeedDeactivateTask(collidedEntity.getPlayer()), Powerup.DURATION_SPEED_MS);
		}
		else if(collidedEntity instanceof Chased){	
			collidedEntity.getPlayer().activateRage();
			
			PowerupRage rage = new PowerupRage();
			rage.id = collidedEntity.getId();
			rage.on = true;
			playerContainer.sendObjectToAll(rage);			
			
			timer.schedule(new RageDeactivateTask(collidedEntity.getPlayer()), Powerup.DURATION_RAGE_MS);
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
	public void goToHole(PlayerConnection connection) {
		if (connection.getEntity() instanceof Chased) {
			connection.setGoInHole(true);
			Entity playerEntity = connection.getEntity();
			Entity targetEntity = gameMap.getEntity((int)playerEntity.getX(), (int)playerEntity.getY());
			if (targetEntity instanceof Hole) {
				holeCollisionDetected(playerEntity, (Hole)targetEntity);
			}
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
				
				move(entity, targetHole.getX(), targetHole.getY(), Shared.DIRECTION_STOP, true);
				playerContainer.sendObjectToAll(new Move(entity.getId(), Shared.DIRECTION_STOP, targetHole.getX(), targetHole.getY(), true), Visibility.BOTH);
				
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
			if (entity instanceof Hole)((Hole)entity).setHoleCooldown(false);			
		}
	}
	class RageDeactivateTask extends TimerTask{
		
		private PlayerConnection player;

		public RageDeactivateTask(PlayerConnection player){
			this.player = player;
		}	
		@Override
		public void run() {
			player.deactivateRage();
			PowerupRage rage = new PowerupRage();
			rage.id = player.getEntity().getId();
			rage.on = false;
			playerContainer.sendObjectToAll(rage);				
		}
		
	}
	class SpeedDeactivateTask extends TimerTask{
		
		private PlayerConnection player;

		public SpeedDeactivateTask(PlayerConnection player){
			this.player = player;
		}	
		@Override
		public void run() {
			player.deactivateSpeed();
			PowerupSpeed speed = new PowerupSpeed();
			speed.id = player.getEntity().getId();
			speed.on = false;
			playerContainer.sendObjectToAll(speed);				
		}
		
	}

	
}
