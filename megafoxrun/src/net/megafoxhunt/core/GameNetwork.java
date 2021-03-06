package net.megafoxhunt.core;

import java.io.IOException;


import net.megafoxhunt.entities.Barricade;
import net.megafoxhunt.entities.Berry;
import net.megafoxhunt.entities.Bomb;
import net.megafoxhunt.entities.Chased;
import net.megafoxhunt.entities.Chaser;
import net.megafoxhunt.entities.EntityMovable;
import net.megafoxhunt.entities.PickupItem;
import net.megafoxhunt.entities.Powerup;

import net.megafoxhunt.shared.KryoNetwork;
import net.megafoxhunt.shared.KryoNetwork.AddBarricade;
import net.megafoxhunt.shared.KryoNetwork.AddBerry;
import net.megafoxhunt.shared.KryoNetwork.AddBomb;
import net.megafoxhunt.shared.KryoNetwork.AddChased;
import net.megafoxhunt.shared.KryoNetwork.AddChaser;
import net.megafoxhunt.shared.KryoNetwork.AddHole;
import net.megafoxhunt.shared.KryoNetwork.AddPickupItem;

import net.megafoxhunt.shared.KryoNetwork.AddPlayer;
import net.megafoxhunt.shared.KryoNetwork.AddPowerup;
import net.megafoxhunt.shared.KryoNetwork.ChangeState;
import net.megafoxhunt.shared.KryoNetwork.ChangeTilesTypes;
import net.megafoxhunt.shared.KryoNetwork.PlayerReady;
import net.megafoxhunt.shared.KryoNetwork.PowerupSpeed;
import net.megafoxhunt.shared.KryoNetwork.ChangeTilesTypes.Tile;
import net.megafoxhunt.shared.KryoNetwork.Disconnect;
import net.megafoxhunt.shared.KryoNetwork.Login;
import net.megafoxhunt.shared.KryoNetwork.Move;
import net.megafoxhunt.shared.KryoNetwork.PowerupRage;
import net.megafoxhunt.shared.KryoNetwork.RemoveEntity;
import net.megafoxhunt.shared.KryoNetwork.RemovePlayer;
import net.megafoxhunt.shared.KryoNetwork.SetItemType;
import net.megafoxhunt.shared.KryoNetwork.SetMap;
import net.megafoxhunt.shared.KryoNetwork.WelcomePlayer;
import net.megafoxhunt.shared.KryoNetwork.Winner;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.FrameworkMessage.Ping;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;

public class GameNetwork {

	private static final int TIMEOUT_MS = 5000;

	private User localUser = new User(0, null);
	public User getLocalUser(){return localUser;}

	private Client kryoClient = new Client();
	public Client getKryoClient(){return kryoClient;}
	
	private boolean pingingServer = false;
	
	private int currentPing;
	
	private boolean connecting = false;
		
