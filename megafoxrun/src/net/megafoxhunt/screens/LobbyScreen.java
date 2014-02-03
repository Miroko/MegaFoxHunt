package net.megafoxhunt.screens;

import java.util.ArrayList;


import net.megafoxhunt.core.MyGdxGame;
import net.megafoxhunt.core.User;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LobbyScreen implements Screen {

	private MyGdxGame game;
	private BitmapFont font;

	public SpriteBatch batch;
	
	private OrthographicCamera camera;
	
	public LobbyScreen(final MyGdxGame game) {
		this.game = game;
		this.batch = new SpriteBatch();
		font = new BitmapFont();
		
		camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
        ArrayList<User> users = MyGdxGame.userContainer.getUsers();
        
        int screenHeight = Gdx.graphics.getHeight();
        
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.setColor(Color.RED);
        font.draw(batch, MyGdxGame.userContainer.getUser().getName(), 25, screenHeight - 20);

        font.setColor(Color.WHITE);
		for(int i = 0; i < users.size(); i++) {
			font.draw(batch, users.get(i).getName(), 25, screenHeight - ((i * 20) + 40));
		}
		batch.end();
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
		font.dispose();
		batch.dispose();
	}
}
