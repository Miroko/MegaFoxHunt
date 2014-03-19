package net.megafox.gameroom;
import java.util.ArrayList;

import java.util.Random;

import net.megafox.entities.Berry;
import net.megafox.entities.Chaser;
import net.megafox.entities.Empty;
import net.megafox.entities.Entity;
import net.megafox.entities.Hole;
import net.megafox.entities.Powerup;



import net.megafox.entities.Chased;
import net.megafox.game.GameMapServerSide;
import net.megafox.game.GameSimulation;
import net.megafox.items.Bomb;
import net.megafox.items.Item;
import net.megafoxhunt.server.IDHandler;
import net.megafoxhunt.server.PlayerConnection;

import net.megafoxhunt.server.ServerRooms;
import net.megafoxhunt.shared.GameMapSharedConfig;
import net.megafoxhunt.shared.KryoNetwork;
import net.megafoxhunt.shared.KryoNetwork.AddPlayer;
import net.megafoxhunt.shared.KryoNetwork.ChangeState;
import net.megafoxhunt.shared.KryoNetwork.Move;
import net.megafoxhunt.shared.KryoNetwork.RemovePlayer;
import net.megafoxhunt.shared.KryoNetwork.SetMap;

public class GameRoom extends Thread {
	
	private static final int MAX_SIZE = 12;
	private static final long UPDATE_RATE_MS = 100;
	
	public static final int MATCH_LENGHT_SECONDS_DEFAULT =  (60 * 3);
	
	public static final int STATE_LOBBY = KryoNetwork.ChangeState.LOBBY;
	public static final int STATE_GAME = KryoNetwork.ChangeState.GAME;
		
	private int roomState;
	private boolean roomRunning = true;
	
	private GameMapServerSide currentMap;	
	private PlayerContainer playerContainer;
	private GameSimulation gameSimulation;		

	private ServerRooms serverRooms;
		
