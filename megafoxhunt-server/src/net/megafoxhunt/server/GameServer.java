package net.megafoxhunt.server;

import java.io.IOException;

import net.megafoxhunt.shared.KryoNetwork;

import net.megafoxhunt.shared.KryoNetwork.Login;
import net.megafoxhunt.shared.KryoNetwork.Move;

import net.megafoxhunt.shared.KryoNetwork.WelcomePlayer;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;

public class GameServer {
	
	// SERVER PORT
	private static final int PORT = 6666;
	
	// KRYO SERVER
	private Server kryoServer;	
	
	// ID HANDLER	
	private IDHandler idHandler;
	public IDHandler getIdHandler(){return idHandler;}	
	
	// ROOM HANDLER	
	private RoomHandler roomHandler;
	public RoomHandler getRoomHandler(){return roomHandler;}
	
	public GameServer(){
		roomHandler = new RoomHandler();
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
					String name = ((Login)object).name;	
					// VALIDATION
					if(name == null || name.isEmpty()) {
						playerConnection.close();
						return;
					} else{
						playerConnection.setName(name);
						System.out.println("User connected: " + name + " (" + playerConnection.getMyId() + ")");						
						
						roomHandler.addPlayer(playerConnection);						
						
						WelcomePlayer wp = new WelcomePlayer();
						wp.id = playerConnection.getMyId();
						playerConnection.sendTCP(wp);
					}
				}
				/*
				 * MOVE 
				 */
				else if (object instanceof Move) {
					Move move = (Move)object;
					playerConnection.getMyCurrentRoom().move(playerConnection, move);
				}
			}
			
			@Override
			public void disconnected (Connection connection) {
				PlayerConnection playerConnection = (PlayerConnection)connection;
				
				int id = playerConnection.getMyId();
				if (idHandler.isValidID(id)) {
					System.out.println("Player left: " + playerConnection.getName() + " (" + id + ")");
					playerConnection.getMyCurrentRoom().removePlayer(playerConnection);
					idHandler.freeID(id);
				}
			}
		}));				
		
		try {
			kryoServer.bind(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	public void start(){
		kryoServer.start();
	}

}
