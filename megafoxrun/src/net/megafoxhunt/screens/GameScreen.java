package net.megafoxhunt.screens;


import net.megafoxhunt.core.GameInputProcessor;
import net.megafoxhunt.core.MyGdxGame;
import net.megafoxhunt.core.User;
import net.megafoxhunt.core.UserContainer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GameScreen implements Screen {

	public static final float UNIT_SCALE = 1 / 32f;
	
	private MyGdxGame game;
	
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	
	public GameScreen() {
		
		Gdx.input.setInputProcessor(new GameInputProcessor());

		map = new TmxMapLoader().load("data/testmap.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, UNIT_SCALE);
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 30, 20);
		camera.update();
		
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.7f, 0.7f, 1.0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
        // TODO: SET CAMERA POSITION TO FOLLOW TARGET

        camera.update();
        
        renderer.setView(camera);
        renderer.render();

        for(User user : UserContainer.getUsersConcurrentSafe()){        	
        	user.getControlledEntity().update(delta);
        }
        
        Batch batch = renderer.getSpriteBatch();
        batch.begin();
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
		map.dispose();
		renderer.dispose();
	}

}
