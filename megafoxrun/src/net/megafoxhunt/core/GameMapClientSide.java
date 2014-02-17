package net.megafoxhunt.core;




import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import net.megafoxhunt.entities.StaticObject;
import net.megafoxhunt.shared.GameMapSharedConfig;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;


public class GameMapClientSide {
	
	private static final int TILEDMAP_COLLISION_LAYER = 0;	
	
	//public static GameMapClientSide MAP_DEBUG = new GameMapClientSide(GameMapSharedConfig.DEBUG_MAP);	
	
	private GameMapSharedConfig config;

	public String getName(){return config.getName();}
	
	private TiledMap tiledMap;
	public TiledMap getTiledMap(){return tiledMap;}
	
	private ArrayList<StaticObject> allObjects;
	@SuppressWarnings("unchecked")
	public ArrayList<StaticObject> getAllObjectsConcurrentSafe(){return (ArrayList<StaticObject>) allObjects.clone();}
	
	public GameMapClientSide(GameMapSharedConfig mapConfig){		
		this.config = mapConfig;		
		this.allObjects = new ArrayList<StaticObject>();
	}
	public void addStaticObject(StaticObject object){		
		allObjects.add(object);		
	}
	public void removeStaticObjectByID(int id){		
		for(StaticObject object : getAllObjectsConcurrentSafe()){
			if(object.getId() == id){
				allObjects.remove(object);
			}
		}		
	}
	public TiledMapTileLayer getCollisionLayer(){		
		return (TiledMapTileLayer)tiledMap.getLayers().get(TILEDMAP_COLLISION_LAYER);
	}
	public void load(){		
		tiledMap = new TmxMapLoader().load(config.getTiledMapPath());
		
		/*
		MapProperties prop = tiledMap.getProperties();
		mapWidth = prop.get("width", Integer.class);
		mapHeight = prop.get("height", Integer.class);
		*/
	}	
	public int getWidth() {
		return config.getWidth();
	}	
	public int getHeight() {
		return config.getHeight();
	}
	public void dispose(){
		tiledMap.dispose();
	}	

}