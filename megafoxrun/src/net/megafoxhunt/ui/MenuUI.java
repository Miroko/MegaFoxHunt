package net.megafoxhunt.ui;

import net.megafoxhunt.core.MyGdxGame;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MenuUI extends Table{
	
	private ImageButton connectButton;
	private ImageButton quitButton;
	
	public MenuUI(){
		setFillParent(true);

		TextureRegionDrawable connectButtonImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.connectButtonTexture));
		
		connectButton = new ImageButton(connectButtonImage);	
		connectButton.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {		
				if(connectButton.isPressed()){					
					MyGdxGame.network.setUsername("TestUser");	
					MyGdxGame.network.start();	
					MyGdxGame.network.connect(MyGdxGame.IP_SERVER, 6666);
				}
			}
		});			
				
		TextureRegionDrawable quitButtonImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.quitButtonTexture));
		
		quitButton = new ImageButton(quitButtonImage);	
		quitButton.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {		
				if(quitButton.isPressed()){					
					MyGdxGame.shutdown();
				}
			}
		});				
		
		add(connectButton);
		row();
		add(quitButton).padTop(20);

	}

}
