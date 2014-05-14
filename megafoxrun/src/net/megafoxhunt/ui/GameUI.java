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
	
		TextureRegionDrawable resumeButtonUpImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.resumeButtonUpTexture));	
		TextureRegionDrawable resumeButtonDownImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.resumeButtonDownTexture));	
		
		resume = new ImageButton(resumeButtonUpImage, resumeButtonDownImage);
		resume.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {		
				if(resume.isPressed()){					
					toggle();
					
					MyGdxGame.resources.painike.play();
				}
			}
		});
		
		TextureRegionDrawable newgameButtonUpImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.newgameButtonUpTexture));	
		TextureRegionDrawable newgameButtonDownImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.newgameButtonDownTexture));	
		
		joinNewGame = new ImageButton(newgameButtonUpImage, newgameButtonDownImage);
		joinNewGame.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {		
				if(joinNewGame.isPressed()){						
					MyGdxGame.network.disconnect();
					MyGdxGame.network.connect(MyGdxGame.IP_SERVER, 6666);
					
					MyGdxGame.resources.painike.play();
				}
			}
		});
		
		TextureRegionDrawable quitButtonUpImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.quitButtonUpTexture));	
		TextureRegionDrawable quitButtonDownImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.quitButtonDownTexture));	
		
		quit = new ImageButton(quitButtonUpImage, quitButtonDownImage);
		quit.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {		
				if(quit.isPressed()){				
					MyGdxGame.network.disconnect();
					UserContainer.removeUsers();
					MyGdxGame.screenHandler.setSceenMenu();	
					
					MyGdxGame.resources.painike.play();
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
