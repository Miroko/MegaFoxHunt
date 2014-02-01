package net.megafoxhunt.core;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import net.megafoxhunt.entities.StaticEntity;

/*
 * Storage for map and entities
 * Handles entities
 */
public class GameMap {
	
	private TiledMap map;	
	private OrthogonalTiledMapRenderer renderer;
	private ArrayList<StaticEntity> entities;
	public void addEntity(StaticEntity e){entities.add(e);}
	public void removeEntity(StaticEntity e){entities.remove(e);}
	
	public GameMap(){
		entities = new ArrayList<StaticEntity>();
		renderer = new OrthogonalTiledMapRenderer(map);	
	}
	public void setMap(String path){
		map = new TmxMapLoader().load(path);
		renderer.setMap(map);
	}
	public void draw(){
		for(StaticEntity e : entities){
			// add in camera bounds check for entity
			e.draw((SpriteBatch) renderer.getSpriteBatch());
		}
		renderer.render();
	}
	public void setView(OrthographicCamera c){
		renderer.setView(c);
	}
	public void dispose(){
		map.dispose();
		renderer.dispose();
	}
}
