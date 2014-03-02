package net.megafoxhunt.ui;

import net.megafoxhunt.core.MyGdxGame;
import net.megafoxhunt.shared.KryoNetwork.Move;
import net.megafoxhunt.shared.KryoNetwork.PlayerReady;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class LobbyUI extends Table{
	private TextButton button;
	
	private Skin skin;

	private TextButtonStyle buttonStyle;
	
	public LobbyUI(){
		setFillParent(true);

		buttonStyle = new TextButtonStyle();	
		buttonStyle.font = new BitmapFont();
		
		button = new TextButton("CLICK TO START GAME", buttonStyle);			
		button.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {		
				if(button.isPressed()){					
					PlayerReady playerReady = new PlayerReady();
					MyGdxGame.network.getKryoClient().sendTCP(playerReady);
				}
			}
		});
		add(button);
	}

}
