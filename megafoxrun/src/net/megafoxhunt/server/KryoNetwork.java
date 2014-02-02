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
		kryo.register(ChangeState.class);
	}
	
	public static class Login{
		public String name;
	}
	
	public static class WelcomePlayer{
		public int id;
	}
	
	public static class AddPlayer{
		public String name;
		public int id;
	}
	
	public static class RemovePlayer{
		public int id;
	}
	
	public static class Move{
		public static final int DIRECTION_UP = 1;
		public static final int DIRECTION_RIGHT = 2;
		public static final int DIRECTION_DOWN = 3;
		public static final int DIRECTION_LEFT = 4;
		
		public int id;
		public int direction;
		public int x;
		public int y;
		
		public Move(int id, int direction, int x, int y) {
			this.id = id;
			this.direction = direction;
			this.x = x;
			this.y = y;
		}
	}
	
	public static class ChangeState {
		public static final int LOBBY = 0;
		public static final int GAME = 1;
		
		public int roomState;
	}
}
