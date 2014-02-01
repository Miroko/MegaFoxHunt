package net.megafoxhunt.server;

import com.esotericsoftware.kryonet.Connection;

public class PlayerConnection extends Connection {
	
	private GameRoom myCurrentRoom;
	
	private String name;
	private int myId;
	
	public PlayerConnection() {
		super();
		
		name = "";
		myId = -1;
	}

	public GameRoom getMyCurrentRoom() {
		return myCurrentRoom;
	}

	public void setMyCurrentRoom(GameRoom myCurrentRoom) {
		this.myCurrentRoom = myCurrentRoom;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMyId() {
		return myId;
	}

	public void setMyId(int id) {
		this.myId = id;
	}
}
