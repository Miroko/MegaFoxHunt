package net.megafoxhunt.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class KryoNetwork {
	
	public static void register(EndPoint ep){
		Kryo kryo = ep.getKryo();
		kryo.register(Login.class);
		kryo.register(WelcomePlayer.class);
		kryo.register(AddPlayer.class);
		kryo.register(RemovePlayer.class);
		kryo.register(Move.class);
	}	
	public static class Login{
		public String name;
	}
	public static class WelcomePlayer{
		public int id;
	}
	public static class AddPlayer{
		String name;
		int id;
	}
	public static class RemovePlayer{
		int id;
	}
	public static class Move{
		int id;
	}
}
