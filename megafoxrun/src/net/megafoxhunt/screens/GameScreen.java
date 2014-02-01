package net.megafoxhunt.screens;

import net.megafoxhunt.core.GameInputProcessor;
import net.megafoxhunt.core.GameMap;
import net.megafoxhunt.core.MyGdxGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class GameScreen implements Screen {

	private MyGdxGame game;
	
	private GameMap gameMap;
	private OrthographicCamera camera;
	
	float x, y;
	private float speed = 30;
	
	public GameScreen(final MyGdxGame game) {
        Gdx.input.setInputProcessor(new GameInputProcessor());        

		this.game = game;	
		this.gameMap = new GameMap();
		this.gameMap.setMap("data/testmap.tmx");
		this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());        
        Gdx.input.setInputProcessor(new GameInputProcessor());
	}	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        camera.position.set(x, y, 0);
        camera.update();
        
        x += speed * delta;
        
        gameMap.setView(camera);
        gameMap.draw();
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width / 2.5f;
		camera.viewportHeight = height / 2.5f;
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
		gameMap.dispose();
	}

}
