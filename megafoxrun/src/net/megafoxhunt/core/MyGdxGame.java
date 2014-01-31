package net.megafoxhunt.core;

import java.io.IOException;

import net.megafoxhunt.entities.Chased;
import net.megafoxhunt.screens.GameScreen;
import net.megafoxhunt.server.KryoNetwork;
import net.megafoxhunt.server.KryoNetwork.WelcomePlayer;
import net.megafoxhunt.server.KryoNetwork.Login;

import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.badlogic.gdx.Game;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;


public class MyGdxGame extends Game {
		
	@Override
	public void create() {
		this.setScreen(new GameScreen(this));	
		PlayerHandler.setPlayerEntity(new Chased("Player",0, 0));
		
		Client client = new Client();
		client.start();
		
		KryoNetwork.register(client);
		
		client.addListener(new ThreadedListener(new Listener() {
			public void connected (Connection connection) {
			}

			public void received (Connection connection, Object object) {
				if (object instanceof WelcomePlayer) {
					WelcomePlayer wp = (WelcomePlayer)object;
					System.out.println("id: " + wp.id);
				}
			}

			public void disconnected (Connection connection) {
				System.exit(0);
			}
		}));
		
		try {
			client.connect(5000, "10.112.123.242", 6666);
			Login login = new Login();
			login.name = "Testi nimi";
			client.sendTCP(login);
			} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
