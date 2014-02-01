package net.megafoxhunt.core;

import java.io.IOException;
import java.util.Scanner;

import net.megafoxhunt.entities.Chased;
import net.megafoxhunt.screens.GameScreen;
import net.megafoxhunt.server.KryoNetwork;
import net.megafoxhunt.server.KryoNetwork.AddPlayer;
import net.megafoxhunt.server.KryoNetwork.RemovePlayer;
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
			@Override
			public void connected (Connection connection) { }

			@Override
			public void received (Connection connection, Object object) {
				if (object instanceof WelcomePlayer) {
					WelcomePlayer wp = (WelcomePlayer)object;
					System.out.println("Welcome, your id is: " + wp.id);
				} else if (object instanceof AddPlayer) {
					AddPlayer addPlayer = (AddPlayer)object;
					System.out.println("player joined: " + addPlayer.name + "(" + addPlayer.id + ")");
				} else if (object instanceof RemovePlayer) {
					RemovePlayer removePlayer = (RemovePlayer)object;
					System.out.println("player left: (" + removePlayer.id + ")");
				}
			}

			@Override
			public void disconnected (Connection connection) {
				System.exit(0);
			}
		}));
		
		Scanner scanner = new Scanner(System.in);
		System.out.print("Anna nimi: ");
		String name = scanner.nextLine();
		scanner.close();
		
		try {
			client.connect(5000, "localhost", 6666);
			Login login = new Login();
			login.name = name;
			client.sendTCP(login);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
