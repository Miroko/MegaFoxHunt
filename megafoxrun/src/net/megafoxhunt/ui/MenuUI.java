package net.megafoxhunt.ui;

import net.megafoxhunt.core.MyGdxGame;
import net.megafoxhunt.screens.MenuScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
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
