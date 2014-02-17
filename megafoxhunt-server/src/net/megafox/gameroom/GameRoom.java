package net.megafox.gameroom;

import net.megafox.entities.Berry;


import net.megafox.entities.Chased;
import net.megafox.game.GameSimulation;
import net.megafoxhunt.server.IDHandler;
import net.megafoxhunt.server.PlayerConnection;
import net.megafoxhunt.shared.KryoNetwork;
import net.megafoxhunt.shared.KryoNetwork.AddPlayer;
import net.megafoxhunt.shared.KryoNetwork.ChangeState;
import net.megafoxhunt.shared.KryoNetwork.Move;
import net.megafoxhunt.shared.KryoNetwork.RemovePlayer;
import net.megafoxhunt.shared.KryoNetwork.SetMap;
import net.megafoxhunt.shared.GameMap;

public class GameRoom extends Thread {
	
	private static final int MAX_SIZE = 12;
	private static final long UPDATE_RATE_MS = 100;
	
	private static final int ROOM_STATE_LOBBY = KryoNetwork.ChangeState.LOBBY;
	private static final int ROOM_STATE_GAME = KryoNetwork.ChangeState.GAME;
	
	private int roomState;
	private boolean roomRunning = true;
	
	private GameMap currentMap;
	
	private PlayerContainer playerContainer;
	private GameSimulation gameSimulation;
	
	private IDHandler idHandler;
	
	public GameRoom(IDHandler idHandler){
		this.roomState = ROOM_STATE_LOBBY;
		this.idHandler = idHandler;
		playerContainer = new PlayerContainer(MAX_SIZE);
	}
	public boolean hasFreeRoom(){
		if(playerContainer.getPlayersConcurrentSafe().size() == MAX_SIZE){
			return false;
		}
		else{
			return true;
		}
	}	
	public void update(float delta){		
		switch (roomState) {
			case ROOM_STATE_LOBBY:
				break;
			case ROOM_STATE_GAME:
				gameSimulation.update(delta);
				break;
		}
	}	
	// Delta should stay near UPDATE_RATE_MS
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
	
	public void startGame() {	
		// SEND MAP	
		changeMap(GameMap.DEBUG_MAP);
		
		// INIT SIMULATION
		gameSimulation = new GameSimulation(playerContainer, currentMap);
		
		// ADD BERRIES
		gameSimulation.addBerry(new Berry(6, 5, idHandler.getFreeID()));
		gameSimulation.addBerry(new Berry(6, 15, idHandler.getFreeID()));
		gameSimulation.addBerry(new Berry(10, 5, idHandler.getFreeID()));
		
		// ADD CHASERS
		// TODO
		
		// ADD CHASED
		for (PlayerConnection player : playerContainer.getPlayersConcurrentSafe()) {
			gameSimulation.addChased(new Chased(10, 10, player.getMyId()));
		}

		// SET STATE
		changeRoomState(ROOM_STATE_GAME);
	}
	/**
	 * Set current map and inform players about the map
	 * @param map
	 */
	private void changeMap(GameMap map){
		currentMap = map;
		playerContainer.sendObjectToAll(new SetMap(map.getName()));
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
		gameSimulation.move(move);		
		playerContainer.sendObjectToAllExcept(player, move);		
	}

}