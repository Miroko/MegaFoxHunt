package net.megafoxhunt.screens;

import net.megafoxhunt.core.GameInputProcessor;
import net.megafoxhunt.core.MyGdxGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GameScreen implements Screen {

private MyGdxGame game;
	
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	
	float x, y;
	
	public GameScreen(final MyGdxGame game) {
		this.game = game;		
		camera = new OrthographicCamera();
        camera.setToOrtho(true, 800, 480);        
        Gdx.input.setInputProcessor(new GameInputProcessor());        
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        camera.position.set(x, y, 0);
        camera.update();
        
        x++;
        y++;
        
        renderer.setView(camera);
        renderer.render();
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width / 2.5f;
		camera.viewportHeight = height / 2.5f;
	}

	@Override
	public void show() {
		map = new TmxMapLoader().load("data/testmap.tmx");
		renderer = new OrthogonalTiledMapRenderer(map);
		
		camera = new OrthographicCamera();
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
