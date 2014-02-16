package net.megafoxhunt.server;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class RoomHandler {
	
	public ReentrantLock lock;

	private ArrayList<GameRoom> rooms;	
	
	public RoomHandler() {
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
			if(room.hasFreeRoom()){				
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
		GameRoom room = new GameRoom();
		room.addPlayerToRoom(playerConnection);
		playerConnection.setMyCurrentRoom(room);
		rooms.add(room);
		room.start();
	}
	@SuppressWarnings("unchecked")
	public ArrayList<GameRoom> getAllRoomsConcurrentSafe() {
		return (ArrayList<GameRoom>) rooms.clone();
	}
	
	
	
	
	
	
	
	
	/*
	public void joinAvailableRoom(PlayerConnection playerConnection) {
		lock.lock();
		
		GameRoom selectedRoom = null;
		
		// Join to first room that has open slots
		for(GameRoom room : rooms) {
			if (room.addPlayer(playerConnection)) {
				selectedRoom = room;
				break;
			}
		}
		
		// Create new room if no available room were found
		if (selectedRoom == null) {
			selectedRoom = new GameRoom();
			selectedRoom.start();
			rooms.add(selectedRoom);
			selectedRoom.addPlayer(playerConnection);
		}
		
		lock.unlock();
		
		playerConnection.setMyCurrentRoom(selectedRoom);
	}
	*/

}
