package net.megafoxhunt.core;

import java.io.IOException;

import java.util.Scanner;

import net.megafoxhunt.entities.Berry;
import net.megafoxhunt.entities.Chased;
import net.megafoxhunt.entities.Chaser;
import net.megafoxhunt.entities.EntityMovable;
import net.megafoxhunt.screens.GameScreen;
import net.megafoxhunt.screens.LobbyScreen;
import net.megafoxhunt.shared.GameMapSharedConfig;
import net.megafoxhunt.shared.KryoNetwork;
import net.megafoxhunt.shared.KryoNetwork.AddBerry;
import net.megafoxhunt.shared.KryoNetwork.AddChased;
import net.megafoxhunt.shared.KryoNetwork.AddChaser;
import net.megafoxhunt.shared.KryoNetwork.AddPlayer;
import net.megafoxhunt.shared.KryoNetwork.ChangeState;
import net.megafoxhunt.shared.KryoNetwork.Login;
import net.megafoxhunt.shared.KryoNetwork.Move;
import net.megafoxhunt.shared.KryoNetwork.RemoveEntity;
import net.megafoxhunt.shared.KryoNetwork.RemovePlayer;
import net.megafoxhunt.shared.KryoNetwork.SetMap;
import net.megafoxhunt.shared.KryoNetwork.WelcomePlayer;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;

public class GameNetwork {
	
	// LOGIN TIMEOUT
	private static final int TIMEOUT_MS = 50000;
	
	// LOCAL USER
	private User localUser = new User(0, null);
	public User getLocalUser(){return localUser;}

	// KRYO CLIENT
	private Client kryoClient = new Client();
	public Client getKryoClient(){return kryoClient;}
	
	// GAME
	private MyGdxGame game;
	
	public GameNetwork(MyGdxGame game){
		this.game = game;
		init();
	}
		
	public void init(){
		KryoNetwork.register(kryoClient);	
		kryoClient.addListener(new ThreadedListener(new Listener() {
			@Override
			public void connected (Connection connection) {
				
			}

			@Override
			public void received (Connection connection, Object object) {
				/*
				 * WELCOME PLAYER
				 */
				if (object instanceof WelcomePlayer) {
					WelcomePlayer welcomePlayer = (WelcomePlayer)object;
					localUser.setID(welcomePlayer.id);
					//DebugConsole.msg("Welcome, your id is: " + welcomePlayer.id);
				}
				/*
				 * ADD USER
				 */
				else if (object instanceof AddPlayer) {
					AddPlayer addPlayer = (AddPlayer)object;
					UserContainer.addUser(new User(addPlayer.id, addPlayer.name));
					//DebugConsole.msg("player joined: " + addPlayer.name + "(" + addPlayer.id + ")");
				}
				/*
				 * REMOVE USER
				 */
				else if (object instanceof RemovePlayer) {
					RemovePlayer removePlayer = (RemovePlayer)object;
					UserContainer.removeUserById(removePlayer.id);
					//DebugConsole.msg("player left: (" + removePlayer.id + ")");
				} 
				/*
				 * CHANGE GAME STATE
				 */
				else if (object instanceof ChangeState) {
					final ChangeState changeState = (ChangeState)object;									
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							if (changeState.roomState == ChangeState.GAME) {
								game.setScreen(new GameScreen(game));
							} else if (changeState.roomState == ChangeState.LOBBY) {
								game.setScreen(new LobbyScreen(game));
							}
						}
					});
				}
				/*
				 * ADD CHASER
				 */
				else if (object instanceof AddChaser) {
					AddChaser addChaser = (AddChaser)object;
					UserContainer.getUserByID(addChaser.id).setControlledEntity(new Chaser(addChaser.id, addChaser.x, addChaser.y));
				}
				/*
				 * ADD CHASED
				 */
				else if (object instanceof AddChased) {
					AddChased addChased = (AddChased)object;
					UserContainer.getUserByID(addChased.id).setControlledEntity(new Chased(addChased.id, addChased.x, addChased.y));
				}
				/*
				 * ADD BERRY TO MAP
				 */
				else if (object instanceof AddBerry) {
					AddBerry addBerry = (AddBerry)object;
					MyGdxGame.gameMap.addStaticObject(new Berry(addBerry.id, addBerry.x, addBerry.y));
				}
				/*
				 * REMOVE ENTITY 
				 */
				else if (object instanceof RemoveEntity) {
					RemoveEntity removeEntity = (RemoveEntity)object;
					// DELETE FROM MAP
					MyGdxGame.gameMap.removeStaticObjectByID(removeEntity.id);
					
					// TODO
					// Refactor this to its own kryo command
					for(User user : UserContainer.getUsersConcurrentSafe()){  
						if (user.getID() == removeEntity.id) {
							user.setControlledEntity(null);
							break;
						}
					}
				}
				/*
				 * MOVE ENTITY
				 */
				else if (object instanceof Move) {
					Move move = (Move)object;					
					EntityMovable entity = (EntityMovable)UserContainer.getUserByID(move.id).getControlledEntity();
					entity.move(move);
				}			
				/*
				 * CHANGE MAP
				 */						
				else if(object instanceof SetMap){					
					SetMap setMap = (SetMap)object;	
					//DebugConsole.msg("Set map: " + setMap.mapName);
					
					// TODO MAKE THIS BETTER
					if(setMap.mapName.equals(GameMapSharedConfig.DEBUG_MAP.getName())){
						MyGdxGame.gameMap = new GameMapClientSide(GameMapSharedConfig.DEBUG_MAP);
					}					
				}				
			}

			@Override
			public void disconnected (Connection connection) {
				// TODO null pointer on disconnection
				//DebugConsole.msg("Disconnected from: " + connection.getRemoteAddressTCP().getHostString());
				MyGdxGame.shutdown();
			}
		}));		
	}	
	/**
	 * @param name Set null for console input
	 */
	public void setUsername(String name){
		if(name == null){
			Scanner scanner = new Scanner(System.in);
			System.out.print("Insert username: ");
			name = scanner.nextLine();
			scanner.close();
		}
		localUser.setName(name);
		UserContainer.addUser(localUser);
		//DebugConsole.msg("Username set: " + localUser.getName());
	}
	public void connect(String host, int port){
		try {
			//DebugConsole.msg("Connecting to: " + host + " Port: " + port);
			kryoClient.connect(TIMEOUT_MS, host, port);
			Login login = new Login();
			login.name = localUser.getName();
			kryoClient.sendTCP(login);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void start() {
		kryoClient.start();		
	}
	
}
