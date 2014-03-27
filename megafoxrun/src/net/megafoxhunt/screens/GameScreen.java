package net.megafoxhunt.screens;



import net.megafoxhunt.core.GameInputProcessor;

import net.megafoxhunt.core.MyGdxGame;
import net.megafoxhunt.core.User;
import net.megafoxhunt.core.UserContainer;

import net.megafoxhunt.entities.Chased;
import net.megafoxhunt.entities.Chaser;
import net.megafoxhunt.entities.EntityMovable;
import net.megafoxhunt.entities.Entity;
import net.megafoxhunt.ui.TouchJoystick;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Logger;

public class GameScreen implements Screen {

	public static final float UNIT_SCALE = 1 / 64f;
	
	private static final int FIT_TILES_WIDTH = 24;
	private static final int FIT_TILES_HEIGHT = 16;

	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	
	private SpriteBatch spriteBatch;

	private TouchJoystick touchJoystick;	
	private GameInputProcessor gameInputProcessor;
	
	private BitmapFont font;
	
	public GameScreen() {			
		spriteBatch = new SpriteBatch();		
		touchJoystick = new TouchJoystick(MyGdxGame.network);
		gameInputProcessor = new GameInputProcessor(MyGdxGame.network, touchJoystick);		
		
		font = new BitmapFont();
		
		camera = new OrthographicCamera();	
		camera.setToOrtho(false, FIT_TILES_WIDTH, FIT_TILES_HEIGHT);
		camera.update();
	}
	
	@Override
	public void render(float delta) {
		
		// TODO syö kaiken suoritustehon?

		// UPDATE ENTITIES
		EntityMovable entity = null;
		for(User user : UserContainer.getUsersConcurrentSafe()){
			entity = user.getControlledEntity();
			if (entity != null) entity.update(delta, MyGdxGame.network, MyGdxGame.mapHandler.currentMap.getCollisionLayer());
		}
		
		// FOCUS CAMERA ON PLAYER ENTITY
		EntityMovable myEntity = MyGdxGame.network.getLocalUser().getControlledEntity();
        if (myEntity != null){
        	camera.position.x = myEntity.getX();
        	camera.position.y = myEntity.getY();
        	keepCameraInBoundaries();
        }
        camera.update();
		
		// CLEAR SCREEN
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// DRAW MAP
        renderer.setView(camera);
        renderer.render();
		
		// INIT BATCH
		Batch batch = renderer.getSpriteBatch();
		batch.begin();		
             
        // DRAW BERRIES AND HOLES
        for(Entity object : MyGdxGame.mapHandler.currentMap.getAllObjectsConcurrentSafe()) {
        	if (object != null) {
        		object.render(batch);
        	}
        }
        
        // DRAW Chased
        for(User user : UserContainer.getUsersConcurrentSafe()){
        	entity = user.getControlledEntity();
        	if (entity != null && entity instanceof Chased) entity.render(batch);
        }
        
        // DRAW Chaser
        for(User user : UserContainer.getUsersConcurrentSafe()){
        	entity = user.getControlledEntity();
        	if (entity != null && entity instanceof Chaser) entity.render(batch);
        }    
        
        batch.end();
        
        
        // DRAW JOYSTICK AND FPS
        spriteBatch.begin();
        touchJoystick.draw(spriteBatch);
        //if (Gdx.app.getType() == ApplicationType.Android) touchJoystick.draw(spriteBatch);
        font.draw(spriteBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 25, 25);
        font.draw(spriteBatch, "Ms: " + MyGdxGame.network.getCurrentPing(), 100, 25);
        spriteBatch.end();
	}

	private void keepCameraInBoundaries() {
		if (camera.position.x < FIT_TILES_WIDTH / 2) {
    		camera.position.x = FIT_TILES_WIDTH / 2;
    	} if (camera.position.x > (MyGdxGame.mapHandler.currentMap.getWidth() - (FIT_TILES_WIDTH / 2))) {
    		camera.position.x = MyGdxGame.mapHandler.currentMap.getWidth() - (FIT_TILES_WIDTH / 2);
    	}
    	
    	if (camera.position.y < FIT_TILES_HEIGHT / 2) {
    		camera.position.y = FIT_TILES_HEIGHT / 2;
    	} if (camera.position.y > (MyGdxGame.mapHandler.currentMap.getHeight() - (FIT_TILES_HEIGHT / 2))) {
    		camera.position.y = MyGdxGame.mapHandler.currentMap.getHeight() - (FIT_TILES_HEIGHT / 2);
    	}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {		
		MyGdxGame.mapHandler.currentMap.load();
		renderer = new OrthogonalTiledMapRenderer(MyGdxGame.mapHandler.currentMap.getTiledMap(), UNIT_SCALE);
		
		Gdx.input.setInputProcessor(gameInputProcessor);
	}

	@Override
	public void hide() {	
		
		MyGdxGame.mapHandler.currentMap.dispose();			
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
		if (renderer != null)
			renderer.dispose();
	}

}
