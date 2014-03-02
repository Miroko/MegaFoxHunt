package net.megafoxhunt.screens;

import net.megafoxhunt.core.MyGdxGame;
import net.megafoxhunt.ui.MenuUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MenuScreen implements Screen {
	
	public static final int VIRTUAL_WIDTH = 800;
	public static final int VIRTUAL_HEIGHT = 600;
	
	private Stage stageUI;		
	private MenuUI menuUI;
	
	public MenuScreen(MyGdxGame game){	  		
		stageUI = new Stage();
	    Gdx.input.setInputProcessor(stageUI);		    
		
	    menuUI = new MenuUI();	  
	    menuUI.setPosition(0, 0);
	    stageUI.addActor(menuUI);
	}
	@Override
	public void resize (int width, int height) {
		stageUI.setViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, true);
	}
	@Override
	public void dispose() {
		stageUI.dispose();
	}

	@Override
	public void render(float delta) {
	    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	    stageUI.act(Gdx.graphics.getDeltaTime());
	    stageUI.draw();		
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


}