	public void init(){
		KryoNetwork.register(kryoClient);	
		kryoClient.addListener(new ThreadedListener(new Listener() {
			@Override
			public void connected (Connection connection) {
				startPingingServer();
			}

			@Override
			public void received (Connection connection, Object object) {
				/*
				 * WELCOME PLAYER
				 */
				if (object instanceof WelcomePlayer) {
					WelcomePlayer welcomePlayer = (WelcomePlayer) object;
					localUser.setID(welcomePlayer.id);					
				}
				/*
				 * ADD USER
				 */
				else if (object instanceof AddPlayer) {
					AddPlayer addPlayer = (AddPlayer) object;
					UserContainer.addUser(new User(addPlayer.id, addPlayer.name));					
				}
				/*
				 * PLAYER READY
				 */
				else if (object instanceof PlayerReady) {
					PlayerReady playerReady = (PlayerReady) object;
					UserContainer.getUserByID(playerReady.id).setReady(playerReady.ready);
				}
				/*
				 * REMOVE USER
				 */
				else if (object instanceof RemovePlayer) {
					RemovePlayer removePlayer = (RemovePlayer) object;
					UserContainer.removeUserById(removePlayer.id);					
				} 
				/*
				 * WINNER				 
				 */
				else if (object instanceof Winner) {
					Winner winner = (Winner)object;	
					if(winner.winner.equals("Chased")){
						MyGdxGame.resources.kettu_voittaa_ja_hurraa.play();
					}
					if(winner.winner.equals("Chased") && localUser.getControlledEntity() instanceof Chased){
						MyGdxGame.resources.voittajien_anthem.play();						
					}
					else if(winner.winner.equals("Chasers") && localUser.getControlledEntity() instanceof Chaser){
						MyGdxGame.resources.voittajien_anthem.play();
					}
					else{
						MyGdxGame.resources.haviajien_anthem.play();
					}					
					MyGdxGame.screenHandler.getLobby().lobbyUI.setWinner(winner.winner);
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
								MyGdxGame.screenHandler.setScreenGame();
								
								MyGdxGame.resources.aloitus_lahtolaukaisu.play();
								
								MyGdxGame.screenHandler.getLobby().lobbyUI.setWinner("null");
								
								//Robot robot = new Robot(MyGdxGame.network, 400);
							} else if (changeState.roomState == ChangeState.LOBBY) {								
								MyGdxGame.screenHandler.setScreenLobby();
							}
						}
					});
				}
				/*
				 * ADD CHASER
				 */
				else if (object instanceof AddChaser) {
					AddChaser addChaser = (AddChaser)object;
					UserContainer.getUserByID(addChaser.id).setControlledEntity(new Chaser(addChaser.id, addChaser.x, addChaser.y, MyGdxGame.mapHandler.currentMap));
				}
				/*
				 * ADD CHASED
				 */
				else if (object instanceof AddChased) {
					AddChased addChased = (AddChased)object;
					UserContainer.getUserByID(addChased.id).setControlledEntity(new Chased(addChased.id, addChased.x, addChased.y, MyGdxGame.mapHandler.currentMap));
				}
				/*
				 * ADD BERRY TO MAP
				 */
				else if (object instanceof AddBerry) {
					AddBerry addBerry = (AddBerry)object;
					MyGdxGame.mapHandler.currentMap.addStaticObject(new Berry(addBerry.id, addBerry.x, addBerry.y));
				}
				/*
				 * ADD POWERUP TO MAP
				 */
				else if (object instanceof AddPowerup) {
					AddPowerup addPowerup = (AddPowerup)object;
					MyGdxGame.mapHandler.currentMap.addStaticObject(new Powerup(addPowerup.id, addPowerup.x, addPowerup.y));
				}
				/*
				 * ADD PICKUP_ITEM TO MAP
				 */
				else if (object instanceof AddPickupItem) {
					AddPickupItem addPickup = (AddPickupItem)object;
					MyGdxGame.mapHandler.currentMap.addStaticObject(new PickupItem(addPickup.id, addPickup.x, addPickup.y));
				}
				/*
				 * ADD HOLE TO MAP
				 */
				else if (object instanceof AddHole) {
				}
				/**
				 * ADD BARRICADE
				 */
				else if (object instanceof AddBarricade) {
					AddBarricade addBarricade = (AddBarricade)object;
					Barricade barricade = new Barricade(addBarricade.id, addBarricade.x, addBarricade.y);
					MyGdxGame.mapHandler.currentMap.addStaticObject(barricade);
					barricade.playBuildSound();
				}
				/*
				 * ADD BOMB
				 */
				else if (object instanceof AddBomb) {
					AddBomb addBomb = (AddBomb)object;
					Bomb bomb = new Bomb(addBomb.id, addBomb.x, addBomb.y);
					MyGdxGame.mapHandler.currentMap.addStaticObject(bomb);
					bomb.playFuseSound();					
				}
				/*
				 * REMOVE ENTITY 
				 */
				else if (object instanceof RemoveEntity) {
					RemoveEntity removeEntity = (RemoveEntity)object;
					// DELETE FROM MAP
					MyGdxGame.mapHandler.currentMap.removeStaticObjectByID(removeEntity.id);
										
					// TODO
					// Refactor this to its own kryo command
					for(User user : UserContainer.getUsersConcurrentSafe()){  
						if (user != null && user.getControlledEntity() != null && user.getControlledEntity().getId() == removeEntity.id) {
							user.setControlledEntity(null);	
							 
							MyGdxGame.resources.koira_saa_ketun_kiinni.play();
							
							break;
						}
					}
				}
				/*
				 * MOVE ENTITY
				 */
				else if (object instanceof Move) {
					Move move = (Move) object;						
					EntityMovable entity = (EntityMovable)UserContainer.getUserByID(move.id).getControlledEntity();
					if (entity != null) entity.move(move);
				}	
				/*
				 * POWERUP SPEED
				 */
				else if (object instanceof PowerupSpeed) {
					PowerupSpeed speed = (PowerupSpeed) object;				

					User user = UserContainer.getUserByID(speed.id);
					if (user == null) return;
					EntityMovable entity = user.getControlledEntity();
					if (entity == null) return;
					
					if(speed.on == true){
						entity.setSpeedMultiplier(1.25f);
						MyGdxGame.resources.koira_keraa_bonuksen_lyhyt.play();
					} else if (speed.on == false){
						entity.resetSpeedMultiplier();
					}			
				}
				/*
				 * POWERUP RAGE
				 */
				else if (object instanceof PowerupRage) {
					PowerupRage rage = (PowerupRage)object;
					User user = UserContainer.getUserByID(rage.id);
					if (user == null) return;
					EntityMovable entity = user.getControlledEntity();
					if (entity != null) {
						entity.setRageMode(rage.on);
						MyGdxGame.resources.kettu_keraa_bonuksen_lyhyt.play();
					}
				}
				/*
				 * CHANGE MAP
				 */						
				else if(object instanceof SetMap){					
					SetMap setMap = (SetMap)object;											
					MyGdxGame.mapHandler.switchMap(setMap.mapName);				
				}
				
				
				/*
				 * CHANGE TILE TYPE
				 */
				else if (object instanceof ChangeTilesTypes) {
					ChangeTilesTypes changeTileType = (ChangeTilesTypes)object;
					if (MyGdxGame.mapHandler == null || MyGdxGame.mapHandler.currentMap == null) return;
					
					for (Tile tile : changeTileType.getTiles()) {
						MyGdxGame.mapHandler.currentMap.changeTile(tile.x, tile.y, tile.type);
					}
				}
				
				/*
				 * PING
				 */
				else if (object instanceof Ping) {
					 Ping ping = (Ping)object;
                     if (ping.isReply) {
                    	 currentPing = connection.getReturnTripTime();
                     }
				}
				
				/*
				 * ITEM SET
				 */
				else if (object instanceof SetItemType) {
					SetItemType item = (SetItemType)object;
					MyGdxGame.network.localUser.setItemType(item.itemType);
				}
			}

