package net.megafoxhunt.server;

import com.esotericsoftware.kryonet.Connection;

public class PlayerConnection extends Connection {
	
	private GameRoom myCurrentRoom;
	
	private String name;
	public String getName(){return name;}
	public void setName(String name){this.name = name;}
	
	private int id;
	public int getID(){return id;}
	
	public PlayerConnection(int id, String name) {
		super();	
		this.id = id;
		this.name = name;
	}

	public GameRoom getMyCurrentRoom() {
		return myCurrentRoom;
	}

	public void setMyCurrentRoom(GameRoom myCurrentRoom) {
		this.myCurrentRoom = myCurrentRoom;
	}

}
