package net.megafoxhunt.server;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import net.megafox.gameroom.GameRoom;

public class RoomHandler {
	
	public ReentrantLock lock;

	private ArrayList<GameRoom> rooms;	
	
	private GameServer gameServer;
	
	public RoomHandler(GameServer gameServer) {
		this.gameServer = gameServer;
		rooms = new ArrayList<GameRoom>();
		lock = new ReentrantLock(true);
	}	
	/**
	 * Add player to game on server
	 * @param playerConnection
	 */
	public void addPlayer(PlayerConnection playerConnection){		
		lock.lock();
		if(joinRoom(playerConnection) == false){
			createNewRoom(playerConnection);
		}
		lock.unlock();
	}
	/**
	 * Try join any available room
	 * @return false if no place in current rooms
	 */
	private boolean joinRoom(PlayerConnection playerConnection){
		for(GameRoom room : rooms) {
			if(room.canJoin()){				
				room.addPlayerToRoom(playerConnection);
				playerConnection.setMyCurrentRoom(room);
				return true;
			}
		}
		return false;
	}
	/**
	 * Creates new room and adds player to that room
	 * @param playerConnection
	 */
	private void createNewRoom(PlayerConnection playerConnection){
		GameRoom room = new GameRoom(gameServer.getIdHandler());
		room.addPlayerToRoom(playerConnection);
		playerConnection.setMyCurrentRoom(room);
		rooms.add(room);
		room.start();
	}
	@SuppressWarnings("unchecked")
	public ArrayList<GameRoom> getAllRoomsConcurrentSafe() {
		return (ArrayList<GameRoom>) rooms.clone();
	}
}
