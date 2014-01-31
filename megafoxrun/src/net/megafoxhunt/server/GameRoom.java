package net.megafoxhunt.server;

import java.util.ArrayList;
import net.megafoxhunt.server.KryoNetwork.AddPlayer;
import net.megafoxhunt.server.KryoNetwork.RemovePlayer;

public class GameRoom extends Thread {
	
	public enum RoomState {
		LOBBY, GAME
	};
	
	public static final int MAX_SIZE = 12;
	
	private static final int UPDATE_FPS = 30;	
	
	private ArrayList<PlayerConnection> players;
	
	private RoomState roomState;
	
	public GameRoom(){
		players = new ArrayList<PlayerConnection>(MAX_SIZE);
		this.roomState = RoomState.LOBBY;
	}
	
	public void update(float delta){
		
	}
	
	public void run(){
		float delta = 0;
		float time1;		
		time1 = System.currentTimeMillis();
		while(true){
			time1 = System.currentTimeMillis();
			update(delta);
			delta = System.currentTimeMillis() - time1;			
		}
	}
	
	public boolean addPlayer(PlayerConnection player) {
		if (players.size() < MAX_SIZE - 1) {
			AddPlayer newPlayerInformation = new AddPlayer();
			newPlayerInformation.id = player.getMyId();
			newPlayerInformation.name = player.getName();
			
			for (PlayerConnection p : players) {
				if (p != player) {
					// send new player information to old players
					p.sendTCP(newPlayerInformation);
					
					// Send players to new player
					AddPlayer oldPlayerInformation = new AddPlayer();
					oldPlayerInformation.id = p.getMyId();
					oldPlayerInformation.name = p.getName();
					player.sendTCP(oldPlayerInformation);
				}
			}
			
			players.add(player);
			
			return true;
		}
		
		return false;
	}
	
	public void removePlayer(PlayerConnection player) {
		if (players.remove(player)) {
			RemovePlayer removePlayer = new RemovePlayer();
			removePlayer.id = player.getMyId();
			
			for (PlayerConnection p : players) {
				p.sendTCP(removePlayer);
			}
		}
	}
}
