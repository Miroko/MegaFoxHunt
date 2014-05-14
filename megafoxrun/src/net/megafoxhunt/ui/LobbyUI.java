package net.megafoxhunt.ui;


import net.megafoxhunt.core.MyGdxGame;
import net.megafoxhunt.core.UserContainer;
import net.megafoxhunt.shared.KryoNetwork.PlayerReady;
import net.megafoxhunt.shared.KryoNetwork.SetPreferedTeam;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class LobbyUI extends Table{
	
	private ImageButton startButton;
	private ImageButton preferDogButton;
	private ImageButton preferFoxButton;
	private TextureRegionDrawable foxWon = null;
	private TextureRegionDrawable dogWon = null;
	private Image victoryImage = new Image(foxWon);
	
	public LobbyUI(){
		setFillParent(true);
			
		TextureRegionDrawable readyButtonUpImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.readyButtonUpTexture));
		TextureRegionDrawable readyButtonDownImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.readyButtonDownTexture));
	
		startButton = new ImageButton(readyButtonUpImage, readyButtonDownImage);
		startButton.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {		
				if(startButton.isPressed()){					
					PlayerReady playerReady = new PlayerReady();
					playerReady.id = MyGdxGame.network.getLocalUser().getID();
					playerReady.ready = true;
					MyGdxGame.network.getKryoClient().sendTCP(playerReady);
				}
			}
		});
		
		TextureRegionDrawable preferFoxButtonUpImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.preferFoxButtonUpTexture));
		TextureRegionDrawable preferFoxButtonDownImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.preferFoxButtonDownTexture));
		
		preferFoxButton = new ImageButton(preferFoxButtonUpImage, preferFoxButtonDownImage);
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
		
		TextureRegionDrawable preferDogButtonUpImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.preferDogButtonUpTexture));
		TextureRegionDrawable preferDogButtonDownImage = new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.preferDogButtonDownTexture));
		
		preferDogButton = new ImageButton(preferDogButtonUpImage, preferDogButtonDownImage);
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

		
		dogWon = preferDogButtonDownImage;
		foxWon = preferFoxButtonDownImage;
		
		setBackground(new TextureRegionDrawable(new TextureRegion(MyGdxGame.resources.lobbyBackground)));	
		
		add(victoryImage);
		row();
		add(startButton);
		row();
		add(preferDogButton).padTop(20);
		row();
		add(preferFoxButton).padTop(20);
		
	}
	public void setWinner(String winner){
		if(winner.equals("chaser")){
			victoryImage.setVisible(true);
			victoryImage.setDrawable(dogWon);
		}
		else if(winner.equals("chased")){
			victoryImage.setVisible(true);
			victoryImage.setDrawable(foxWon);
		}
		else{
			victoryImage.setVisible(false);
		}
	}

}
