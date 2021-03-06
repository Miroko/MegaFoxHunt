package net.megafoxhunt.ui;

import net.megafoxhunt.core.MyGdxGame;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MenuUI extends Table{
	
	private ImageButton connectButton;
	private ImageButton quitButton;
	private ImageButton creditsButton;
	private TextField nameField;
	
	public MenuUI(){
		setFillParent(true);
										
		TextFieldStyle fieldStyle = new TextFieldStyle();
		fieldStyle.font = new BitmapFont();
		fieldStyle.fontColor = Color.BLACK;
		
		TextureRegionDrawable namefield = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.namefieldTexture));
		
		fieldStyle.background = namefield;
		nameField = new TextField("player", fieldStyle);
		if(MyGdxGame.network.getLocalUser().getName() == null){
			nameField.setText("player");
		}
		else{
			nameField.setText(MyGdxGame.network.getLocalUser().getName());
		}
		
		TextureRegionDrawable connectButtonUpImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.connectButtonUpTexture));
		TextureRegionDrawable connectButtonDownImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.connectButtonDownTexture));
		
		connectButton = new ImageButton(connectButtonUpImage, connectButtonDownImage);	
		connectButton.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {	
				if(connectButton.isPressed()) {
					if (!MyGdxGame.network.isConnecting()) {
						MyGdxGame.network.setUsername(nameField.getText());						
						MyGdxGame.network.connect(MyGdxGame.IP_SERVER, 6666);
						
						MyGdxGame.resources.painike.play();
					}
				}
			}
		});			
				
		TextureRegionDrawable quitButtonUpImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.quitButtonUpTexture));
		TextureRegionDrawable quitButtonDownImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.quitButtonDownTexture));
		
		quitButton = new ImageButton(quitButtonUpImage, quitButtonDownImage);	
		quitButton.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {		
				if(quitButton.isPressed()){		
					MyGdxGame.resources.painike.play();
					
					MyGdxGame.shutdown();					
				}
			}
		});		
		
		TextureRegionDrawable creditsButtonUpImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.creditsButtonUpTexture));
		TextureRegionDrawable creditsButtonDownImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.creditsButtonDownTexture));
		
		creditsButton = new ImageButton(creditsButtonUpImage, creditsButtonDownImage);	
		creditsButton.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {		
				if(creditsButton.isPressed()){		
					MyGdxGame.screenHandler.setScreenCredits();
					
					MyGdxGame.resources.painike.play();				
				}
			}
		});	
		
		setBackground(new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.menuBackground)));
		
		add(nameField).padTop(220);
		row();
		add(connectButton);
		row();
		add(quitButton);
		row();
		add(creditsButton);

	}

}
