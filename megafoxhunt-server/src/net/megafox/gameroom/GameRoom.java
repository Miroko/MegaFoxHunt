package net.megafox.gameroom;
import java.util.ArrayList;
import java.util.Random;

import net.megafox.entities.Berry;
import net.megafox.entities.Chaser;
import net.megafox.entities.Empty;
import net.megafox.entities.Entity;
import net.megafox.entities.Hole;



import net.megafox.entities.Chased;
import net.megafox.game.GameMapServerSide;
import net.megafox.game.GameSimulation;
import net.megafoxhunt.server.IDHandler;
import net.megafoxhunt.server.PlayerConnection;
import net.megafoxhunt.server.RoomHandler;
import net.megafoxhunt.shared.GameMapSharedConfig;
import net.megafoxhunt.shared.KryoNetwork;
import net.megafoxhunt.shared.KryoNetwork.AddPlayer;
import net.megafoxhunt.shared.KryoNetwork.ChangeState;
import net.megafoxhunt.shared.KryoNetwork.Move;
import net.megafoxhunt.shared.KryoNetwork.RemovePlayer;
import net.megafoxhunt.shared.KryoNetwork.SetMap;
import net.megafoxhunt.shared.KryoNetwork.Winner;

public class GameRoom extends Thread {
	
	private static final int MAX_SIZE = 12;
	private static final long UPDATE_RATE_MS = 100;
	
	public static final int MATCH_LENGHT_SECONDS_DEFAULT =  (60 * 3);
	
	public static final int STATE_LOBBY = KryoNetwork.ChangeState.LOBBY;
	public static final int STATE_GAME = KryoNetwork.ChangeState.GAME;
		
	private int roomState;
	private boolean roomRunning = true;
	
	private ArrayList<PlayerConnection> playersReady;
	
	private GameMapServerSide currentMap;	
	private PlayerContainer playerContainer;
	private GameSimulation gameSimulation;		

	private RoomHandler roomHandler;
		
	public GameRoom(RoomHandler roomHandler){
		this.roomHandler = roomHandler;
		playerContainer = new PlayerContainer(MAX_SIZE);
		playersReady = new ArrayList<>();
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
					roomHandler.switchToLobby(this);
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
	/*
	 * Player is ready command from client
	 * Starts game if everyone ready
	 */
	public void playerReady(PlayerConnection player){	
		playersReady.add(player);
		
		// If same amount of players in lobby and ready
		if(playerContainer.getPlayersConcurrentSafe().size() == playersReady.size()){		
			roomHandler.startGame(this);
		}
	}
	public void setChasedsAndChasers(){
		int counter = 0;
		for (PlayerConnection player : playerContainer.getPlayersConcurrentSafe()) {
			if ((counter % 2) == 0) {
				Chased chased = new Chased(2, 13 + (counter * 1), player.getMyId());
				player.setEntity(chased);
				gameSimulation.addChased(chased);
			} else {
				Chaser chaser = new Chaser(33, 13 + (counter * 1), player.getMyId());
				player.setEntity(chaser);
				gameSimulation.addChaser(chaser);
			}
			counter++;
		}
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
		playersReady.clear();
	}
}