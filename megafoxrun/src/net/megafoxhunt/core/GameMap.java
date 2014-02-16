package net.megafoxhunt.core;




import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import net.megafoxhunt.entities.StaticObject;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;


public class GameMap {
	
	private static final int COLLISION_LAYER = 0;	
	
	public static GameMap MAP_DEBUG = new GameMap("Debug", "data/basic_map.tmx");	
	
	private static GameMap CURRENT_MAP;	
	
	// might not need this
	private ReentrantLock lock;
	
	private int mapWidth;
	private int mapHeight;
	
	private String name;
	public String getName(){return name;}
	
	private String tiledMapPath;
	
	private TiledMap tiledMap;
	public TiledMap getTiledMap(){return tiledMap;}
	
	private ArrayList<StaticObject> allObjects;
	@SuppressWarnings("unchecked")
	public ArrayList<StaticObject> getAllObjectsConcurrentSafe(){return (ArrayList<StaticObject>) allObjects.clone();}
	
	public GameMap(String name, String tiledMapPath){
		this.lock = new ReentrantLock(true);
		this.name = name;
		this.tiledMapPath = tiledMapPath;		
		this.allObjects = new ArrayList<StaticObject>();
	}
	public void addStaticObject(StaticObject object){
		lock.lock();
		allObjects.add(object);
		lock.unlock();
	}
	public void removeStaticObjectByID(int id){
		lock.lock();
		for(StaticObject object : getAllObjectsConcurrentSafe()){
			if(object.getId() == id){
				allObjects.remove(object);
			}
		}
		lock.unlock();
	}
	public static void setCurrentMap(GameMap map){
		CURRENT_MAP = map;
	}
	public static GameMap getCurrentMap(){
		return CURRENT_MAP;
	}
	public static GameMap getMapByName(String name){		
		if(name.equals(MAP_DEBUG.getName())){
			return MAP_DEBUG;
		}
		return null;				
	}
	public TiledMapTileLayer getCollisionLayer(){		
		return (TiledMapTileLayer)tiledMap.getLayers().get(COLLISION_LAYER);
	}
	public void load(){		
		tiledMap = new TmxMapLoader().load(tiledMapPath);
		MapProperties prop = tiledMap.getProperties();
		mapWidth = prop.get("width", Integer.class);
		mapHeight = prop.get("height", Integer.class);
	}
	public void dispose(){
		tiledMap.dispose();
	}
	
	public int getMapWidth() {
		return mapWidth;
	}
	
	public int getMapHeight() {
		return mapHeight;
	}
	

	

}
