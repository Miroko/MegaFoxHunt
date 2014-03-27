package net.megafoxhunt.screens;

import net.megafoxhunt.core.MyGdxGame;
import net.megafoxhunt.core.User;
import net.megafoxhunt.core.UserContainer;
import net.megafoxhunt.ui.LobbyUI;
import net.megafoxhunt.ui.MenuUI;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class LobbyScreen implements Screen {
	
	private Stage stageUI;		

	private BitmapFont font;

	public SpriteBatch batch;
	
	private OrthographicCamera camera;
	
	public LobbyScreen() {		
		stageUI = new Stage();
				
	    LobbyUI lobbyUI = new LobbyUI();	  
	    
	    stageUI.addActor(lobbyUI);	    
	    
		batch = new SpriteBatch();
		font = new BitmapFont();
		
		camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

	    stageUI.act(Gdx.graphics.getDeltaTime());
	    stageUI.draw();	
               
        camera.update();
        batch.setProjectionMatrix(camera.combined);        
        
        batch.begin();
		drawLobbyUsers();
		batch.end();
	}
	private void drawLobbyUsers(){	
		for(int i = 0; i < UserContainer.numberOfUsers(); i++) {
			User userToDraw = UserContainer.getUsersConcurrentSafe().get(i);
			if(userToDraw == MyGdxGame.network.getLocalUser()){
				// SELF
		        font.setColor(Color.RED);
			}
			else{
		        // OTHERS
		        font.setColor(Color.WHITE);
			}
			font.draw(batch, userToDraw.getName(), 25, Gdx.graphics.getHeight() - ((i * 20) + 40));
		}
	}
	@Override
	public void resize (int width, int height) {
		stageUI.setViewport(MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT, true);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stageUI);			
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
		stageUI.dispose();
		font.dispose();
		batch.dispose();
	}
}
