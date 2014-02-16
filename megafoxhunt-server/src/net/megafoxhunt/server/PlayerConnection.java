package net.megafoxhunt.server;

import net.megafox.gameroom.GameRoom;

import com.esotericsoftware.kryonet.Connection;

public class PlayerConnection extends Connection {
	
	private GameRoom myCurrentRoom;
	
	private String name;
	public String getName(){return name;}
	public void setName(String name) { this.name = name; }
	
	private int myId;
	public int getMyId(){ return myId; }
	
	public PlayerConnection(int id, String name) {
		super();	
		this.myId = id;
		this.name = name;
	}

	public GameRoom getMyCurrentRoom() {
		return myCurrentRoom;
	}

	public void setMyCurrentRoom(GameRoom myCurrentRoom) {
		this.myCurrentRoom = myCurrentRoom;
	}

}
