package net.megafoxhunt.core;

import java.io.IOException;




import java.util.Scanner;

import net.megafoxhunt.debug.DebugConsole;
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
	
	// LOGIN TIMEOUT
	private static final int TIMEOUT_MS = 50000;
	
	// LOCAL USER
	private static User USER = new User(0, null);
	public static User getUser(){return USER;}

	private static Client CLIENT = new Client();
	public static Client getClient(){return CLIENT;}
	
	public static void init(){
		KryoNetwork.register(CLIENT);	
		CLIENT.addListener(new ThreadedListener(new Listener() {
			@Override
			public void connected (Connection connection) {
				DebugConsole.msg("Connected to: " + connection.getRemoteAddressTCP().getHostString());
			}

			@Override
			public void received (Connection connection, Object object) {
				/*
				 * WELCOME PLAYER
				 */
				if (object instanceof WelcomePlayer) {
					WelcomePlayer welcomePlayer = (WelcomePlayer)object;
					USER.setId(welcomePlayer.id);
					DebugConsole.msg("Welcome, your id is: " + welcomePlayer.id);
				}
				/*
				 * ADD USER
				 */
				else if (object instanceof AddPlayer) {
					AddPlayer addPlayer = (AddPlayer)object;
					UserContainer.addUser(new User(addPlayer.id, addPlayer.name));
					DebugConsole.msg("player joined: " + addPlayer.name + "(" + addPlayer.id + ")");
				}
				/*
				 * REMOVE USER
				 */
				else if (object instanceof RemovePlayer) {
					RemovePlayer removePlayer = (RemovePlayer)object;
					UserContainer.removeUserById(removePlayer.id);
					DebugConsole.msg("player left: (" + removePlayer.id + ")");
				} 
				/*
				 * CHANGE GAME STATE
				 */
				else if (object instanceof ChangeState) {
					final ChangeState changeState = (ChangeState)object;
					
					// TODO miksi postrunnable?					
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							if (changeState.roomState == ChangeState.GAME) {
								MyGdxGame.getInstance().setScreen(new GameScreen());
							} else if (changeState.roomState == ChangeState.LOBBY) {
								MyGdxGame.getInstance().setScreen(new LobbyScreen());
							}
						}
					});
				}
				/*
				 * ADD ENTITY
				 */
				else if (object instanceof AddEntity) {
					AddEntity addEntity = (AddEntity)object;
					EntityContainer.createEntity(addEntity.id, addEntity.type, addEntity.x, addEntity.y, addEntity.direction);
				}
				/*
				 * MOVE ENTITY
				 */
				else if (object instanceof Move) {
					Move move = (Move)object;
					AliveEntity entity = (AliveEntity)EntityContainer.getEntity(move.id);
					entity.setDirection(move.direction);
					entity.setX(move.x);
					entity.setY(move.y);
				}
			}

			@Override
			public void disconnected (Connection connection) {
				DebugConsole.msg("Disconnected from: " + connection.getRemoteAddressTCP().getHostString());
				MyGdxGame.shutdown();
			}
		}));
		CLIENT.start();
	}	
	/**
	 * @param name Set null for console input
	 */
	public static void setUsername(String name){
		if(name == null){
			Scanner scanner = new Scanner(System.in);
			System.out.print("Insert username: ");
			name = scanner.nextLine();
			scanner.close();
		}
		USER.setName(name);
		DebugConsole.msg("Username set: " + USER.getName());
	}
	public static void connect(String host, int port){
		try {
			DebugConsole.msg("Connecting to: " + host + " Port: " + port);
			CLIENT.connect(TIMEOUT_MS, host, port);
			Login login = new Login();
			login.name = USER.getName();
			CLIENT.sendTCP(login);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
}
