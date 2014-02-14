package net.megafoxhunt.core;

import net.megafoxhunt.entities.Entity;



public class User {
	
	private int id;
	public int getID(){return id;}
	public void setID(int id){this.id = id;}
	
	private String name;
	public String getName(){return name;}
	public void setName(String name){this.name = name;}
	
	private Entity controlledEntity;
	public void setControlledEntity(Entity entity){controlledEntity = entity;}
	public Entity getControlledEntity(){return controlledEntity;}

	public User(int id, String name) {
		this.id = id;
		this.name = name;
	}
}
