package net.megafoxrun.server;

import java.io.IOException;

import com.esotericsoftware.kryonet.Server;

public class GameServer {

	public GameServer(){
		Server server = new Server();
		server.start();
		try {
			server.bind(6666);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
