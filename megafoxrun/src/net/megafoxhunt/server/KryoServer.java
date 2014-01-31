package net.megafoxhunt.server;

import java.io.IOException;
import java.util.ArrayList;

import net.megafoxhunt.server.KryoNetwork.Login;
import net.megafoxhunt.server.KryoNetwork.WelcomePlayer;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class KryoServer {
	
	private Server server;
	
	private ArrayList<GameRoom> rooms;
	
	private int nextAvailableId = 1;
	
	public KryoServer(int port){
		rooms = new ArrayList<GameRoom>();
		server = new Server(){
			protected Connection newConnection(){
				return new PlayerConnection();
			}
		};
		
		KryoNetwork.register(server);

		server.addListener(new Listener(){
			@Override
			public void received(Connection connection, Object object){
				PlayerConnection playerConnection = (PlayerConnection) connection;
				int id = playerConnection.getMyId();
				
				if(object instanceof Login){
					if(id != -1){ return; }
					
					String name = ((Login)object).name;
					
					if (name == null || name.isEmpty()) {
						connection.close();
						return;
					}
					
					playerConnection.setName(name);
					
					playerConnection.setMyId(nextAvailableId);
					nextAvailableId++;
					
					System.out.println("User connected: " + name + "(" + playerConnection.getMyId() + ")");
					
					WelcomePlayer wp = new WelcomePlayer();
					wp.id = playerConnection.getMyId();
					playerConnection.sendTCP(wp);
					
					searchAvailableRoom(playerConnection);
				}
			}
			
			@Override
			public void disconnected (Connection connection) {
				PlayerConnection playerConnection = (PlayerConnection)connection;
				
				int id = playerConnection.getMyId();
				if (id != -1) {
					System.out.println("Player left: " + playerConnection.getName() + "(" + playerConnection.getMyId() + ")");
					playerConnection.dispose();
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
	
	private void searchAvailableRoom(PlayerConnection playerConnection) {
		GameRoom selectedRoom = null;
		
		// Join to first room that has open slots
		for(GameRoom room : rooms) {
			if (room.addPlayer(playerConnection)) {
				selectedRoom = room;
				break;
			}
		}
		
		// Create new room if no available room were found
		if (selectedRoom == null) {
			selectedRoom = new GameRoom();
			selectedRoom.start();
			rooms.add(selectedRoom);
			selectedRoom.addPlayer(playerConnection);
		}
		
		playerConnection.setMyCurrentRoom(selectedRoom);
	}
}
