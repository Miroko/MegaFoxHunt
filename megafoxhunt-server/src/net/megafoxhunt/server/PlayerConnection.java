package net.megafoxhunt.server;

import net.megafox.entities.Chaser;
import net.megafox.entities.Entity;
import net.megafox.gameroom.GameRoom;
import net.megafox.items.Item;
import net.megafoxhunt.shared.KryoNetwork.SetItemType;
import net.megafoxhunt.shared.Shared;

import com.esotericsoftware.kryonet.Connection;

public class PlayerConnection extends Connection {
		
	private GameRoom myCurrentRoom;
	private Entity entity;
	
	private String name;
	public String getName(){return name;}
	public void setName(String name) { this.name = name; }
	
	private int myId;
	public int getMyId(){ return myId; }
	
	private Item currentItem = null;
	
	private boolean playerReady = false;
	public boolean isReady(){return playerReady;}
	public void setReady(boolean ready){playerReady = ready;}
	
	private int currentRageId;
	private boolean rageOn = false;
	public void activateRage(int rageId){
		rageOn = true;
		currentRageId = rageId;
	}
	public boolean deactivateRage(int rageId){
		if(currentRageId == rageId){
			rageOn = false;
			return true;
		}
		return false;
	}
	
	private int currentSpeedId;
	private boolean speedOn = false;
	public boolean isSpeedOn(){return speedOn;}
	public void activateSpeed(int speedId){
		speedOn = true;
		currentSpeedId = speedId;
	}
	public boolean deactivateSpeed(int speedId){
		if(currentSpeedId == speedId){
			speedOn = false;
			return true;
		}
		return false;
	}
	
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
		speedOn = false;
		rageOn = false;
		removeItem();
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
	public void activateItem() {
		Item item = getCurrentItem();
		if (item == null) return;
		else if(entity instanceof Chaser){
			if(((Chaser)entity).isEaten()){
				return;
			}
		}	
		else if (item.activate(getEntity().getX(), getEntity().getY(), this)) {
			setCurrentItem(null);
			SetItemType setItemType = new SetItemType(Shared.ITEM_EMPTY);
			this.sendTCP(setItemType);
		}			
	}	
	public void removeItem(){
		setCurrentItem(null);
		SetItemType setItemType = new SetItemType(Shared.ITEM_EMPTY);
		this.sendTCP(setItemType);
	}
	public Item getCurrentItem() {
		return currentItem;
	}
	public void setCurrentItem(Item item) {
		this.currentItem = item;
		if (item != null) {
			SetItemType setItemType = new SetItemType(item.getItemType());
			this.sendTCP(setItemType);
		}
	}
	public boolean isRageOn() {
		return rageOn;
	}

}
