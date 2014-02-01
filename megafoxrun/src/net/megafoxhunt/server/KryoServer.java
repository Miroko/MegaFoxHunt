package net.megafoxhunt.server;

import java.io.IOException;

import net.megafoxhunt.server.KryoNetwork.Login;
import net.megafoxhunt.server.KryoNetwork.WelcomePlayer;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class KryoServer {
	
	private IDHandler idHandler;
	private Server server;	
	private RoomHandler roomHandler;
	
	public KryoServer(int port){
		roomHandler = new RoomHandler();
		idHandler = new IDHandler();
		server = new Server(){
			protected Connection newConnection(){
				return new PlayerConnection(idHandler.getFreeID(), null);
			}
		};
		
		KryoNetwork.register(server);

		server.addListener(new Listener(){
			@Override
			public void received(Connection connection, Object object){
				PlayerConnection playerConnection = (PlayerConnection) connection;						
				if(object instanceof Login){					
					String name = ((Login)object).name;					
					if(name == null || name.isEmpty()) {
						connection.close();
						return;
					}
					else{
						playerConnection.setName(name);
						System.out.println("User connected: " + name + " (" + playerConnection.getID() + ")");
						
						roomHandler.searchAvailableRoom(playerConnection);
						
						WelcomePlayer wp = new WelcomePlayer();
						wp.id = playerConnection.getID();
						playerConnection.sendTCP(wp);
					}
				}
			}
			
			@Override
			public void disconnected (Connection connection) {
				PlayerConnection playerConnection = (PlayerConnection)connection;				
				int id = playerConnection.getID();
				if (idHandler.isValidID(id)) {
					System.out.println("Player left: " + playerConnection.getName() + " (" + id + ")");
					playerConnection.getMyCurrentRoom().removePlayer(playerConnection);
					idHandler.freeID(id);
				}
			}
		});
		
		try {
			server.bind(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		server.start();
	}
}
