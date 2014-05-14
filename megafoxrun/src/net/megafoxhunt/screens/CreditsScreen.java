package net.megafoxhunt.screens;

import net.megafoxhunt.ui.CreditsUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class CreditsScreen implements Screen{
	
	private Stage stageUI;	
	private CreditsUI creditsUI;
	
	public CreditsScreen(){
		stageUI = new Stage();		
		creditsUI = new CreditsUI();
	    stageUI.addActor(creditsUI);	
	}

	@Override
	public void render(float delta) {
	    stageUI.act(Gdx.graphics.getDeltaTime());
	    stageUI.draw();		
	}

	@Override
	public void resize(int width, int height) {
		stageUI.setViewport(width, height, true);	
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stageUI);
	}

	@Override
	public void hide() {
				
	}

	@Override
	public void pause() {
				
	}

	@Override
	public void resume() {
				
	}

	@Override
	public void dispose() {
		stageUI.dispose();
	}

}
