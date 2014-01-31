package net.megafoxhunt.core;

import java.util.ArrayList;

import net.megafoxhunt.entities.StaticEntity;

public class GameMap {
	
	private ArrayList<StaticEntity> entities;
	public void addEntity(StaticEntity e){entities.add(e);}
	public void removeEntity(StaticEntity e){entities.remove(e);}
	
	// tiled load
	
	public GameMap(){
		entities = new ArrayList<StaticEntity>();
	}
	
	

}
