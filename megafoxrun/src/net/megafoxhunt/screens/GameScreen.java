package net.megafoxhunt.screens;



import net.megafoxhunt.core.GameInputProcessor;

import net.megafoxhunt.core.MyGdxGame;
import net.megafoxhunt.core.User;
import net.megafoxhunt.core.UserContainer;

import net.megafoxhunt.entities.Chased;
import net.megafoxhunt.entities.Chaser;
import net.megafoxhunt.entities.EntityMovable;
import net.megafoxhunt.entities.Entity;
import net.megafoxhunt.ui.GameUI;
import net.megafoxhunt.ui.TouchJoystick;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Logger;

public class GameScreen implements Screen{

	public static final float UNIT_SCALE = 1 / 64f;
	
	private static final int FIT_TILES_WIDTH = 20;
	private static final int FIT_TILES_HEIGHT = 15;
	
	private Stage stageUI;	

	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	
	private SpriteBatch spriteBatch;

	private TouchJoystick touchJoystick;	
	private GameUI gameUI;
	private GameInputProcessor gameInputProcessor;
	
	public GameScreen() {	
		stageUI = new Stage();
		
		gameUI = new GameUI();
		stageUI.addActor(gameUI);
		
		spriteBatch = new SpriteBatch();		
		touchJoystick = new TouchJoystick(MyGdxGame.network);
		gameInputProcessor = new GameInputProcessor(MyGdxGame.network, gameUI, touchJoystick);
		
		camera = new OrthographicCamera();	
		camera.setToOrtho(false, FIT_TILES_WIDTH, FIT_TILES_HEIGHT);
		camera.update();
	}
	
	@Override
	public void render(float delta) {
		// TODO syö kaiken suoritustehon?

		MyGdxGame.mapHandler.currentMap.removeOldObjects();

		
		// UPDATE ENTITIES
		EntityMovable entity = null;
		for(User user : UserContainer.getUsersConcurrentSafe()){
			entity = user.getControlledEntity();
			if (entity != null) entity.update(delta, MyGdxGame.network);
		}
		
		// FOCUS CAMERA ON PLAYER ENTITY
		EntityMovable myEntity = MyGdxGame.network.getLocalUser().getControlledEntity();
        if (myEntity != null){
        	camera.position.x = myEntity.getX();
        	camera.position.y = myEntity.getY();
        	keepCameraInBoundaries();
        }
       // camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
        
        
		
		// CLEAR SCREEN
        Gdx.gl.glClearColor(0.015f, 0.65f, 0.027f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// DRAW MAP
        renderer.setView(camera);
        renderer.render(new int[] {0, 1, 2});
		
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
        
        
        renderer.render(new int[] {3});
        
        // DRAW JOYSTICK AND FPS
        spriteBatch.begin();
        touchJoystick.draw(spriteBatch);
        //if (Gdx.app.getType() == ApplicationType.Android) touchJoystick.draw(spriteBatch);
        drawInterface(spriteBatch, camera);
        spriteBatch.end();
        
        // UI
	    stageUI.act(Gdx.graphics.getDeltaTime());
	    stageUI.draw();	        
	}
	private void drawInterface(SpriteBatch batch, Camera camera) {
		int berryCount = MyGdxGame.mapHandler.currentMap.getBerryCount();
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();

		MyGdxGame.resources.BASIC_FONT.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 25, 25);
		MyGdxGame.resources.BASIC_FONT.draw(batch, "Ms: " + MyGdxGame.network.getCurrentPing(), 100, 25);
		MyGdxGame.resources.BASIC_FONT.draw(batch, "Berries: " + berryCount, screenWidth - screenWidth / 2, screenHeight - 50);
		
		for (User user : UserContainer.getUsersConcurrentSafe()) {
			if (user != null && user.getControlledEntity() != null) {
	        	Vector3 v3 = new Vector3(user.getControlledEntity().getX(), user.getControlledEntity().getY(), 0);
	        	camera.project(v3);
	        	MyGdxGame.resources.BASIC_FONT.draw(batch, user.getName(), v3.x, v3.y);
			}
        }
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
		spriteBatch = new SpriteBatch();
	}

	@Override
	public void show() {	
		gameUI.setVisible(false);
		
		MyGdxGame.mapHandler.currentMap.load();
		renderer = new OrthogonalTiledMapRenderer(MyGdxGame.mapHandler.currentMap.getTiledMap(), UNIT_SCALE);
		
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(gameInputProcessor);
		multiplexer.addProcessor(new GestureDetector(gameInputProcessor));
		multiplexer.addProcessor(stageUI);
		Gdx.input.setInputProcessor(multiplexer);
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
