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
			public void received(Connection c, Object o){
				System.out.println("Receiving");
				PlayerConnection pc = (PlayerConnection) c;
				String name = pc.name;
				if(o instanceof Login){
					if(name != null){return;}
					System.out.println("Player name: " + name);
					
					WelcomePlayer wp = new WelcomePlayer();
					wp.id = 1;
					pc.sendTCP(wp);
				}
			}
		});
		try {
			server.bind(port);
			System.out.println("started");
		} catch (IOException e) {
			e.printStackTrace();
		}
		server.start();
	}
	
	
	
	
	

}
