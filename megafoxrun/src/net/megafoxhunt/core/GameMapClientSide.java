package net.megafoxhunt.core;




import java.util.ArrayList;
import java.util.Iterator;

import net.megafoxhunt.entities.Barricade;
import net.megafoxhunt.entities.Berry;
import net.megafoxhunt.entities.Chased;
import net.megafoxhunt.entities.Chaser;
import net.megafoxhunt.entities.Entity;
import net.megafoxhunt.entities.Powerup;
import net.megafoxhunt.shared.GameMapSharedConfig;
import net.megafoxhunt.shared.KryoNetwork.ChangeState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;


public class GameMapClientSide {
	
	private static final int TILEDMAP_COLLISION_LAYER = 0;	

	private GameMapSharedConfig config;

	public String getName(){return config.getName();}
	
	private int berryCount = 0;
	
	private TiledMap tiledMap;
	public TiledMap getTiledMap(){return tiledMap;}
	
	private ArrayList<Entity> allObjects;
	@SuppressWarnings("unchecked")
	public ArrayList<Entity> getAllObjectsConcurrentSafe(){return (ArrayList<Entity>) allObjects.clone();}
	
	public ArrayList<Barricade> barricades;
	
	public GameMapClientSide(GameMapSharedConfig mapConfig){		
		this.config = mapConfig;		
		this.allObjects = new ArrayList<Entity>();
		this.barricades = new ArrayList<Barricade>();
	}
	public void addStaticObject(Entity object){		
		allObjects.add(object);
		if (object instanceof Barricade){
			barricades.add((Barricade)object);			
		} else if (object instanceof Berry) {
			berryCount++;
		}
	}
	public void removeStaticObjectByID(int id){		
		for(Entity object : getAllObjectsConcurrentSafe()){
			if(object.getId() == id){
				object.setShouldBeRemoved(true);
				if (object instanceof Barricade){
					barricades.remove(object);
				} else if (object instanceof Berry) {
					berryCount--;
				}
			}
		}		
	}
	
	public void removeOldObjects() {
		Iterator<Entity> i = allObjects.iterator();
		while (i.hasNext()) {
		   Entity s = i.next();
		   if (s.getShouldBeRemoved()){
			   i.remove();			   
			   if(s instanceof Berry){
				   ((Berry)s).playEatSound();
			   }
			   else if (s instanceof Powerup){
					MyGdxGame.resources.PICKUP.play();
			   }
		   }
		}
	}
	
	public boolean isBlocked(int x, int y) {
		TiledMapTileLayer m = (TiledMapTileLayer) tiledMap.getLayers().get(TILEDMAP_COLLISION_LAYER);
		if (m.getCell(x, y).getTile().getProperties().containsKey("wall")) return true;
		
		for (Barricade barricade : barricades) {
			if (barricade.getX() == x && barricade.getY() == y) return true;
		}
		
		return false;
	}
	
	public TiledMapTileLayer getCollisionLayer(){		
		return (TiledMapTileLayer)tiledMap.getLayers().get(TILEDMAP_COLLISION_LAYER);
	}
	public void load(){		
		tiledMap = new TmxMapLoader().load(config.getTiledMapPath());
	}	
	public int getWidth() {
		return config.getWidth();
	}	
	public int getHeight() {
		return config.getHeight();
	}
	public void dispose(){
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				tiledMap.dispose();
			}
		});		
	}
	
	public int getBerryCount() {
		return berryCount;
	}

	public void changeTile(int x, int y, int type) {
		((TiledMapTileLayer)tiledMap.getLayers().get(0)).getCell(x, y).setTile(tiledMap.getTileSets().getTile(type));
	}
}
