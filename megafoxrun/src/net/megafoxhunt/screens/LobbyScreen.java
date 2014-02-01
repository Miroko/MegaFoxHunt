package net.megafoxhunt.screens;

import java.util.ArrayList;

import net.megafoxhunt.core.MyGdxGame;
import net.megafoxhunt.core.User;
import net.megafoxhunt.core.UsersHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class LobbyScreen implements Screen {

	private MyGdxGame game;
	private BitmapFont font;

	OrthographicCamera camera;
	
	public LobbyScreen(final MyGdxGame game) {
		this.game = game;
		font = new BitmapFont();
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
        ArrayList<User> users = UsersHandler.getUsers();
        User me = UsersHandler.getMyUser();
        
        int screenHeight = Gdx.graphics.getHeight();
        
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        font.setColor(Color.RED);
        font.draw(game.batch, UsersHandler.getMyUser().getName(), 25, screenHeight - 20);

        font.setColor(Color.WHITE);
		for(int i = 0; i < users.size(); i++) {
			font.draw(game.batch, users.get(i).getName(), 25, screenHeight - ((i * 20) + 40));
		}
		game.batch.end();
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
	}
}
