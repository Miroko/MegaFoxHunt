package net.megafoxhunt.server;

import java.io.IOException;

import net.megafoxhunt.shared.KryoNetwork;
import com.esotericsoftware.kryonet.Connection;
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

		server.addListener(new ServerListener(this));
		
		try {
			server.bind(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		server.start();
		
		// For debugging
		ServerDebugInput s = new ServerDebugInput(this);
		s.start();
	}
	
	public RoomHandler getRoomHandler() {
		return roomHandler;
	}
	
	public IDHandler getIdHandler() {
		return idHandler;
	}
}
