package net.megafoxhunt.shared;


import java.util.ArrayList;
import java.util.List;

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
		kryo.register(SetMap.class);
		kryo.register(AddBerry.class);
		kryo.register(RemoveEntity.class);
		kryo.register(PlayerReady.class);
		kryo.register(Message.class);
		kryo.register(Winner.class);
		kryo.register(AddHole.class);
		kryo.register(ActivateItem.class);
		kryo.register(ChangeTilesTypes.class);
		kryo.register(ArrayList.class);
		kryo.register(ChangeTilesTypes.Tile.class);
		kryo.register(GoInHole.class);
		kryo.register(ActivatePowerup.class);
		kryo.register(AddPowerup.class);
		kryo.register(SetPreferedTeam.class);
	}	
	/*
	 * MESSAGE
	 * 
	 */
	public static class Message{
		public String message;
	}
	public static class Winner{
		public String winner;
	}
	/*
	 * CLIENT HANDLING
	 */
	public static class Login{
		public String name;
	}	
	public static class WelcomePlayer{
		public int id;
	}	
	/*
	 * LOBBY
	 */
	public static class ChangeState {
		public static final int LOBBY = 0;
		public static final int GAME = 1;
		
		public int roomState;
	}
	public static class PlayerReady {		
		
	}
	public static class SetPreferedTeam{
		public static final String Chasers = "Chasers";
		public static final String Chased = "Chased";
		
		public String team;
	}
	public static class AddPlayer{
		public String name;
		public int id;
	}	
	public static class RemovePlayer{
		public int id;
	}	
	/*
	 * ENTITIES
	 */
	public static class AddPowerup{
		public int id;
		public int x;
		public int y;
		
		public AddPowerup(){}
		public AddPowerup(int id, int x, int y) {
			this.id = id;
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
	public static class AddBerry {
		public int id;
		public int x;
		public int y;
		
		public AddBerry(){}
		public AddBerry(int id, int x, int y) {
			this.id = id;
			this.x = x;
			this.y = y;
		}
	}
	
	public static class AddHole {
		public int id;
		public int x;
		public int y;
		
		public AddHole(){}
		public AddHole(int id, int x, int y) {
			this.id = id;
			this.x = x;
			this.y = y;
		}
	}
	public static class RemoveEntity {
		public int id;
	}
	/*
	 * MAP
	 */
	public static class SetMap{
		public String mapName;
		
		public SetMap(){}
		public SetMap(String mapName){
			this.mapName = mapName;
		}
	}
	/*
	 * GAME
	 */
	public static class Move{
		public int id;
		public int direction;
		public int x;
		public int y;
		public boolean force = false;
		
		public Move() { }
		public Move(int id, int direction, int x, int y, boolean force) {
			this.id = id;
			this.direction = direction;
			this.x = x;
			this.y = y;
			this.force = force;
		}
	}
	public static class GoInHole {		
		
	}	
	public static class ActivatePowerup{
		
	}
	public static class ActivateItem { }
	
	public static class ChangeTilesTypes {
		private List<Tile> tiles;
		
		public ChangeTilesTypes() {
			tiles = new ArrayList<Tile>();
		}
		
		public void addTile(int x, int y, int type) {
			tiles.add(new Tile(x, y, type));
		}
		
		public List<Tile> getTiles() {
			return tiles;
		}
		
		public static class Tile {
			public int x;
			public int y;
			public int type;
			
			private Tile() {}
			private Tile(int x, int y, int type) {
				this.x = x;
				this.y = y;
				this.type = type;
			}
		}
	}
}
