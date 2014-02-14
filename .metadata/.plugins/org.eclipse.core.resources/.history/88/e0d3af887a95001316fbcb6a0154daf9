package net.megafoxhunt.server;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class RoomHandler {

	private ArrayList<GameRoom> rooms;
	
	private ReentrantLock lock;
	
	public RoomHandler() {
		rooms = new ArrayList<GameRoom>();
		lock = new ReentrantLock(true);
	}
	
	public ArrayList<GameRoom> getRooms() {
		return (ArrayList<GameRoom>)rooms.clone();
	}
	
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
}
