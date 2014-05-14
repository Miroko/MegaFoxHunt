package net.megafoxhunt.ui;

import net.megafoxhunt.core.MyGdxGame;
import net.megafoxhunt.core.UserContainer;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class GameUI extends Table{
	
	private ImageButton resume;
	private ImageButton joinNewGame;
	private ImageButton quit;
	
	public GameUI(){		
		setFillParent(true);	
	
		TextureRegionDrawable resumeImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.readyButtonTexture));	
		resume = new ImageButton(resumeImage);
		resume.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {		
				if(resume.isPressed()){					
					toggle();
				}
			}
		});
		
		TextureRegionDrawable joinNewGameImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.connectButtonUpTexture));	
		joinNewGame = new ImageButton(joinNewGameImage);
		joinNewGame.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {		
				if(joinNewGame.isPressed()){	
					MyGdxGame.network.disconnect();
					MyGdxGame.network.connect(MyGdxGame.IP_SERVER, 6666);
				}
			}
		});
		
		TextureRegionDrawable quitImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.quitButtonUpTexture));	
		quit = new ImageButton(quitImage);
		quit.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {		
				if(quit.isPressed()){				
					MyGdxGame.network.disconnect();
					UserContainer.removeUsers();
					MyGdxGame.screenHandler.setSceenMenu();	
				}
			}
		});
		
		add(resume);
		row();
		add(joinNewGame).padTop(20);
		row();
		add(quit).padTop(20);
		
	}	
	public void toggle() {
		setVisible(!isVisible());		
	}

}
