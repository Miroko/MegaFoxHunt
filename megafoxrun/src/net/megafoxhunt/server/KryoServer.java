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
		server.start();
		KryoNetwork.register(server);
		try {
			server.bind(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		server.addListener(new Listener(){
			public void receiver(Connection c, Object o){
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
	}
	
	
	
	
	

}
