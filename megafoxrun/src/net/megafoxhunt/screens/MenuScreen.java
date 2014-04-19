package net.megafoxhunt.screens;

import net.megafoxhunt.core.MyGdxGame;
import net.megafoxhunt.ui.MenuUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MenuScreen implements Screen {
	
	private Stage stageUI;		
	
	public MenuScreen(){	  		
		stageUI = new Stage();	        
		
	    MenuUI menuUI = new MenuUI();	  
	  
	    stageUI.addActor(menuUI);
	    
	    MyGdxGame.resources.MUSIC.setVolume(0.5f);
	    MyGdxGame.resources.MUSIC.setLooping(true);
	    MyGdxGame.resources.MUSIC.play();
	}
	@Override
	public void resize (int width, int height) {
		stageUI.setViewport(width, height, true);
	}
	@Override
	public void dispose() {
		stageUI.dispose();
	}

	@Override
	public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
	    stageUI.act(Gdx.graphics.getDeltaTime());
	    stageUI.draw();		
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


}
