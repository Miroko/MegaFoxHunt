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
	private TextField nameField;
	
	public MenuUI(){
		setFillParent(true);
				
		TextFieldStyle fieldStyle = new TextFieldStyle();
		fieldStyle.font = new BitmapFont();
		fieldStyle.fontColor = Color.BLACK;
		
		Pixmap background = new Pixmap(120,30, Format.RGB888);
		background.setColor(Color.WHITE);
		background.fill();
		
		TextureRegionDrawable backgroundImage = new TextureRegionDrawable(new TextureRegion(new Texture(background)));
		
		fieldStyle.background = backgroundImage;
		nameField = new TextField("player", fieldStyle);
		if(MyGdxGame.network.getLocalUser().getName() == null){
			nameField.setText("player");
		}
		else{
			nameField.setText(MyGdxGame.network.getLocalUser().getName());
		}
		
		TextureRegionDrawable connectButtonImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.connectButtonTexture));
		
		connectButton = new ImageButton(connectButtonImage);	
		connectButton.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {		
				if(connectButton.isPressed()){					
					MyGdxGame.network.setUsername(nameField.getText());						
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
		
		add(nameField);
		row();
		add(connectButton).padTop(20);
		row();
		add(quitButton).padTop(20);

	}

}