			@Override
			public void disconnected (Connection connection) {
				stopPingingServer();
				//MyGdxGame.shutdown();
			}
		}));		
	}	
	/**
	 * @param name Set null for console input
	 */
	public void setUsername(String name){
		localUser.setName(name);
		UserContainer.addUser(localUser);		
	}
	public void connect(String host, int port){
		if (kryoClient.isConnected() || connecting) return;
		
		connecting = true;
		try {			
			start();
			kryoClient.connect(TIMEOUT_MS, host, port);
			if (kryoClient.isConnected()) {
				Login login = new Login();
				login.name = localUser.getName();
				kryoClient.sendTCP(login);
			} else {
				connecting = false;
			}
		} catch (IOException ex) {
			connecting = false;
			ex.printStackTrace();
		}
	}
	public void disconnect(){
		Disconnect disconnect = new Disconnect();
		kryoClient.sendTCP(disconnect);		
		stop();
	}	
	public void startPingingServer() {
		pingingServer = true;
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (pingingServer) {
					kryoClient.updateReturnTripTime();
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) { return; }
				}
			}
		}).start();
	}
	
	public int getCurrentPing() {
		return currentPing;
	}
	
	public void stopPingingServer() {
		pingingServer = false;
	}

	public void start() {
		kryoClient.start();		
	}
	public void stop(){
		connecting = false;
		stopPingingServer();
		kryoClient.stop();
	}

	public boolean isConnecting() {
		return connecting;
	}
}
