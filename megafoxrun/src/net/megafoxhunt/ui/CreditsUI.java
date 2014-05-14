package net.megafoxhunt.ui;

import net.megafoxhunt.core.MyGdxGame;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class CreditsUI extends Table{

	private ImageButton backButton;
	
	public CreditsUI(){
		setFillParent(true);
		
		TextureRegionDrawable backButtonUpImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.backButtonUpTexture));
		TextureRegionDrawable backButtonDownImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.backButtonDownTexture));
	
		backButton = new ImageButton(backButtonUpImage, backButtonDownImage);
		backButton.addListener(new ChangeListener() {	
			@Override
			public void changed(ChangeEvent event, Actor actor) {
			if(backButton.isPressed()){										
					MyGdxGame.screenHandler.setSceenMenu();					
					MyGdxGame.resources.painike.play();
				}				
			}
		});
		
		setBackground(new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.creditsBackground)));
		
		bottom().right();		
		add(backButton);
	}
}
