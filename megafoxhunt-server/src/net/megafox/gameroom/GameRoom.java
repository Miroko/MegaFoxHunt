package net.megafox.gameroom;

import java.util.ArrayList;
import java.util.Random;

import net.megafox.entities.Berry;
import net.megafox.entities.Chaser;
import net.megafox.entities.Empty;
import net.megafox.entities.Entity;



import net.megafox.entities.Chased;
import net.megafox.game.GameMapServerSide;
import net.megafox.game.GameSimulation;
import net.megafoxhunt.server.IDHandler;
import net.megafoxhunt.server.PlayerConnection;
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
	
	private static final int ROOM_STATE_LOBBY = KryoNetwork.ChangeState.LOBBY;
	private static final int ROOM_STATE_GAME = KryoNetwork.ChangeState.GAME;
	
	private int roomState;
	private boolean roomRunning = true;
	
	private GameMapServerSide currentMap;	
	private PlayerContainer playerContainer;
	private GameSimulation gameSimulation;
	
	private IDHandler idHandler;
	
	public GameRoom(IDHandler idHandler){
		this.roomState = ROOM_STATE_LOBBY;
		this.idHandler = idHandler;
		playerContainer = new PlayerContainer(MAX_SIZE);
	}
	public boolean canJoin(){
		if (roomState == ROOM_STATE_GAME) return false;
		
		if(playerContainer.getPlayersConcurrentSafe().size() == MAX_SIZE){
			return false;
		} else{
			return true;
		}
	}	
	public void update(float delta){		
		switch (roomState) {
			case ROOM_STATE_LOBBY:
				break;
			case ROOM_STATE_GAME:
				gameSimulation.update(delta, playerContainer);
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
	
	private void changeRoomState(int state) {
		roomState = state;
		
		ChangeState changeState = new ChangeState();
		changeState.roomState = roomState;
	
		playerContainer.sendObjectToAll(changeState);
	}
	private void generateBerries(int amount){
		Random r = new Random();
		Entity[][] collisionMap = currentMap.getCollisionMap();
		
		int x;
		int y;
		
		for (int i = 0; i < amount; i++) {
			x = r.nextInt(currentMap.getWidth());
			y = r.nextInt(currentMap.getHeight());
			if(collisionMap[x][y].getClass().equals(Empty.class)){
				Berry berry = new Berry(x, y, idHandler.getFreeID());
				gameSimulation.addBerry(berry);
				currentMap.addEntity(berry);
			} else i--;
		}
	}
	public void startGame() {
		if (roomState == ROOM_STATE_GAME) return;
		
		// SET AND SEND MAP	
		changeMap(GameMapSharedConfig.DEBUG_MAP);
		
		// INIT GAME SIMULATION
		gameSimulation = new GameSimulation(playerContainer, currentMap);
		
		// ADD CHASERS
		// TODO
		
		// ADD CHASED
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
		
		// ADD BERRIES
		generateBerries(GameMapServerSide.TOTAL_BERRIES);

		// SET STATE
		changeRoomState(ROOM_STATE_GAME);
	}
	/**
	 * Set current map and inform players about the map
	 * @param map
	 */
	private void changeMap(GameMapSharedConfig mapConfig){
		currentMap = new GameMapServerSide(mapConfig);
		playerContainer.sendObjectToAll(new SetMap(currentMap.getName()));
	}	
	/**
	 * Adds player to room and informs other players in the room about the addition
	 * @param player
	 */
	public void addPlayerToRoom(PlayerConnection player) {
		playerContainer.addPlayer(player);

		// SEND NEW PLAYER INFORMATION TO OLD PLAYERS
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
	public void removePlayer(final PlayerConnection player) {
		playerContainer.removePlayer(player);
		
		// INFORM ABOUT DELETION
		RemovePlayer removePlayer = new RemovePlayer();
		removePlayer.id = player.getMyId();
		playerContainer.sendObjectToAllExcept(player, removePlayer);
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
}