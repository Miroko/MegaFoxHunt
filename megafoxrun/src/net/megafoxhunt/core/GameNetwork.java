package net.megafoxhunt.core;

import java.io.IOException;


import net.megafoxhunt.entities.AliveEntity;
import net.megafoxhunt.entities.EntityContainer;
import net.megafoxhunt.screens.GameScreen;
import net.megafoxhunt.screens.LobbyScreen;
import net.megafoxhunt.server.KryoNetwork;
import net.megafoxhunt.server.KryoNetwork.AddEntity;
import net.megafoxhunt.server.KryoNetwork.AddPlayer;
import net.megafoxhunt.server.KryoNetwork.ChangeState;
import net.megafoxhunt.server.KryoNetwork.Login;
import net.megafoxhunt.server.KryoNetwork.Move;
import net.megafoxhunt.server.KryoNetwork.RemovePlayer;
import net.megafoxhunt.server.KryoNetwork.WelcomePlayer;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;

public class GameNetwork {
	
	private static final int TIMEOUT_MS = 50000;

	private Client client;
	public Client getClient(){return client;}
	
	public GameNetwork(){		
		client = new Client();
		KryoNetwork.register(client);
		client.start();		
		
		client.addListener(new ThreadedListener(new Listener() {
			@Override
			public void connected (Connection connection) { }

			@Override
			public void received (Connection connection, Object object) {
				/*
				 * WELCOME PLAYER
				 */
				if (object instanceof WelcomePlayer) {
					WelcomePlayer welcomePlayer = (WelcomePlayer)object;
					MyGdxGame.userContainer.getUser().setId(welcomePlayer.id);					
					System.out.println("Welcome, your id is: " + welcomePlayer.id);
				}
				/*
				 * ADD PLAYER
				 */
				else if (object instanceof AddPlayer) {
					AddPlayer addPlayer = (AddPlayer)object;
					MyGdxGame.userContainer.addUser(new User(addPlayer.id, addPlayer.name));
					System.out.println("player joined: " + addPlayer.name + "(" + addPlayer.id + ")");
				}
				/*
				 * REMOVE PLAYER
				 */
				else if (object instanceof RemovePlayer) {
					RemovePlayer removePlayer = (RemovePlayer)object;
					MyGdxGame.userContainer.removeUserById(removePlayer.id);
					System.out.println("player left: (" + removePlayer.id + ")");
				} 
				/*
				 * CHANGE STATE
				 */
				else if (object instanceof ChangeState) {
					final ChangeState changeState = (ChangeState)object;
					
					// TODO miksi postrunnable?
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							if (changeState.roomState == ChangeState.GAME) {
								MyGdxGame.INSTANCE.setScreen(new GameScreen(MyGdxGame.INSTANCE, MyGdxGame.getEntityContainer()));
							} else if (changeState.roomState == ChangeState.LOBBY) {
								MyGdxGame.INSTANCE.setScreen(new LobbyScreen(MyGdxGame.INSTANCE));
							}
						}
					});
				}
				/*
				 * ADD ENTITY
				 */
				else if (object instanceof AddEntity) {
					AddEntity addEntity = (AddEntity)object;
					EntityContainer.createEntity(MyGdxGame.getEntityContainer(), addEntity.id, addEntity.type, addEntity.x, addEntity.y, addEntity.direction);
				}
				/*
				 * MOVE
				 */
				else if (object instanceof Move) {
					Move move = (Move)object;
					AliveEntity entity = (AliveEntity)MyGdxGame.getEntityContainer().getEntity(move.id);
					entity.setDirection(move.direction);
					entity.setX(move.x);
					entity.setY(move.y);
				}
			}

			@Override
			public void disconnected (Connection connection) {
				System.exit(0);
			}
		}));

	}
	public void connect(String host, int port){
		try {
			client.connect(TIMEOUT_MS, host, port);
			Login login = new Login();
			login.name = MyGdxGame.userContainer.getUser().getName();
			client.sendTCP(login);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
}
