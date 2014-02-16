package net.megafoxhunt.server;

import net.megafoxhunt.shared.KryoNetwork.Login;
import net.megafoxhunt.shared.KryoNetwork.Move;
import net.megafoxhunt.shared.KryoNetwork.WelcomePlayer;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ServerListener extends Listener {

	private GameServer server;
	
	public ServerListener(GameServer server) {
		this.server = server;
	}
	
	@Override
	public void received(Connection connection, final Object object){
		final PlayerConnection playerConnection = (PlayerConnection) connection;		
		
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				if(object instanceof Login){					
					String name = ((Login)object).name;					
					if(name == null || name.isEmpty()) {
						playerConnection.close();
						return;
					} else{
						playerConnection.setName(name);
						System.out.println("User connected: " + name + " (" + playerConnection.getMyId() + ")");
						
						server.getRoomHandler().joinAvailableRoom(playerConnection);
						
						WelcomePlayer wp = new WelcomePlayer();
						wp.id = playerConnection.getMyId();
						playerConnection.sendTCP(wp);
					}
				} else if (object instanceof Move) {
					Move move = (Move)object;
					playerConnection.getMyCurrentRoom().move(playerConnection, move);
				}
			}
		});
		t1.start();
	}
	
	@Override
	public void disconnected (Connection connection) {
		final PlayerConnection playerConnection = (PlayerConnection)connection;
		
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				int id = playerConnection.getMyId();
				if (server.getIdHandler().isValidID(id)) {
					System.out.println("Player left: " + playerConnection.getName() + " (" + id + ")");
					playerConnection.getMyCurrentRoom().removePlayer(playerConnection);
					server.getIdHandler().freeID(id);
				}
			}
		});
		t1.start();
	}
}
