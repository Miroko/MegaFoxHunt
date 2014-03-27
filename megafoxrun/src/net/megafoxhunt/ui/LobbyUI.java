package net.megafoxhunt.ui;


import net.megafoxhunt.core.MyGdxGame;

import net.megafoxhunt.shared.KryoNetwork.PlayerReady;
import net.megafoxhunt.shared.KryoNetwork.SetPreferedTeam;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class LobbyUI extends Table{
	
	private ImageButton startButton;
	private ImageButton preferDogButton;
	private ImageButton preferFoxButton;
	
	public LobbyUI(){
		setFillParent(true);
			
		TextureRegionDrawable startButtonImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.readyButtonTexture));
	
		startButton = new ImageButton(startButtonImage);
		startButton.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {		
				if(startButton.isPressed()){					
					PlayerReady playerReady = new PlayerReady();
					MyGdxGame.network.getKryoClient().sendTCP(playerReady);
				}
			}
		});
		
		TextureRegionDrawable preferFoxButtonImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.preferFoxButtonTexture));
		
		preferFoxButton = new ImageButton(preferFoxButtonImage);
		preferFoxButton.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {		
				if(preferFoxButton.isPressed()){						
					SetPreferedTeam setPreferedTeam = new SetPreferedTeam();
					setPreferedTeam.team = SetPreferedTeam.Chased;
					MyGdxGame.network.getKryoClient().sendTCP(setPreferedTeam);
				}
			}
		});		
		
		TextureRegionDrawable preferDogButtonImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.preferDogButtonTexture));
		
		preferDogButton = new ImageButton(preferDogButtonImage);
		preferDogButton.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {		
				if(preferDogButton.isPressed()){					
					SetPreferedTeam setPreferedTeam = new SetPreferedTeam();
					setPreferedTeam.team = SetPreferedTeam.Chasers;
					MyGdxGame.network.getKryoClient().sendTCP(setPreferedTeam);
				}
			}
		});		
		
		
		add(startButton);
		row();
		add(preferDogButton).padTop(20);
		row();
		add(preferFoxButton).padTop(20);
		
	}

}
