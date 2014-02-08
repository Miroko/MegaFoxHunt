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
		kryo.register(AddChaser.class);
		kryo.register(AddChased.class);
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
		public int id;
		public int direction;
		public float x;
		public float y;
		
		public Move() { }
		public Move(int id, int direction, float x, float y) {
			this.id = id;
			this.direction = direction;
			this.x = x;
			this.y = y;
		}
	}
	public static class AddChaser {
		public int id;
		public int x;
		public int y;
		
		public AddChaser(){}
		public AddChaser(int id, int x, int y) {
			this.id = id;
			this.x = x;
			this.y = y;
		}
	}
	public static class AddChased {
		public int id;
		public int x;
		public int y;
		
		public AddChased(){}
		public AddChased(int id, int x, int y) {
			this.id = id;
			this.x = x;
			this.y = y;
		}
	}
	public static class RemoveEntity {
		public int id;
	}
	
	public static class ChangeState {
		public static final int LOBBY = 0;
		public static final int GAME = 1;
		
		public int roomState;
	}
}
