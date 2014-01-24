package net.megafoxrun.server;

import java.io.IOException;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class GameServer {

	public GameServer(int port){
		Server server = new Server();
		server.start();
		try {
			server.bind(port);
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		 Kryo kryo = server.getKryo();
		    kryo.register(DebugRequest.class);
		
		server.addListener(new Listener() {
		       public void received (Connection connection, Object object) {
		          if (object instanceof DebugRequest) {
		        	  DebugRequest request = (DebugRequest)object;
		             System.out.println(request.text);

		             DebugRequest response = new DebugRequest();
		             response.text = "Thanks";
		             connection.sendTCP(response);
		          }
		       }
		    });
	}
	
}
