package net.megafoxhunt.entities;


public class EntityContainer {
	/*
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
				
				if (id == GameNetwork.getUser().getID()) {
					PlayerHandler.setPlayerEntity((AliveEntity)entity);
				}
				break;
		}
		
		if (entity != null) {
			addEntity(id, entity);
		}
	}
	*/
}
