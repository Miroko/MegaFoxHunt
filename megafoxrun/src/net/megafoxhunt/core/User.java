package net.megafoxhunt.core;

import net.megafoxhunt.entities.EntityMovable;
import net.megafoxhunt.shared.Shared;



public class User {
	
	private int id;
	public int getID(){return id;}
	public void setID(int id){this.id = id;}
	
	private String name = "player";
	public String getName(){return name;}
	public void setName(String name){this.name = name;}
	
	private boolean ready = false;
	public void setReady(boolean ready){this.ready = ready;}
	public boolean getReady(){return ready;}
	
	// 0 = no item
	private int itemType = 0;
	
	private EntityMovable controlledEntity;
	public void setControlledEntity(EntityMovable entity){controlledEntity = entity;}
	public EntityMovable getControlledEntity(){return controlledEntity;}

	public User(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public void setItemType(int itemType) {
		this.itemType = itemType;
		if(itemType == Shared.ITEM_BOMB){
			MyGdxGame.resources.koira_ker‰‰_pommin.play();
		}
		else if(itemType == Shared.ITEM_BARRICADE){
			MyGdxGame.resources.kettu_ker‰‰_barrikadin.play();
		}
	}
	
	public int getItemType() {
		return itemType;
	}
}
