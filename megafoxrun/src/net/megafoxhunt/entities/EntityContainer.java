package net.megafoxhunt.entities;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import net.megafoxhunt.core.GameNetwork;
import net.megafoxhunt.core.MyGdxGame;
import net.megafoxhunt.core.PlayerHandler;
import net.megafoxhunt.core.UserContainer;


public class EntityContainer {
	
	/*
	 * Pelaajien Entityt saisi varmaan sidottua User luokkaan
	 * 
	 */
	
	public static final int ALIVE_ENTITY = 1;
	
	private static ConcurrentHashMap<Integer, StaticEntity> ENTITIES = new ConcurrentHashMap<Integer, StaticEntity>();;

	public static void addEntity(int key, StaticEntity entity) {
		ENTITIES.put(key, entity);
	}
	
	public static void removeEntity(int key) {
		ENTITIES.remove(key);
	}
	
	public static StaticEntity getEntity(int id) {
		return ENTITIES.get(id);
	}
	
	public static Collection<StaticEntity> getValues() {
		return ENTITIES.values();
	}
	
	public static void createEntity(int id, int type, int x, int y, int direction) {
		StaticEntity entity = null;
		
		switch(type) {
			case ALIVE_ENTITY:
				entity = new AliveEntity(id, "asd", x, y, 50, 1);
				
				if (id == GameNetwork.getUser().getId()) {
					PlayerHandler.setPlayerEntity((AliveEntity)entity);
				}
				break;
		}
		
		if (entity != null) {
			addEntity(id, entity);
		}
	}
}
