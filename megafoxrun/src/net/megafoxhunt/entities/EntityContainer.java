package net.megafoxhunt.entities;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import net.megafoxhunt.core.PlayerHandler;
import net.megafoxhunt.core.UsersHandler;

public class EntityContainer {

	public static final int ALIVE_ENTITY = 1;
	
	private ConcurrentHashMap<Integer, StaticEntity> container;
	
	public EntityContainer() {
		container = new ConcurrentHashMap<Integer, StaticEntity>();
	}
	
	public void addEntity(int key, StaticEntity entity) {
		container.put(key, entity);
	}
	
	public void removeEntity(int key) {
		container.remove(key);
	}
	
	public StaticEntity getEntity(int id) {
		return container.get(id);
	}
	
	public Collection<StaticEntity> getValues() {
		return container.values();
	}
	
	public static void createEntity(EntityContainer container, int id, int type, int x, int y, int direction) {
		StaticEntity entity = null;
		
		switch(type) {
			case ALIVE_ENTITY:
				entity = new AliveEntity(id, "asd", x, y, 50, 1);
				
				if (id == UsersHandler.getMyUser().getId()) {
					PlayerHandler.setPlayerEntity((AliveEntity)entity);
				}
				break;
		}
		
		if (entity != null) {
			container.addEntity(id, entity);
		}
	}
}
