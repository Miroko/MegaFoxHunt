package net.megafoxhunt.core;




import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;


public class GameMap {
	
	private static final int COLLISION_LAYER = 0;
	
	
	public static GameMap MAP_DEBUG = new GameMap("Debug", "data/basic_map.tmx");	
	
	private static GameMap CURRENT_MAP;	
	
	private String name;
	public String getName(){return name;}
	
	private String tiledMapPath;
	
	private TiledMap tiledMap;
	public TiledMap getTiledMap(){return tiledMap;}	
	
	public GameMap(String name, String tiledMapPath){
		this.name = name;
		this.tiledMapPath = tiledMapPath;		
	}
	public static void setCurrentMap(GameMap map){
		CURRENT_MAP = map;
		CURRENT_MAP.load();
	}
	public static GameMap getCurrentMap(){
		return CURRENT_MAP;
	}
	public static GameMap getMapByName(String name){
		GameMap map = null;
		if(name.equals(MAP_DEBUG.getName())){
			map = MAP_DEBUG;
		}		
		return map;			
	}
	public TiledMapTileLayer getCollisionLayer(){		
		return (TiledMapTileLayer)tiledMap.getLayers().get(COLLISION_LAYER);
	}
	public void load(){		
		tiledMap = new TmxMapLoader().load(tiledMapPath);		
	}
	public void dispose(){
		tiledMap.dispose();
	}
	
	

	

}