	public GameRoom(ServerRooms serverRooms){
		this.serverRooms = serverRooms;
		playerContainer = new PlayerContainer(MAX_SIZE);
		switchState(STATE_LOBBY);			
	}
	public int getRoomState(){
		return roomState;
	}	
	/**
	 * Sets match end time
	 * @param MatchLenght in seconds
	 */
	public void startClock(long matchLenght){
		gameSimulation.resetTime(matchLenght);
	}
	public boolean hasRoom(){
		if(playerContainer.getPlayersConcurrentSafe().size() == MAX_SIZE){
			return false;
		} else{
			return true;
		}
	}
	public boolean isEmpty(){
		return playerContainer.getPlayersConcurrentSafe().isEmpty();
	}
	public void update(float delta){		
		switch (roomState) {
			case STATE_LOBBY:
				break;
			case STATE_GAME:
				gameSimulation.update(delta, playerContainer);
				boolean winnerFound = gameSimulation.findWinner();
				if(winnerFound){
					serverRooms.switchToLobby(this);
				}			
				break;
		}
	}	
	/**
	 * Calls update(delta)
	 * delta in ms
	 */
	public void run(){
		long time_last_update = System.currentTimeMillis();
		long time_loop_start;
		long time_sleep;		
		while(roomRunning) {
			time_loop_start = System.currentTimeMillis();
			update(time_loop_start - time_last_update);			
			time_sleep = UPDATE_RATE_MS - (System.currentTimeMillis() - time_loop_start);
			if(time_sleep > 0){
				try {
					Thread.sleep(time_sleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}	
			time_last_update = time_loop_start;
		}
	}
	
	public void switchState(int state) {
		roomState = state;
		
		ChangeState changeState = new ChangeState();
		changeState.roomState = roomState;
	
		playerContainer.sendObjectToAll(changeState);
	}
	
	public void generateBerries(int amount, IDHandler idHandler){
		Random r = new Random();
		Entity[][] collisionMap = currentMap.getCollisionMap();
		
		int x;
		int y;		
		
		int berriesAdded = 0;		
		while(berriesAdded < amount){
			x = r.nextInt(currentMap.getWidth());
			y = r.nextInt(currentMap.getHeight());
			if(collisionMap[x][y].getClass().equals(Empty.class)){
				Berry berry = new Berry(x, y, idHandler.getFreeID());
				gameSimulation.addBerry(berry);
				currentMap.addEntity(berry);	
				berriesAdded++;
			} 
		}
	}
	
	public void generateHoles(int amount, IDHandler idHandler){
		Random r = new Random();
		Entity[][] collisionMap = currentMap.getCollisionMap();
		
		int x;
		int y;		
		
		int holesAdded = 0;		
		while(holesAdded < amount){
			x = r.nextInt(currentMap.getWidth());
			y = r.nextInt(currentMap.getHeight());
			if(collisionMap[x][y].getClass().equals(Empty.class)){
				Hole hole = new Hole(x, y, idHandler.getFreeID());
				gameSimulation.addHole(hole);
				currentMap.addEntity(hole);	
				holesAdded++;
			} 
		}
	}
	public void addPowerups(int amount, IDHandler idHandler){
		Random r = new Random();
		Entity[][] collisionMap = currentMap.getCollisionMap();
		
		int x;
		int y;		
		
		int powerupsAdded = 0;		
		while(powerupsAdded < amount){
			x = r.nextInt(currentMap.getWidth());
			y = r.nextInt(currentMap.getHeight());
			if(collisionMap[x][y].getClass().equals(Empty.class)){
				Powerup powerup = new Powerup(x, y, idHandler.getFreeID());
				gameSimulation.addPowerup(powerup);
				currentMap.addEntity(powerup);	
				powerupsAdded++;
			} 
		}
	}
	public boolean allPlayersReady(){
		boolean allReady = false;
		int playersReady = 0;
		for(PlayerConnection player : playerContainer.getPlayersConcurrentSafe()){
			if(player.isReady()){
				playersReady++;
			}
		}
		if(playersReady == playerContainer.getPlayersConcurrentSafe().size()){				
			allReady = true;
		}
		return allReady;
	}
	public void setChasedsAndChasers(){	
		ArrayList<PlayerConnection> connectionsNeedingTeam = new ArrayList<>();
		ArrayList<PlayerConnection> allConnections = (ArrayList<PlayerConnection>) playerContainer.getPlayersConcurrentSafe();
		ArrayList<PlayerConnection> chasers = new ArrayList<>();
		ArrayList<PlayerConnection> chased = new ArrayList<>();
		int playersInOneTeam = connectionsNeedingTeam.size()/2;
		Random random = new Random();
		
		for(PlayerConnection connection : allConnections){
			if(connection.getPreferedTeam() != null){
			switch (connection.getPreferedTeam()) {
				case Chasers:
					chasers.add(connection);
					break;
				case Chased:
					chased.add(connection);
					break;
				}
			}
		}
	
		while(chased.size() > playersInOneTeam){
			PlayerConnection player = chased.get(random.nextInt(chased.size()));
			chased.remove(player);
			connectionsNeedingTeam.add(player);			
		}
		while(chasers.size() > playersInOneTeam){
			PlayerConnection player = chasers.get(random.nextInt(chasers.size()));
			chasers.remove(player);
			connectionsNeedingTeam.add(player);
		}	
		for (PlayerConnection player : connectionsNeedingTeam) {
			if(chased.size() > chasers.size()){
				chasers.add(player);
			}
			else{
				chased.add(player);
			}	
		}
		for (PlayerConnection player : chased) {
			int index = 0;
			setPlayerToChased(player, index);	
			index++;
		}
		for (PlayerConnection player : chasers) {			
			int index = 0;
			setPlayerToChaser(player, index);	
			index++;			
		}
	}
	private void setPlayerToChaser(PlayerConnection connection, int positionOffset){
		Chaser chaser = new Chaser(33, 12 + (positionOffset), connection.getMyId(), connection);
		connection.setEntity(chaser);
		connection.setCurrentItem(new Bomb(gameSimulation));
		gameSimulation.addChaser(chaser);
	}
	private void setPlayerToChased(PlayerConnection connection, int positionOffset){
		Chased chased = new Chased(2, 12 + (positionOffset), connection.getMyId(), connection);
		connection.setEntity(chased);
		gameSimulation.addChased(chased);
	}
	public void startSimulation() {		
		gameSimulation = new GameSimulation(playerContainer, currentMap);		
	}
	/**
	 * Set current map and inform players about the map
	 * @param map
	 */
	public void changeMap(GameMapSharedConfig mapConfig){
		currentMap = new GameMapServerSide(mapConfig);
		playerContainer.sendObjectToAll(new SetMap(currentMap.getName()));
	}	
	/**
	 * Adds player to room and informs other players in the room about the addition
	 * @param player
	 */
	public void addPlayerToRoom(PlayerConnection player) {
		playerContainer.addPlayer(player);
		player.setMyCurrentRoom(this);

		AddPlayer addPlayer = new AddPlayer();
		addPlayer.id = player.getMyId();
		addPlayer.name = player.getName();
		playerContainer.sendObjectToAllExcept(player, addPlayer);
		
		syncNewPlayer(player);
	}
	/**
	 * Informs new player about old players in room 
	 * @param player
	 */
	private void syncNewPlayer(final PlayerConnection player) {
		AddPlayer addPlayer = new AddPlayer();
		for (PlayerConnection conn : playerContainer.getPlayersConcurrentSafe()) {
			if (conn != player) {
				addPlayer.id = conn.getMyId();
				addPlayer.name = conn.getName();
				player.sendTCP(addPlayer);
			}
		}
		
		// Change new player state to current room state
		ChangeState changeState = new ChangeState();
		changeState.roomState = roomState;
		player.sendTCP(changeState);
	}	
	/**
	 * Removes player and informs other players about it
	 * @param player
	 */
	public boolean removePlayer(PlayerConnection playerConnection) {			
		RemovePlayer removePlayer = new RemovePlayer();
		removePlayer.id = playerConnection.getMyId();
		playerContainer.sendObjectToAllExcept(playerConnection, removePlayer);
		
		playerContainer.removePlayer(playerConnection);	
		
		playerConnection.setMyCurrentRoom(null);
		
		return true;
	}
	/**
	 * Moves player
	 * @param player
	 * @param move Move command received
	 */
	public void move(PlayerConnection player, Move move) {
		gameSimulation.move(player.getEntity(), move.x, move.y, move.direction);		
		playerContainer.sendObjectToAllExcept(player, move);		
	}	
	public PlayerContainer getPlayerContainer() {
		return playerContainer;
	}
	/*
	 * Removes all players from room
	 */
	public void removePlayers() {
		for(PlayerConnection playerConnection : playerContainer.getPlayersConcurrentSafe()){
			removePlayer(playerConnection);
		}
	}
	public void endMatch() {	
		gameSimulation = null;
		currentMap = null;
		for(PlayerConnection playerConnection : playerContainer.getPlayersConcurrentSafe()){
			playerConnection.resetData();
		}
	}
}