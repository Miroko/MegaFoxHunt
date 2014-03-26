package net.megafoxhunt.server;

import java.io.IOException;

import net.megafox.gameroom.GameRoom;
import net.megafoxhunt.server.PlayerConnection.Team;
import net.megafoxhunt.shared.KryoNetwork;

import net.megafoxhunt.shared.KryoNetwork.ActivateItem;
import net.megafoxhunt.shared.KryoNetwork.GoInHole;
import net.megafoxhunt.shared.KryoNetwork.Login;
import net.megafoxhunt.shared.KryoNetwork.Message;
import net.megafoxhunt.shared.KryoNetwork.Move;
import net.megafoxhunt.shared.KryoNetwork.PlayerReady;
import net.megafoxhunt.shared.KryoNetwork.SetPreferedTeam;

import net.megafoxhunt.shared.KryoNetwork.WelcomePlayer;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;

public class GameServer {

	private static final int PORT = 6666;
	public Server kryoServer;	
	public IDHandler idHandler;
	public ServerRooms serverRooms;
		
	public GameServer(){
		serverRooms = new ServerRooms(this);
		idHandler = new IDHandler();		
		init();		
	}
	private void init(){
		kryoServer = new Server(){
			protected Connection newConnection(){
				return new PlayerConnection(idHandler.getFreeID(), null);
			}
		};	
		KryoNetwork.register(kryoServer);
			
		kryoServer.addListener(new ThreadedListener(new Listener() {
			@Override
			public void received(Connection connection, Object object){
				PlayerConnection playerConnection = (PlayerConnection) connection;				
				/*
				 * LOGIN
				 */
				if(object instanceof Login){	
					handleLogin((Login)object, playerConnection);
				}
				/*
				 * MOVE 
				 */
				else if (object instanceof Move) {
					Move move = (Move)object;
					playerConnection.getMyCurrentRoom().move(playerConnection, move);
				}
				/*
				 * PLAYER READY 
				 */
				else if (object instanceof PlayerReady) {				
					playerConnection.setReady();
					/*
					 * START GAME IF EVERYONE READY
					 */
					if(playerConnection.getMyCurrentRoom().allPlayersReady()){
						serverRooms.startGame(playerConnection.getMyCurrentRoom());	
					}									
				}
				/*
				 * SET PREFERED TEAM 
				 */
				else if(object instanceof SetPreferedTeam){
					SetPreferedTeam setPreferedTeam = (SetPreferedTeam) object;
					if(setPreferedTeam.team == "Chasers"){
						playerConnection.setPreferedTeam(Team.Chased);
					}
					else if(setPreferedTeam.team == "Chased"){
						playerConnection.setPreferedTeam(Team.Chasers);
					}				
				}
				/*
				 * ITEM ACTIVATE
				 */				
				else if (object instanceof ActivateItem) {
					playerConnection.activateItem();
				}
				/**
				 * GO IN HOLE
				 */
				else if (object instanceof GoInHole) {
					playerConnection.getMyCurrentRoom().goToHole(playerConnection);
				}
			}
			
			@Override
			public void disconnected (Connection connection) {
				PlayerConnection playerConnection = (PlayerConnection)connection;
				try {
					handleDisconnection(playerConnection);
				} catch (Exception e) {					
					System.out.println("Disconnection error");
				}
			}
		}));					
		try {
			kryoServer.bind(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private boolean handleDisconnection(PlayerConnection connection) throws Exception{		
		boolean playerRemovedFromRoom = removePlayerFromRoom(connection);
		if(playerRemovedFromRoom){			
			logDisconnection(connection);
			connection.dispose(idHandler);
		}
		else{
			return false;
		}		
		return true;
	}
	private void handleLogin(Login login, PlayerConnection connection){
		boolean playerConnected = addConnectingPlayer(login, connection);
		if(playerConnected){
			logConnection(connection);
		}
	}
	private boolean addConnectingPlayer(Login login, PlayerConnection connection){
		String name = login.name;	
		boolean validName = setName(name, connection);
		if(validName){			
			boolean playerAdded = addPlayer(connection);
			if(playerAdded){
				sendWelcome(connection);
				return true;
			}
		}
		else{
			sendDisconnection("Invalid username", connection);
			connection.close();			
		}
		return false;
	}
	/*
	 * Check if name is valid
	 */
	private boolean setName(String name, PlayerConnection connection){
		if(name == null || name.isEmpty()) {			
			return false;
		}
		else{
			connection.setName(name);
			return true;
		}		
	}
	/*
	 * Adds player to room
	 * if none available creates new
	 */
	private boolean addPlayer(PlayerConnection connection){
		GameRoom roomAddedTo = serverRooms.addPlayer(connection);
		if(roomAddedTo == null){			
			GameRoom room = serverRooms.createNewRoom();
			room.addPlayerToRoom(connection);				
			room.start();
			roomAddedTo = room;
		}
		if(connection.getMyCurrentRoom() == null){
			return false;
		}
		return true;
	}
	private void logConnection(PlayerConnection connection){
		System.out.println("Connected: " + connection.getRemoteAddressTCP() + " " + connection.getName() + " " + connection.getMyId());		
	}
	private void logDisconnection(PlayerConnection connection){
		System.out.println("Disconnected: " + connection.getRemoteAddressTCP() + " " + connection.getName() + " " + connection.getMyId());		
	}
	private boolean removePlayerFromRoom(PlayerConnection connection){
		boolean playerRemovedFromRoom = false;
		GameRoom room = connection.getMyCurrentRoom();
		playerRemovedFromRoom = room.removePlayer(connection);
		if(room.isEmpty()){
			serverRooms.removeRoom(room);
		}
		return playerRemovedFromRoom;
	}
	private void sendWelcome(PlayerConnection connection){
		WelcomePlayer wp = new WelcomePlayer();
		wp.id = connection.getMyId();		
		connection.sendTCP(wp);			
	}
	private void sendDisconnection(String message, PlayerConnection connection){
		Message disconnectionMessage = new Message();
		disconnectionMessage.message = message;
	}
	public void printInfo(){
		System.out.println("Players: " + kryoServer.getConnections().length);
		serverRooms.printInfo();
	}
	public void start(){
		kryoServer.start();
	}

}
