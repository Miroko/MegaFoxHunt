package net.megafoxhunt.core;

import net.megafoxhunt.entities.EntityMovable;



public class User {
	
	private int id;
	public int getID(){return id;}
	public void setID(int id){this.id = id;}
	
	private String name;
	public String getName(){return name;}
	public void setName(String name){this.name = name;}
	
	private EntityMovable controlledEntity;
	public void setControlledEntity(EntityMovable entity){controlledEntity = entity;}
	public EntityMovable getControlledEntity(){return controlledEntity;}

	public User(int id, String name) {
		this.id = id;
		this.name = name;
	}
}
