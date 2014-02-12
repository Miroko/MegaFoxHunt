package net.megafoxhunt.screens;


import net.megafoxhunt.core.GameInputProcessor;

import net.megafoxhunt.core.GameMap;
import net.megafoxhunt.core.GameNetwork;

import net.megafoxhunt.core.User;
import net.megafoxhunt.core.UserContainer;
import net.megafoxhunt.debug.DebugConsole;

import net.megafoxhunt.entities.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GameScreen implements Screen {

	public static final float UNIT_SCALE = 1 / 64f;

	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	
	public GameScreen() {
		DebugConsole.msg("Set screen: GameScreen");
		
		Gdx.input.setInputProcessor(new GameInputProcessor());
		
		/*
		 * LOAD MAP
		 */
		GameMap.getCurrentMap().load();	
		
		renderer = new OrthogonalTiledMapRenderer(GameMap.getCurrentMap().getTiledMap(), UNIT_SCALE);
				
		camera = new OrthographicCamera();	
		camera.setToOrtho(false, 30, 20);
		camera.update();		
	}
	
	@Override
	public void render(float delta) {
		
		// TODO syö kaiken suoritustehon?

		// UPDATE ENTITIES
		for(User user : UserContainer.getUsersConcurrentSafe()){        	
			user.getControlledEntity().update(delta);
		}
		
		// FOCUS CAMERA ON PLAYER ENTITY
		Entity myEntity = GameNetwork.getUser().getControlledEntity();
        if (myEntity != null){
        	camera.position.x = myEntity.getX();
        	camera.position.y = myEntity.getY();
        }
        camera.update();
		
		// CLEAR SCREEN
		Gdx.gl.glClearColor(0.7f, 0.7f, 1.0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		// DRAW MAP
        renderer.setView(camera);
        renderer.render();
		
		// INIT BATCH
		Batch batch = renderer.getSpriteBatch();
		batch.begin();		
             
        // DRAW ENTITIES
        for(User user : UserContainer.getUsersConcurrentSafe()){
        	user.getControlledEntity().render(batch);
        }        
        batch.end(); 
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {		
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		GameMap.getCurrentMap().dispose();
		renderer.dispose();
	}

}
