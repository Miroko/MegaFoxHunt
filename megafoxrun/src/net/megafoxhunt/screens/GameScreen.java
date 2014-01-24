package net.megafoxhunt.screens;

import net.megafoxhunt.core.GameInputProcessor;
import net.megafoxhunt.core.MyGdxGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class GameScreen implements Screen {

	private MyGdxGame game;
	
	private OrthographicCamera camera;
	
	public GameScreen(final MyGdxGame game) {
		this.game = game;		
		camera = new OrthographicCamera();
        camera.setToOrtho(true, 800, 480);        
        Gdx.input.setInputProcessor(new GameInputProcessor());        
	}
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

}
