package net.megafoxhunt.server;

import java.util.ArrayList;

import net.megafoxhunt.shared.KryoNetwork;
import net.megafoxhunt.shared.KryoNetwork.AddChaser;
import net.megafoxhunt.shared.KryoNetwork.AddPlayer;
import net.megafoxhunt.shared.KryoNetwork.ChangeState;
import net.megafoxhunt.shared.KryoNetwork.Move;
import net.megafoxhunt.shared.KryoNetwork.RemovePlayer;
import net.megafoxhunt.shared.KryoNetwork.SetMap;
import net.megafoxhunt.shared.Shared;

public class GameRoom extends Thread {
	
	private static final int MAX_SIZE = 12;
	private static final long UPDATE_RATE_MS = 100;
	
	private static final int ROOM_STATE_LOBBY = KryoNetwork.ChangeState.LOBBY;
	private static final int ROOM_STATE_GAME = KryoNetwork.ChangeState.GAME;
	
	private int roomState;
	
	private PlayerContainer playerContainer;

	private boolean roomRunning = true;
	
	public GameRoom(){
		this.roomState = ROOM_STATE_LOBBY;
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
	public void update(double delta){
		// TODO
		/*
		ArrayList<PlayerConnection> players = playerContainer.getPlayersConcurrentSafe();
		
		switch (roomState) {
			case ROOM_STATE_LOBBY:
				break;
			case ROOM_STATE_GAME:
				break;
		}
		*/
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
	
	public void changeRoomState(int state) {
		roomState = state;
		
		ChangeState changeState = new ChangeState();
		changeState.roomState = roomState;
	
		playerContainer.sendObjectToAll(changeState);
	}
	
	public void startGame() {	
		ArrayList<PlayerConnection> players = playerContainer.getPlayersConcurrentSafe();
		// SEND ENTITIES
		for (PlayerConnection player : players) {
			playerContainer.sendObjectToAll(new AddChaser(player.getMyId(), 10, 10));
		}
		// SEND MAP
		changeMap(Shared.Map.DEBUG_MAP.name);
		// SET STATE
		changeRoomState(ROOM_STATE_GAME);
	}
	public void changeMap(String name){
		playerContainer.sendObjectToAll(new SetMap(name));
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
		playerContainer.sendObjectToAllExcept(player, move);
	}
}