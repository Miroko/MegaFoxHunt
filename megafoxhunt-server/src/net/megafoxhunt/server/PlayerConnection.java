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
	
	private boolean playerReady = false;
	public boolean isReady(){return playerReady;}
	public void setReady(){playerReady = true;}
	
	private boolean goInHole = false;
	public void setGoInHole(boolean go){goInHole = go;}
	public boolean isGoingInHole(){return goInHole;}	
	
	public enum Team{
		Chasers, Chased
	}
	private Team preferedTeam = Team.Chasers;
	public Team getPreferedTeam(){return preferedTeam;}
	public void setPreferedTeam(Team preferedTeam){
		this.preferedTeam = preferedTeam;
	}	
	public void resetData(){
		playerReady = false;
		preferedTeam = null;
	}
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
	public void activateItem() {
		Item item = getCurrentItem();
		if (item == null) return;
		
		item.activateItem(getEntity().getX(), getEntity().getY(), this);
		
		// remove used item
		//playerConnection.setCurrentItem(null);
	}	
	public void setCurrentItem(Item item) {
		this.currentItem = item;
	}
}
