package net.megafoxhunt.screens;

import net.megafoxhunt.core.MyGdxGame;
import net.megafoxhunt.ui.MenuUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MenuScreen implements Screen {
	
	private Stage stageUI;		
	
	public MenuScreen(MyGdxGame game){	  		
		stageUI = new Stage();
	    Gdx.input.setInputProcessor(stageUI);		    
		
	    MenuUI menuUI = new MenuUI();	  
	    menuUI.setPosition(0, 0);
	    stageUI.addActor(menuUI);
	}
	@Override
	public void resize (int width, int height) {
		stageUI.setViewport(MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT, true);
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
