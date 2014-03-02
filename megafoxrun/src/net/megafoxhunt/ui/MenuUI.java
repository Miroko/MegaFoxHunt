package net.megafoxhunt.ui;

import net.megafoxhunt.core.MyGdxGame;
import net.megafoxhunt.screens.MenuScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MenuUI extends Table{
		
	private TextButton button;
	
	private Skin skin;

	private TextButtonStyle buttonStyle;
	
	public MenuUI(){
		setFillParent(true);

		buttonStyle = new TextButtonStyle();	
		buttonStyle.font = new BitmapFont();
		
		button = new TextButton("CLICK TO CONNECT", buttonStyle);			
		button.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {		
				if(button.isPressed()){					
					MyGdxGame.network.setUsername("TestUser");	
					MyGdxGame.network.start();	
					MyGdxGame.network.connect(MyGdxGame.IP_SERVER, 6666);
				}
			}
		});
		add(button);
	}

}
