package net.megafoxhunt.core;

import net.megafoxhunt.entities.AliveEntity;

public class User {
	
	private int id;
	public int getID(){return id;}
	public void setID(int id){this.id = id;}
	
	private String name;
	public String getName(){return name;}
	public void setName(String name){this.name = name;}
	
	private AliveEntity controlledEntity;
	public void setControlledEntity(AliveEntity entity){controlledEntity = entity;}
	public AliveEntity getControlledEntity(){return controlledEntity;}

	public User(int id, String name) {
		this.id = id;
		this.name = name;
	}
}
