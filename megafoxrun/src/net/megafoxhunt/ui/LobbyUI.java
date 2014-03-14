package net.megafoxhunt.ui;

import net.megafoxhunt.core.MyGdxGame;
import net.megafoxhunt.shared.KryoNetwork.Move;
import net.megafoxhunt.shared.KryoNetwork.PlayerReady;
import net.megafoxhunt.shared.KryoNetwork.SetPreferedTeam;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class LobbyUI extends Table{
	
	private TextButton start;
	private TextButton fox;
	private TextButton dog;
	
	private Skin skin;

	private TextButtonStyle buttonStyle;
	
	public LobbyUI(){
		setFillParent(true);

		buttonStyle = new TextButtonStyle();	
		buttonStyle.font = new BitmapFont();
		
		start = new TextButton("CLICK TO START GAME", buttonStyle);			
		start.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {		
				if(start.isPressed()){					
					PlayerReady playerReady = new PlayerReady();
					MyGdxGame.network.getKryoClient().sendTCP(playerReady);
				}
			}
		});
		add(start);
		
		/*
		
		fox = new TextButton("CLICK TO SET PREFERED TEAM FOX", buttonStyle);			
		fox.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {		
				if(fox.isPressed()){					
					SetPreferedTeam setPreferedTeam = new SetPreferedTeam();
					setPreferedTeam.team = SetPreferedTeam.Chased;
					MyGdxGame.network.getKryoClient().sendTCP(setPreferedTeam);
				}
			}
		});		
		add(fox);
		
		dog = new TextButton("CLICK TO SET PREFERED TEAM DOG", buttonStyle);			
		dog.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {		
				if(dog.isPressed()){					
					SetPreferedTeam setPreferedTeam = new SetPreferedTeam();
					setPreferedTeam.team = SetPreferedTeam.Chasers;
					MyGdxGame.network.getKryoClient().sendTCP(setPreferedTeam);
				}
			}
		});		
		add(dog);
		*/
		
	}

}
