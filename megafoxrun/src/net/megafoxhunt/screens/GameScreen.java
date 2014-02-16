package net.megafoxhunt.screens;


import net.megafoxhunt.core.GameInputProcessor;

import net.megafoxhunt.core.GameMap;
import net.megafoxhunt.core.GameNetwork;
import net.megafoxhunt.core.User;
import net.megafoxhunt.core.UserContainer;
import net.megafoxhunt.debug.DebugConsole;
import net.megafoxhunt.entities.Entity;
import net.megafoxhunt.entities.StaticObject;
import net.megafoxhunt.ui.TouchJoystick;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GameScreen implements Screen {

	public static final float UNIT_SCALE = 1 / 64f;
	
	private static final int FIT_TILES_WIDTH = 24;
	private static final int FIT_TILES_HEIGHT = 16;

	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	
	private SpriteBatch spriteBatch;
	
	private GameNetwork network;
	
	private TouchJoystick touchJoystick;
	
	public GameScreen(GameNetwork network) {
		DebugConsole.msg("Set screen: GameScreen");
		this.network = network;		
		spriteBatch = new SpriteBatch();		
		touchJoystick = new TouchJoystick(network);
		Gdx.input.setInputProcessor(new GameInputProcessor(network, touchJoystick));
		
		/*
		 * LOAD MAP
		 */
		GameMap.getCurrentMap().load();	
		
		renderer = new OrthogonalTiledMapRenderer(GameMap.getCurrentMap().getTiledMap(), UNIT_SCALE);
				
		camera = new OrthographicCamera();	
		camera.setToOrtho(false, FIT_TILES_WIDTH, FIT_TILES_HEIGHT);
		camera.update();		
	}
	
	@Override
	public void render(float delta) {
		
		// TODO syö kaiken suoritustehon?

		// UPDATE ENTITIES
		for(User user : UserContainer.getUsersConcurrentSafe()){        	
			user.getControlledEntity().update(delta, network);
		}
		
		// FOCUS CAMERA ON PLAYER ENTITY
		Entity myEntity = network.getLocalUser().getControlledEntity();
        if (myEntity != null){
        	camera.position.x = myEntity.getX();
        	camera.position.y = myEntity.getY();
        	keepCameraInBoundaries();
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
        // DRAW BERRIES
        for(StaticObject object : GameMap.getCurrentMap().getAllObjectsConcurrentSafe()){
        	object.render(batch);
        }
        
        batch.end();
        
        spriteBatch.begin();
        touchJoystick.draw(spriteBatch);
        spriteBatch.end();
	}

	private void keepCameraInBoundaries() {
		if (camera.position.x < FIT_TILES_WIDTH / 2) {
    		camera.position.x = FIT_TILES_WIDTH / 2;
    	} if (camera.position.x > (GameMap.getCurrentMap().getMapWidth() - (FIT_TILES_WIDTH / 2))) {
    		camera.position.x = GameMap.getCurrentMap().getMapWidth() - (FIT_TILES_WIDTH / 2);
    	}
    	
    	if (camera.position.y < FIT_TILES_HEIGHT / 2) {
    		camera.position.y = FIT_TILES_HEIGHT / 2;
    	} if (camera.position.y > (GameMap.getCurrentMap().getMapHeight() - (FIT_TILES_HEIGHT / 2))) {
    		camera.position.y = GameMap.getCurrentMap().getMapHeight() - (FIT_TILES_HEIGHT / 2);
    	}
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
