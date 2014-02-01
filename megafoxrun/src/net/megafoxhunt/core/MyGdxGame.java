package net.megafoxhunt.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import net.megafoxhunt.entities.Chased;
import net.megafoxhunt.screens.GameScreen;
import net.megafoxhunt.screens.LobbyScreen;
import net.megafoxhunt.screens.MenuScreen;
import net.megafoxhunt.server.KryoNetwork;
import net.megafoxhunt.server.KryoNetwork.AddPlayer;
import net.megafoxhunt.server.KryoNetwork.ChangeState;
import net.megafoxhunt.server.KryoNetwork.RemovePlayer;
import net.megafoxhunt.server.KryoNetwork.WelcomePlayer;
import net.megafoxhunt.server.KryoNetwork.Login;

import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;


public class MyGdxGame extends Game {
	
	public SpriteBatch batch;
	
	private String myName;
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		
		this.setScreen(new MenuScreen());	
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
					UsersHandler.setMyUser(new User(wp.id, myName));
					System.out.println("Welcome, your id is: " + wp.id);
				} else if (object instanceof AddPlayer) {
					AddPlayer addPlayer = (AddPlayer)object;
					UsersHandler.addUser(new User(addPlayer.id, addPlayer.name));
					System.out.println("player joined: " + addPlayer.name + "(" + addPlayer.id + ")");
				} else if (object instanceof RemovePlayer) {
					RemovePlayer removePlayer = (RemovePlayer)object;
					UsersHandler.removeUserById(removePlayer.id);
					System.out.println("player left: (" + removePlayer.id + ")");
				} else if (object instanceof ChangeState) {
					final ChangeState changeState = (ChangeState)object;
					
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							if (changeState.roomState == ChangeState.GAME) {
								MyGdxGame.this.setScreen(new GameScreen(MyGdxGame.this));
							} else if (changeState.roomState == ChangeState.LOBBY) {
								MyGdxGame.this.setScreen(new LobbyScreen(MyGdxGame.this));
							}
						}
					});
				}
			}

			@Override
			public void disconnected (Connection connection) {
				System.exit(0);
			}
		}));
		
		Scanner scanner = new Scanner(System.in);
		System.out.print("Anna nimi: ");
		myName = scanner.nextLine();
		scanner.close();
		
		try {
			client.connect(5000, "localhost", 6666);
			Login login = new Login();
			login.name = myName;
			client.sendTCP(login);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void dispose() {
		batch.dispose();
	}
}
