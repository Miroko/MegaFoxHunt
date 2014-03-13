package net.megafoxhunt.server;

import net.megafox.entities.Entity;
import net.megafox.gameroom.GameRoom;
import net.megafox.items.Item;

import com.esotericsoftware.kryonet.Connection;

public class PlayerConnection extends Connection {
	
	private GameRoom myCurrentRoom;
	private Entity entity;
	
	private String name;
	public String getName(){return name;}
	public void setName(String name) { this.name = name; }
	
	private int myId;
	public int getMyId(){ return myId; }
	
	private Item currentItem;
	
	public PlayerConnection(int id, String name) {
		super();	
		this.myId = id;
		this.name = name;
	}
	
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
	public Entity getEntity() {
		return entity;
	}

	public GameRoom getMyCurrentRoom() {
		return myCurrentRoom;
	}

	public void setMyCurrentRoom(GameRoom myCurrentRoom) {
		this.myCurrentRoom = myCurrentRoom;
	}
	public void dispose(IDHandler idHandler){
		idHandler.freeID(myId);
	}

	public Item getCurrentItem() {
		return currentItem;
	}
	
	public void setCurrentItem(Item item) {
		this.currentItem = item;
	}
}
